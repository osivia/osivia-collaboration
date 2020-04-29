package org.osivia.services.editor.image.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Editor attached image source form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorImageSourceAttachedForm {

    /**
     * Constructor.
     */
    public EditorImageSourceAttachedForm() {
        super();
    }
}
