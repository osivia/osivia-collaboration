package org.osivia.services.calendar.portlet.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.time.DateUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.portlet.model.events.DailyEvent;
import org.osivia.services.calendar.portlet.model.events.TimeSlotEvent;
import org.osivia.services.calendar.portlet.service.generator.ICalendarGenerator;
import org.osivia.services.calendar.portlet.utils.Collision;
import org.osivia.services.calendar.portlet.utils.CollisionEvent;
import org.springframework.stereotype.Service;

/**
 * Calendar collision manager implementation.
 *
 * @author Cédric Krommenhoek
 * @see ICalendarCollisionManager
 */
@Service
public class CalendarCollisionManagerImpl implements ICalendarCollisionManager {

    /** Table width. */
    private static final float TABLE_WIDTH = 100;
    /** Day margin width. */
    private static final float DAY_MARGIN_WIDTH = 5;
    /** Week margin width. */
    private static final float WEEK_MARGIN_WIDTH = 1;


    /**
     * Default constructor.
     */
    public CalendarCollisionManagerImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDailyEvents(PortalControllerContext portalControllerContext, List<DailyEvent> events, Date date) {
        List<CollisionEvent> collisionEvents = this.getCollisionEvents(events, date);

        StringBuilder builder;

        // Update events
        for (CollisionEvent collisionEvent : collisionEvents) {
            TimeSlotEvent event = collisionEvent.getEvent();

            int column = collisionEvent.getColumn();
            int count = collisionEvent.getCollisionsCount();
            // Event width
            float eventWidth = (TABLE_WIDTH / Float.valueOf(count));

            // Left
            builder = new StringBuilder();
            builder.append(Float.valueOf(column - 1) * eventWidth);
            builder.append("%");
            event.setLeft(builder.toString());

            // Width
            builder = new StringBuilder();
            builder.append(eventWidth - DAY_MARGIN_WIDTH);
            builder.append("%");
            event.setWidth(builder.toString());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateWeeklyEvents(PortalControllerContext portalControllerContext, List<DailyEvent> events, Date date) {
        Calendar calendar = GregorianCalendar.getInstance(portalControllerContext.getRequest().getLocale());
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        StringBuilder builder;

        for (int i = 0; i < ICalendarGenerator.DAYS_IN_WEEK; i++) {
            // Week day
            Date weekDay = calendar.getTime();

            // Week day event
            List<DailyEvent> weekDayEvents = new ArrayList<DailyEvent>();
            for (DailyEvent event : events) {
                TimeSlotEvent timeSlotEvent = (TimeSlotEvent) event;

                if (DateUtils.isSameDay(weekDay, new Date(timeSlotEvent.getStartTime()))) {
                    weekDayEvents.add(event);
                }
            }

            List<CollisionEvent> collisionEvents = this.getCollisionEvents(weekDayEvents, weekDay);

            // Update events
            for (CollisionEvent collisionEvent : collisionEvents) {
                TimeSlotEvent event = collisionEvent.getEvent();

                int column = collisionEvent.getColumn();
                int count = collisionEvent.getCollisionsCount();
                // Column width
                float columnWidth = (TABLE_WIDTH / Float.valueOf(ICalendarGenerator.DAYS_IN_WEEK));
                // Event width
                float eventWidth = (columnWidth / Float.valueOf(count));

                // Left
                float left = ((columnWidth * Float.valueOf(i)) + (Float.valueOf(column - 1) * eventWidth));
                builder = new StringBuilder();
                builder.append(left);
                builder.append("%");
                event.setLeft(builder.toString());

                // Width
                builder = new StringBuilder();
                builder.append(eventWidth - WEEK_MARGIN_WIDTH);
                builder.append("%");
                event.setWidth(builder.toString());
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }


    /**
     * Get collision events.
     *
     * @param events events
     * @param date current date
     * @return collision events
     */
    private List<CollisionEvent> getCollisionEvents(List<DailyEvent> events, Date date) {
        // Collisions
        List<Collision> collisions = this.initCollisions(date);
        // Collision events
        List<CollisionEvent> collisionEvents = new ArrayList<CollisionEvent>(events.size());
        for (DailyEvent event : events) {
            TimeSlotEvent timeSlotEvent = (TimeSlotEvent) event;

            long start = timeSlotEvent.getStartTime();
            long end = timeSlotEvent.getEndTime();

            // Collision event
            CollisionEvent collisionEvent = new CollisionEvent(timeSlotEvent);
            collisionEvents.add(collisionEvent);

            List<Collision> collisionsClone = new ArrayList<Collision>(collisions);
            for (Collision collision : collisionsClone) {
                if ((collision.getBegin() < end) && (collision.getEnd() > start)) {
                    // Replace collision
                    collisions.remove(collision);

                    // Begin collision
                    if (collision.getBegin() < start) {
                        Collision beginCollision = new Collision(collision.getEvents());
                        beginCollision.setBegin(Math.min(collision.getBegin(), start));
                        beginCollision.setEnd(Math.max(collision.getBegin(), start));
                        if (collision.getBegin() > start) {
                            beginCollision.getEvents().add(collisionEvent);
                        }
                        collisions.add(beginCollision);
                    }

                    // Inner collision
                    Collision innerCollision = new Collision(collision.getEvents());
                    innerCollision.setBegin(Math.max(collision.getBegin(), start));
                    innerCollision.setEnd(Math.min(collision.getEnd(), end));
                    innerCollision.getEvents().add(collisionEvent);
                    collisions.add(innerCollision);

                    // End collision
                    if (collision.getEnd() > end) {
                        Collision endCollision = new Collision(collision.getEvents());
                        endCollision.setBegin(Math.min(collision.getEnd(), end));
                        endCollision.setEnd(Math.max(collision.getEnd(), end));
                        if (collision.getEnd() < end) {
                            endCollision.getEvents().add(collisionEvent);
                        }
                        collisions.add(endCollision);
                    }
                }
            }
        }
        Collections.sort(collisions);

        // Define column and collisions count
        for (Collision collision : collisions) {
            List<CollisionEvent> currentSortedCollisionEvents = collision.getEvents();
            Collections.sort(currentSortedCollisionEvents);

            for (CollisionEvent currentCollisionEvent : currentSortedCollisionEvents) {
                if (!currentCollisionEvent.isInitialized()) {
                    Set<CollisionEvent> currentCollisionEvents = this.getCollisionEvents(collisions, currentCollisionEvent);
                    int columnsCount = this.getColumnsCount(collisions, currentCollisionEvent);
                    int columnNumber = this.getColumnNumber(currentCollisionEvents, currentCollisionEvent, columnsCount);

                    currentCollisionEvent.setColumn(columnNumber);
                    currentCollisionEvent.setCollisionsCount(columnsCount);
                    currentCollisionEvent.setInitialized(true);
                }
            }
        }
        return collisionEvents;
    }


    /**
     * Collisions initialization.
     *
     * @param date current date
     * @return collisions
     */
    private List<Collision> initCollisions(Date date) {
        Collision collision = new Collision();
        long begin = DateUtils.truncate(date, Calendar.DAY_OF_MONTH).getTime();
        long end = begin + DateUtils.MILLIS_PER_DAY;
        collision.setBegin(begin);
        collision.setEnd(end);

        // Collisions initialization
        List<Collision> collisions = new ArrayList<Collision>();
        collisions.add(collision);
        return collisions;
    }


    /**
     * Get collision events.
     *
     * @param collisions collisions
     * @param collisionEvent collision event
     * @return collision events
     */
    private Set<CollisionEvent> getCollisionEvents(List<Collision> collisions, CollisionEvent collisionEvent) {
        // Event
        TimeSlotEvent event = collisionEvent.getEvent();

        Set<CollisionEvent> collisionsEvents = new TreeSet<CollisionEvent>();
        for (Collision collision : collisions) {
            if ((collision.getBegin() < event.getEndTime()) && (collision.getEnd() > event.getStartTime())) {
                collisionsEvents.addAll(collision.getEvents());
            }
        }
        return collisionsEvents;
    }


    /**
     * Get collisions count.
     *
     * @param collisions collisions
     * @param currentCollisionEvent current collision event
     * @return collisions count
     */
    private int getColumnsCount(List<Collision> collisions, CollisionEvent currentCollisionEvent) {
        int count = 1;
        for (Collision collision : collisions) {
            if (collision.getEvents().contains(currentCollisionEvent)) {
                count = Math.max(count, collision.getEvents().size());
            }
        }
        return count;
    }


    /**
     * Get column number.
     *
     * @param currentCollisionEvents current collision events
     * @param currentCollisionEvent current collision event
     * @param columnsCount columns count
     * @return column number
     */
    private int getColumnNumber(Set<CollisionEvent> currentCollisionEvents, CollisionEvent currentCollisionEvent, int columnsCount) {
        // Column numbers
        SortedSet<Integer> numbers = new TreeSet<Integer>();
        for (int i = 1; i <= columnsCount; i++) {
            numbers.add(i);
        }
        for (CollisionEvent collisionEvent : currentCollisionEvents) {
            if (collisionEvent.isInitialized()) {
                float eventBegin = Float.valueOf(collisionEvent.getColumn() - 1) / Float.valueOf(collisionEvent.getCollisionsCount());
                float eventEnd = Float.valueOf(collisionEvent.getColumn()) / Float.valueOf(collisionEvent.getCollisionsCount());

                for (int i = 1; i <= columnsCount; i++) {
                    float columnBegin = Float.valueOf(i - 1) / Float.valueOf(columnsCount);
                    float columnEnd = Float.valueOf(i) / Float.valueOf(columnsCount);

                    if ((eventBegin < columnEnd) && (eventEnd > columnBegin)) {
                        numbers.remove(i);
                    }
                }
            }
        }

        return numbers.first();
    }

}
