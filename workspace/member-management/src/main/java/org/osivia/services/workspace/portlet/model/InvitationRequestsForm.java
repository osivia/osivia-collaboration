package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitation requests form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class InvitationRequestsForm {

    /** Invitation requests. */
    private List<InvitationRequest> requests;
    /** Loaded indicator. */
    private boolean loaded;


    /**
     * Constructor.
     */
    public InvitationRequestsForm() {
        super();
    }


    /**
     * Getter for requests.
     * 
     * @return the requests
     */
    public List<InvitationRequest> getRequests() {
        return requests;
    }

    /**
     * Setter for requests.
     * 
     * @param requests the requests to set
     */
    public void setRequests(List<InvitationRequest> requests) {
        this.requests = requests;
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
