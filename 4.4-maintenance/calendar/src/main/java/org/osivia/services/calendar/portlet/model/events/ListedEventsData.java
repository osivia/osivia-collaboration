package org.osivia.services.calendar.portlet.model.events;

import java.util.List;

/**
 * Listed events data abstract super-class
 * 
 * @author Cédric Krommenhoek
 * @param <T> event type
 * @see EventsData
 * @see Event
 */
public abstract class ListedEventsData<T extends Event> extends EventsData {

    /** Events. */
    private List<T> events;


    /**
     * Default constructor.
     */
    public ListedEventsData() {
        super();
    }


    /**
     * Getter for events.
     *
     * @return the events
     */
    public List<T> getEvents() {
        return this.events;
    }

    /**
     * Setter for events.
     *
     * @param events the events to set
     */
    public void setEvents(List<T> events) {
        this.events = events;
    }

}
