package org.osivia.services.workspace.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Invitation states enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum InvitationState {

    /** Invitation sent. */
    SENT(true, "info", "glyphicons glyphicons-question-sign"),
    /** Accepted invitation. */
    ACCEPTED(false, "success", "glyphicons glyphicons-ok-sign"),
    /** Rejected invitation. */
    REJECTED(false, "danger", "glyphicons glyphicons-remove-sign");


    /** Editable indicator. */
    private final boolean editable;
    /** Internationalization key. */
    private final String key;
    /** HTML classes. */
    private final String htmlClasses;
    /** Icon. */
    private final String icon;


    /**
     * Constructor.
     * 
     * @param editable editable indicator
     * @param color color
     * @param icon icon
     */
    private InvitationState(boolean editable, String color, String icon) {
        this.editable = editable;
        this.key = "WORKSPACE_MEMBER_MANAGEMENT_INVITATION_STATE_" + StringUtils.upperCase(this.name());
        this.htmlClasses = "text-" + color;
        this.icon = icon;
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
     * Getter for editable.
     * 
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
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

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

}
