package org.osivia.services.widgets.move.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.move.portlet.model.MoveForm;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Move portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface MoveService {

    /**
     * Document path window property.
     */
    String DOCUMENT_PATH_WINDOW_PROPERTY = "osivia.move.path";
    /**
     * Document identifiers window property.
     */
    String DOCUMENT_IDENTIFIERS_WINDOW_PROPERTY = "osivia.move.identifiers";
    /**
     * Ignored paths window property.
     */
    String IGNORED_PATHS_WINDOW_PROPERTY = "osivia.move.ignored-paths";
    /**
     * Base path window property.
     */
    String BASE_PATH_WINDOW_PROPERTY = "osivia.move.base-path";
    /**
     * Accepted types window property.
     */
    String ACCEPTED_TYPES_WINDOW_PROPERTY = "osivia.move.accepted-types";
    /**
     * Redirection URL window property.
     */
    String REDIRECTION_URL_WINDOW_PROPERTY = "osivia.move.redirection-url";


    /**
     * Get move form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    MoveForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Move.
     *
     * @param portalControllerContext portal controller context
     * @param form                    move form
     */
    void move(PortalControllerContext portalControllerContext, MoveForm form) throws PortletException, IOException;


    /**
     * Browse.
     *
     * @param portalControllerContext portal controller context
     * @return JSON data
     */
    String browse(PortalControllerContext portalControllerContext) throws PortletException;

}
