package org.osivia.services.calendar.portlet.model.events;

import java.util.Date;

/**
 * Time slot event.
 *
 * @author Cédric Krommenhoek
 * @see DailyEvent
 */
public class TimeSlotEvent extends DailyEvent {

    /** Event top position. */
    private String top;
    /** Event left position. */
    private String left;
    /** Event height. */
    private String height;
    /** Event width. */
    private String width;

    /** Event start time. */
    private long startTime;
    /** Event end time. */
    private long endTime;


    /**
     * Constructor.
     *
     * @param event event
     * @param currentDate current date
     */
    public TimeSlotEvent(Event event, Date currentDate) {
        super(event, currentDate);
    }


    /**
     * Getter for top.
     *
     * @return the top
     */
    public String getTop() {
        return this.top;
    }

    /**
     * Setter for top.
     *
     * @param top the top to set
     */
    public void setTop(String top) {
        this.top = top;
    }

    /**
     * Getter for left.
     *
     * @return the left
     */
    public String getLeft() {
        return this.left;
    }

    /**
     * Setter for left.
     *
     * @param left the left to set
     */
    public void setLeft(String left) {
        this.left = left;
    }

    /**
     * Getter for height.
     *
     * @return the height
     */
    public String getHeight() {
        return this.height;
    }

    /**
     * Setter for height.
     *
     * @param height the height to set
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * Getter for width.
     *
     * @return the width
     */
    public String getWidth() {
        return this.width;
    }

    /**
     * Setter for width.
     *
     * @param width the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Getter for startTime.
     *
     * @return the startTime
     */
    public long getStartTime() {
        return this.startTime;
    }

    /**
     * Setter for startTime.
     *
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter for endTime.
     *
     * @return the endTime
     */
    public long getEndTime() {
        return this.endTime;
    }

    /**
     * Setter for endTime.
     *
     * @param endTime the endTime to set
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}
