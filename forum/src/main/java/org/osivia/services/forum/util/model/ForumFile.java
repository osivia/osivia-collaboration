package org.osivia.services.forum.util.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.activation.MimeType;
import java.io.File;

/**
 * Forum file java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Primary
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForumFile {

    /** Blob index. */
    private Integer blobIndex;
    /** Upload temporary file. */
    private File temporaryFile;
    /** Temporary file name. */
    private String fileName;
    /** Temporary file mime type. */
    private MimeType mimeType;
    /** URL. */
    private String url;


    /**
     * Constructor.
     */
    public ForumFile() {
        super();
    }


    /**
     * Getter for blobIndex.
     *
     * @return the blobIndex
     */
    public Integer getBlobIndex() {
        return blobIndex;
    }

    /**
     * Setter for blobIndex.
     *
     * @param blobIndex the blobIndex to set
     */
    public void setBlobIndex(Integer blobIndex) {
        this.blobIndex = blobIndex;
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

    /**
     * Getter for url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for url.
     *
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
