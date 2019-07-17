package org.osivia.services.calendar.event.preview.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.apache.commons.lang.StringUtils;
import org.jboss.portal.core.model.portal.PortalObjectId;
import org.jboss.portal.core.model.portal.PortalObjectPath;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.web.IWebIdService;
import org.osivia.services.calendar.event.edition.portlet.service.CalendarEventEditionService;
import org.osivia.services.calendar.event.preview.portlet.model.CalendarEventPreviewForm;
import org.osivia.services.calendar.event.preview.portlet.repository.CalendarEventPreviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * WebId service.
     */
    @Autowired
    private IWebIdService webIdService;


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

        // TODO


        // View URL
        String cmsPath = this.webIdService.webIdToCmsPath(documentContext.getWebId());
        Map<String, String> parameters = new HashMap<>(0);
        String contextualization = IPortalUrlFactory.CONTEXTUALIZATION_PORTLET;
        String viewUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, pagePath, cmsPath, parameters, contextualization, null, null, null, null, null);
        form.setViewUrl(viewUrl);

        // Edit URL
        if (documentContext.getPermissions().isEditable()) {
            // Portlet instance
            String instance = CalendarEventEditionService.PORTLET_INSTANCE;
            // Window properties
            Map<String, String> properties = new HashMap<>();
            properties.put(Constants.WINDOW_PROP_URI, document.getPath());
            properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
            properties.put("osivia.ajaxLink", "1");
            // URL
            String editUrl;
            try {
                editUrl = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, instance, properties);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
            form.setEditUrl(editUrl);
        }

        return form;
    }

}
