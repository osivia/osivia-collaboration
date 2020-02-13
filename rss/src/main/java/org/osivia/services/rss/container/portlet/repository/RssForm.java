package org.osivia.services.rss.container.portlet.repository;

import org.nuxeo.ecm.automation.client.model.DocRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
	 * Rss view form java-bean.
	 *
	 * @author Frédéric Boudan
	 */
	@Component
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public class RssForm {

	    /** Dod id (Nuxeo) */
	    private String docid;

	    private String title;
	    
	    /** Calendar document, may be null. */
	    private DocRef document;
	    /** Calendar parent path. */
	    private String parentPath;
	    
		public String getDocid() {
			return docid;
		}
		public void setDocid(String docid) {
			this.docid = docid;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public DocRef getDocument() {
			return document;
		}
		public void setDocument(DocRef document) {
			this.document = document;
		}
		public String getParentPath() {
			return parentPath;
		}
		public void setParentPath(String parentPath) {
			this.parentPath = parentPath;
		}
	}
