package org.osivia.services.forum.edition.portlet.model;

import org.osivia.services.forum.util.model.AbstractForumThreadForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Forum edition form java-bean.
 *
 * @author Cédric Krommenhoek
 * @see AbstractForumThreadForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForumEditionForm extends AbstractForumThreadForm {

    /** Title. */
    private String title;
    /** Description. */
    private String description;
    /** Vignette. */
    private Vignette vignette;

    /** Document type. */
    private String documentType;


    /**
     * Constructor.
     */
    public ForumEditionForm() {
        super();
    }


    /**
     * Getter for title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for vignette.
     *
     * @return the vignette
     */
    public Vignette getVignette() {
        return vignette;
    }

    /**
     * Setter for vignette.
     *
     * @param vignette the vignette to set
     */
    public void setVignette(Vignette vignette) {
        this.vignette = vignette;
    }

    /**
     * Getter for documentType.
     *
     * @return the documentType
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * Setter for documentType.
     *
     * @param documentType the documentType to set
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

}
