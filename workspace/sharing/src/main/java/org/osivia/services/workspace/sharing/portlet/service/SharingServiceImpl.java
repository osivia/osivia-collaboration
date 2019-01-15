package org.osivia.services.workspace.sharing.portlet.service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.workspace.sharing.portlet.model.SharingForm;
import org.osivia.services.workspace.sharing.portlet.model.SharingWindowProperties;
import org.osivia.services.workspace.sharing.portlet.repository.SharingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Sharing portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SharingService
 */
@Service
public class SharingServiceImpl implements SharingService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private SharingRepository repository;


    /**
     * Constructor.
     */
    public SharingServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SharingWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Window properties
        SharingWindowProperties windowProperties = this.applicationContext.getBean(SharingWindowProperties.class);

        // Path
        String path = window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
        windowProperties.setPath(path);

        return windowProperties;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SharingForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        // Form
        SharingForm form = this.applicationContext.getBean(SharingForm.class);

        // Enabled indicator
        boolean enabled = this.repository.isSharingEnabled(portalControllerContext, path);
        form.setEnabled(enabled);

        if (enabled) {
            // Link
            // TODO
        }

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void enableSharing(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException {
        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        this.repository.enableSharing(portalControllerContext, path);

        // Update model
        form.setEnabled(true);
        // TODO
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void disableSharing(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException {
        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        this.repository.disableSharing(portalControllerContext, path);

        // Update model
        form.setEnabled(false);
        // TODO
    }

}
