package org.osivia.services.forum.thread.portlet.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class ForumThreadOptions {

    /** Forum thread Nuxeo document. */
    private Document document;


    /**
     * Constructor.
     */
    public ForumThreadOptions() {
        super();
    }


    /**
     * Getter for document.
     *
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Setter for document.
     *
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }

}
