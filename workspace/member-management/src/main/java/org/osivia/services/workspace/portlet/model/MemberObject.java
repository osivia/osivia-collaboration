package org.osivia.services.workspace.portlet.model;

import javax.naming.Name;

import org.osivia.portal.api.directory.v2.model.Person;

/**
 * Member object abstract super-class.
 * 
 * @author Cédric Krommenhoek
 */
public abstract class MemberObject {

    /** Deleted indicator. */
    private boolean deleted;

    /** Identifier. */
    private final String id;
    /** DN. */
    private final Name dn;
    /** Display name. */
    private final String displayName;
    /** Avatar URL. */
    private final String avatar;
    /** Mail. */
    private final String mail;


    /**
     * Constructor.
     * 
     * @param person person
     */
    public MemberObject(Person person) {
        super();
        this.id = person.getUid();
        this.dn = person.getDn();
        this.displayName = person.getDisplayName();
        this.avatar = person.getAvatar().getUrl();
        this.mail = person.getMail();
    }


    /**
     * Constructor used when no person was found with this UID.
     * 
     * @param uid person UID
     */
    protected MemberObject(String uid) {
        super();
        this.id = uid;
        this.dn = null;
        this.displayName = uid;
        this.avatar = null;
        this.mail = null;
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
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for dn.
     * 
     * @return the dn
     */
    public Name getDn() {
        return dn;
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
     * Getter for avatar.
     * 
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Getter for mail.
     * 
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

}
