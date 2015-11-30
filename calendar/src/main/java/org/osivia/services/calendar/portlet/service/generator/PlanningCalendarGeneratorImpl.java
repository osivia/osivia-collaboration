package org.osivia.services.calendar.portlet.service.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.portlet.PortletException;

import org.apache.commons.lang.time.DateUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.model.events.DailyEvent;
import org.osivia.services.calendar.portlet.model.events.Event;
import org.osivia.services.calendar.portlet.model.events.EventsData;
import org.osivia.services.calendar.portlet.model.events.PlanningCalendarCompactEventsData;
import org.osivia.services.calendar.portlet.model.events.PlanningCalendarEvent;
import org.osivia.services.calendar.portlet.model.events.PlanningCalendarEventHeader;
import org.osivia.services.calendar.portlet.model.events.PlanningCalendarEventsData;
import org.osivia.services.calendar.portlet.service.ICalendarService;
import org.osivia.services.calendar.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Planning calendar generator implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarGeneratorImpl
 */
@Service(value = "planningCalendarGenerator")
public class PlanningCalendarGeneratorImpl extends CalendarGeneratorImpl {

    /** Calendar service. */
    @Autowired
    private ICalendarService calendarService;


    /**
     * Default constructor.
     */
    public PlanningCalendarGeneratorImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodTypes getPeriodType() {
        return PeriodTypes.PLANNING;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected CalendarData generateSpecializedCalendarData(PortalControllerContext portalControllerContext) throws PortletException {
        return new CalendarData();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateSpecializedCalendarData(PortalControllerContext portalControllerContext, CalendarData calendarData) {
        // Do nothing
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDisplayDate(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        // Date format
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, portalControllerContext.getRequest().getLocale());

        StringBuilder builder = new StringBuilder();
        builder.append(dateFormat.format(calendarData.getStartDate()));
        builder.append(" - ");
        builder.append(dateFormat.format(calendarData.getEndDate()));
        return builder.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected EventsData generateSpecializedEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData, List<Event> events)
            throws PortletException {
        // Compact indicator
        boolean compact = this.calendarService.isCompact(portalControllerContext);
        // User locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Bundle
        Bundle bundle = this.getBundleFactory().getBundle(locale);
        // Time format
        DateFormat timeFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT, locale);
        // Start calendar
        Calendar startCalendar = GregorianCalendar.getInstance(locale);
        // End calendar
        Calendar endCalendar = GregorianCalendar.getInstance(locale);
        // String builder
        StringBuilder builder;

        // Events
        List<PlanningCalendarEvent> planningEvents = new ArrayList<PlanningCalendarEvent>(events.size());
        SortedMap<PlanningCalendarEventHeader, List<DailyEvent>> planningCompactEvents = new TreeMap<PlanningCalendarEventHeader, List<DailyEvent>>();
        for (Event event : events) {
            startCalendar.setTime(event.getStartDate());
            endCalendar.setTime(event.getEndDate());

            // Begin indicator
            boolean begin = true;

            while (begin || startCalendar.before(endCalendar)) {
                // Current date
                Date currentDate = DateUtils.truncate(startCalendar.getTime(), Calendar.DAY_OF_MONTH);

                if (!currentDate.before(calendarData.getStartDate()) && currentDate.before(calendarData.getEndDate())) {
                    // Planning event
                    DailyEvent planningEvent;
                    if (compact) {
                        planningEvent = new DailyEvent(event, currentDate);
                    } else {
                        planningEvent = new PlanningCalendarEvent(event, currentDate);
                    }

                    // Begin indicator
                    planningEvent.setBegin(begin);

                    // End indicator
                    boolean end = DateUtils.isSameDay(startCalendar, endCalendar);
                    planningEvent.setEnd(end);

                    // Time
                    builder = new StringBuilder();
                    if (event.isAllDay() || !(begin || end)) {
                        builder.append(bundle.getString("CALENDAR_EVENT_ALL_DAY"));
                    } else {
                        if (begin) {
                            builder.append(timeFormat.format(event.getStartDate()));
                            if (end) {
                                builder.append(" - ");
                            }
                        }
                        if (end) {
                            builder.append(timeFormat.format(event.getEndDate()));
                        }
                    }
                    planningEvent.setTime(builder.toString());

                    // Header
                    PlanningCalendarEventHeader header = new PlanningCalendarEventHeader(startCalendar.getTime());
                    // Day of month
                    header.setDayOfMonth(String.valueOf(startCalendar.get(Calendar.DAY_OF_MONTH)));
                    // Day of week
                    header.setDayOfWeek(startCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale));
                    // Month display
                    builder = new StringBuilder();
                    builder.append(startCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale));
                    builder.append(" ");
                    builder.append(startCalendar.get(Calendar.YEAR));
                    header.setMonth(builder.toString());


                    if (compact) {
                        List<DailyEvent> dailyEvents = planningCompactEvents.get(header);
                        if (dailyEvents == null) {
                            dailyEvents = new ArrayList<DailyEvent>();
                            planningCompactEvents.put(header, dailyEvents);
                        }
                        dailyEvents.add(planningEvent);
                    } else {
                        PlanningCalendarEvent planningCalendarEvent = (PlanningCalendarEvent) planningEvent;
                        planningCalendarEvent.setHeader(header);
                        planningEvents.add(planningCalendarEvent);
                    }
                }

                // Increment calendar
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                startCalendar = DateUtils.truncate(startCalendar, Calendar.DAY_OF_MONTH);
                begin = false;
            }
        }

        // Events data
        EventsData eventsData;
        if (compact) {
            PlanningCalendarCompactEventsData planningCompactEventsData = new PlanningCalendarCompactEventsData();
            eventsData = planningCompactEventsData;

            planningCompactEventsData.setMappedEvents(planningCompactEvents);
        } else {
            PlanningCalendarEventsData planningEventsData = new PlanningCalendarEventsData();
            eventsData = planningEventsData;

            // Sort events
            Collections.sort(planningEvents);
            planningEventsData.setEvents(planningEvents);
        }
        return eventsData;
    }

}
