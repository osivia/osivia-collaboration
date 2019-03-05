package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Change invitation request role form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractChangeRoleForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChangeInvitationRequestRoleForm extends AbstractChangeRoleForm<InvitationRequest> {

    /**
     * Constructor.
     */
    public ChangeInvitationRequestRoleForm() {
        super();
    }


    /**
     * Getter for selectedInvitationRequests.
     * 
     * @return the selectedInvitationRequests
     */
    public List<InvitationRequest> getSelectedInvitationRequests() {
        return this.getMembers();
    }

    /**
     * Setter for selectedInvitationRequests.
     * 
     * @param selectedInvitationRequests the selectedInvitationRequests to set
     */
    public void setSelectedInvitationRequests(List<InvitationRequest> selectedInvitationRequests) {
        this.setMembers(selectedInvitationRequests);
    }

}
