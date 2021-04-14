package org.osivia.services.editor.image.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.*;
import org.osivia.services.editor.image.portlet.repository.EditorImageRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Copy image Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CopyImageCommand implements INuxeoCommand {

    /**
     * Source document path.
     */
    private String sourcePath;
    /**
     * Target document path.
     */
    private String targetPath;


    /**
     * Constructor.
     */
    public CopyImageCommand() {
        super();
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        // Source
        Document source = documentService.getDocument(new PathRef(this.sourcePath), "file");
        // Target
        DocRef target = new PathRef(this.targetPath);

        PropertyMap fileContent = source.getProperties().getMap("file:content");

        if (fileContent != null) {
            String data = fileContent.getString("data");
            Blob blob = nuxeoSession.getFile(data);
            documentService.setBlob(target, blob, EditorImageRepository.ATTACHED_IMAGES_PROPERTY);
        }

        return null;
    }


    @Override
    public String getId() {
        return null;
    }


    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
}
