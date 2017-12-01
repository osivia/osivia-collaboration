package org.osivia.services.calendar.view.portlet.service.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletException;

import org.apache.commons.lang.time.DateUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.view.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.view.portlet.model.events.DailyCalendarEventsData;
import org.osivia.services.calendar.view.portlet.model.events.DailyEvent;
import org.osivia.services.calendar.view.portlet.model.events.Event;
import org.osivia.services.calendar.view.portlet.model.events.EventsData;
import org.osivia.services.calendar.view.portlet.model.events.TimeSlotEvent;
import org.osivia.services.calendar.view.portlet.utils.PeriodTypes;
import org.springframework.stereotype.Service;

/**
 * Weekly calendar generator implementation.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 * @see CalendarGeneratorImpl
 */
@Service(value = "schedulerCalendarGenerator")
public class SchedulerCalendarGeneratorImpl extends CalendarGeneratorImpl {


    /**
     * Default constructor.
     */
    public SchedulerCalendarGeneratorImpl() {
        super();
        periodType = PeriodTypes.WEEK;
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
