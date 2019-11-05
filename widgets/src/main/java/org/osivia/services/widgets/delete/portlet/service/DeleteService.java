package org.osivia.services.widgets.delete.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.delete.portlet.model.DeleteForm;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Delete portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface DeleteService {

    /**
     * Portlet instance.
     */
    String PORTLET_INSTANCE = "osivia-services-widgets-delete-instance";

    /**
     * Document path window property.
     */
    String DOCUMENT_PATH_WINDOW_PROPERTY = "osivia.delete.path";
    /**
     * Document identifiers window property.
     */
    String DOCUMENT_IDENTIFIERS_WINDOW_PROPERTY = "osivia.delete.identifiers";
    /**
     * Redirection path window property.
     */
    String REDIRECTION_PATH_WINDOW_PROPERTY = "osivia.delete.redirection-path";


    /**
     * Get form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    DeleteForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Delete.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     */
    void delete(PortalControllerContext portalControllerContext, DeleteForm form) throws PortletException, IOException;

}
