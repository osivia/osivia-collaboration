package org.osivia.services.workspace.sharing.portlet.service;

import java.io.IOException;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.sharing.portlet.model.SharingForm;
import org.osivia.services.workspace.sharing.portlet.model.SharingWindowProperties;

/**
 * Sharing portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SharingService {

    /** Document path window property . */
    String DOCUMENT_PATH_WINDOW_PROPERTY = "osivia.sharing.path";


    /**
     * Get window properties.
     * 
     * @param portalControllerContext portal controller context
     * @return window properties
     * @throws PortletException
     */
    SharingWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    SharingForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Enable sharing.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void enableSharing(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException;


    /**
     * Disable sharing.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void disableSharing(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException;


    /**
     * Update permissions.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     */
    void updatePermissions(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException;


    /**
     * Remove user permission.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @param user user
     * @throws PortletException
     */
    void removeUser(PortalControllerContext portalControllerContext, SharingForm form, String user) throws PortletException;


    /**
     * Close modal.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     * @throws IOException
     */
    void close(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException, IOException;

}
