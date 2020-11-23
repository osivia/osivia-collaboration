package org.osivia.services.calendar.event.preview.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jboss.portal.core.model.portal.PortalObjectId;
import org.jboss.portal.core.model.portal.PortalObjectPath;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.calendar.event.preview.portlet.model.CalendarEventPreviewForm;
import org.osivia.services.calendar.event.preview.portlet.repository.CalendarEventPreviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import java.util.Date;

/**
 * Calendar event preview portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarEventPreviewService
 */
@Service
public class CalendarEventPreviewServiceImpl implements CalendarEventPreviewService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private CalendarEventPreviewRepository repository;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;


    @Override
    public CalendarEventPreviewForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Document path
        String path = window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
        // Document context
        NuxeoDocumentContext documentContext = this.repository.getDocumentContext(portalControllerContext, path);
        // Document
        Document document = documentContext.getDocument();

        // Page identifier
        String pageId = window.getProperty(PAGE_ID_WINDOW_PROPERTY);
        // Page path
        String pagePath;
        if (StringUtils.isEmpty(pageId)) {
            pagePath = null;
        } else {
            PortalObjectId pageObjectId = PortalObjectId.parse(pageId, PortalObjectPath.SAFEST_FORMAT);
            pagePath = pageObjectId.toString(PortalObjectPath.CANONICAL_FORMAT);
        }


        // Form
        CalendarEventPreviewForm form = this.applicationContext.getBean(CalendarEventPreviewForm.class);

        // Title
        String title = document.getTitle();
        form.setTitle(title);
        // All day indicator
        boolean allDay = BooleanUtils.toBoolean(document.getProperties().getBoolean("vevent:allDay"));
        form.setAllDay(allDay);
        // Start date
        Date startDate = document.getDate("vevent:dtstart");
        form.setStartDate(startDate);
        // End date
        Date endDate = document.getDate("vevent:dtend");
        form.setEndDate(endDate);
        // All day end date
        Date endDateAllDay;
        if (allDay) {
            endDateAllDay = DateUtils.addDays(endDate, -1);
        } else {
            endDateAllDay = null;
        }
        form.setEndDateAllDay(endDateAllDay);
        // Same day indicator
        boolean sameDay;
        if (allDay) {
            sameDay = DateUtils.isSameDay(startDate, endDateAllDay);
        } else {
            sameDay = DateUtils.isSameDay(startDate, endDate);
        }
        form.setSameDay(sameDay);
        // Location
        String location = document.getString("vevent:location");
        form.setLocation(location);

        // Detail URL
        String detailUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, pagePath, document.getPath(), null, null, null, null, null, null, null);
        form.setDetailUrl(detailUrl);

        // Edit URL
        if (documentContext.getPermissions().isEditable()) {
            // TODO : L'API ne permet pas actuellement de faire un lien CMS avec un mode d'édition actif
        }

        return form;
    }

}
