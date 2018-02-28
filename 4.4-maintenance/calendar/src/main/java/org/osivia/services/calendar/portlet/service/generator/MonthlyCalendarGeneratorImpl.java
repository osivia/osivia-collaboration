package org.osivia.services.calendar.portlet.service.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.portlet.PortletException;

import org.apache.commons.lang.time.DateUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.model.calendar.MonthlyCalendarData;
import org.osivia.services.calendar.portlet.model.calendar.MonthlyCalendarDay;
import org.osivia.services.calendar.portlet.model.calendar.MonthlyCalendarWeek;
import org.osivia.services.calendar.portlet.model.events.DailyCalendarEventsData;
import org.osivia.services.calendar.portlet.model.events.DailyEvent;
import org.osivia.services.calendar.portlet.model.events.Event;
import org.osivia.services.calendar.portlet.model.events.EventsData;
import org.osivia.services.calendar.portlet.utils.PeriodTypes;
import org.springframework.stereotype.Service;

/**
 * Monthly calendar generator implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarGeneratorImpl
 */
@Service(value = "monthlyCalendarGenerator")
public class MonthlyCalendarGeneratorImpl extends CalendarGeneratorImpl {

    /**
     * Default constructor.
     */
    public MonthlyCalendarGeneratorImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodTypes getPeriodType() throws PortletException {
        return PeriodTypes.MONTH;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected CalendarData generateSpecializedCalendarData(PortalControllerContext portalControllerContext) throws PortletException {
        MonthlyCalendarData calendarData = new MonthlyCalendarData();

        // Period type
        PeriodTypes periodType = this.getPeriodType();
        // Selected date
        Date selectedDate = this.getSelectedDate(portalControllerContext, periodType);
        // Start date
        Date startDate = this.getStartDate(portalControllerContext, periodType, selectedDate);
        // End date
        Date endDate = this.getEndDate(portalControllerContext, periodType, selectedDate, startDate);

        // Headers
        calendarData.setHeaders(this.getHeaders(portalControllerContext, startDate));

        // Weeks
        calendarData.setWeeks(this.getWeeks(portalControllerContext, selectedDate, startDate, endDate));

        return calendarData;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateSpecializedCalendarData(PortalControllerContext portalControllerContext, CalendarData calendarData) {
        MonthlyCalendarData monthlyCalendarData = (MonthlyCalendarData) calendarData;
        Date selectedDate = calendarData.getSelectedDate();
        Date startDate = calendarData.getStartDate();
        Date endDate = calendarData.getEndDate();

        // Headers
        monthlyCalendarData.setHeaders(this.getHeaders(portalControllerContext, startDate));

        // Weeks
        monthlyCalendarData.setWeeks(this.getWeeks(portalControllerContext, selectedDate, startDate, endDate));
    }


    /**
     * Get calendar headers.
     *
     * @param portalControllerContext portal controller context
     * @param startDate start date
     * @return calendar headers
     */
    private List<String> getHeaders(PortalControllerContext portalControllerContext, Date startDate) {
        // User locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Calendar
        Calendar calendar = GregorianCalendar.getInstance(locale);
        calendar.setTime(startDate);

        // Headers
        List<String> headers = new ArrayList<String>(DAYS_IN_WEEK);
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            headers.add(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return headers;
    }


    /**
     * Get monthly calendar weeks.
     *
     * @param portalControllerContext portal controller context
     * @param selectedDate selected date
     * @param startDate start date
     * @param endDate end date
     * @return monthly calendar weeks
     */
    private List<MonthlyCalendarWeek> getWeeks(PortalControllerContext portalControllerContext, Date selectedDate, Date startDate, Date endDate) {
        // User locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Calendar
        Calendar calendar = GregorianCalendar.getInstance(locale);
        calendar.setTime(startDate);
        // Current month
        int currentMonth = DateUtils.toCalendar(selectedDate).get(Calendar.MONTH);

        // Weeks
        List<MonthlyCalendarWeek> weeks = new ArrayList<MonthlyCalendarWeek>();
        while (!calendar.getTime().after(endDate)) {
            MonthlyCalendarWeek week = new MonthlyCalendarWeek();
            week.setName(String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));

            List<MonthlyCalendarDay> days = new ArrayList<MonthlyCalendarDay>(DAYS_IN_WEEK);
            week.setDays(days);
            for (int i = 0; i < DAYS_IN_WEEK; i++) {
                MonthlyCalendarDay day = new MonthlyCalendarDay(calendar.getTime());
                day.setName(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                day.setCurrentMonth(currentMonth == calendar.get(Calendar.MONTH));

                // Add month display to first day
                if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                    day.setMonthDisplay(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, locale));
                }

                days.add(day);

                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            weeks.add(week);
        }
        return weeks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDisplayDate(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        // Date format
        DateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", portalControllerContext.getRequest().getLocale());

        return dateFormat.format(calendarData.getSelectedDate());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected EventsData generateSpecializedEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData, List<Event> events)
            throws PortletException {
        // User locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Time format
        DateFormat timeFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, locale);
        // Start calendar
        Calendar startCalendar = GregorianCalendar.getInstance(locale);
        // End calendar
        Calendar endCalendar = GregorianCalendar.getInstance(locale);
        // String builder
        StringBuilder builder;

        // Events
        SortedMap<String, List<DailyEvent>> fullDayEvents = new TreeMap<String, List<DailyEvent>>();
        for (Event event : events) {
            startCalendar.setTime(event.getStartDate());
            endCalendar.setTime(event.getEndDate());

            // Begin indicator
            boolean begin = true;

            while (begin || startCalendar.before(endCalendar)) {
                // Current date
                Date currentDate = DateUtils.truncate(startCalendar.getTime(), Calendar.DAY_OF_MONTH);

                // Event
                DailyEvent fullDayEvent = new DailyEvent(event, currentDate);
                fullDayEvent.setBegin(begin);

                // End indicator
                boolean end = DateUtils.isSameDay(startCalendar, endCalendar);
                fullDayEvent.setEnd(end);

                // Time
                if (begin) {
                    fullDayEvent.setTime(timeFormat.format(event.getStartDate()));
                }

                // Map key
                builder = new StringBuilder();
                builder.append(startCalendar.get(Calendar.WEEK_OF_YEAR));
                builder.append(":");
                builder.append(startCalendar.get(Calendar.DAY_OF_MONTH));
                String key = builder.toString();

                // Add to events map
                List<DailyEvent> fullDayEventsList = fullDayEvents.get(key);
                if (fullDayEventsList == null) {
                    fullDayEventsList = new ArrayList<DailyEvent>();
                    fullDayEvents.put(key, fullDayEventsList);
                }
                fullDayEventsList.add(fullDayEvent);

                // Increment calendar
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                startCalendar = DateUtils.truncate(startCalendar, Calendar.DAY_OF_MONTH);
                begin = false;
            }
        }

        // Events data
        DailyCalendarEventsData eventsData = new DailyCalendarEventsData();
        eventsData.setMappedEvents(fullDayEvents);
        return eventsData;
    }

}
