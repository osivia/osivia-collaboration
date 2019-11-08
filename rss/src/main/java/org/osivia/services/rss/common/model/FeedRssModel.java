package org.osivia.services.rss.common.model;

import java.net.URL;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FeedRssModel {

	public String displayName;
	public URL url;
	public String partId;
	public String syncId;
	public DocumentDTO document;

	public FeedRssModel() {
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getSyncId() {
		return syncId;
	}

	public void setSyncId(String syncId) {
		this.syncId = syncId;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public DocumentDTO getDocument() {
		return document;
	}

	public void setDocument(DocumentDTO document) {
		this.document = document;
	}

}
