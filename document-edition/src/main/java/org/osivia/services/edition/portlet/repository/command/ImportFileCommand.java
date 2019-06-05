package org.osivia.services.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Import file Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportFileCommand implements INuxeoCommand {

    /**
     * Operation identifier.
     */
    private static final String OPERATION_ID = "FileManager.Import";

    /**
     * Parent document path.
     */
    private final String parentPath;
    /**
     * File binary.
     */
    private final Blob binary;


    /**
     * Constructor.
     *
     * @param parentPath  parent document path
     * @param binary      file binary
     */
    public ImportFileCommand(String parentPath, Blob binary) {
        super();
        this.parentPath = parentPath;
        this.binary = binary;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest(OPERATION_ID);
        operationRequest.setInput(this.binary);
        operationRequest.setHeader("nx_es_sync", String.valueOf(true));
        operationRequest.setContextProperty("currentDocument", this.parentPath);
        operationRequest.set("overwite", true);

        return operationRequest.execute();
    }


    @Override
    public String getId() {
        return null;
    }

}
