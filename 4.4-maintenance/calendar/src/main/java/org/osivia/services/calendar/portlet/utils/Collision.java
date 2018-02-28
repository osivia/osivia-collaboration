package org.osivia.services.calendar.portlet.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Collision java-bean.
 *
 * @author Cédric Krommenhoek
 * @see Comparable
 */
public class Collision implements Comparable<Collision> {

    /** Collision begin time. */
    private long begin;
    /** Collision end time. */
    private long end;
    /** Collision events. */
    private final List<CollisionEvent> events;


    /**
     * Default constructor.
     */
    public Collision() {
        super();
        this.events = new ArrayList<CollisionEvent>();
    }

    /**
     * Constructor.
     *
     * @param events collision events
     */
    public Collision(List<CollisionEvent> events) {
        super();
        this.events = new ArrayList<CollisionEvent>(events);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Collision other) {
        Long begin1 = this.begin;
        Long begin2 = other.begin;
        return begin1.compareTo(begin2);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (int) (this.begin ^ (this.begin >>> 32));
        result = (prime * result) + (int) (this.end ^ (this.end >>> 32));
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
        Collision other = (Collision) obj;
        if (this.begin != other.begin) {
            return false;
        }
        if (this.end != other.end) {
            return false;
        }
        return true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Collision [begin=" + this.begin + ", end=" + this.end + ", events=" + this.events + "]";
    }


    /**
     * Getter for begin.
     *
     * @return the begin
     */
    public long getBegin() {
        return this.begin;
    }

    /**
     * Setter for begin.
     *
     * @param begin the begin to set
     */
    public void setBegin(long begin) {
        this.begin = begin;
    }

    /**
     * Getter for end.
     *
     * @return the end
     */
    public long getEnd() {
        return this.end;
    }

    /**
     * Setter for end.
     *
     * @param end the end to set
     */
    public void setEnd(long end) {
        this.end = end;
    }

    /**
     * Getter for events.
     *
     * @return the events
     */
    public List<CollisionEvent> getEvents() {
        return this.events;
    }

}
