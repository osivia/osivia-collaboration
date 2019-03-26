package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Add invitations to group form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractAddToGroupForm
 * @see Invitation
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddInvitationsToGroupForm extends AbstractAddToGroupForm<Invitation> {

    /**
     * Constructor.
     */
    public AddInvitationsToGroupForm() {
        super();
    }

}
