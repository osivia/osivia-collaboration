package org.osivia.services.workspace.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Object document java-bean.
 * 
 * @author Cédric Krommenhoek
 */
public abstract class ObjectDocument {

    /** Document DTO. */
    private final DocumentDTO document;


    /**
     * Constructor.
     * @param document document DTO
     */
    public ObjectDocument(DocumentDTO document) {
        super();
        this.document = document;
    }


    public DocumentDTO getDocument() {
        return document;
    }

}
