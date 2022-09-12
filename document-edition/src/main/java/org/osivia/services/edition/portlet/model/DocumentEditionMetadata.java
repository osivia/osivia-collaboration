package org.osivia.services.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Metadata.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DocumentEditionMetadata {

    /**
     * Description.
     */
    private String description;
    /**
     * Vignette.
     */
    private Picture vignette;


    /**
     * Constructor.
     */
    public DocumentEditionMetadata() {
        super();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Picture getVignette() {
        return vignette;
    }

    public void setVignette(Picture vignette) {
        this.vignette = vignette;
    }
}
