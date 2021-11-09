package org.osivia.services.workspace.filebrowser.portlet.repository.command;

import java.util.List;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.DocRefs;
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
    private final List<String> sourcePaths;
    /** Target path. */
    private final String targetPath;


    /**
     * Constructor.
     * 
     * @param sourcePath source path
     * @param targetPath target path
     */
    public CopyDocumentCommand(List<String> sourcePaths, String targetPath) {
        super();
        this.sourcePaths = sourcePaths;
        this.targetPath = targetPath;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        DocRefs sources = new DocRefs();
        for(String path : sourcePaths) {
        	sources.add(new PathRef(path));
        }
        // Target
        DocRef target = new PathRef(this.targetPath);

        return documentService.copy(sources, target);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
