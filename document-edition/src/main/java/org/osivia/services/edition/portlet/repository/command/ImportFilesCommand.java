package org.osivia.services.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Import files Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportFilesCommand implements INuxeoCommand {

    /**
     * Operation identifier.
     */
    private static final String OPERATION_ID = "FileManager.Import";

    /**
     * Parent document path.
     */
    private String parentPath;
    /**
     * File binaries.
     */
    private List<Blob> binaries;


    /**
     * Constructor.
     */
    public ImportFilesCommand() {
        super();
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest(OPERATION_ID);
        operationRequest.setHeader("nx_es_sync", String.valueOf(true));
        operationRequest.setContextProperty("currentDocument", this.parentPath);

        Document document = null;
        for (Blob binary : binaries) {
            operationRequest.setInput(binary);
            document = (Document) operationRequest.execute();
        }

        return document;
    }


    @Override
    public String getId() {
        return null;
    }


    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public void setBinaries(List<Blob> binaries) {
        this.binaries = binaries;
    }
}
