package org.osivia.services.workspace.edition.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.edition.portlet.model.Editorial;
import org.osivia.services.workspace.edition.portlet.model.Image;
import org.osivia.services.workspace.edition.portlet.model.Task;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace edition repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceEditionRepository {

    /**
     * Get workspace.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    Document getWorkspace(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get workspace vignette.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace or room Nuxeo document
     * @return vignette
     * @throws PortletException
     */
    Image getVignette(PortalControllerContext portalControllerContext, Document workspace) throws PortletException;


    /**
     * Get workspace banner.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace or room Nuxeo document
     * @return banner
     * @throws PortletException
     */
    Image getBanner(PortalControllerContext portalControllerContext, Document workspace) throws PortletException;


    /**
     * Get workspace tasks.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace or room Nuxeo document
     * @return tasks
     * @throws PortletException
     */
    List<Task> getTasks(PortalControllerContext portalControllerContext, Document workspace) throws PortletException;


    /**
     * Get workspace editorial.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace or room Nuxeo document
     * @return editorial
     * @throws PortletException
     */
    Editorial getEditorial(PortalControllerContext portalControllerContext, Document workspace) throws PortletException;


    /**
     * Create workspace editorial.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace
     * @return editorial
     * @throws PortletException
     */
    Editorial createEditorial(PortalControllerContext portalControllerContext, Document workspace) throws PortletException;


    /**
     * Check permissions.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace or room Nuxeo document
     * @return true if current user has management permissions
     * @throws PortletException
     */
    boolean checkPermissions(PortalControllerContext portalControllerContext, Document workspace) throws PortletException;


    /**
     * Update workspace type.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace Nuxeo document
     * @param workspaceType updated workspace type
     * @throws PortletException
     */
    void updateWorkspaceType(PortalControllerContext portalControllerContext, Document workspace, WorkspaceType workspaceType) throws PortletException;


    /**
     * Update tasks.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace or room Nuxeo document
     * @param tasks tasks
     * @throws PortletException
     */
    void updateTasks(PortalControllerContext portalControllerContext, Document workspace, List<Task> tasks) throws PortletException;


    /**
     * Update workspace or room properties.
     *
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     */
    void updateProperties(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


    /**
     * Update workspace or room editorial.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     */
    void updateEditorial(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


    /**
     * Check workspace title availability.
     * 
     * @param form workspace edition form
     * @return true if no workspace with this title exists
     * @throws PortletException
     */
    boolean checkTitleAvailability(WorkspaceEditionForm form) throws PortletException;


    /**
     * Check webId availability.
     * 
     * @param webId webId
     * @return true if webId is available
     * @throws PortletException
     */
    boolean checkWebIdAvailability(String webId) throws PortletException;


    /**
     * Delete workspace.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;

}
