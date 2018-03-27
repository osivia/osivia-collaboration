package org.osivia.services.calendar.view.portlet.model;

/**
 * Calendar edition modes enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum CalendarEditionMode {

    /** Creation mode. */
    CREATION,
    /** Edition mode. */
    EDITION;
	
	
    /** Default mode. */
    private static final CalendarEditionMode DEFAULT = CREATION;


    /** Identifier. */
    private final String id;


    /**
     * Constructor.
     */
    private CalendarEditionMode() {
        this.id = this.name();
    }


    /**
     * Get mode from identifier.
     *
     * @param id mode identifier
     * @return mode
     */
    public static CalendarEditionMode fromId(String id) {
    	CalendarEditionMode result = DEFAULT;
        for (CalendarEditionMode mode : CalendarEditionMode.values()) {
            if (mode.id.equals(id)) {
                result = mode;
                break;
            }
        }
        return result;
    }


    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }
}
