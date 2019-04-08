package org.osivia.services.widgets.rename.portlet.service;

import java.io.IOException;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.widgets.rename.portlet.model.RenameForm;
import org.osivia.services.widgets.rename.portlet.repository.RenameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Rename portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see RenameService
 */
@Service
public class RenameServiceImpl implements RenameService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private RenameRepository repository;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public RenameServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public RenameForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Current document
        Document document = this.getCurrentDocument(portalControllerContext);

        // Rename form
        RenameForm form = this.applicationContext.getBean(RenameForm.class);

        // Document title
        String title = document.getTitle();
        form.setTitle(title);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, RenameForm form) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Current document path
        String path = this.getCurrentPath(portalControllerContext);
        // Current document context
        NuxeoDocumentContext documentContext = this.repository.getDocumentContext(portalControllerContext, path);
        // Current document
        Document document = documentContext.getDocument();

        // Old document title
        String oldTitle = document.getTitle();
        // New document title
        String newTitle = StringUtils.trim(form.getTitle());

        if (!StringUtils.equals(oldTitle, newTitle)) {
            // Rename
            this.repository.rename(portalControllerContext, path, newTitle);

            // Notification
            String message = bundle.getString("RENAME_MESSAGE_SUCCESS");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        }

        // Redirection
        String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null,
                null);
        response.sendRedirect(url);
    }


    /**
     * Get current Nuxeo document.
     * 
     * @param portalControllerContext portal controller context
     * @return Nuxeo document
     * @throws PortletException
     */
    private Document getCurrentDocument(PortalControllerContext portalControllerContext) throws PortletException {
        // Current document path
        String path = this.getCurrentPath(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = this.repository.getDocumentContext(portalControllerContext, path);

        return documentContext.getDocument();
    }


    /**
     * Get current document path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    private String getCurrentPath(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Current window
        PortalWindow window = WindowFactory.getWindow(request);

        return window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
    }

}
