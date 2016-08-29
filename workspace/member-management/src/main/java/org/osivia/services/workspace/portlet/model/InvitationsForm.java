package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitations form java-bean
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class InvitationsForm {

    /** Pending invitations. */
    private List<Invitation> pending;
    /** History invitations. */
    private List<Invitation> history;


    /**
     * Constructor.
     */
    public InvitationsForm() {
        super();
    }


    /**
     * Getter for pending.
     * 
     * @return the pending
     */
    public List<Invitation> getPending() {
        return pending;
    }

    /**
     * Setter for pending.
     * 
     * @param pending the pending to set
     */
    public void setPending(List<Invitation> pending) {
        this.pending = pending;
    }

    /**
     * Getter for history.
     * 
     * @return the history
     */
    public List<Invitation> getHistory() {
        return history;
    }

    /**
     * Setter for history.
     * 
     * @param history the history to set
     */
    public void setHistory(List<Invitation> history) {
        this.history = history;
    }

}
