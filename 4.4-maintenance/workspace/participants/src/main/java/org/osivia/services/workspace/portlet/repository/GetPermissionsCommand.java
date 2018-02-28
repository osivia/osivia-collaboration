package org.osivia.services.workspace.portlet.repository;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONObject;

/**
 * Get permissions Nuxeo command
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetPermissionsCommand implements INuxeoCommand {

    /** Document. */
    private final Document document;


    /**
     * Constructor.
     *
     * @param document document
     */
    public GetPermissionsCommand(Document document) {
        super();
        this.document = document;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest("Document.GetACLs");
        operationRequest.setInput(this.document);

        // Execution
        Blob blob = (Blob) operationRequest.execute();

        // Result content
        String content = IOUtils.toString(blob.getStream(), "UTF-8");

        // JSON object
        return JSONObject.fromObject(content);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
        builder.append(this.document.getId());
        return builder.toString();
    }

}
