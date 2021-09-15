package org.osivia.services.forum.util.service;

import org.osivia.services.forum.util.model.ForumFile;
import org.osivia.services.forum.util.model.ForumFiles;
import org.osivia.services.forum.util.model.comparator.ForumBlobIndexComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Forum service implementation abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class AbstractForumServiceImpl {

    /** Attachment temporary file prefix. */
    protected static final String ATTACHMENT_TEMPORARY_FILE_PREFIX = "attachment-";

    /** Temporary file suffix. */
    private static final String TEMPORARY_FILE_SUFFIX = ".tmp";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Blob index comparator. */
    @Autowired
    private ForumBlobIndexComparator blobIndexComparator;


    /**
     * Constructor.
     */
    public AbstractForumServiceImpl() {
        super();
    }


    /**
     * Upload attachments.
     *
     * @param attachments attachments
     * @throws IOException
     */
    protected void uploadAttachments(ForumFiles attachments) throws IOException {
        // Attachment files
        List<ForumFile> files = attachments.getFiles();
        if (files == null) {
            files = new ArrayList<>();
            attachments.setFiles(files);
        }

        for (MultipartFile multipartFile : attachments.getUpload()) {
            // Attachment file
            ForumFile file = this.applicationContext.getBean(ForumFile.class);

            this.setAttachmentFileProperties(multipartFile, file, ATTACHMENT_TEMPORARY_FILE_PREFIX);

            files.add(file);
        }
    }


    /**
     * Set attachment file properties.
     *
     * @param upload multipart file upload
     * @param file   attachment file
     * @param prefix temporary file prefix
     * @throws IOException
     */
    protected void setAttachmentFileProperties(MultipartFile upload, ForumFile file, String prefix) throws IOException {
        // Temporary file
        File temporaryFile = file.getTemporaryFile();
        if (temporaryFile != null) {
            // Delete temporary file
            temporaryFile.delete();
        }
        temporaryFile = File.createTempFile(prefix, TEMPORARY_FILE_SUFFIX);
        temporaryFile.deleteOnExit();
        upload.transferTo(temporaryFile);
        file.setTemporaryFile(temporaryFile);

        // File name
        String s = Normalizer.normalize(upload.getOriginalFilename(), Normalizer.Form.NFC);
        file.setFileName(s);

        // Mime type
        MimeType mimeType;
        try {
            mimeType = new MimeType(upload.getContentType());
        } catch (MimeTypeParseException e) {
            mimeType = null;
        }
        file.setMimeType(mimeType);
    }


    /**
     * Delete attachment.
     *
     * @param attachments attachments
     * @throws IOException
     */
    protected void deleteAttachment(ForumFiles attachments) throws IOException {
        // Deleted attachment index
        Integer deletedIndex = attachments.getDeletedIndex();

        if (deletedIndex != null) {
            // Attachment files
            List<ForumFile> files = attachments.getFiles();

            // Attachment file
            ForumFile file = files.get(deletedIndex);

            // Blob index
            Integer blobIndex = file.getBlobIndex();
            if (blobIndex != null) {
                SortedSet<Integer> deletedBlobIndexes = attachments.getDeletedBlobIndexes();
                if (deletedBlobIndexes == null) {
                    deletedBlobIndexes = new TreeSet<>(this.blobIndexComparator);
                    attachments.setDeletedBlobIndexes(deletedBlobIndexes);
                }
                deletedBlobIndexes.add(blobIndex);
            }

            // Temporary file
            File temporaryFile = file.getTemporaryFile();
            if (temporaryFile != null) {
                // Delete temporary file
                temporaryFile.delete();
            }

            files.remove(file);
        }

        attachments.setDeletedIndex(null);
    }

}
