package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Change invitations role form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractChangeRoleForm
 * @see Invitation
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChangeInvitationsRoleForm extends AbstractChangeRoleForm<Invitation> {

    /**
     * Constructor.
     */
    public ChangeInvitationsRoleForm() {
        super();
    }

}
