package org.osivia.services.workspace.edition.portlet.repository;

import java.io.File;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
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
        documentService.setProperty(workspace, "dc:title", this.form.getTitle());
        documentService.setProperty(workspace, "dc:description", this.form.getDescription());
        
        // Vignette
        Image vignette = this.form.getVignette();
        if (vignette.isUpdated()) {
            // Temporary file
            File temporaryFile = vignette.getTemporaryFile();
            // File blob
            Blob blob = new FileBlob(temporaryFile);

            documentService.setBlob(workspace, blob, "ttc:vignette");

            // Delete temporary file
            temporaryFile.delete();
        } else if (vignette.isDeleted()) {
            documentService.removeBlob(workspace, "ttc:vignette");
        }

        // Banner
        Image banner = this.form.getBanner();
        if (banner.isUpdated()) {
            // Temporary file
            File temporaryFile = banner.getTemporaryFile();
            // File blob
            Blob blob = new FileBlob(temporaryFile);

            documentService.setBlob(workspace, blob, "ttcs:headImage");

            // Delete temporary file
            temporaryFile.delete();
        } else if (banner.isDeleted()) {
            documentService.removeBlob(workspace, "ttcs:headImage");
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
