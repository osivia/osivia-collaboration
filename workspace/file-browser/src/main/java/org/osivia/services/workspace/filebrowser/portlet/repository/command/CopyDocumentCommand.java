package org.osivia.services.workspace.filebrowser.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Copy document Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CopyDocumentCommand implements INuxeoCommand {

    /** Source path. */
    private final String sourcePath;
    /** Target path. */
    private final String targetPath;


    /**
     * Constructor.
     * 
     * @param sourcePath source path
     * @param targetPath target path
     */
    public CopyDocumentCommand(String sourcePath, String targetPath) {
        super();
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Source
        DocRef source = new PathRef(this.sourcePath);
        // Target
        DocRef target = new PathRef(this.targetPath);

        return documentService.copy(source, target);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
