package org.osivia.services.calendar.view.portlet.repository.command;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.calendar.event.view.portlet.service.CalendarEventViewService;
import org.osivia.services.calendar.view.portlet.model.events.EventKey;
import org.osivia.services.calendar.view.portlet.model.events.EventToSync;
import org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

import static org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository.DOCUMENT_TYPE_EVENEMENT;
import static org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository.LAST_MODIFIED_SOURCE;
import static org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository.ID_SOURCE_PROPERTY;
import static org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository.ID_PARENT_SOURCE_PROPERTY;

/**
 * List Nuxeo events command.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 * @see INuxeoCommand
 */
public class SynchronizationCommand implements INuxeoCommand {

    /** Nuxeo query filter context. */
    private NuxeoQueryFilterContext queryContext;
    /** Context path. */
    private final String contextPath;
    /** List of event to synchronize */
    private Map<EventKey, EventToSync> mapVevent;

    private final String parentPath;
    /** logger */
    protected static final Log logger = LogFactory.getLog(SynchronizationCommand.class);

    /**
     * Constructor.
     *
     * @param queryContext Nuxeo query filter context
     * @param contextPath context path
     */
    public SynchronizationCommand(NuxeoQueryFilterContext queryContext, String contextPath, String parentPath, Map<EventKey, EventToSync> mapVevent) {
        super();
        this.queryContext = queryContext;
        this.contextPath = contextPath;
        this.mapVevent = mapVevent;
        this.parentPath = parentPath;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
    	//List event in Nuxeo that were previously synchronized from the idAgenda source
    	Documents documents = (Documents) list(nuxeoSession);
    	String idSource;
    	String idParentSource;
//    	Date startReccuringSource;
//    	Calendar calStartReccuringSource;
    	Calendar calLastModified = Calendar.getInstance();
    	Date lastModified;
    	List<String> documentsToDelete = new ArrayList<>();
    	List<EventToSync> documentsToUpdate = new ArrayList<>();
    	
    	DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
    	PropertyMap map;
    	EventKey eventKey;
    	
    	for (Document document : documents)
        {
        	logger.info("Traitement de l'événement : "+document.getId());
            
        	idSource = document.getString(ID_SOURCE_PROPERTY);
            if (StringUtils.isNotEmpty(idSource)) {
            	idParentSource = document.getString(ID_PARENT_SOURCE_PROPERTY);
            	eventKey = new EventKey(idSource, idParentSource, null);
//            	startReccuringSource = sourceContent.getDate("startDateReccuringSource");
//            	if (startReccuringSource == null)
//            	{
//            		calStartReccuringSource = null;
//            	}
//            	else
//            	{
//            		calStartReccuringSource = Calendar.getInstance();
//            		calStartReccuringSource.setTime(startReccuringSource);
//            	}
            	lastModified = document.getDate(LAST_MODIFIED_SOURCE);
            	
            	if (lastModified != null) calLastModified.setTime(lastModified);
            	EventToSync eventToSync = mapVevent.get(eventKey);
            	if (eventToSync == null)
            	{
            		documentsToDelete.add(document.getId());
            	}
            	else
            	{
            		if (calLastModified != null && eventToSync.getLastModifiedSource().getTime().compareTo(calLastModified.getTime())!=0)
//            				&& eventToSync.getStartReccuringCalSource().getTime() != null
//            				&& calStartReccuringSource != null
//            				&& eventToSync.getStartReccuringCalSource().getTime().compareTo(calStartReccuringSource.getTime()) != 0)
            		{
            			eventToSync.setId(document.getId());
//            			Event eventToUpdate = new Event(document.getId(),eventToSync.getTitle(),
//            					eventToSync.getStartDate(), eventToSync.getEndDate(), 
//            					false, null, eventToSync.getSummary(),null, this.idAgenda, eventToSync.getIdEventSource(), eventToSync.getCreatedSource(), eventToSync.getLastModifiedSource());
            			documentsToUpdate.add(eventToSync);
            		}
            		mapVevent.remove(eventKey);
            	}
            }
        }
    	//First, delete event that were deleted until the last synchronization
    	for(String documentId: documentsToDelete)
    	{
    		documentService.remove(documentId);
    	}
    	//Second, update event that were updated until the last synchronization
    	for(EventToSync eventToUpdate: documentsToUpdate)
    	{
    		map = fillMap(eventToUpdate);
    		documentService.update(new DocRef(eventToUpdate.getId()), map, true);
    	}
    	//Third, add new events
    	for(EventToSync eventToCreate: this.mapVevent.values())
    	{
    		map = fillMap(eventToCreate);
    		documentService.createDocument(new DocRef(this.parentPath), DOCUMENT_TYPE_EVENEMENT, null, map, true);
    	}

    	return null;
    }

    /**
     * List event in Nuxeo that were previously synchronized from the idAgenda source
     * @param nuxeoSession
     * @return
     * @throws Exception
     */
    private Object list(Session nuxeoSession) throws Exception
    {
    	// Clause
        StringBuilder clause = new StringBuilder();
        clause.append(" ecm:primaryType='VEVENT' ");
        clause.append(" AND (sync:idParentSource is not null) ");
        clause.append(" ORDER BY vevent:dtstart");

        // Filter on published documents
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(this.queryContext, clause.toString());

        // Request
        OperationRequest request;
        request = nuxeoSession.newRequest("Document.Query");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");

        request.set("query", "SELECT * FROM Document WHERE " + filteredRequest);

        return request.execute();
    }
    
    private PropertyMap fillMap(EventToSync event) {
        PropertyMap map = new PropertyMap();
        //Les dates sont déjà au format UTC, donc on passe par DateFormatUtils.format sinon Nuxeo enlève 1 ou 2 heures en pensant qu'on lui passe des dates en GMT+1
        if (event.isAllDay())
        {
	        map.set(CalendarViewRepository.END_DATE_PROPERTY, formatDate(event.getEndCal()));
	        map.set(CalendarViewRepository.START_DATE_PROPERTY, formatDate(event.getStartCal()));
        }
        else
        {
	        map.set(CalendarViewRepository.END_DATE_PROPERTY, getDateWithServerTimeZone(event.getEndCal()));
	        map.set(CalendarViewRepository.START_DATE_PROPERTY, getDateWithServerTimeZone(event.getStartCal()));
        }
        map.set(CalendarViewRepository.TITLE_PROPERTY, event.getTitle());
        map.set(CalendarEventViewService.DESCRIPTION_PROPERTY, event.getDescription());
        map.set(CalendarViewRepository.ID_PARENT_SOURCE_PROPERTY, event.getIdAgendaSource());
        map.set(CalendarViewRepository.ID_SOURCE_PROPERTY, event.getIdEventSource());
        map.set(CalendarViewRepository.CREATED_SOURCE, getDateWithServerTimeZone(event.getCreateCalSource()));
        map.set(CalendarViewRepository.LAST_MODIFIED_SOURCE, getDateWithServerTimeZone(event.getLastModifiedSource()));
        map.set(CalendarViewRepository.ALL_DAY_PROPERTY,event.isAllDay());
//        map.set(ICalendarRepository.START_DATE_RECCURING_SOURCE, getDateWithServerTimeZone(event.getStartReccuringCalSource()));
        return map;
    }
    
    private Date getDateWithServerTimeZone(Calendar calDate)
    {
    	if (calDate == null)
    	{
    		return null;
    	}
    	else
    	{
    		calDate.setTimeZone(TimeZone.getDefault());
    	}
    	return calDate.getTime();
    }
    
	private String formatDate(Calendar calDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calDate.setTimeZone(TimeZone.getDefault());
		return sdf.format(calDate.getTime());
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Calendar/" + this.contextPath;
    }

}
