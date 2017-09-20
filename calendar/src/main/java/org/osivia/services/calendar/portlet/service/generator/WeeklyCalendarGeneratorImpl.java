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


    /**
     * Default constructor.
     */
    public WeeklyCalendarGeneratorImpl() {
        super();
        periodType = PeriodTypes.WEEK;
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
        Date selectedDate = this.getSelectedDate(portalControllerContext, calendarData.getPeriodType());
        // Start date
        Date startDate = this.getStartDate(portalControllerContext, calendarData.getPeriodType(), selectedDate);
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

        return calendarData;
    }


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

}
