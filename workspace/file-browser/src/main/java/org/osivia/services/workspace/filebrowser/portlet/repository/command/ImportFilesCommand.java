package org.osivia.services.workspace.filebrowser.portlet.repository.command;

import java.text.Normalizer;
import java.util.List;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.StreamBlob;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Import files Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportFilesCommand implements INuxeoCommand {

    /** Current path. */
    private final String path;
    /** Upload multipart files. */
    private final List<MultipartFile> upload;


    /**
     * Constructor.
     * 
     * @param path current path
     * @param upload upload multipart files
     */
    public ImportFilesCommand(String path, List<MultipartFile> upload) {
        super();
        this.path = path;
        this.upload = upload;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Blobs
        Blobs blobs = new Blobs(this.upload.size());
        for (MultipartFile multipartFile : this.upload) {

            String s = Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);

            Blob blob = new StreamBlob(multipartFile.getInputStream(), s,  multipartFile.getContentType());
            blobs.add(blob);
        }

        // Operation request
        OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.Import");
        operationRequest.setInput(blobs);
        operationRequest.setHeader("nx_es_sync", String.valueOf(true));
        operationRequest.setContextProperty("currentDocument", this.path);
        operationRequest.set("overwite", true);

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
