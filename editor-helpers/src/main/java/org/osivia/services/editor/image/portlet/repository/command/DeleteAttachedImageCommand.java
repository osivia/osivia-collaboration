package org.osivia.services.editor.image.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.services.editor.image.portlet.repository.EditorImageRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Delete attached image Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeleteAttachedImageCommand implements INuxeoCommand {

    /**
     * Document path.
     */
    private final String path;
    /**
     * Attached image index.
     */
    private final int index;


    /**
     * Constructor.
     *
     * @param path  document path
     * @param index attached image index
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DeleteAttachedImageCommand(String path, int index) {
        super();
        this.path = path;
        this.index = index;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document
        DocRef document = new PathRef(this.path);
        // XPath
        String xpath = EditorImageRepository.ATTACHED_IMAGES_PROPERTY + "/item[" + this.index + "]";

        documentService.removeBlob(document, xpath);

        return null;
    }


    @Override
    public String getId() {
        return null;
    }

}
