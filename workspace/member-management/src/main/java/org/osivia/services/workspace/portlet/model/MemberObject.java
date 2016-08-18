package org.osivia.services.workspace.portlet.model;

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
    
    private Person person;


    /**
     * Constructor.
     * 
     * @param person person
     */
    public MemberObject(Person person) {
        super();
        this.id = person.getUid();
        this.person = person;
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


	public Person getPerson() {
		return person;
	}


	public void setPerson(Person person) {
		this.person = person;
	}


}
