package org.osivia.services.editor.image.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Editor document image source form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorImageSourceDocumentForm {

    /**
     * Filter.
     */
    private String filter;

    /**
     * Documents.
     */
    private List<DocumentDTO> documents;


    /**
     * Constructor.
     */
    public EditorImageSourceDocumentForm() {
        super();
    }


    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<DocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDTO> documents) {
        this.documents = documents;
    }
}
