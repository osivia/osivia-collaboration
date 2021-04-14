package org.osivia.services.editor.link.portlet.model;

import org.osivia.services.editor.common.model.SourceDocumentForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorLinkSourceDocumentForm extends SourceDocumentForm {

    /**
     * Constructor.
     */
    public EditorLinkSourceDocumentForm() {
        super();
    }

}
