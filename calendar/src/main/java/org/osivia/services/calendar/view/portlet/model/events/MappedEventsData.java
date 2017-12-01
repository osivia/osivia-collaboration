package org.osivia.services.calendar.view.portlet.model.events;

import java.util.List;
import java.util.SortedMap;

/**
 * Mapped events abstract super-class.
 *
 * @author Cédric Krommenhoek
 * @param <ID> key type
 * @param <T> event type
 * @see ListedEventsData
 * @see Event
 */
public abstract class MappedEventsData<ID, T extends Event> extends ListedEventsData<T> {

    /** Mapped events. */
    private SortedMap<ID, List<T>> mappedEvents;


    /**
     * Default constructor.
     */
    public MappedEventsData() {
        super();
    }


    /**
     * Getter for mappedEvents.
     *
     * @return the mappedEvents
     */
    public SortedMap<ID, List<T>> getMappedEvents() {
        return this.mappedEvents;
    }

    /**
     * Setter for mappedEvents.
     *
     * @param mappedEvents the mappedEvents to set
     */
    public void setMappedEvents(SortedMap<ID, List<T>> mappedEvents) {
        this.mappedEvents = mappedEvents;
    }

}
