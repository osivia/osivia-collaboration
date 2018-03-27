package org.osivia.services.calendar.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.edition.portlet.model.CalendarEditionForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar edition Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractCalendarCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarEditionCommand extends AbstractCalendarCommand {

    /** Calendar edition options. */
    private final CalendarEditionOptions options;
    /** Calendar edition form. */
    private final CalendarEditionForm form;


    /**
     * Constructor.
     * 
     * @param options calendar edition options
     * @param form calendar edition form
     */
    public CalendarEditionCommand(CalendarEditionOptions options, CalendarEditionForm form) {
        super();
        this.options = options;
        this.form = form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Document
        Document document = this.options.getDocument();

        // Properties
        PropertyMap properties = this.getProperties(this.form);

        // Update
        document = documentService.update(document, properties);

        // Vignette
        this.setVignette(documentService, document, this.form);

        return document;
    }

}
