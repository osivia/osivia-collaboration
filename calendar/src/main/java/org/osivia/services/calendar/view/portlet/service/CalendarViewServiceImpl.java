package org.osivia.services.calendar.view.portlet.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.WindowState;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.service.CalendarServiceImpl;
import org.osivia.services.calendar.edition.portlet.model.CalendarSynchronizationSource;
import org.osivia.services.calendar.view.portlet.model.CalendarOptions;
import org.osivia.services.calendar.view.portlet.model.CalendarViewForm;
import org.osivia.services.calendar.view.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.view.portlet.model.events.DailyCalendarEventsData;
import org.osivia.services.calendar.view.portlet.model.events.DailyEvent;
import org.osivia.services.calendar.view.portlet.model.events.Event;
import org.osivia.services.calendar.view.portlet.model.events.EventKey;
import org.osivia.services.calendar.view.portlet.model.events.EventToSync;
import org.osivia.services.calendar.view.portlet.model.events.EventsData;
import org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository;
import org.osivia.services.calendar.view.portlet.service.generator.ICalendarGenerator;
import org.osivia.services.calendar.view.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarParser;
import net.fortuna.ical4j.data.CalendarParserFactory;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.ParameterFactoryRegistry;
import net.fortuna.ical4j.model.PropertyFactoryRegistry;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Calendar service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarServiceImpl
 * @see CalendarViewService
 */
@Service
public class CalendarViewServiceImpl extends CalendarServiceImpl implements CalendarViewService {

    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;

    /** Calendar repository. */
    @Autowired
    protected CalendarViewRepository repository;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /** Bundle factory. */
    private final IBundleFactory bundleFactory;

    /** logger */
    protected static final Log logger = LogFactory.getLog(CalendarViewServiceImpl.class);

    /**
     * Constructor.
     */
    public CalendarViewServiceImpl() {
        super();

        // Bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getTitle(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarData getCalendarData(PortalControllerContext portalControllerContext, String periodTypeName) throws PortletException {
        PeriodTypes periodType = this.getPeriodType(portalControllerContext, periodTypeName);

        // Generator
        ICalendarGenerator generator = this.getGenerator(portalControllerContext, periodType);
        return generator.generateCalendarData(portalControllerContext, periodType);
    }


    /**
     * Get period type.
     *
     * @param portalControllerContext portal controller context
     * @param periodTypeName period type name, may be null
     * @return period type
     * @throws PortletException
     */
    protected PeriodTypes getPeriodType(PortalControllerContext portalControllerContext, String periodTypeName) throws PortletException {
        // Configuration
        CalendarOptions configuration = this.repository.getConfiguration(portalControllerContext);
        if (StringUtils.isBlank(configuration.getCmsPath())) {
            throw new PortletException(this.getInternationalizedProperty(portalControllerContext, "MESSAGE_CMS_PATH_NOT_DEFINED"));
        }

        // Period type
        PeriodTypes periodType;
        if (configuration.getPeriodTypeName() != null && !configuration.getPeriodTypeName().isEmpty())
        {
        	periodTypeName = configuration.getPeriodTypeName();
        	periodType = PeriodTypes.fromName(periodTypeName);
        }
        if (periodTypeName == null) {
            periodType = PeriodTypes.WEEK;
        } else {
            periodType = PeriodTypes.fromName(periodTypeName);
        }

        if (this.isCompact(portalControllerContext) && !periodType.isCompactable()) {
            // Force planning view
            periodType = PeriodTypes.PLANNING;
        }

        return periodType;
    }


    /**
     * Get generator from his period type.
     *
     * @param portalControllerContext portal controller context
     * @param periodType period type
     * @return generator
     * @throws PortletException
     */
    private ICalendarGenerator getGenerator(PortalControllerContext portalControllerContext, PeriodTypes periodType) throws PortletException {
        ICalendarGenerator result = null;

        // Search generator into application context
        Map<String, ICalendarGenerator> generators = this.applicationContext.getBeansOfType(ICalendarGenerator.class);
        for (ICalendarGenerator generator : generators.values()) {
            if (generator.getPeriodType().getViewPath().equals(periodType.getViewPath())) {
                result = generator;
                generator.setPeriodType(periodType);
                break;
            }
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCompact(PortalControllerContext portalControllerContext) throws PortletException {
        // Configuration
        CalendarOptions configuration = this.repository.getConfiguration(portalControllerContext);
        // Maximized view state indicator
        boolean maximized = WindowState.MAXIMIZED.equals(portalControllerContext.getRequest().getWindowState());

        return (configuration.isCompactView() && !maximized);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public EventsData getEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        // Generator
        ICalendarGenerator generator = calendarData.getGenerator();
        return generator.generateEventsData(portalControllerContext, calendarData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Event getEvent(PortalControllerContext portalControllerContext, String docid) throws PortletException {
        return this.repository.getEvent(portalControllerContext, docid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getViewPath(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        // Period type
        PeriodTypes periodType = calendarData.getPeriodType();

        // Insert content menubar items
        this.repository.insertContentMenubarItems(portalControllerContext);

        // View path
        String viewPath = periodType.getViewPath();
        if (this.isCompact(portalControllerContext) && periodType.isCompactable()) {
            viewPath += "-compact";
        }

        return viewPath;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String selectPreviousPeriod(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        return this.changeSelectedDate(portalControllerContext, calendarData, -1);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String selectNextPeriod(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        return this.changeSelectedDate(portalControllerContext, calendarData, 1);
    }


    /**
     * Change selected date.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @param diff period diff
     * @return selected date
     * @throws PortletException
     */
    private String changeSelectedDate(PortalControllerContext portalControllerContext, CalendarData calendarData, int diff) throws PortletException {
        // Selected date
        Date selectedDate = calendarData.getSelectedDate();
        // Calendar
        Calendar calendar = GregorianCalendar.getInstance(portalControllerContext.getRequest().getLocale());
        calendar.setTime(selectedDate);

        // Change date
        calendar.add(calendarData.getPeriodType().getField(), diff);
        selectedDate = calendar.getTime();

        ICalendarGenerator generator = calendarData.getGenerator();
        // Update calendar data
        generator.updateCalendarData(portalControllerContext, calendarData, selectedDate);
        // Update events data
        // generator.generateEventsData(portalControllerContext, calendarData);

        return StringEscapeUtils.escapeHtml(SELECTED_DATE_FORMAT.format(selectedDate));
    }


    /**
     * Get internationalized property.
     *
     * @param portalControllerContext portal controller context
     * @param key property key
     * @return internationationalized property
     */
    private String getInternationalizedProperty(PortalControllerContext portalControllerContext, String key) {
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);
        return bundle.getString(key);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void definePortletUri(PortalControllerContext portalControllerContext) throws PortletException {
        this.repository.definePortletUri(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, CalendarViewForm form, java.util.TimeZone timezone) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
        try {
            Event event = null;
            if (form.getDocId() != null) {
                event = this.repository.getEvent(portalControllerContext, form.getDocId());
            }
            if (event != null && event.getIdEventSource() != null) {
                // Forbiden to change an event synchronize from another calendar
                this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_NO_RIGHT_SYNCHRO"),
                        NotificationsType.WARNING);
            } else {
                this.repository.save(portalControllerContext, form, timezone);

                this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_EVENT_SAVE"), NotificationsType.SUCCESS);
            }

        } catch (NuxeoException e) {
            if (NuxeoException.ERROR_FORBIDDEN == e.getErrorCode()) {

                this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_NO_RIGHT"), NotificationsType.WARNING);
            } else
                throw e;
        }
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void synchronize(PortalControllerContext portalControllerContext) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        List<CalendarSynchronizationSource> listUrlSource = this.repository.getSynchronizationSources(portalControllerContext);
        HashMap<EventKey, EventToSync> mapEvents = new HashMap<EventKey, EventToSync>();
        try {
            for (CalendarSynchronizationSource source : listUrlSource) {
                URL url = new URL(source.getUrl());
                URLConnection conn = url.openConnection();

                
                CalendarParser parser = CalendarParserFactory.getInstance().createParser();
                
                PropertyFactoryRegistry propertyFactoryRegistry = new PropertyFactoryRegistry();
                
                ParameterFactoryRegistry parameterFactoryRegistry = new ParameterFactoryRegistry();
                
                TimeZoneRegistry tzRegistry = TimeZoneRegistryFactory.getInstance().createRegistry();
                
                
                CalendarBuilder builder = new CalendarBuilder(parser, propertyFactoryRegistry, parameterFactoryRegistry, tzRegistry);
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                net.fortuna.ical4j.model.Calendar calendar;
                List<VEvent> listEvent = new ArrayList<VEvent>();

                calendar = builder.build(rd);
                ComponentList listComponent = calendar.getComponents();
                listEvent = listComponent.getComponents(Component.VEVENT);
                VTimeZone vTimeZoneAllEvent = (VTimeZone) calendar.getComponent(Component.VTIMEZONE);
                TimeZone timeZoneAllEvent = null;

                // If timezone not inquire, default timezone is GMT
                if (vTimeZoneAllEvent == null) {
                    TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
                    timeZoneAllEvent = registry.getTimeZone("GMT");
                } else {
                    timeZoneAllEvent = new TimeZone(vTimeZoneAllEvent);
                }

                Calendar cal;
                EventKey key;
                for (VEvent event : listEvent) {
                    if (event.getRecurrenceId() == null) {
                        cal = null;
                    } else {
                        cal = Calendar.getInstance();
                        if (event.getRecurrenceId().getTimeZone() == null) {
                            cal.setTimeZone(timeZoneAllEvent);
                        } else {
                            cal.setTimeZone(event.getRecurrenceId().getTimeZone());
                        }
                        cal.setTime(event.getRecurrenceId().getDate());
                    }
                    key = new EventKey(event.getUid().getValue(), source.getId(), cal);
                    mapEvents.put(key, buildEvent(event, source.getId(), timeZoneAllEvent));
                }
            }
            this.repository.synchronize(portalControllerContext, mapEvents);

            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_SYNCHRO_DONE"), NotificationsType.SUCCESS);

        } catch (ParserException e) {
            logger.error("Erreur de parsing lors de la synchronisation, détail:");
            e.printStackTrace();
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_SYNCHRO_FAILED"), NotificationsType.WARNING);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_SYNCHRO_FAILED_URL"), NotificationsType.WARNING);
        } catch (IOException e) {
            e.printStackTrace();
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_SYNCHRO_FAILED"), NotificationsType.WARNING);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(PortalControllerContext portalControllerContext, CalendarViewForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
        try {
            if (form.getDocId() != null) {
                Event event = this.repository.getEvent(portalControllerContext, form.getDocId());
                if (event.getIdEventSource() != null) {
                    // Forbiden to change an event synchronize from another calendar
                    this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_NO_RIGHT_SYNCHRO"),
                            NotificationsType.WARNING);
                } else {
                    this.repository.remove(portalControllerContext, form);

                    this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_DELETE_DONE"),
                            NotificationsType.SUCCESS);
                }
            }
        } catch (NuxeoException e) {
            if (NuxeoException.ERROR_FORBIDDEN == e.getErrorCode()) {
                this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MESSAGE_NO_RIGHT"), NotificationsType.WARNING);
            } else
                throw e;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEventEditable(PortalControllerContext portalControllerContext, String docid) throws PortletException {
        return this.repository.isEventEditable(portalControllerContext, docid);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, CalendarColor> getSourcesColor(PortalControllerContext portalControllerContext) throws PortletException {

        HashMap<String, CalendarColor> mapSourceColor = new HashMap<String, CalendarColor>();
        mapSourceColor.put("PRIMARY", CalendarColor.fromId(this.repository.getColorIdAgenda(portalControllerContext)));
        List<CalendarSynchronizationSource> listSource = this.repository.getSynchronizationSources(portalControllerContext);
        for (CalendarSynchronizationSource source : listSource) {
            mapSourceColor.put(source.getId(), source.getColor());
        }
        return mapSourceColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColorIdAgenda(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getColorIdAgenda(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray loadEventsArray(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        EventsData eventsData = getEventsData(portalControllerContext, calendarData);
        List<DailyEvent> listEvent = ((DailyCalendarEventsData) eventsData).getEvents();

        Map<String, CalendarColor> mapColor = getSourcesColor(portalControllerContext);

        JSONArray array = new JSONArray();
        JSONObject object = null;
        Iterator<DailyEvent> iterator = listEvent.iterator();
        DailyEvent event = null;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formaterAllDay = new SimpleDateFormat("yyyy-MM-dd");
        while (iterator.hasNext()) {
            event = iterator.next();
            object = new JSONObject();
            object.put("text", event.getTitle());
            if (event.isAllDay()) {
                object.put("start_date", formaterAllDay.format(event.getStartDate()));
                object.put("end_date", formaterAllDay.format(event.getEndDate()));
            } else {
                object.put("start_date", formater.format(event.getStartDate()));
                object.put("end_date", formater.format(event.getEndDate()));
            }
            object.put("doc_id", event.getId());
            object.put("color", event.getBckgColor());
            object.put("view_url", event.getViewURL());
            if (event.getBckgColor() == null) {
                if (event.getIdParentSource() == null) {
                    if (mapColor.get("PRIMARY") != null) {
                        object.put("extraClass", mapColor.get("PRIMARY").getBackgroundClass());
                    }
                } else {
                    if (mapColor.get(event.getIdParentSource()) == null) {
                        object.put("extraClass", CalendarColor.DEFAULT);
                    } else {
                        object.put("extraClass", mapColor.get(event.getIdParentSource()).getBackgroundClass());
                    }
                }
            } else {
                object.put("extraClass", CalendarColor.fromId(event.getBckgColor()).getBackgroundClass());
            }
            array.add(object);
        }
        return array;
    }


    public EventToSync buildEvent(VEvent vevent, String idAgenda, TimeZone timeZoneAllEvent) throws PortletException {
        boolean allDay = (vevent.getStartDate().getValue().length() == 8 && vevent.getEndDate().getValue().length() == 8);

        Calendar startCal = Calendar.getInstance();
        if (vevent.getStartDate().getTimeZone() != null) {
            startCal.setTimeZone(vevent.getStartDate().getTimeZone());
        } else {
            startCal.setTimeZone(timeZoneAllEvent);
        }
        startCal.setTime(vevent.getStartDate().getDate());

        Calendar endCal = Calendar.getInstance();
        if (vevent.getEndDate().getTimeZone() != null) {
            endCal.setTimeZone(vevent.getEndDate().getTimeZone());
        } else {
            endCal.setTimeZone(timeZoneAllEvent);
        }
        endCal.setTime(vevent.getEndDate().getDate());

        Calendar createdCal = Calendar.getInstance();
        if (vevent.getCreated().getTimeZone() != null) {
            createdCal.setTimeZone(vevent.getCreated().getTimeZone());
        } else {
            createdCal.setTimeZone(timeZoneAllEvent);
        }
        createdCal.setTime(vevent.getCreated().getDate());

        Calendar lastModifiedCal = Calendar.getInstance();
        if (vevent.getLastModified().getTimeZone() != null) {
            lastModifiedCal.setTimeZone(vevent.getLastModified().getTimeZone());
        } else {
            lastModifiedCal.setTimeZone(timeZoneAllEvent);
        }
        lastModifiedCal.setTime(vevent.getLastModified().getDate());

        Calendar startReccuringStartSource;
        if (vevent.getRecurrenceId() == null) {
            startReccuringStartSource = null;
        } else {
            startReccuringStartSource = Calendar.getInstance();
            if (vevent.getRecurrenceId().getTimeZone() != null) {
                startReccuringStartSource.setTimeZone(vevent.getRecurrenceId().getTimeZone());
            } else {
                startReccuringStartSource.setTimeZone(timeZoneAllEvent);
            }
            startReccuringStartSource.setTime(vevent.getRecurrenceId().getDate());
        }
        String description = (vevent.getDescription() == null) ? null : vevent.getDescription().getValue();
        String summary = (vevent.getSummary() == null) ? null : vevent.getSummary().getValue();
        String uid = (vevent.getUid() == null) ? null : vevent.getUid().getValue();
        String location = (vevent.getLocation() == null) ? null : vevent.getLocation().getValue();

        return new EventToSync(null, summary, allDay, startCal, endCal, description, idAgenda, uid, createdCal, lastModifiedCal, startReccuringStartSource, location);
    }

}

