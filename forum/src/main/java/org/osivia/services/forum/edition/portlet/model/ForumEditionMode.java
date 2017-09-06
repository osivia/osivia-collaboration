package org.osivia.services.forum.edition.portlet.model;

/**
 * Forum edition modes enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum ForumEditionMode {

    /** Creation mode. */
    CREATION,
    /** Edition mode. */
    EDITION;


    /** Default mode. */
    private static final ForumEditionMode DEFAULT = CREATION;


    /** Identifier. */
    private final String id;


    /**
     * Constructor.
     */
    private ForumEditionMode() {
        this.id = this.name();
    }


    /**
     * Get mode from identifier.
     *
     * @param id mode identifier
     * @return mode
     */
    public static ForumEditionMode fromId(String id) {
        ForumEditionMode result = DEFAULT;
        for (ForumEditionMode mode : ForumEditionMode.values()) {
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
