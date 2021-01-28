package org.osivia.services.editor.image.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.SortedSet;

/**
 * Editor attached image source form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorImageSourceAttachedForm {

    /**
     * Upload multipart file.
     */
    private MultipartFile upload;
    /**
     * Attached images.
     */
    private SortedSet<AttachedImage> attachedImages;


    /**
     * Constructor.
     */
    public EditorImageSourceAttachedForm() {
        super();
    }


    public MultipartFile getUpload() {
        return upload;
    }

    public void setUpload(MultipartFile upload) {
        this.upload = upload;
    }

    public SortedSet<AttachedImage> getAttachedImages() {
        return attachedImages;
    }

    public void setAttachedImages(SortedSet<AttachedImage> attachedImages) {
        this.attachedImages = attachedImages;
    }
}
