package org.osivia.services.workspace.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReIndexCommand implements INuxeoCommand {


    /** Form. */
    private final WorkspaceEditionForm form;
    
    /**
     * Constructor.
     *
     * @param form form
     */
    public ReIndexCommand(WorkspaceEditionForm form) {
        super();
        this.form = form;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        // Workspace
        Document workspace = this.form.getDocument();
    	
		OperationRequest reindex = nuxeoSession.newRequest("Document.ReIndexES");
		reindex.set("repositoryName", workspace.getRepository());
		reindex.set("type", "QUERY");
		reindex.set("query", "SELECT * FROM Document where ecm:path STARTSWITH '"
				+ workspace.getPath() + "'");
		reindex.execute();
		
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
