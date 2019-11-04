package org.osivia.services.workspace.quota.portlet.repository;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get quota update Nuxeo command.
 * 
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateQuotaCommand implements INuxeoCommand {


    /** Quota size*/
    private Long size;
    
    /** Workspace path size*/
    private String path;


    /**
     * Constructor.
     * 
     * @param basePath base path
     */
    public UpdateQuotaCommand(String path,Long size) {
        super();

        this.size = size;
        this.path = path;
    }


    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        Document docRoot = documentService.getDocument(new PathRef(path)); 

        // Apply new quota
        documentService.setProperty(docRoot, "qt:maxSize", size !=null ? Long.toString(size) : null);

        // Remove link to procedure (if exists).
        documentService.setProperty(docRoot, "qtc:uuid", null);

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
        builder.append(this.path);
        return builder.toString();
    }

}
