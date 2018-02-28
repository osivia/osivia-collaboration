/**
 * 
 */
package org.osivia.services.versions.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;



/**
 * @author david
 *
 */
public class Version extends DocumentDTO {
    
    /** Version label. */
    private String label;
    
    /** Comment. */
    private String comment;
    
    /** Is current version (case of restore). */
    private boolean isCurrentVersion;
    
    /**
     * Default constructor.
     */
    public Version(){
        super();
    }
    
    /**
     * @param documentDto
     */
    public Version(DocumentDTO documentDto){
        super(documentDto);
    }
    
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the label
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param label the label to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the isCurrentVersion
     */
    public boolean isCurrentVersion() {
        return isCurrentVersion;
    }

    /**
     * @param isCurrentVersion the isCurrentVersion to set
     */
    public void setCurrentVersion(boolean isCurrentVersion) {
        this.isCurrentVersion = isCurrentVersion;
    }
    
}
