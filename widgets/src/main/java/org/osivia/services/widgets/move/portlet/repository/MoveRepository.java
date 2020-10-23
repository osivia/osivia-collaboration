package org.osivia.services.widgets.move.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.move.portlet.model.MoveWindowProperties;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Move portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface MoveRepository {

    /**
     * Get base path.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @return path
     */
    String getBasePath(PortalControllerContext portalControllerContext, MoveWindowProperties windowProperties) throws PortletException;


    /**
     * Get navigation path.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @param basePath                base path
     * @return path
     */
    String getNavigationPath(PortalControllerContext portalControllerContext, MoveWindowProperties windowProperties, String basePath) throws PortletException;


    /**
     * Get document.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @return document
     */
    Document getDocument(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Move.
     *
     * @param portalControllerContext portal controller context
     * @param basePath                base path
     * @param identifiers             source identifiers
     * @param targetPath              target path
     */
    void move(PortalControllerContext portalControllerContext, String basePath, List<String> identifiers, String targetPath) throws PortletException;


    /**
     * Gets the user workspace.
     *
     * @param portalControllerContext the portal controller context
     * @return the user workspace
     * @throws PortletException the portlet exception
     */
    Document getUserWorkspace(PortalControllerContext portalControllerContext) throws PortletException;

}
