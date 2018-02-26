package fr.toutatice.collaboratif.purgeworkspaces.portlet.model;

import java.util.Date;
import java.util.List;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/** WorkspaceLine bean */
public class WorkspaceLine {

	private DocumentDTO document;
	
	private String description;
	
	private List<String> animators;
	
	private Date expirationDate;
	
	private Date deletedDate;
	
	private boolean selected;
	
	private boolean expired;
	
	private String lastContributor;

	/** Constructor */
	public WorkspaceLine(DocumentDTO document, String description, List<String> animators, Date expirationDate,  Date deletedDate, String lastContributor,boolean expired) {
		super();
		this.document = document;
		this.description = description;
		this.animators = animators;
		this.expirationDate = expirationDate;
		this.deletedDate = deletedDate;
		this.expired = expired;
		this.lastContributor = lastContributor;
	}

	public List<String> getAnimators() {
		return animators;
	}

	public void setAnimators(List<String> animators) {
		this.animators = animators;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public String getLastContributor() {
		return lastContributor;
	}

	public void setLastContributor(String lastContributor) {
		this.lastContributor = lastContributor;
	}

	public DocumentDTO getDocument() {
		return document;
	}

	public void setDocument(DocumentDTO document) {
		this.document = document;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
