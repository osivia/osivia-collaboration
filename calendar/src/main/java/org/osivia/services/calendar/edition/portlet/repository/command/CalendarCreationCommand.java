package org.osivia.services.calendar.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.edition.portlet.model.CalendarEditionForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar creation Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractCalendarCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarCreationCommand extends AbstractCalendarCommand {

    /** Calendar Nuxeo document type. */
    private static final String DOCUMENT_TYPE = "Agenda";

    /** Show in menu indicator Nuxeo document property. */
    private static final String SHOW_IN_MENU_PROPERTY = "ttc:showInMenu";


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
    public CalendarCreationCommand(CalendarEditionOptions options, CalendarEditionForm form) {
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
        properties.set(SHOW_IN_MENU_PROPERTY, true);

        // Creation
        Document document = documentService.createDocument(parent, DOCUMENT_TYPE, null, properties, true);

        // Vignette
        this.setVignette(documentService, document, this.form);

        return document;
    }

}
