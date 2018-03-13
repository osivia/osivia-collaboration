package org.osivia.services.sets.edition.portlet.model;

import java.util.List;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Sets edition form
 * @author Julien Barberet
 *
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class SetsEditionForm {
	
	private Document workspace;
	
	private DocumentDTO document;
	
	private List<AddedDocument> list;
	
	private boolean toSave;
	
	private boolean enableSets;
	
    /** Document webId. */
    private String documentWebId;
	
    /**
     * Constructor
     */
	public SetsEditionForm() {
		super();
	}
	
	/**
	 * Constructor
	 * @param workspace
	 */
	public SetsEditionForm(Document workspace)
	{
		super();
		this.workspace = workspace;
		toSave = false;
	}

	public List<AddedDocument> getList() {
		return list;
	}

	public void setList(List<AddedDocument> list) {
		this.list = list;
	}

	public Document getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Document workspace) {
		this.workspace = workspace;
	}

	public boolean isToSave() {
		return toSave;
	}

	public void setToSave(boolean toSave) {
		this.toSave = toSave;
	}

	public DocumentDTO getDocument() {
		return document;
	}

	public void setDocument(DocumentDTO document) {
		this.document = document;
	}

	public String getDocumentWebId() {
		return documentWebId;
	}

	public void setDocumentWebId(String documentWebId) {
		this.documentWebId = documentWebId;
	}

	public boolean isEnableSets() {
		return enableSets;
	}

	public void setEnableSets(boolean enableSets) {
		this.enableSets = enableSets;
	}
}
