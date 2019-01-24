package org.osivia.services.workspace.sharing.portlet.model;

import org.apache.commons.lang.StringUtils;

/**
 * Permissions enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum SharingPermission {

    /** Read permission. */
    READ("Read"),
    /** Write permission. */
    WRITE("ReadWrite");


    /** Default permission. */
    public static final SharingPermission DEFAULT = READ;


    /** Link permission identifier. */
    private final String id;
    /** Internationalization key. */
    private final String key;


    /**
     * Constructor.
     */
    private SharingPermission(String id) {
        this.id = id;
        this.key = "SHARING_LINK_PERMISSION_" + StringUtils.upperCase(this.name());
    }


    /**
     * Get permission from identifier.
     * 
     * @param id identifier
     * @return permission
     */
    public static SharingPermission fromId(String id) {
        SharingPermission result = DEFAULT;

        for (SharingPermission value : SharingPermission.values()) {
            if (StringUtils.equals(id, value.id)) {
                result = value;
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

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

}
