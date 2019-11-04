package org.osivia.services.workspace.portlet.model;

import java.util.Date;

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

    /** Document deletion date. */
    private Date deletionDate;
    /** Document last contributor. */
    private String lastContributor;
    /** Document location. */
    private ParentDocument location;
    /** File size, may be null. */
    private Long size;    
    /** Document selected indicator. */
    private boolean selected;


    /**
     * Constructor.
     * 
     * @param path document path
     */
    public TrashedDocument(String path) {
        super(path);
    }


    /**
     * Getter for deletionDate.
     * 
     * @return the deletionDate
     */
    public Date getDeletionDate() {
        return deletionDate;
    }

    /**
     * Setter for deletionDate.
     * 
     * @param deletionDate the deletionDate to set
     */
    public void setDeletionDate(Date deletionDate) {
        this.deletionDate = deletionDate;
    }

    /**
     * Getter for lastContributor.
     * 
     * @return the lastContributor
     */
    public String getLastContributor() {
        return lastContributor;
    }

    /**
     * Setter for lastContributor.
     * 
     * @param lastContributor the lastContributor to set
     */
    public void setLastContributor(String lastContributor) {
        this.lastContributor = lastContributor;
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


    /**
     * Getter for size.
     * 
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * Setter for size.
     * 
     * @param size the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }    
    
    /**
     * Getter for selected.
     * 
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Setter for selected.
     * 
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
