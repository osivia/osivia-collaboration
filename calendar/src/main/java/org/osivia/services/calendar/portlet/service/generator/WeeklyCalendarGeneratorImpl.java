package org.osivia.services.calendar.portlet.service.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletException;

import org.apache.commons.lang.time.DateUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.model.events.DailyCalendarEventsData;
import org.osivia.services.calendar.portlet.model.events.DailyEvent;
import org.osivia.services.calendar.portlet.model.events.Event;
import org.osivia.services.calendar.portlet.model.events.EventsData;
import org.osivia.services.calendar.portlet.model.events.TimeSlotEvent;
import org.osivia.services.calendar.portlet.utils.PeriodTypes;
import org.springframework.stereotype.Service;

/**
 * Weekly calendar generator implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarGeneratorImpl
 */
@Service(value = "weeklyCalendarGenerator")
public class WeeklyCalendarGeneratorImpl extends CalendarGeneratorImpl {

//    /** Collision manager. */
//    @Autowired
//    private ICalendarCollisionManager collisionManager;


    /**
     * Default constructor.
     */
    public WeeklyCalendarGeneratorImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodTypes getPeriodType() throws PortletException {
        return PeriodTypes.WEEK;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected CalendarData generateSpecializedCalendarData(PortalControllerContext portalControllerContext) throws PortletException {
        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();

        CalendarData calendarData = new CalendarData();

        // Selected date
        Date selectedDate = this.getSelectedDate(portalControllerContext, this.getPeriodType());
        // Start date
        Date startDate = this.getStartDate(portalControllerContext, this.getPeriodType(), selectedDate);
//
//        // Headers
//        calendarData.setHeaders(this.getHeaders(portalControllerContext, startDate));

        // Today week day
        Calendar todayCalendar = GregorianCalendar.getInstance(locale);
        Calendar startCalendar = GregorianCalendar.getInstance(locale);
        startCalendar.setTime(startDate);
        if ((todayCalendar.get(Calendar.YEAR) == startCalendar.get(Calendar.YEAR))
                && (todayCalendar.get(Calendar.WEEK_OF_YEAR) == startCalendar.get(Calendar.WEEK_OF_YEAR))) {
            // Same week

            int dayOfWeek = ((todayCalendar.get(Calendar.DAY_OF_WEEK) - todayCalendar.getFirstDayOfWeek()) + 1) % ICalendarGenerator.DAYS_IN_WEEK;
            if (dayOfWeek == 0) {
                dayOfWeek = DAYS_IN_WEEK;
            }

//            calendarData.setToday(dayOfWeek);
        }

//        // Auto-scroll
//        int position = DEFAULT_SCROLL_TOP_HOUR * HOUR_DISPLAY_HEIGHT;
//        calendarData.setAutoScroll(String.valueOf(position));

        return calendarData;
    }


//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void updateSpecializedCalendarData(PortalControllerContext portalControllerContext, CalendarData calendarData) {
//        WeeklyCalendarData weeklyCalendarData = (WeeklyCalendarData) calendarData;
//        Date startDate = calendarData.getStartDate();
//
////        // Headers
////        weeklyCalendarData.setHeaders(this.getHeaders(portalControllerContext, startDate));
//    }


//    /**
//     * Get calendar headers.
//     *
//     * @param portalControllerContext portal controller context
//     * @param startDate start date
//     * @return calendar headers
//     */
//    private List<WeeklyCalendarHeader> getHeaders(PortalControllerContext portalControllerContext, Date startDate) {
//        // User locale
//        Locale locale = portalControllerContext.getRequest().getLocale();
//        // Calendar
//        Calendar calendar = GregorianCalendar.getInstance(locale);
//        calendar.setTime(startDate);
//        // Date format
//        DateFormat weekDayFormat = new SimpleDateFormat("EEE", locale);
//        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
//
//        // Headers
//        List<WeeklyCalendarHeader> headers = new ArrayList<WeeklyCalendarHeader>(DAYS_IN_WEEK);
//        for (int i = 0; i < DAYS_IN_WEEK; i++) {
//            Date date = calendar.getTime();
//
//            // Header
//            WeeklyCalendarHeader header = new WeeklyCalendarHeader(date);
//
//            StringBuilder builder = new StringBuilder();
//            builder.append(weekDayFormat.format(date));
//            builder.append(" ");
//            builder.append(dateFormat.format(date));
//            header.setName(builder.toString());
//
//            headers.add(header);
//
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//        }
//        return headers;
//    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDisplayDate(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        // Bundle
        Bundle bundle = this.getBundleFactory().getBundle(portalControllerContext.getRequest().getLocale());
        // Calendar
        Calendar calendar = DateUtils.toCalendar(calendarData.getSelectedDate());

        StringBuilder builder = new StringBuilder();
        builder.append(bundle.getString("CALENDAR_WEEK"));
        builder.append(" ");
        builder.append(calendar.get(Calendar.WEEK_OF_YEAR));
        return builder.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected EventsData generateSpecializedEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData, List<Event> events)
            throws PortletException {
        // User locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Start calendar
        Calendar startCalendar = GregorianCalendar.getInstance(locale);

        // Events
        List<DailyEvent> dailyEvents = new ArrayList<DailyEvent>(events.size());
        for (Event event : events) {
            // Current date
            Date currentDate = DateUtils.truncate(startCalendar.getTime(), Calendar.DAY_OF_MONTH);

            // Time slot event
            TimeSlotEvent timeSlotEvent = new TimeSlotEvent(event, currentDate);
            
            // Add to events list
            dailyEvents.add(timeSlotEvent);
        }

        // Events data
        DailyCalendarEventsData eventsData = new DailyCalendarEventsData();
        eventsData.setEvents(dailyEvents);
        return eventsData;
    }
    /**protected EventsData generateSpecializedEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData, List<Event> events)
            throws PortletException {
        // User locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Bundle
        Bundle bundle = this.getBundleFactory().getBundle(locale);
        // Time format
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        // Start calendar
        Calendar startCalendar = GregorianCalendar.getInstance(locale);
        // End calendar
        Calendar endCalendar = GregorianCalendar.getInstance(locale);
        // String builder
        StringBuilder builder;

        // Events
        List<DailyEvent> dailyEvents = new ArrayList<DailyEvent>(events.size());
        SortedMap<String, List<DailyEvent>> fullDayEvents = new TreeMap<String, List<DailyEvent>>();
        for (Event event : events) {
            startCalendar.setTime(event.getStartDate());
            endCalendar.setTime(event.getEndDate());


            if (!event.isAllDay() && DateUtils.isSameDay(startCalendar, endCalendar)) {
                // Current date
                Date currentDate = DateUtils.truncate(startCalendar.getTime(), Calendar.DAY_OF_MONTH);

                // Time slot event
                TimeSlotEvent timeSlotEvent = new TimeSlotEvent(event, currentDate);
                timeSlotEvent.setBegin(true);
                timeSlotEvent.setEnd(true);

                // Time
                builder = new StringBuilder();
                builder.append(timeFormat.format(event.getStartDate()));
                builder.append(" - ");
                builder.append(timeFormat.format(event.getEndDate()));
                timeSlotEvent.setTime(builder.toString());

                // Top
                builder = new StringBuilder();
                builder.append((DateUtils.getFragmentInMilliseconds(startCalendar, Calendar.DAY_OF_MONTH) / Float.valueOf(DateUtils.MILLIS_PER_HOUR))
                        * HOUR_DISPLAY_HEIGHT);
                builder.append("px");
                timeSlotEvent.setTop(builder.toString());

                // Height
                builder = new StringBuilder();
                builder.append(this.getEventHours(startCalendar, endCalendar, true, true) * HOUR_DISPLAY_HEIGHT);
                builder.append("px");
                timeSlotEvent.setHeight(builder.toString());

                timeSlotEvent.setStartTime(event.getStartDate().getTime());
                timeSlotEvent.setEndTime(event.getEndDate().getTime());

                // Add to events list
                dailyEvents.add(timeSlotEvent);
            } else {
                // Full day events

                // Begin indicator
                boolean begin = true;

                while (begin || startCalendar.before(endCalendar)) {
                    // Current date
                    Date currentDate = DateUtils.truncate(startCalendar.getTime(), Calendar.DAY_OF_MONTH);

                    if (!currentDate.before(calendarData.getStartDate()) && currentDate.before(calendarData.getEndDate())) {
                        // Full day event
                        DailyEvent fullDayEvent = new DailyEvent(event, currentDate);
                        fullDayEvent.setBegin(begin);

                        // End indicator
                        boolean end = DateUtils.isSameDay(startCalendar, endCalendar);
                        fullDayEvent.setEnd(end);

                        // Time
                        builder = new StringBuilder();
                        if (!event.isAllDay() && begin) {
                            builder.append(timeFormat.format(event.getStartDate()));
                        } else if (!event.isAllDay() && end) {
                            builder.append(timeFormat.format(event.getEndDate()));
                        } else {
                            builder.append(bundle.getString("CALENDAR_EVENT_ALL_DAY"));
                        }
                        fullDayEvent.setTime(builder.toString());

                        // Map key
                        int dayOfWeek = ((startCalendar.get(Calendar.DAY_OF_WEEK) - startCalendar.getFirstDayOfWeek()) + 1) % ICalendarGenerator.DAYS_IN_WEEK;
                        if (dayOfWeek == 0) {
                            dayOfWeek = DAYS_IN_WEEK;
                        }
                        String key = "week-day-" + String.valueOf(dayOfWeek);

                        // Add to events map
                        List<DailyEvent> fullDayEventsList = fullDayEvents.get(key);
                        if (fullDayEventsList == null) {
                            fullDayEventsList = new ArrayList<DailyEvent>();
                            fullDayEvents.put(key, fullDayEventsList);
                        }
                        fullDayEventsList.add(fullDayEvent);
                    }

                    // Increment calendar
                    startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    startCalendar = DateUtils.truncate(startCalendar, Calendar.DAY_OF_MONTH);
                    begin = false;
                }
            }
        }
        // Collision manager
        this.collisionManager.updateWeeklyEvents(portalControllerContext, dailyEvents, calendarData.getSelectedDate());

        // Events data
        DailyCalendarEventsData eventsData = new DailyCalendarEventsData();
        eventsData.setEvents(dailyEvents);
        eventsData.setMappedEvents(fullDayEvents);
        return eventsData;
    }**/

}
