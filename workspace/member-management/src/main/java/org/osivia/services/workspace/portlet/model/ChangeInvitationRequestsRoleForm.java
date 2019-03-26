package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Change invitation requests role form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractChangeRoleForm
 * @see InvitationRequest
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChangeInvitationRequestsRoleForm extends AbstractChangeRoleForm<InvitationRequest> {

    /**
     * Constructor.
     */
    public ChangeInvitationRequestsRoleForm() {
        super();
    }

}
