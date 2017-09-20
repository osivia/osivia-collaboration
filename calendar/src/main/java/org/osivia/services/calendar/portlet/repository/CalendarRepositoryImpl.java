package org.osivia.services.calendar.portlet.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSPublicationInfos;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.services.calendar.portlet.model.CalendarConfiguration;
import org.osivia.services.calendar.portlet.model.events.Event;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Calendar repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see ICalendarRepository
 */
@Repository
public class CalendarRepositoryImpl implements ICalendarRepository {

    /** CMS path window property name. */
    private static final String CMS_PATH_WINDOW_PROPERTY = "osivia.calendar.cmsPath";
    /** Default view window property name. */
    private static final String DEFAULT_VIEW_WINDOW_PROPERTY = "osivia.calendar.defaultView";
    /** Compact view indicator window property name. */
    private static final String COMPACT_VIEW_WINDOW_PROPERTY = "osivia.calendar.compactView";
    /** Nuxeo document request attribute name. */
    private static final String DOCUMENT_REQUEST_ATTRIBUTE = "osivia.calendar.document";


    /**
     * Default constructor.
     */
    public CalendarRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarConfiguration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        // Current window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Calendar configuration
        CalendarConfiguration configuration = new CalendarConfiguration();
        configuration.setCmsPath(window.getProperty(CMS_PATH_WINDOW_PROPERTY));
        configuration.setPeriodTypeName(window.getProperty(DEFAULT_VIEW_WINDOW_PROPERTY));
        configuration.setCompactView(BooleanUtils.toBoolean(window.getProperty(COMPACT_VIEW_WINDOW_PROPERTY)));

        return configuration;
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
    public void saveConfiguration(PortalControllerContext portalControllerContext, CalendarConfiguration configuration) throws PortletException {
        // Current window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        window.setProperty(CMS_PATH_WINDOW_PROPERTY, configuration.getCmsPath());
        window.setProperty(DEFAULT_VIEW_WINDOW_PROPERTY, configuration.getPeriodTypeName());
        window.setProperty(COMPACT_VIEW_WINDOW_PROPERTY, BooleanUtils.toStringTrueFalse(configuration.isCompactView()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> getEvents(PortalControllerContext portalControllerContext, Date start, Date end) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());

        // CMS path
        String cmsPath = this.getCMSPath(nuxeoController);

        List<Event> events;
        if (StringUtils.isEmpty(cmsPath)) {
            events = null;
        } else {

            // Nuxeo command
            INuxeoCommand nuxeoCommand = new ListEventsCommand(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, cmsPath, start, end);
            Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);

            // Events
            events = new ArrayList<Event>(documents.size());
            for (Document document : documents) {
                // Document properties
                String id = document.getId();
                String title = document.getTitle();
                Date startDate = document.getDate(START_DATE_PROPERTY);
                Date endDate = document.getDate(END_DATE_PROPERTY);
                boolean allDay = BooleanUtils.isTrue(document.getProperties().getBoolean(ALL_DAY_PROPERTY));
                String viewURL = nuxeoController.getLink(document).getUrl();

                if ((startDate != null) && (endDate != null)) {
                    // Event
                    Event event = new Event(id, title, startDate, endDate, allDay, viewURL);
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
    public void insertContentMenubarItems(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());

        try {
            Document document = this.getDocument(nuxeoController);
            if ((document != null) && ("Agenda".equals(document.getType()))) {
                nuxeoController.setCurrentDoc(document);

                // CMS service
                ICMSService cmsService = NuxeoController.getCMSService();
                // CMS context
                CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

                // CMS path
                String cmsPath = this.getCMSPath(nuxeoController);

                // CMS publication infos
                CMSPublicationInfos cmsPublicationInfos = cmsService.getPublicationInfos(cmsContext, cmsPath);

                // Add button if editable
                if (cmsPublicationInfos.isEditableByUser()) {
                    nuxeoController.setParentPathToCreate(cmsPath);
                }

                // Insert content menubar items
                nuxeoController.insertContentMenuBarItems();
            }
        } catch (CMSException e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void definePortletUri(PortalControllerContext portalControllerContext) throws PortletException {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, portalControllerContext.getResponse(),
                portalControllerContext.getPortletCtx());

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
    private String getCMSPath(NuxeoController nuxeoController) throws PortletException {
        // Portlet configuration
        CalendarConfiguration configuration = this.getConfiguration(nuxeoController.getPortalCtx());

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
    private Document getDocument(NuxeoController nuxeoController) throws PortletException {
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

}
