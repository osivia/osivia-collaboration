package org.osivia.services.workspace.portlet.model;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.directory.v2.model.Person;

/**
 * Member object abstract super-class.
 * 
 * @author Cédric Krommenhoek
 */
public abstract class MemberObject {

    /** Deleted indicator. */
    private boolean deleted;

    /** Person. */
    private final Person person;
    /** Identifier. */
    private final String id;
    /** Display name. */
    private final String displayName;
    /** Extra. */
    private final String extra;


    /**
     * Constructor.
     * 
     * @param person person
     */
    public MemberObject(Person person) {
        super();
        this.person = person;
        this.id = person.getUid();

        if (StringUtils.isEmpty(person.getDisplayName())) {
            this.displayName = person.getUid();
            this.extra = null;
        } else {
            this.displayName = person.getDisplayName();
            this.extra = person.getMail();
        }
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
        this.displayName = uid;
        this.extra = null;
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

    /**
     * Getter for displayName.
     * 
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Getter for extra.
     * 
     * @return the extra
     */
    public String getExtra() {
        return extra;
    }

}
