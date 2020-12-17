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
    private final String path;
    /**
     * Attached image temporary file.
     */
    private final File temporaryFile;
    /**
     * Attached image file name.
     */
    private final String fileName;
    /**
     * Attached image content type.
     */
    private final String contentType;


    /**
     * Constructor.
     *
     * @param path          document path
     * @param temporaryFile attached image temporary file
     * @param fileName      attached image file name
     * @param contentType   attached image content type
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AddAttachedImageCommand(String path, File temporaryFile, String fileName, String contentType) {
        super();
        this.path = path;
        this.temporaryFile = temporaryFile;
        this.fileName = fileName;
        this.contentType = contentType;
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

}
