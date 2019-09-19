package org.osivia.services.calendar.view.portlet.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import fr.toutatice.portail.cms.nuxeo.api.*;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.repository.CalendarRepositoryImpl;
import org.osivia.services.calendar.common.repository.command.EventRemoveCommand;
import org.osivia.services.calendar.edition.portlet.model.CalendarSynchronizationSource;
import org.osivia.services.calendar.view.portlet.model.CalendarEditionMode;
import org.osivia.services.calendar.view.portlet.model.CalendarOptions;
import org.osivia.services.calendar.view.portlet.model.CalendarViewForm;
import org.osivia.services.calendar.view.portlet.model.events.Event;
import org.osivia.services.calendar.view.portlet.model.events.EventKey;
import org.osivia.services.calendar.view.portlet.model.events.EventToSync;
import org.osivia.services.calendar.view.portlet.repository.command.EventEditionCommand;
import org.osivia.services.calendar.view.portlet.repository.command.EventGetCommand;
import org.osivia.services.calendar.view.portlet.repository.command.EventListCommand;
import org.osivia.services.calendar.view.portlet.repository.command.SynchronizationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Calendar repository implementation.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 * @see CalendarRepositoryImpl
 * @see CalendarViewRepository
 */
@Repository
public class CalendarViewRepositoryImpl extends CalendarRepositoryImpl implements CalendarViewRepository {

    /** Nuxeo document request attribute name. */
    private static final String DOCUMENT_REQUEST_ATTRIBUTE = "osivia.calendar.document";


    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;

    
    /**
     * Constructor.
     */
    public CalendarViewRepositoryImpl() {
        super();

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarOptions getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        // Current window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Calendar configuration
        CalendarOptions configuration = this.applicationContext.getBean(CalendarOptions.class);
        configuration.setCmsPath(window.getProperty(CMS_PATH_WINDOW_PROPERTY));
        configuration.setPeriodTypeName(window.getProperty(DEFAULT_VIEW_WINDOW_PROPERTY));
        configuration.setCompactView(BooleanUtils.toBoolean(window.getProperty(COMPACT_VIEW_WINDOW_PROPERTY)));
        configuration.setReadOnly(BooleanUtils.toBoolean(window.getProperty(READ_ONLY_WINDOW_PROPERTY)));
        configuration.setIntegration(BooleanUtils.toBoolean(window.getProperty(INTEGRATION_WINDOW_PROPERTY)));

        return configuration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getCalendarPath(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return this.getCMSPath(nuxeoController);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());

        Document document = this.getDocument(nuxeoController);

        String title = null;
        if (document != null) {
            title = document.getTitle();
        }

        return title;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<CalendarSynchronizationSource> getSynchronizationSources(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());

        Document document = this.getDocument(nuxeoController);

        ArrayList<CalendarSynchronizationSource> listSource = new ArrayList<CalendarSynchronizationSource>();
        if (document != null) {
	        PropertyList propertyList = (PropertyList) document.getProperties().get(LIST_SOURCE_SYNCHRO);
	        if (propertyList != null) {
	            CalendarSynchronizationSource source;
	            for (int i = 0; i < propertyList.size(); i++) {
	                PropertyMap map = propertyList.getMap(i);
	                source = new CalendarSynchronizationSource();
	                source.setColor((map.getString(COLOR_SYNCHRONIZATION) == null) ? null : CalendarColor.fromId(map.getString(COLOR_SYNCHRONIZATION)));
	                source.setDisplayName(map.getString(DISPLAYNAME_SYNCHRONIZATION));
	                source.setUrl(map.getString(URL_SYNCHRONIZATION));
	                source.setId(map.getString(SOURCEID_SYNCHRONIZATION));
	                listSource.add(source);
	            }
	        }
	    }

        return listSource;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getColorIdAgenda(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());

        Document document = this.getDocument(nuxeoController);
        if (document != null)
        {
        	return document.getString(PRIMARY_CALENDAR_COLOR);
        } else
        {
        	return "";
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration(PortalControllerContext portalControllerContext, CalendarOptions configuration) throws PortletException {
        // Current window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        window.setProperty(CMS_PATH_WINDOW_PROPERTY, configuration.getCmsPath());
        window.setProperty(DEFAULT_VIEW_WINDOW_PROPERTY, configuration.getPeriodTypeName());
        window.setProperty(COMPACT_VIEW_WINDOW_PROPERTY, BooleanUtils.toStringTrueFalse(configuration.isCompactView()));
        window.setProperty(READ_ONLY_WINDOW_PROPERTY, BooleanUtils.toStringTrueFalse(configuration.isReadOnly()));
        window.setProperty(INTEGRATION_WINDOW_PROPERTY, BooleanUtils.toStringTrueFalse(configuration.isIntegration()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> getEvents(PortalControllerContext portalControllerContext, Date start, Date end) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS path
        String cmsPath = this.getCMSPath(nuxeoController);

        List<Event> events;
        if (StringUtils.isEmpty(cmsPath)) {
            events = null;
        } else {
            List<CalendarSynchronizationSource> listSource = getSynchronizationSources(portalControllerContext);

            // Nuxeo command
            INuxeoCommand nuxeoCommand = new EventListCommand(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, cmsPath, start, end, listSource);
            Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);

            // Events
            events = new ArrayList<Event>(documents.size());

            for (Document document : documents) {
                if ((document.getDate(START_DATE_PROPERTY) != null) && (document.getDate(END_DATE_PROPERTY) != null)) {
                    // Event
                    Event event = fillEvent(document, nuxeoController);
                    events.add(event);
                }
            }
        }

        return events;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Event getEvent(PortalControllerContext portalControllerContext, String docid) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());
        // CMS path
        String cmsPath = this.getCMSPath(nuxeoController);
        INuxeoCommand nuxeoCommand = new EventGetCommand(cmsPath, docid);
        Document document = (Document) nuxeoController.executeNuxeoCommand(nuxeoCommand);

        return fillEvent(document, nuxeoController);
    }


    /**
     * Fill event attributes
     * 
     * @param document
     * @param nuxeoController
     * @return event filled
     */
    protected Event fillEvent(Document document, NuxeoController nuxeoController) {
        String id = document.getId();
        String title = document.getTitle();
        Date startDate = document.getDate(START_DATE_PROPERTY);
        Date endDate = document.getDate(END_DATE_PROPERTY);
        String bckgcolor = document.getString(BCKG_COLOR);
        boolean allDay = BooleanUtils.isTrue(document.getProperties().getBoolean(ALL_DAY_PROPERTY));
        String viewURL = nuxeoController.getLink(document).getUrl();
        String idEventSrc;
        String idParentSrc;
        idEventSrc = document.getString(ID_SOURCE_PROPERTY);
        idParentSrc = document.getString(ID_PARENT_SOURCE_PROPERTY);

        Event event = this.applicationContext.getBean(Event.class, id, title, startDate, endDate, allDay, bckgcolor, viewURL, idEventSrc, idParentSrc);

        // Last modified date
        Date lastModified = document.getDate("dc:modified");
        event.setLastModified(lastModified);

        // Location
        String location = document.getString("vevent:location");
        event.setLocation(location);

        // Description
        String content = document.getString("note:note");
        if (StringUtils.isNotBlank(content)) {
            String description = nuxeoController.transformHTMLContent(content);
            event.setDescription(description);
        }

        return event;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void definePortletUri(PortalControllerContext portalControllerContext) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, portalControllerContext.getResponse(), portalControllerContext.getPortletCtx());

        // Path
        String path = this.getCMSPath(nuxeoController);
        request.setAttribute(Constants.REQUEST_ATTR_URI, path);
    }


    /**
     * Get CMS path.
     *
     * @param nuxeoController Nuxeo controller
     * @return CMS path
     * @throws PortletException
     */
    protected String getCMSPath(NuxeoController nuxeoController) throws PortletException {
        // Portlet configuration
        CalendarOptions configuration = this.getConfiguration(nuxeoController.getPortalCtx());

        // Context path
        String cmsPath;
        if (StringUtils.isNotBlank(configuration.getCmsPath())) {
            cmsPath = configuration.getCmsPath();
        } else {
            // Current window
            PortalWindow window = WindowFactory.getWindow(nuxeoController.getRequest());

            cmsPath = window.getPageProperty("osivia.cms.basePath");
        }

        return nuxeoController.getComputedPath(cmsPath);
    }


    /**
     * Get Nuxeo document.
     *
     * @param nuxeoController Nuxeo controller
     * @return document
     * @throws PortletException
     */
    protected Document getDocument(NuxeoController nuxeoController) throws PortletException {
        PortletRequest request = nuxeoController.getRequest();
        Document document = (Document) request.getAttribute(DOCUMENT_REQUEST_ATTRIBUTE);

        if (document == null) {
            try {
                String cmsPath = this.getCMSPath(nuxeoController);
                if (StringUtils.isNotEmpty(cmsPath)) {
                    NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(cmsPath);
                    document = documentContext.getDocument();
                    request.setAttribute(DOCUMENT_REQUEST_ATTRIBUTE, document);
                }
            } catch (NuxeoException e) {
                if (NuxeoException.ERROR_FORBIDDEN != e.getErrorCode()) {
                    throw new PortletException(e);
                }
            }
        }

        return document;
    }


    /**
     * {@inheritDoc}
     */
	@Override
	public void save(PortalControllerContext portalControllerContext, CalendarViewForm form) throws PortletException {
		// Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS path
        String cmsPath = this.getCMSPath(nuxeoController);
        form.setParentPath(cmsPath);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(EventEditionCommand.class, form);
        Document document = (Document) nuxeoController.executeNuxeoCommand(command);
        
        if (CalendarEditionMode.EDITION.equals(form.getMode())) {
            // Refresh document
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());
            documentContext.reload();
            document = documentContext.getDocument();
        }

        form.setDocument(document);
	}


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEventEditable(PortalControllerContext portalControllerContext, String docid) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, portalControllerContext.getResponse(), portalControllerContext.getPortletCtx());
        NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();
        return documentContext.getPermissions().isEditable();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(PortalControllerContext portalControllerContext, CalendarViewForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        INuxeoCommand command = new EventRemoveCommand(form.getDocId());
        nuxeoController.executeNuxeoCommand(command);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void synchronize(PortalControllerContext portalControllerContext, Map<EventKey, EventToSync> map) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS path
        String cmsPath = this.getCMSPath(nuxeoController);
        String parentPath = nuxeoController.getContentPath();

        INuxeoCommand nuxeoCommand = new SynchronizationCommand(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, cmsPath, parentPath, map);
        nuxeoController.executeNuxeoCommand(nuxeoCommand);
    }

}
