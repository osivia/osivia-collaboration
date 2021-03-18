package org.osivia.services.workspace.portlet.model;

import java.util.List;
import java.util.Set;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Invitations form java-bean
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class InvitationsForm extends AbstractMembersForm<Invitation> {

    /** Invitation identifiers. */
    private Set<String> identifiers;
    /** Purge invitations history availability indicator. */
    private boolean purgeAvailable;
    /** Loaded invitations indicator. */
    private boolean loaded;
    
    /**
     * Message if a batch is running
     */
	private boolean batchRunning = false;
	
	/**
	 * Time estimated
	 */
	private int timeEstimated;


    /**
     * Constructor.
     */
    public InvitationsForm() {
        super();
    }


    /**
     * Getter for invitations.
     * 
     * @return the invitations
     */
    public List<Invitation> getInvitations() {
        return this.getMembers();
    }

    /**
     * Setter for invitations.
     * 
     * @param invitations the invitations to set
     */
    public void setInvitations(List<Invitation> invitations) {
        this.setMembers(invitations);
    }

    /**
     * Getter for identifiers.
     * 
     * @return the identifiers
     */
    public Set<String> getIdentifiers() {
        return identifiers;
    }

    /**
     * Setter for identifiers.
     * 
     * @param identifiers the identifiers to set
     */
    public void setIdentifiers(Set<String> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * Getter for purgeAvailable.
     * 
     * @return the purgeAvailable
     */
    public boolean isPurgeAvailable() {
        return purgeAvailable;
    }

    /**
     * Setter for purgeAvailable.
     * 
     * @param purgeAvailable the purgeAvailable to set
     */
    public void setPurgeAvailable(boolean purgeAvailable) {
        this.purgeAvailable = purgeAvailable;
    }

    /**
     * Getter for loaded.
     * 
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Setter for loaded.
     * 
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }


    
    
	public boolean isBatchRunning() {
		return batchRunning;
	}

	public void setBatchRunning(boolean batchRunning) {
		this.batchRunning = batchRunning;

	}

	

	public int getTimeEstimated() {
		return timeEstimated;
	}


	public void setTimeEstimated(int timeEstimated) {
		this.timeEstimated = timeEstimated;
		
	}


}
