package org.osivia.services.calendar.event.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.event.edition.portlet.model.CalendarEventEditionForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar event creation Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractCalendarEventCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarEventCreationCommand extends AbstractCalendarEventCommand {

    /** Calendar event Nuxeo document type. */
    private static final String DOCUMENT_TYPE = "VEVENT";


    /** Calendar edition options. */
    private final CalendarEditionOptions options;
    /** Calendar event edition form. */
    private final CalendarEventEditionForm form;


    /**
     * Constructor.
     * 
     * @param options calendar edition options
     * @param form calendar event edition form
     */
    public CalendarEventCreationCommand(CalendarEditionOptions options, CalendarEventEditionForm form) {
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

        // Parent
        DocRef parent = new DocRef(this.options.getParentPath());

        // Properties
        PropertyMap properties = this.getProperties(this.form);

        // Creation
        Document document = documentService.createDocument(parent, DOCUMENT_TYPE, null, properties, true);

        // Attachments
        this.setAttachments(documentService, document, this.form);

        return document;
    }

}
