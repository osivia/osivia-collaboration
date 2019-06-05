package org.osivia.services.widgets.issued.portlet.service;

import java.io.IOException;
import java.util.Date;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.widgets.issued.portlet.model.IssuedForm;
import org.osivia.services.widgets.issued.portlet.repository.IssuedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Issued date portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see IssuedService
 */
@Service
public class IssuedServiceImpl implements IssuedService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private IssuedRepository repository;

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
    public IssuedServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public IssuedForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Current document path
        String path = this.getCurrentPath(portalControllerContext);

        // Form
        IssuedForm form = this.applicationContext.getBean(IssuedForm.class);

        // Issued date
        Date date = this.repository.getIssuedDate(portalControllerContext, path);
        form.setDate(date);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, IssuedForm form) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Current document path
        String path = this.getCurrentPath(portalControllerContext);

        // Update issued date
        boolean publish = this.repository.setIssuedDate(portalControllerContext, path, form.getDate());

        // Notification
        String message = bundle.getString("UPDATE_ISSUED_DATE_MESSAGE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);

        if (publish) {
            // Redirection
            String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null,
                    null, null);
            response.sendRedirect(url);
        } else {
            // Warning
            response.setRenderParameter("warning", String.valueOf(true));
        }
    }



    /**
     * Get current document path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    private String getCurrentPath(PortalControllerContext portalControllerContext) throws PortletException {
        // Current window
        PortalWindow window = getWindow(portalControllerContext);

        return window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
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
