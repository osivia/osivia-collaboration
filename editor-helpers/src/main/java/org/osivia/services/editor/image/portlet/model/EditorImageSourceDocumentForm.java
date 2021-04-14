package org.osivia.services.editor.image.portlet.model;

import org.osivia.services.editor.common.model.SourceDocumentForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Editor document image source form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorImageSourceDocumentForm extends SourceDocumentForm {

    /**
     * Constructor.
     */
    public EditorImageSourceDocumentForm() {
        super();
    }

}
