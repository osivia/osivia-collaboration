package org.osivia.services.forum.edition.portlet.model;

import javax.activation.MimeType;
import java.io.File;

/**
 * Uploaded object abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class UploadedObject {

    /** Upload temporary file. */
    private File temporaryFile;
    /** Temporary file name. */
    private String fileName;
    /** Temporary file mime type. */
    private MimeType mimeType;


    /**
     * Constructor.
     */
    public UploadedObject() {
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
     * Getter for fileName.
     *
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Setter for fileName.
     *
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Getter for mimeType.
     *
     * @return the mimeType
     */
    public MimeType getMimeType() {
        return mimeType;
    }

    /**
     * Setter for mimeType.
     *
     * @param mimeType the mimeType to set
     */
    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

}
