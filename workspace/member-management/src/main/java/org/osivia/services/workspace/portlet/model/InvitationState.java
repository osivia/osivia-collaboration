package org.osivia.services.workspace.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Invitation states enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum InvitationState {

    /** Invitation sent. */
    SENT("info"),
    /** Accepted invitation. */
    ACCEPTED("success"),
    /** Rejected invitation. */
    REJECTED("danger");


    /** Internationalization key. */
    private final String key;
    /** HTML classes. */
    private final String htmlClasses;


    /**
     * Constructor.
     * 
     * @param color color
     */
    private InvitationState(String color) {
        this.key = "INVITATION_STATE_" + StringUtils.upperCase(this.name());
        this.htmlClasses = "label label-" + color;
    }


    /**
     * Get invitation state from its name.
     * 
     * @param name name
     * @return invitation state
     */
    public static InvitationState fromName(String name) {
        InvitationState result = null;
        for (InvitationState value : InvitationState.values()) {
            if (value.name().equals(name)) {
                result = value;
                break;
            }
        }
        return result;
    }


    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Getter for htmlClasses.
     * 
     * @return the htmlClasses
     */
    public String getHtmlClasses() {
        return htmlClasses;
    }

}
