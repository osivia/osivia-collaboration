package org.osivia.services.workspace.portlet.model;

import java.util.List;

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
public class InvitationsForm {

    /** Invitations. */
    private List<Invitation> invitations;
    /** Loaded invitations indicator. */
    private boolean loaded;


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
        return invitations;
    }

    /**
     * Setter for invitations.
     * 
     * @param invitations the invitations to set
     */
    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
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

}
