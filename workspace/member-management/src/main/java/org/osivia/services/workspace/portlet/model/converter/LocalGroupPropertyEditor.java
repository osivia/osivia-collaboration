package org.osivia.services.workspace.portlet.model.converter;

import java.beans.PropertyEditorSupport;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Local group property editor.
 * 
 * @author Cédric Krommenhoek
 * @see PropertyEditorSupport
 */
@Component
public class LocalGroupPropertyEditor extends PropertyEditorSupport {

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;


    /**
     * Constructor.
     */
    public LocalGroupPropertyEditor() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        // Local group
        CollabProfile localGroup = this.workspaceService.getProfile(text);

        this.setValue(localGroup);
    }

}
