package org.osivia.services.calendar.view.portlet.model.events;

import java.util.Date;

/**
 * Daily event.
 *
 * @author Cédric Krommenhoek
 * @see Event
 */
public class DailyEvent extends Event implements Comparable<DailyEvent> {

    /** Begin indicator. */
    private boolean begin;
    /** End indicator. */
    private boolean end;

    /** Current date. */
    private final Date currentDate;


    /**
     * Constructor.
     *
     * @param event event
     * @param currentDate current date
     */
    public DailyEvent(Event event, Date currentDate) {
        super(event);
        this.currentDate = currentDate;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(DailyEvent other) {
        return this.currentDate.compareTo(other.currentDate);
    }


    /**
     * Getter for begin.
     *
     * @return the begin
     */
    public boolean isBegin() {
        return this.begin;
    }

    /**
     * Setter for begin.
     *
     * @param begin the begin to set
     */
    public void setBegin(boolean begin) {
        this.begin = begin;
    }

    /**
     * Getter for end.
     *
     * @return the end
     */
    public boolean isEnd() {
        return this.end;
    }

    /**
     * Setter for end.
     *
     * @param end the end to set
     */
    public void setEnd(boolean end) {
        this.end = end;
    }

    /**
     * Getter for currentDate.
     *
     * @return the currentDate
     */
    public Date getCurrentDate() {
        return this.currentDate;
    }

}
