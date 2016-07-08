package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvitationsCreationForm {

    /** Person identifiers . */
    private List<String> identifiers;
    /** Role. */
    private WorkspaceRole role;


    /**
     * Constructor.
     */
    public InvitationsCreationForm() {
        super();
    }


    /**
     * Getter for identifiers.
     * 
     * @return the identifiers
     */
    public List<String> getIdentifiers() {
        return identifiers;
    }

    /**
     * Setter for identifiers.
     * 
     * @param identifiers the identifiers to set
     */
    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * Getter for role.
     * 
     * @return the role
     */
    public WorkspaceRole getRole() {
        return role;
    }

    /**
     * Setter for role.
     * 
     * @param role the role to set
     */
    public void setRole(WorkspaceRole role) {
        this.role = role;
    }

}
