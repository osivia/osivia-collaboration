package org.osivia.services.widgets.rename.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
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

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.IOException;

/**
 * Rename portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see RenameService
 */
@Service
public class RenameServiceImpl implements RenameService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private RenameRepository repository;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Notifications service.
     */
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
        Document document = this.getCurrentDocument(portalControllerContext, true);

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

        // Current document
        Document document = this.getCurrentDocument(portalControllerContext, false);

        // Old document title
        String oldTitle = document.getTitle();
        // New document title
        String newTitle = StringUtils.trim(form.getTitle());

        if (!StringUtils.equals(oldTitle, newTitle)) {
            // Rename
            this.repository.rename(portalControllerContext, document.getPath(), newTitle);

            // Notification
            String message = bundle.getString("RENAME_MESSAGE_SUCCESS");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        }

        // Reload document to invalidate cache
        this.getCurrentDocument(portalControllerContext, true);

        // Redirection
        String redirectionPath = this.getRedirectionPath(portalControllerContext);
        String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, redirectionPath, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null,
                null);
        response.sendRedirect(url);
    }


    /**
     * Get current Nuxeo document.
     *
     * @param portalControllerContext portal controller context
     * @param reload reload document context indicator
     * @return Nuxeo document
     */
    private Document getCurrentDocument(PortalControllerContext portalControllerContext, boolean reload) throws PortletException {
        // Current document path
        String path = this.getCurrentPath(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = this.repository.getDocumentContext(portalControllerContext, path);
        if (reload) {
            documentContext.reload();
        }

        return documentContext.getDocument();
    }


    /**
     * Get current document path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    private String getCurrentPath(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = getWindow(portalControllerContext);

        return window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
    }


    /**
     * Get redirection path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    private String getRedirectionPath(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = getWindow(portalControllerContext);

        String path = window.getProperty(REDIRECTION_PATH_WINDOW_PROPERTY);
        if (StringUtils.isEmpty(path)) {
            path = this.getCurrentPath(portalControllerContext);
        }

        return path;
    }


    /**
     * Get current window.
     *
     * @param portalControllerContext portal controller context
     * @return window
     */
    private PortalWindow getWindow(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        return WindowFactory.getWindow(request);
    }

}
