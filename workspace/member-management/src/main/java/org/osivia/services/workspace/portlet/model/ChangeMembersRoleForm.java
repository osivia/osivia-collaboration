package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Change members role form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractChangeRoleForm
 * @see Member
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChangeMembersRoleForm extends AbstractChangeRoleForm<Member> {

    /**
     * Constructor.
     */
    public ChangeMembersRoleForm() {
        super();
    }

}
