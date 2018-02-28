package org.osivia.services.workspace.portlet.model;

import java.util.Date;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.directory.v2.model.Person;

/**
 * Member object abstract super-class.
 * 
 * @author Cédric Krommenhoek
 */
public abstract class MemberObject {

    /** Display name. */
    private String displayName;
    /** Extra. */
    private String extra;
    /** Date. */
    private Date date;
    /** Role. */
    private WorkspaceRole role;
    /** Edited indicator. */
    private boolean edited;
    /** Deleted indicator. */
    private boolean deleted;

    /** Person. */
    private final Person person;
    /** Identifier. */
    private final String id;


    /**
     * Constructor.
     * 
     * @param person person
     */
    public MemberObject(Person person) {
        super();
        this.person = person;
        this.id = person.getUid();
    }


    /**
     * Constructor used when no person was found with this UID.
     * 
     * @param uid person UID
     */
    protected MemberObject(String uid) {
        super();
        this.person = null;
        this.id = uid;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MemberObject)) {
            return false;
        }
        MemberObject other = (MemberObject) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }


    /**
     * Getter for displayName.
     * 
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter for displayName.
     * 
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter for extra.
     * 
     * @return the extra
     */
    public String getExtra() {
        return extra;
    }

    /**
     * Setter for extra.
     * 
     * @param extra the extra to set
     */
    public void setExtra(String extra) {
        this.extra = extra;
    }

    /**
     * Getter for date.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for date.
     * 
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for role.
     * 
     * @return the role
     */
    public WorkspaceRole getRole() {
        return role;
    }

    /**
     * Setter for role.
     * 
     * @param role the role to set
     */
    public void setRole(WorkspaceRole role) {
        this.role = role;
    }

    /**
     * Getter for edited.
     * 
     * @return the edited
     */
    public boolean isEdited() {
        return edited;
    }

    /**
     * Setter for edited.
     * 
     * @param edited the edited to set
     */
    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    /**
     * Getter for deleted.
     * 
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Setter for deleted.
     * 
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Getter for person.
     * 
     * @return the person
     */
    public Person getPerson() {
        return person;
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
