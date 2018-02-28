package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * ACL entry java-bean.
 *
 * @author Cédric Krommenhoek
 * @see Record
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AclEntry extends Record {

    /** Role. */
    private Role role;
    /** Editable. */
    private boolean editable;
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
     * Getter for editable.
     * 
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Setter for editable.
     * 
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
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
