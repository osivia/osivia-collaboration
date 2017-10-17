package org.osivia.services.workspace.edition.portlet.repository.command;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.workspace.edition.portlet.model.Image;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Update workspace or room properties Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdatePropertiesCommand implements INuxeoCommand {

    /** Form. */
    private final WorkspaceEditionForm form;


    /**
     * Constructor.
     *
     * @param form form
     */
    public UpdatePropertiesCommand(WorkspaceEditionForm form) {
        super();
        this.form = form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Workspace
        Document workspace = this.form.getDocument();
        
        // Updated properties
        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", this.form.getTitle());
        properties.set("dc:description", this.form.getDescription());
        if (this.form.isRoot()) {
            properties.set("ttc:pageTemplate", StringUtils.trimToNull(this.form.getTemplate()));
        }
        documentService.update(workspace, properties, this.form.isRoot());

        // Visual
        Image visual = this.form.getVisual();
        if (visual.isUpdated()) {
            // Temporary file
            File temporaryFile = visual.getTemporaryFile();
            // File blob
            Blob blob = new FileBlob(temporaryFile);

            documentService.setBlob(workspace, blob, "ttc:vignette");
            documentService.setBlob(workspace, blob, "ttcn:picture");

            // Delete temporary file
            temporaryFile.delete();
        } else if (visual.isDeleted()) {
            documentService.removeBlob(workspace, "ttc:vignette");
            documentService.removeBlob(workspace, "ttcn:picture");
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
