package org.osivia.services.editor.image.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.services.editor.image.portlet.repository.EditorImageRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Add attached image Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddAttachedImageCommand implements INuxeoCommand {

    /**
     * Document path.
     */
    private String path;
    /**
     * Attached image temporary file.
     */
    private File temporaryFile;
    /**
     * Attached image file name.
     */
    private String fileName;
    /**
     * Attached image content type.
     */
    private String contentType;


    /**
     * Constructor.
     */
    public AddAttachedImageCommand() {
        super();
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document
        DocRef document = new PathRef(this.path);
        // File blob
        Blob blob = new FileBlob(this.temporaryFile, this.fileName, this.contentType);

        documentService.setBlob(document, blob, EditorImageRepository.ATTACHED_IMAGES_PROPERTY);

        return null;
    }


    @Override
    public String getId() {
        return null;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void setTemporaryFile(File temporaryFile) {
        this.temporaryFile = temporaryFile;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
