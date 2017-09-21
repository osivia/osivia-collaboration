package org.osivia.services.calendar.portlet.service.generator;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.lang.time.DateUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.model.events.Event;
import org.osivia.services.calendar.portlet.model.events.EventsData;
import org.osivia.services.calendar.portlet.repository.ICalendarRepository;
import org.osivia.services.calendar.portlet.service.ICalendarService;
import org.osivia.services.calendar.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Calendar generator implementation super-class.
 *
 * @author Cédric Krommenhoek
 * @see ICalendarGenerator
 */
public abstract class CalendarGeneratorImpl implements ICalendarGenerator {

    /** Hour display height (in px). */
    protected static final int HOUR_DISPLAY_HEIGHT = 40;
    /** Default scroll top hour. */
    protected static final int DEFAULT_SCROLL_TOP_HOUR = 7;

    /** Planning compact maximum number of days to display. */
    protected static final int PLANNING_COMPACT_MAX = 5;

    /** Round factor. */
    private static final float ROUND_FACTOR = 1000;


    /** Calendar repository. */
    @Autowired
    private ICalendarRepository calendarRepository;

    /** Bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public CalendarGeneratorImpl() {
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
    public CalendarData generateCalendarData(PortalControllerContext portalControllerContext) throws PortletException {
        CalendarData calendarData = this.generateSpecializedCalendarData(portalControllerContext);
        // Period type
        calendarData.setPeriodType(this.getPeriodType());
        // Generator
        calendarData.setGenerator(this);
        // Dates
        this.fillCalendarDates(portalControllerContext, calendarData, null);

        return calendarData;
    }


    /**
     * Generate specialized calendar data.
     *
     * @param portalControllerContext portal controller context
     * @return specialized calendar data
     * @throws PortletException
     */
    protected abstract CalendarData generateSpecializedCalendarData(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCalendarData(PortalControllerContext portalControllerContext, CalendarData calendarData, Date selectedDate) throws PortletException {
        // Dates
        this.fillCalendarDates(portalControllerContext, calendarData, selectedDate);

        this.updateSpecializedCalendarData(portalControllerContext, calendarData);
    }


    /**
     * Update specialized calendar data.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     */
    protected abstract void updateSpecializedCalendarData(PortalControllerContext portalControllerContext, CalendarData calendarData);


    /**
     * Fill calendar dates.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @param forcedSelectedDate forced selected date, may be null
     * @throws PortletException
     */
    private void fillCalendarDates(PortalControllerContext portalControllerContext, CalendarData calendarData, Date forcedSelectedDate) throws PortletException {
        // Period type
        PeriodTypes periodType = calendarData.getPeriodType();

        // Selected date
        Date selectedDate = forcedSelectedDate;
        if (selectedDate == null) {
            // Parameterized date
            selectedDate = this.getSelectedDate(portalControllerContext, periodType);
        }
        calendarData.setSelectedDate(selectedDate);

        // Start date
        Date startDate = this.getStartDate(portalControllerContext, periodType, selectedDate);
        calendarData.setStartDate(startDate);

        // End date
        Date endDate = this.getEndDate(portalControllerContext, periodType, selectedDate, startDate);
        calendarData.setEndDate(endDate);

        // Display date
        String displayDate = this.getDisplayDate(portalControllerContext, calendarData);
        calendarData.setDisplayDate(displayDate);
    }


    /**
     * Get selected date.
     *
     * @param portalControllerContext portal controller context
     * @param periodType period type
     * @return selected date
     */
    protected Date getSelectedDate(PortalControllerContext portalControllerContext, PeriodTypes periodType) {
        Date selectedDate;

        if (PeriodTypes.PLANNING.equals(periodType)) {
            selectedDate = new Date();
        } else {
            String parameter = portalControllerContext.getRequest().getParameter(ICalendarService.SELECTED_DATE_PARAMETER);
            if (parameter == null) {
                selectedDate = new Date();
            } else {
                try {
                    selectedDate = ICalendarService.SELECTED_DATE_FORMAT.parse(parameter);
                } catch (ParseException e) {
                    selectedDate = new Date();
                }
            }
        }

        return DateUtils.truncate(selectedDate, Calendar.DAY_OF_MONTH);
    }


    /**
     * Get start date.
     *
     * @param portalControllerContext portal controller context
     * @param periodType period type
     * @param selectedDate selected date
     * @return start date
     */
    protected Date getStartDate(PortalControllerContext portalControllerContext, PeriodTypes periodType, Date selectedDate) {
        Calendar calendar = GregorianCalendar.getInstance(portalControllerContext.getRequest().getLocale());
        calendar.setTime(selectedDate);
        if (PeriodTypes.MONTH.equals(periodType)) {
            // Set first day of month
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            // Flush calendar between 2 "set" calls
            calendar.getTimeInMillis();
            // Set first day of week
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        } else if (PeriodTypes.WEEK.equals(periodType)) {
            // Set first day of week
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        }
        return calendar.getTime();
    }


    /**
     * Get end date.
     *
     * @param portalControllerContext portal controller context
     * @param periodType period type
     * @param selectedDate selected date
     * @param startDate start date
     * @return end date
     */
    protected Date getEndDate(PortalControllerContext portalControllerContext, PeriodTypes periodType, Date selectedDate, Date startDate) {
        // End date
        Date endDate;
        
        if (PeriodTypes.PLANNING.equals(periodType)) {
            endDate = null;
        } else {
            Calendar calendar = GregorianCalendar.getInstance(portalControllerContext.getRequest().getLocale());
            if (PeriodTypes.MONTH.equals(periodType)) {
                calendar.setTime(selectedDate);
                // Set last day of month
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                // Flush calendar between 2 "set" calls
                calendar.getTimeInMillis();
                // Set first day of last week
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                // Add a week
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            } else {
                calendar.setTime(startDate);
                calendar.add(periodType.getField(), 1);
            }
            calendar.add(Calendar.MILLISECOND, -1);
            endDate = calendar.getTime();
        }
        
        return endDate;
    }


    /**
     * Get display date.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @return display date
     * @throws PortletException
     */
    protected abstract String getDisplayDate(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException;


    /**
     * {@inheritDoc}
     */
    @Override
    public EventsData generateEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        // Events
        List<Event> events = this.calendarRepository.getEvents(portalControllerContext, calendarData);

        // Events data
        EventsData eventsData;
        if (events == null) {
            eventsData = null;
        } else {
            eventsData = this.generateSpecializedEventsData(portalControllerContext, calendarData, events);
        }

        return eventsData;
    }


    /**
     * Generate specialized events data.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @param events events
     * @return specialized events data
     * @throws PortletException
     */
    protected abstract EventsData generateSpecializedEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData, List<Event> events)
            throws PortletException;


    /**
     * Get event hours.
     *
     * @param startCalendar start calendar
     * @param endCalendar end calendar
     * @param begin begin indicator
     * @param end end indicator
     * @return event hours
     */
    protected float getEventHours(Calendar startCalendar, Calendar endCalendar, boolean begin, boolean end) {
        long start;
        if (begin) {
            start = DateUtils.getFragmentInMilliseconds(startCalendar, Calendar.DAY_OF_MONTH);
        } else {
            start = 0;
        }
        long stop;
        if (end) {
            stop = DateUtils.getFragmentInMilliseconds(endCalendar, Calendar.DAY_OF_MONTH);
        } else {
            stop = DateUtils.MILLIS_PER_DAY;
        }

        float hours = Long.valueOf(stop - start).floatValue() / Float.valueOf(DateUtils.MILLIS_PER_HOUR);
        return Math.round(hours * ROUND_FACTOR) / ROUND_FACTOR;
    }


    /**
     * Getter for bundleFactory.
     *
     * @return the bundleFactory
     */
    public IBundleFactory getBundleFactory() {
        return this.bundleFactory;
    }

}
