package org.osivia.services.calendar.portlet.model.calendar;

/**
 * Scrollable calendar data abstract super-class for daily and weekly views.
 *
 * @author Cédric Krommenhoek
 * @see CalendarData
 */
public abstract class ScrollableCalendarData extends CalendarData {

    /** Auto-scrolling size (in px). */
    private String autoScroll;


    /**
     * Default constructor.
     */
    public ScrollableCalendarData() {
        super();
    }


    /**
     * Getter for autoScroll.
     *
     * @return the autoScroll
     */
    public String getAutoScroll() {
        return this.autoScroll;
    }

    /**
     * Setter for autoScroll.
     *
     * @param autoScroll the autoScroll to set
     */
    public void setAutoScroll(String autoScroll) {
        this.autoScroll = autoScroll;
    }

}
