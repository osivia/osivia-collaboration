package org.osivia.services.sets.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * AddedDocument
 * @author Julien Barberet
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddedDocument extends DocumentDTO{

    /** in bin indicator. */
    private boolean inBin;
    /** Deleted indicator */
    private boolean deleted;
    /** Order. */
    private int order;
    /** Web id*/
    private String webId;

	
    /**
     * Constructor
     * @param dto
     * @param webId
     * @param inBin
     * @param order
     */
    public AddedDocument(DocumentDTO dto, String webId, boolean inBin, int order) {
    	super();
    	this.setIcon(dto.getIcon());
    	this.setTitle(dto.getTitle());
    	this.setPath(dto.getPath());
    	this.webId = webId;
    	this.inBin = inBin;
    	this.order = order;
    	this.deleted = false;
    }

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}


	public boolean isInBin() {
		return inBin;
	}


	public void setInBin(boolean inBin) {
		this.inBin = inBin;
	}

	public String getWebId() {
		return webId;
	}


	public void setWebId(String webId) {
		this.webId = webId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
