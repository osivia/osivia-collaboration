package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.ParentDocument;
import org.osivia.services.workspace.portlet.model.TrashedDocument;

/**
 * Trash portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface TrashRepository {

    /**
     * Get trashed documents.
     * 
     * @param portalControllerContext portal controller context
     * @return trashed documents
     * @throws PortletException
     */
    List<TrashedDocument> getTrashedDocuments(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Delete all items.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void deleteAll(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Restore all items.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void restoreAll(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Delete selected items.
     * 
     * @param portalControllerContext portal controller context
     * @param selectedPaths selected item paths
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, List<String> selectedPaths) throws PortletException;


    /**
     * Restore selected items.
     * 
     * @param portalControllerContext portal controller context
     * @param selectedPaths selected paths
     * @throws PortletException
     */
    void restore(PortalControllerContext portalControllerContext, List<String> selectedPaths) throws PortletException;


    /**
     * Get location parent documents.
     * 
     * @param portalControllerContext portal controller context
     * @param path location path
     * @return parent documents
     * @throws PortletException
     */
    List<ParentDocument> getLocationParents(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
