package org.osivia.services.workspace.portlet.repository;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DropProcedureCommand implements INuxeoCommand {

	private String path;

	public DropProcedureCommand(String path) {
		this.path = path;
		
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
		// Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        documentService.remove(path);
		
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
