package org.osivia.services.edition.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File edition form java-bean.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileEditionForm extends AbstractDocumentEditionForm {

    /**
     * Constructor.
     */
    public FileEditionForm() {
        super();
    }

}
