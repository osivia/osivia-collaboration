package org.osivia.services.calendar.portlet.service.generator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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
     * Constructor.
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

//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void updateSpecializedCalendarData(PortalControllerContext portalControllerContext, CalendarData calendarData) {
//        // Do nothing
//    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDisplayDate(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        return null;
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
        SortedMap<PlanningCalendarEventHeader, List<DailyEvent>> planningEvents = new TreeMap<PlanningCalendarEventHeader, List<DailyEvent>>();
        for (Event event : events) {
            startCalendar.setTime(event.getStartDate());
            endCalendar.setTime(event.getEndDate());

            // Begin indicator
            boolean begin = true;

            while (begin || startCalendar.before(endCalendar)) {
                // Current date
                Date currentDate = DateUtils.truncate(startCalendar.getTime(), Calendar.DAY_OF_MONTH);

                if (!currentDate.before(calendarData.getStartDate())) {
                    // Planning event
                    DailyEvent planningEvent = new DailyEvent(event, currentDate);

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


                    List<DailyEvent> dailyEvents = planningEvents.get(header);
                    if (dailyEvents == null) {
                        dailyEvents = new ArrayList<DailyEvent>();
                        planningEvents.put(header, dailyEvents);
                    }
                    dailyEvents.add(planningEvent);
                }

                // Increment calendar
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                startCalendar = DateUtils.truncate(startCalendar, Calendar.DAY_OF_MONTH);
                begin = false;
            }
        }
        
        
        // Events data
        PlanningCalendarEventsData eventsData = new PlanningCalendarEventsData();


        if (compact && (planningEvents.size() > PLANNING_COMPACT_MAX)) {
            Set<PlanningCalendarEventHeader> keySet = planningEvents.keySet();
            PlanningCalendarEventHeader[] keys = keySet.toArray(new PlanningCalendarEventHeader[keySet.size()]);
            PlanningCalendarEventHeader key = keys[PLANNING_COMPACT_MAX];
            planningEvents = planningEvents.headMap(key);
            
            // Last date
            PlanningCalendarEventHeader lastKey = planningEvents.lastKey();
            eventsData.setLastDate(lastKey.getDate());
        }
        
        eventsData.setMappedEvents(planningEvents);

        return eventsData;
    }

}
