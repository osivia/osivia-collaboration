package org.osivia.services.rss.common.model;

import java.util.List;

import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ContainerRssModel {

	public String name;
	public String path;
	public DocumentDTO document;
	public Document doc;	
	
    /** feeds sources. */
    private List<FeedRssModel> feedSources;	


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public DocumentDTO getDocument() {
		return document;
	}

	public void setDocument(DocumentDTO document) {
		this.document = document;
	}

	public List<FeedRssModel> getFeedSources() {
		return feedSources;
	}

	public void setFeedSources(List<FeedRssModel> feedSources) {
		this.feedSources = feedSources;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

}
