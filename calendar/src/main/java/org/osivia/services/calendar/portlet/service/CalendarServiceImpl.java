package org.osivia.services.calendar.portlet.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.WindowState;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.services.calendar.portlet.model.CalendarConfiguration;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.model.events.EventsData;
import org.osivia.services.calendar.portlet.repository.ICalendarRepository;
import org.osivia.services.calendar.portlet.service.generator.ICalendarGenerator;
import org.osivia.services.calendar.portlet.utils.PeriodTypes;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Calendar service implementation.
 *
 * @author Cédric Krommenhoek
 * @see ICalendarService
 */
@Service
public class CalendarServiceImpl implements ICalendarService, ApplicationContextAware {

    /** Calendar repository. */
    @Autowired
    private ICalendarRepository calendarRepository;

    /** Application context. */
    private ApplicationContext applicationContext;


    /** Bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public CalendarServiceImpl() {
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
        return this.calendarRepository.getTitle(portalControllerContext);
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
    private PeriodTypes getPeriodType(PortalControllerContext portalControllerContext, String periodTypeName) throws PortletException {
        // Configuration
        CalendarConfiguration configuration = this.calendarRepository.getConfiguration(portalControllerContext);
        if (StringUtils.isBlank(configuration.getCmsPath())) {
            throw new PortletException(this.getInternationalizedProperty(portalControllerContext, "MESSAGE_CMS_PATH_NOT_DEFINED"));
        }

        // Period type
        PeriodTypes periodType;
        if (periodTypeName == null) {
            periodType = PeriodTypes.fromName(configuration.getPeriodTypeName());
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
        CalendarConfiguration configuration = this.calendarRepository.getConfiguration(portalControllerContext);
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
    public String getViewPath(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        // Period type
        PeriodTypes periodType = calendarData.getPeriodType();

        // Insert content menubar items
        this.calendarRepository.insertContentMenubarItems(portalControllerContext);

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
        //generator.generateEventsData(portalControllerContext, calendarData);

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
        this.calendarRepository.definePortletUri(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}

