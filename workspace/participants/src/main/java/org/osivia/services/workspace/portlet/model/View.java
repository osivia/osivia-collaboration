package org.osivia.services.workspace.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Participant views enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum View {

    /** Full view. */
    FULL,
    /** Compact view. */
    COMPACT;


    /** Default view. */
    public static final View DEFAULT = COMPACT;


    /** View path. */
    private final String path;
    /** View internationalization key. */
    private final String key;


    /**
     * Constructor.
     */
    private View() {
        this.path = "view-" + StringUtils.lowerCase(this.toString());
        this.key = "WORKSPACE_PARTICIPANTS_VIEW_" + StringUtils.upperCase(this.toString());
    }


    /**
     * Get view.
     * 
     * @param view
     * @return view
     */
    public static View get(String view) {
        View result = DEFAULT;
        for (View value : View.values()) {
            if (StringUtils.equals(value.toString(), view)) {
                result = value;
                break;
            }
        }
        return result;
    }


    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

}
