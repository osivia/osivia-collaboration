package org.osivia.services.workspace.filebrowser.portlet.repository.command;

import java.text.Normalizer;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.StreamBlob;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Import a zip file in Nuxeo command.
 * 
 * @author Loïc Billon
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportZipCommand implements INuxeoCommand {

    /** Current path. */
    private final String path;
    /** Upload multipart files. */
    private final MultipartFile upload;


    /**
     * Constructor.
     * 
     * @param path current path
     * @param upload upload multipart files
     */
    public ImportZipCommand(String path, MultipartFile upload) {
        super();
        this.path = path;
        this.upload = upload;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        String s = Normalizer.normalize(upload.getOriginalFilename(), Normalizer.Form.NFD);

        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        Blob blob = new StreamBlob(upload.getInputStream(), s,  upload.getContentType());

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.ImportZip");
        operationRequest.setInput(blob);
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

}
