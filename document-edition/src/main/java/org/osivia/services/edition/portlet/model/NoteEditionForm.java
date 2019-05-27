package org.osivia.services.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Note edition form java-bean.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NoteEditionForm extends AbstractDocumentEditionForm {

    /**
     * Content.
     */
    private String content;


    /**
     * Constructor.
     */
    public NoteEditionForm() {
        super();
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
