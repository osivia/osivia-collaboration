package org.osivia.services.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Document edition window properties.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DocumentEditionWindowProperties {

    /**
     * Document path.
     */
    private String documentPath;

    /**
     * Parent document path.
     */
    private String parentDocumentPath;

    /**
     * Document type.
     */
    private String documentType;


    /**
     * Constructor.
     */
    public DocumentEditionWindowProperties() {
        super();
    }


    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getParentDocumentPath() {
        return parentDocumentPath;
    }

    public void setParentDocumentPath(String parentDocumentPath) {
        this.parentDocumentPath = parentDocumentPath;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

}
