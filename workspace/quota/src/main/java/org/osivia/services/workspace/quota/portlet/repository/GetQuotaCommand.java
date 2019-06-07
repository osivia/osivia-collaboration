package org.osivia.services.workspace.quota.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.portal.core.web.IWebIdService;
import org.osivia.services.workspace.quota.portlet.model.QuotaItem;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get trashed documents Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetQuotaCommand implements INuxeoCommand {

	/** User Id */
	private final String path;

	/**
	 * Constructor.
	 * 
	 * @param basePath
	 *            base path
	 */
	public GetQuotaCommand(String path) {
		super();
		this.path = path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
        
		List<QuotaItem> items = new ArrayList<>();

        DocRef workspace = new PathRef(path);
		OperationRequest request = nuxeoSession.newRequest("Quota.GetInfo");
		request.setInput(workspace);

		Object quota = request.execute();

		return quota;
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
