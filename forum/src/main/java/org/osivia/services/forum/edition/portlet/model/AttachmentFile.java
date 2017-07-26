package org.osivia.services.forum.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Attachment file java-bean.
 *
 * @author Cédric Krommenhoek
 * @see UploadedObject
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AttachmentFile extends UploadedObject {

    /** Blob index. */
    private Integer blobIndex;


    /**
     * Constructor.
     */
    public AttachmentFile() {
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

}
