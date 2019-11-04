package org.osivia.services.workspace.quota.portlet.model;

import java.util.Date;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * 
 * @author Loïc Billon
 *
 */
public class BigFile {

	/** nuxeo DTO document */
	private DocumentDTO document;
	
    /** Document deletion date. */
    private Date modificationDate;
    /** Document last contributor. */
    private String lastContributor;
	
	private Long size;
	
	/** in trash, the doc is not linkable */
	private boolean inTrash = false;

	public DocumentDTO getDocument() {
		return document;
	}

	public void setDocument(DocumentDTO document) {
		this.document = document;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public boolean isInTrash() {
		return inTrash;
	}

	public void setInTrash(boolean inTrash) {
		this.inTrash = inTrash;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getLastContributor() {
		return lastContributor;
	}

	public void setLastContributor(String lastContributor) {
		this.lastContributor = lastContributor;
	}
	
	
}
