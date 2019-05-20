package org.osivia.services.workspace.portlet.model;

import java.util.Date;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Trashed document java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrashedDocument extends ObjectDocument {

    /** Document location. */
    private ParentDocument location;


    /**
     * Constructor.
     * @param document document DTO
     */
    public TrashedDocument(DocumentDTO document) {
        super(document);
    }


    /**
     * Getter for location.
     * 
     * @return the location
     */
    public ParentDocument getLocation() {
        return location;
    }

    /**
     * Setter for location.
     * 
     * @param location the location to set
     */
    public void setLocation(ParentDocument location) {
        this.location = location;
    }

}
