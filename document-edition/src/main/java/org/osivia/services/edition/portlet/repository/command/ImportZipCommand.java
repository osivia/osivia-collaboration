package org.osivia.services.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Import a zip file in Nuxeo command.
 *
 * @author Loïc Billon
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportZipCommand implements INuxeoCommand {

    /**
     * Current path.
     */
    private String path;
    /**
     * Upload multipart files.
     */
    private Blob binary;


    /**
     * Constructor.
     */
    public ImportZipCommand() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.ImportZip");
        operationRequest.setInput(binary);
        operationRequest.setHeader("nx_es_sync", String.valueOf(true));
        operationRequest.setContextProperty("currentDocument", this.path);

        return operationRequest.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void setBinary(Blob binary) {
        this.binary = binary;
    }
}
