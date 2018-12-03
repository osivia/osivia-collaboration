package org.osivia.services.calendar.view.portlet.repository.command;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.calendar.view.portlet.model.CalendarEditionMode;
import org.osivia.services.calendar.view.portlet.model.CalendarViewForm;
import org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Agenda edition Nuxeo command.
 *
 * @author Julien Barberet
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventEditionCommand implements INuxeoCommand {
    
    private static final String DOCUMENT_TYPE = "VEVENT";


    /** Calendar edition form. */
    private final CalendarViewForm form;



    /**
     * Constructor.
     *
     * @param form calendar edition form
     */
    public EventEditionCommand(CalendarViewForm form) {
        super();
        this.form = form;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Edition mode
        CalendarEditionMode mode = this.form.getMode();

        // Document
        Document document;

        if (CalendarEditionMode.CREATION.equals(mode)) {
            document = this.create(nuxeoSession);
        } else if (CalendarEditionMode.EDITION.equals(mode)) {
            document = this.edit(nuxeoSession);
        } else {
            document = null;
        }

        return document;
    }


    @Override
    public String getId() {
        return null;
    }


    /**
     * Create document.
     *
     * @param nuxeoSession Nuxeo session
     * @return document
     * @throws Exception
     */
    private Document create(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Parent
        DocRef parent = new DocRef(this.form.getParentPath());
        // Properties
        PropertyMap properties = new PropertyMap();
        //Les dates sont déjà au format UTC, donc on passe par DateFormatUtils.format sinon Nuxeo enlève 1 ou 2 heures en pensant qu'on lui passe des dates en GMT+1
        
        properties.set(CalendarViewRepository.END_DATE_PROPERTY, this.form.getEndDate());
        properties.set(CalendarViewRepository.START_DATE_PROPERTY, this.form.getStartDate());
        properties.set(CalendarViewRepository.TITLE_PROPERTY, this.form.getTitle());

        // Creation
        Document document = documentService.createDocument(parent, DOCUMENT_TYPE, null, properties, true);


        return document;
    }

    /**
     * Edit document.
     *
     * @param nuxeoSession Nuxeo session
     * @return document
     * @throws Exception
     */
    private Document edit(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);


        // Document
        DocRef docRef = new DocRef(this.form.getDocId());
        
        // Update document properties
        PropertyMap map = new PropertyMap();
        map.set(CalendarViewRepository.END_DATE_PROPERTY, this.form.getEndDate());
        map.set(CalendarViewRepository.START_DATE_PROPERTY, this.form.getStartDate());
        map.set(CalendarViewRepository.TITLE_PROPERTY, this.form.getTitle());
        Document document = documentService.update(docRef, map, true);

        return document;
    }

}
