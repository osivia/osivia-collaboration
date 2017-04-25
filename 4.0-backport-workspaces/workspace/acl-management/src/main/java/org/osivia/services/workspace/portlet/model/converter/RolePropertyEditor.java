package org.osivia.services.workspace.portlet.model.converter;

import java.beans.PropertyEditorSupport;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.services.workspace.portlet.model.Role;
import org.springframework.stereotype.Component;

/**
 * Role property editor.
 * 
 * @author Cédric Krommenhoek
 * @see PropertyEditorSupport
 */
@Component
public class RolePropertyEditor extends PropertyEditorSupport {

    /**
     * Constructor.
     */
    public RolePropertyEditor() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        WorkspaceRole workspaceRole = WorkspaceRole.fromId(text);
        Role role = new Role(workspaceRole);
        this.setValue(role);
    }

}
