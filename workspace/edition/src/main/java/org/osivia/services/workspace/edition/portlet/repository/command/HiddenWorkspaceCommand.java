package org.osivia.services.workspace.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HiddenWorkspaceCommand implements INuxeoCommand  {


    /** Workspace path. */
    private final String path;
    
    /**
     * Constructor.
     * 
     * @param path workspace path
     */
    public HiddenWorkspaceCommand(String path) {
        super();
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(Session nuxeoSession) throws Exception {
        // Document reference
        DocRef docRef = new DocRef(this.path);
        
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

		documentService.addFacets(docRef, "HiddenInNavigation");
		
		return null;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append(this.path);
        return builder.toString();
    }
}
