package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Add members to group form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractAddToGroupForm
 * @see Member
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddMembersToGroupForm extends AbstractAddToGroupForm<Member> {

    /**
     * Constructor.
     */
    public AddMembersToGroupForm() {
        super();
    }

}
