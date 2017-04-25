package org.osivia.services.workspace.portlet.model;

/**
 * ACL entry java-bean.
 *
 * @author Cédric Krommenhoek
 * @see Record
 */
public class AclEntry extends Record {

    /** Role. */
    private Role role;
    /** Updated indicator. */
    private boolean updated;
    /** Deleted indicator. */
    private boolean deleted;


    /**
     * Constructor.
     */
    public AclEntry() {
        super();
    }



    /**
     * Getter for role.
     *
     * @return the role
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Setter for role.
     *
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Getter for updated.
     * 
     * @return the updated
     */
    public boolean isUpdated() {
        return this.updated;
    }

    /**
     * Setter for updated.
     * 
     * @param updated the updated to set
     */
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    /**
     * Getter for deleted.
     *
     * @return the deleted
     */
    public boolean isDeleted() {
        return this.deleted;
    }

    /**
     * Setter for deleted.
     *
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
