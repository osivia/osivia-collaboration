package org.osivia.services.calendar.view.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.service.CalendarServiceImpl;
import org.osivia.services.calendar.edition.portlet.model.CalendarSynchronizationSource;
import org.osivia.services.calendar.integration.portlet.service.CalendarIntegrationService;
import org.osivia.services.calendar.view.portlet.model.CalendarOptions;
import org.osivia.services.calendar.view.portlet.model.CalendarViewForm;
import org.osivia.services.calendar.view.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.view.portlet.model.events.*;
import org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository;
import org.osivia.services.calendar.view.portlet.service.generator.ICalendarGenerator;
import org.osivia.services.calendar.view.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.*;

/**
 * Calendar service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarServiceImpl
 * @see CalendarViewService
 */
@Service
public class CalendarViewServiceImpl extends CalendarServiceImpl implements CalendarViewService {

    /**
     * Log.
     */
    private final Log log;
    /**
     * ProdId.
     */
    private final ProdId prodId;


    /**
     * Application context.
     */
    @Autowired
    protected ApplicationContext applicationContext;

    /**
     * Calendar repository.
     */
    @Autowired
    protected CalendarViewRepository repository;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Notifications service.
     */
    @Autowired
    private INotificationsService notificationsService;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public CalendarViewServiceImpl() {
        super();

        // Log
        this.log = LogFactory.getLog(this.getClass());
        // ProdId
        this.prodId = new ProdId("-//OSIVIA Portal//4.7//FR");
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
        if (periodTypeName == null) {
            if (configuration.getPeriodTypeName() != null && !configuration.getPeriodTypeName().isEmpty()) {
                periodTypeName = configuration.getPeriodTypeName();
                periodType = PeriodTypes.fromName(periodTypeName);
            } else {
                periodType = PeriodTypes.WEEK;
            }
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
     * Add integration menubar item.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    @SuppressWarnings("unchecked")
    protected void addIntegrationMenubarItem(PortalControllerContext portalControllerContext) throws PortletException {
        // Calendar options
        CalendarOptions options = this.repository.getConfiguration(portalControllerContext);

        if (options.isIntegration()) {
            // Portlet request
            PortletRequest request = portalControllerContext.getRequest();
            // Internationalization bundle
            Bundle bundle = this.bundleFactory.getBundle(request.getLocale());
            // Menubar
            List<MenubarItem> menubar = (List<MenubarItem>) request.getAttribute(Constants.PORTLET_ATTR_MENU_BAR);

            // Calendar path
            String path = this.repository.getCalendarPath(portalControllerContext);

            // URL
            String url;
            if (StringUtils.isEmpty(path)) {
                url = null;
            } else {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("format", "ics");

                try {
                    url = this.portalUrlFactory.getPermaLink(portalControllerContext, "integration", parameters, path,
                            IPortalUrlFactory.PERM_LINK_TYPE_PORTLET_RESOURCE);
                } catch (PortalException e) {
                    throw new PortletException(e);
                }
            }

            if (StringUtils.isNotEmpty(url)) {
                MenubarItem item = new MenubarItem("CALENDAR_INTEGRATION_ICS", bundle.getString("CALENDAR_INTEGRATION_ICS_MENUBAR_ITEM"), null,
                        MenubarGroup.SPECIFIC, 0, url, null, null, null);
                menubar.add(item);
            }
        }
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
    public void save(PortalControllerContext portalControllerContext, CalendarViewForm form) throws PortletException {
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
                this.repository.save(portalControllerContext, form);

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

        if (CollectionUtils.isNotEmpty(listUrlSource)) {
            HashMap<EventKey, EventToSync> mapEvents = new HashMap<>();
            try {
                for (CalendarSynchronizationSource source : listUrlSource) {
                    // URL
                    URL url = new URL(source.getUrl());
                    // URL connection
                    URLConnection connection = url.openConnection();

                    // Calendar builder
                    CalendarBuilder calendarBuilder = new CalendarBuilder();
                    // Calendar
                    net.fortuna.ical4j.model.Calendar calendar = calendarBuilder.build(connection.getInputStream());

                    ComponentList listComponent = calendar.getComponents();
                    List<VEvent> listEvent = listComponent.getComponents(Component.VEVENT);
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
            log.error("Erreur de parsing lors de la synchronisation, détail:");
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
    public boolean isCalendarReadOnly(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Calendar options
        CalendarOptions options = this.repository.getConfiguration(portalControllerContext);

        // Anonymous remote user indicator
        boolean anonymousUser = StringUtils.isEmpty(request.getRemoteUser());
        
        return options.isReadOnly() || anonymousUser;
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
            object.put("preview_url", event.getPreviewUrl());
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
            object.put("readonly", this.isEventReadOnly(portalControllerContext, event));

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


    /**
     * Check if event is read only.
     * 
     * @param portalControllerContext portal controller context
     * @param event event
     * @return true if event is read only
     * @throws PortletException
     */
    protected boolean isEventReadOnly(PortalControllerContext portalControllerContext, Event event) throws PortletException {
        // Synchronized event indicator
        boolean synchronizedEvent = StringUtils.isNotEmpty(event.getIdEventSource());

        return synchronizedEvent || this.isCalendarReadOnly(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void integrate(PortalControllerContext portalControllerContext, OutputStream outputStream, String format) throws PortletException, IOException {
        // Calendar
        net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
        calendar.getProperties().add(this.prodId);
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);


        // Events
        List<Event> events = this.repository.getEvents(portalControllerContext, null, null);

        if (CollectionUtils.isNotEmpty(events)) {
            for (Event event : events) {
                VEvent vevent = createVEvent(portalControllerContext, event);
                if (vevent != null) {
                    calendar.getComponents().add(vevent);
                }
            }
        }

        CalendarOutputter outputter = new CalendarOutputter();
        outputter.output(calendar, outputStream);
    }


    /**
     * Create calendar event.
     *
     * @param portalControllerContext portal controller context
     * @param event                   event
     * @throws PortletException
     * @throws IOException
     */
    protected VEvent createVEvent(PortalControllerContext portalControllerContext, Event event) throws PortletException, IOException {
        VEvent vevent;

        // Start
        net.fortuna.ical4j.model.Date start;
        if (event.getStartDate() == null) {
            start = null;
        } else if (event.isAllDay()) {
            start = new net.fortuna.ical4j.model.Date(event.getStartDate());
        } else {
            DateTime dateTime = new DateTime(event.getStartDate());
            dateTime.setUtc(true);
            start = dateTime;
        }

        // Summary
        String summary = event.getTitle();

        if ((start != null) && StringUtils.isNotEmpty(summary)) {
            vevent = new VEvent(start, summary);
            PropertyList properties = vevent.getProperties();

            // End
            if (event.getEndDate() != null) {
                DtEnd end;
                if (event.isAllDay()) {
                    end = new DtEnd(new net.fortuna.ical4j.model.Date(event.getEndDate()));
                } else {
                    end = new DtEnd(new DateTime(event.getEndDate()));
                    end.setUtc(true);
                }
                properties.add(end);
            }

            // UID
            if (StringUtils.isNotEmpty(event.getId())) {
                Uid uid = new Uid(event.getId());
                properties.add(uid);
            }

            // Last modified
            if (event.getLastModified() != null) {
                LastModified lastModified = new LastModified(new DateTime(event.getLastModified()));
                properties.add(lastModified);
            }

            // Location
            if (StringUtils.isNotBlank(event.getLocation())) {
                Location location = new Location(event.getLocation());
                properties.add(location);
            }

            // Description
            if (StringUtils.isNotBlank(event.getDescription())) {
                Description description = new Description(event.getDescription());
                properties.add(description);
            }
        } else {
            vevent = null;
        }

        return vevent;
    }


    @Override
    public String getIntegrationUrl(PortalControllerContext portalControllerContext) throws PortletException {
        // Calendar options
        CalendarOptions options = this.repository.getConfiguration(portalControllerContext);

        // URL
        String url;

        if (options.isIntegration()) {
            // Calendar path
            String path = this.repository.getCalendarPath(portalControllerContext);

            // Portlet instance
            String instance = CalendarIntegrationService.PORTLET_INSTANCE;

            // Window properties
            Map<String, String> properties = new HashMap<>();
            properties.put(CalendarIntegrationService.DOCUMENT_PATH_WINDOW_PROPERTY, path);

            try {
                url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, instance, properties, PortalUrlType.MODAL);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        } else {
            url = null;
        }

        return url;
    }

}

