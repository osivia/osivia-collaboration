package org.osivia.services.pins.edition.portlet.model;

import java.util.List;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Pins edition form
 * @author jbarberet
 *
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class PinsEditionForm {
	
	private Document workspace;
	
	private DocumentDTO document;
	
	private List<PinnedDocument> listPins;
	
	private boolean toSave;
	
	private boolean canPin;
	
    /** Document webId. */
    private String documentWebId;
	
    /**
     * Constructor
     */
	public PinsEditionForm() {
		super();
	}
	
	/**
	 * Constructor
	 * @param workspace
	 */
	public PinsEditionForm(Document workspace)
	{
		super();
		this.workspace = workspace;
		toSave = false;
	}

	public List<PinnedDocument> getListPins() {
		return listPins;
	}

	public void setListPins(List<PinnedDocument> listPins) {
		this.listPins = listPins;
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

	public boolean isCanPin() {
		return canPin;
	}

	public void setCanPin(boolean canPin) {
		this.canPin = canPin;
	}

	
}
