package org.osivia.services.forum.util.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.*;
import org.osivia.services.forum.edition.portlet.repository.ForumEditionRepository;
import org.osivia.services.forum.util.model.ForumFile;
import org.osivia.services.forum.util.model.ForumFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Forum Nuxeo command abstract super-class.
 */
public abstract class AbstractForumCommand implements INuxeoCommand {

    /**
     * Constructor.
     */
    public AbstractForumCommand() {
        super();
    }


    /**
     * Set blobs.
     *
     * @param documentService Nuxeo document service
     * @param ref             Nuxeo document reference
     * @param attachments     attachments
     * @throws Exception
     */
    protected void setBlobs(DocumentService documentService, DocRef ref, ForumFiles attachments) throws Exception {
        // Attachment files
        List<ForumFile> files = attachments.getFiles();


        // Remove blobs
        if (CollectionUtils.isNotEmpty(attachments.getDeletedBlobIndexes())) {
            for (Integer index : attachments.getDeletedBlobIndexes()) {
                StringBuilder xpath = new StringBuilder();
                xpath.append(ForumEditionRepository.ATTACHMENTS_PROPERTY);
                xpath.append("/item[");
                xpath.append(index);
                xpath.append("]");

                documentService.removeBlob(ref, xpath.toString());
            }
        }


        if (CollectionUtils.isNotEmpty(files)) {
            // Blobs
            List<Blob> blobs = getBlobs(files);

            if (!blobs.isEmpty()) {
                documentService.setBlobs(ref, new Blobs(blobs), ForumEditionRepository.ATTACHMENTS_PROPERTY);

                // Delete temporary files
                for (ForumFile file : files) {
                    if (file.getTemporaryFile() != null) {
                        file.getTemporaryFile().delete();
                    }
                }
            }
        }
    }


    /**
     * Get blobs.
     *
     * @param files files
     * @return blobs
     */
    public List<Blob> getBlobs(List<ForumFile> files) {
        List<Blob> blobs = new ArrayList<>(files.size());

        for (ForumFile file : files) {
            // Temporary file
            File temporaryFile = file.getTemporaryFile();

            if (temporaryFile != null) {
                // File name
                String fileName = file.getFileName();
                // Mime type
                String mimeType = file.getMimeType().getBaseType();

                // File blob
                Blob blob = new FileBlob(temporaryFile, fileName, mimeType);

                blobs.add(blob);
            }
        }
        return blobs;
    }

}
