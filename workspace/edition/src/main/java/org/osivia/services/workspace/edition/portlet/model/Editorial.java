package org.osivia.services.workspace.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Editorial java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Editorial {

    /** Displayed indicator. */
    private boolean displayed;
    /** Initial displayed indicator. */
    private boolean initialDisplayed;
    /** Document DTO. */
    private DocumentDTO document;


    /**
     * Constructor.
     */
    public Editorial() {
        super();
    }


    /**
     * Getter for displayed.
     *
     * @return the displayed
     */
    public boolean isDisplayed() {
        return this.displayed;
    }

    /**
     * Setter for displayed.
     *
     * @param displayed the displayed to set
     */
    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    /**
     * Getter for initialDisplayed.
     * 
     * @return the initialDisplayed
     */
    public boolean isInitialDisplayed() {
        return initialDisplayed;
    }

    /**
     * Setter for initialDisplayed.
     * 
     * @param initialDisplayed the initialDisplayed to set
     */
    public void setInitialDisplayed(boolean initialDisplayed) {
        this.initialDisplayed = initialDisplayed;
    }

    /**
     * Getter for document.
     *
     * @return the document
     */
    public DocumentDTO getDocument() {
        return this.document;
    }

    /**
     * Setter for document.
     *
     * @param document the document to set
     */
    public void setDocument(DocumentDTO document) {
        this.document = document;
    }


}
