package org.osivia.services.calendar.common.model;

import java.io.File;

import javax.activation.MimeType;

/**
 * Temporary file abstract super-class.
 * 
 * @author Cédric Krommenhoek
 */
public abstract class AbstractTemporaryFile {

    /** Uploaded temporary file. */
    private File temporaryFile;
    /** Uploaded temporary file name. */
    private String temporaryFileName;
    /** Uploaded temporary file mime type. */
    private MimeType temporaryMimeType;
    /** Deleted indicator. */
    private boolean deleted;


    /**
     * Constructor.
     */
    public AbstractTemporaryFile() {
        super();
    }


    /**
     * Getter for temporaryFile.
     * 
     * @return the temporaryFile
     */
    public File getTemporaryFile() {
        return temporaryFile;
    }

    /**
     * Setter for temporaryFile.
     * 
     * @param temporaryFile the temporaryFile to set
     */
    public void setTemporaryFile(File temporaryFile) {
        this.temporaryFile = temporaryFile;
    }

    /**
     * Getter for temporaryFileName.
     * 
     * @return the temporaryFileName
     */
    public String getTemporaryFileName() {
        return temporaryFileName;
    }

    /**
     * Setter for temporaryFileName.
     * 
     * @param temporaryFileName the temporaryFileName to set
     */
    public void setTemporaryFileName(String temporaryFileName) {
        this.temporaryFileName = temporaryFileName;
    }

    /**
     * Getter for temporaryMimeType.
     * 
     * @return the temporaryMimeType
     */
    public MimeType getTemporaryMimeType() {
        return temporaryMimeType;
    }

    /**
     * Setter for temporaryMimeType.
     * 
     * @param temporaryMimeType the temporaryMimeType to set
     */
    public void setTemporaryMimeType(MimeType temporaryMimeType) {
        this.temporaryMimeType = temporaryMimeType;
    }

    /**
     * Getter for deleted.
     * 
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Setter for deleted.
     * 
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
