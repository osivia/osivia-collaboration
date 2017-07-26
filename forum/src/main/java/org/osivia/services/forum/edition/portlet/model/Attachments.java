package org.osivia.services.forum.edition.portlet.model;

import org.osivia.services.forum.edition.portlet.model.comparator.BlobIndexComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Attachments java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Attachments {

    /** Uploaded multipart files. */
    private List<MultipartFile> upload;
    /** Deleted attachment index. */
    private Integer deletedIndex;

    /** Attachment files. */
    private final List<AttachmentFile> files;
    /** Deleted blob indexes. */
    private final SortedSet<Integer> deletedBlobIndexes;


    /**
     * Constructor.
     *
     * @param blobIndexComparator blob index comparator
     */
    public Attachments(Comparator<Integer> blobIndexComparator) {
        super();
        this.files = new ArrayList<>();
        this.deletedBlobIndexes = new TreeSet<>(blobIndexComparator);
    }


    /**
     * Getter for upload.
     *
     * @return the upload
     */
    public List<MultipartFile> getUpload() {
        return upload;
    }

    /**
     * Setter for upload.
     *
     * @param upload the upload to set
     */
    public void setUpload(List<MultipartFile> upload) {
        this.upload = upload;
    }

    /**
     * Getter for deletedIndex.
     *
     * @return the deletedIndex
     */
    public Integer getDeletedIndex() {
        return deletedIndex;
    }

    /**
     * Setter for deletedIndex.
     *
     * @param deletedIndex the deletedIndex to set
     */
    public void setDeletedIndex(Integer deletedIndex) {
        this.deletedIndex = deletedIndex;
    }

    /**
     * Getter for files.
     *
     * @return the files
     */
    public List<AttachmentFile> getFiles() {
        return files;
    }

    /**
     * Getter for deletedBlobIndexes.
     *
     * @return the deletedBlobIndexes
     */
    public SortedSet<Integer> getDeletedBlobIndexes() {
        return deletedBlobIndexes;
    }

}
