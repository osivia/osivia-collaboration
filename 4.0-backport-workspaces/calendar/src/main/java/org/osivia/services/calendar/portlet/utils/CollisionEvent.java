package org.osivia.services.calendar.portlet.utils;

import org.osivia.services.calendar.portlet.model.events.TimeSlotEvent;

/**
 * Collision event java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class CollisionEvent implements Comparable<CollisionEvent> {

    /** Column number. */
    private int column;
    /** Collisions count. */
    private int collisionsCount;
    /** Event. */
    private final TimeSlotEvent event;
    /** Initialized indicator. */
    private boolean initialized = false;


    /**
     * Constructor.
     *
     * @param event event
     */
    public CollisionEvent(TimeSlotEvent event) {
        super();
        this.event = event;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(CollisionEvent other) {
        Long start1 = this.getEvent().getStartTime();
        Long start2 = other.getEvent().getStartTime();

        int result = start1.compareTo(start2);
        if (result == 0) {
            Long end1 = this.getEvent().getEndTime();
            Long end2 = other.getEvent().getEndTime();

            result = -end1.compareTo(end2);
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.event == null) ? 0 : this.event.hashCode());
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CollisionEvent other = (CollisionEvent) obj;
        if (this.event == null) {
            if (other.event != null) {
                return false;
            }
        } else if (!this.event.equals(other.event)) {
            return false;
        }
        return true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CollisionEvent [event=" + this.event + "]";
    }


    /**
     * Getter for column.
     *
     * @return the column
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Setter for column.
     *
     * @param column the column to set
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Getter for collisionsCount.
     *
     * @return the collisionsCount
     */
    public int getCollisionsCount() {
        return this.collisionsCount;
    }

    /**
     * Setter for collisionsCount.
     *
     * @param collisionsCount the collisionsCount to set
     */
    public void setCollisionsCount(int collisionsCount) {
        this.collisionsCount = collisionsCount;
    }

    /**
     * Getter for event.
     *
     * @return the event
     */
    public TimeSlotEvent getEvent() {
        return this.event;
    }

    /**
     * Getter for initialized.
     *
     * @return the initialized
     */
    public boolean isInitialized() {
        return this.initialized;
    }

    /**
     * Setter for initialized.
     *
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

}
