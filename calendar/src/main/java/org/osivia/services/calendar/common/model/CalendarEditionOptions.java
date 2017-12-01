package org.osivia.services.calendar.common.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Calendar edition options java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarOptions
 */
@Component("edition-options")
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CalendarEditionOptions extends CalendarOptions {

    /** Creation indicator. */
    private boolean creation;
    /** Parent path. */
    private String parentPath;
    /** Calendar Nuxeo document. */
    private Document document;

    /** Portlet title. */
    private String portletTitle;


    /**
     * Constructor.
     */
    public CalendarEditionOptions() {
        super();
    }


    /**
     * Getter for creation.
     * 
     * @return the creation
     */
    public boolean isCreation() {
        return creation;
    }

    /**
     * Setter for creation.
     * 
     * @param creation the creation to set
     */
    public void setCreation(boolean creation) {
        this.creation = creation;
    }

    /**
     * Getter for parentPath.
     * 
     * @return the parentPath
     */
    public String getParentPath() {
        return parentPath;
    }

    /**
     * Setter for parentPath.
     * 
     * @param parentPath the parentPath to set
     */
    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
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

    /**
     * Getter for portletTitle.
     * 
     * @return the portletTitle
     */
    public String getPortletTitle() {
        return portletTitle;
    }

    /**
     * Setter for portletTitle.
     * 
     * @param portletTitle the portletTitle to set
     */
    public void setPortletTitle(String portletTitle) {
        this.portletTitle = portletTitle;
    }

}
