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
