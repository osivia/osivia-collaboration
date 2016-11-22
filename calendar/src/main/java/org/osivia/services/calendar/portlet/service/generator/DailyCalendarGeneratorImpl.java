package org.osivia.services.calendar.portlet.service.generator;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.model.calendar.DailyCalendarData;
import org.osivia.services.calendar.portlet.model.events.DailyCalendarEventsData;
import org.osivia.services.calendar.portlet.model.events.DailyEvent;
import org.osivia.services.calendar.portlet.model.events.Event;
import org.osivia.services.calendar.portlet.model.events.EventsData;
import org.osivia.services.calendar.portlet.model.events.TimeSlotEvent;
import org.osivia.services.calendar.portlet.service.ICalendarCollisionManager;
import org.osivia.services.calendar.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Daily calendar generator implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarGeneratorImpl
 */
@Service(value = "dailyCalendarGenerator")
public class DailyCalendarGeneratorImpl extends CalendarGeneratorImpl {

    /** Collision manager. */
    @Autowired
    private ICalendarCollisionManager collisionManager;


    /**
     * Default constructor.
     */
    public DailyCalendarGeneratorImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodTypes getPeriodType() throws PortletException {
        return PeriodTypes.DAY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected CalendarData generateSpecializedCalendarData(PortalControllerContext portalControllerContext) throws PortletException {
        DailyCalendarData calendarData = new DailyCalendarData();

        // Selected date
        Date selectedDate = this.getSelectedDate(portalControllerContext, this.getPeriodType());

        // Header
        calendarData.setHeader(this.getHeader(portalControllerContext, selectedDate));

        // Auto-scroll
        int position = DEFAULT_SCROLL_TOP_HOUR * HOUR_DISPLAY_HEIGHT;
        calendarData.setAutoScroll(String.valueOf(position));

        return calendarData;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateSpecializedCalendarData(PortalControllerContext portalControllerContext, CalendarData calendarData) {
        DailyCalendarData dailyCalendarData = (DailyCalendarData) calendarData;
        Date selectedDate = calendarData.getSelectedDate();

        // Header
        dailyCalendarData.setHeader(this.getHeader(portalControllerContext, selectedDate));
    }


    /**
     * Get calendar header.
     *
     * @param portalControllerContext portal controller context
     * @param selectedDate selected date
     * @return calendar header
     */
    private String getHeader(PortalControllerContext portalControllerContext, Date selectedDate) {
        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Date format
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);

        return dateFormat.format(selectedDate);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getDisplayDate(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
        // Date format
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, portalControllerContext.getRequest().getLocale());

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
        // Bundle
        Bundle bundle = this.getBundleFactory().getBundle(locale);
        // Time format
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        // Selected date
        Date selectedDate = calendarData.getSelectedDate();
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

            // Begin & end indicators
            boolean begin = DateUtils.isSameDay(selectedDate, event.getStartDate());
            boolean end = DateUtils.isSameDay(selectedDate, event.getEndDate());

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
            String time = builder.toString();


            // Event
            DailyEvent dailyEvent;
            if (!event.isAllDay() && begin && end) {
                // Time slot event
                TimeSlotEvent timeSlotEvent = new TimeSlotEvent(event, selectedDate);
                dailyEvent = timeSlotEvent;

                // Top
                String top;
                if (begin) {
                    builder = new StringBuilder();
                    builder.append((DateUtils.getFragmentInMilliseconds(startCalendar, Calendar.DAY_OF_MONTH) / Float.valueOf(DateUtils.MILLIS_PER_HOUR))
                            * HOUR_DISPLAY_HEIGHT);
                    builder.append("px");
                    top = builder.toString();
                } else {
                    top = "0";
                }
                timeSlotEvent.setTop(top);

                // Height
                builder = new StringBuilder();
                builder.append(this.getEventHours(startCalendar, endCalendar, begin, end) * HOUR_DISPLAY_HEIGHT);
                builder.append("px");
                timeSlotEvent.setHeight(builder.toString());

                timeSlotEvent.setStartTime(event.getStartDate().getTime());
                timeSlotEvent.setEndTime(event.getEndDate().getTime());

                dailyEvents.add(timeSlotEvent);
            } else {
                // Full day event
                dailyEvent = new DailyEvent(event, selectedDate);

                List<DailyEvent> fullDayEventsList = fullDayEvents.get(StringUtils.EMPTY);
                if (fullDayEventsList == null) {
                    fullDayEventsList = new ArrayList<DailyEvent>();
                    fullDayEvents.put(StringUtils.EMPTY, fullDayEventsList);
                }
                fullDayEventsList.add(dailyEvent);
            }
            dailyEvent.setBegin(begin);
            dailyEvent.setEnd(end);
            dailyEvent.setTime(time);
        }

        // Collision manager
        this.collisionManager.updateDailyEvents(portalControllerContext, dailyEvents, selectedDate);

        // Events data
        DailyCalendarEventsData eventsData = new DailyCalendarEventsData();
        eventsData.setEvents(dailyEvents);
        eventsData.setMappedEvents(fullDayEvents);
        return eventsData;
    }

}
