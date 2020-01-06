package org.osivia.services.workspace.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Parent document.
 * 
 * @author Cédric Krommenhoek
 * @see ObjectDocument
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ParentDocument extends ObjectDocument {

    /**
     * Constructor.
     * @param document document DTO
     */
    public ParentDocument(DocumentDTO document) {
        super(document);
    }

}
