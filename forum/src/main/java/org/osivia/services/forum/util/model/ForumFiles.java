package org.osivia.services.forum.util.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

/**
 * Forum files java-bean.
 *
 * @author Cédric Krommenhoek
 * @see Cloneable
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForumFiles implements Cloneable {

    /** Files. */
    private List<ForumFile> files;
    /** Uploaded multipart files. */
    private List<MultipartFile> upload;
    /** Deleted attachment index. */
    private Integer deletedIndex;
    /** Deleted blob indexes. */
    private SortedSet<Integer> deletedBlobIndexes;


    /**
     * Constructor.
     */
    public ForumFiles() {
        super();
    }


    @Override
    public ForumFiles clone() {
        ForumFiles clone = new ForumFiles();
        clone.files = new ArrayList<>(this.files);
        return clone;
    }


    /**
     * Getter for files.
     *
     * @return the files
     */
    public List<ForumFile> getFiles() {
        return files;
    }

    /**
     * Setter for files.
     *
     * @param files the files to set
     */
    public void setFiles(List<ForumFile> files) {
        this.files = files;
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
     * Getter for deletedBlobIndexes.
     *
     * @return the deletedBlobIndexes
     */
    public SortedSet<Integer> getDeletedBlobIndexes() {
        return deletedBlobIndexes;
    }

    /**
     * Setter for deletedBlobIndexes.
     *
     * @param deletedBlobIndexes the deletedBlobIndexes to set
     */
    public void setDeletedBlobIndexes(SortedSet<Integer> deletedBlobIndexes) {
        this.deletedBlobIndexes = deletedBlobIndexes;
    }

}
