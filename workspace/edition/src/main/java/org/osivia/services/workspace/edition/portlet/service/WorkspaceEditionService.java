package org.osivia.services.workspace.edition.portlet.service;

import java.io.IOException;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionOptions;

/**
 * Workspace edition service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceEditionService {

    /**
     * Get options.
     * 
     * @param portalControllerContext portal controller context
     * @return options
     * @throws PortletException
     */
    WorkspaceEditionOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get workspace edition form.
     *
     * @param portalControllerContext portal controller context
     * @param options options
     * @return form
     * @throws PortletException
     */
    WorkspaceEditionForm getForm(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options) throws PortletException;


    /**
     * Sort tasks.
     *
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     */
    void sort(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


    /**
     * Upload vignette.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     * @throws IOException
     */
    void uploadVignette(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException, IOException;


    /**
     * Delete vignette.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     */
    void deleteVignette(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


    /**
     * Save edition.
     *
     * @param portalControllerContext portal controller context
     * @param options options
     * @param form workspace edition form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options, WorkspaceEditionForm form) throws PortletException;


    /**
     * Create task.
     * 
     * @param portalControllerContext
     * @param form workspace edition form
     * @throws PortletException
     */
    void createTask(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


    /**
     * Delete workspace and return redirection URL.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @return URL
     * @throws PortletException
     */
    String delete(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options) throws PortletException;


    /**
     * Get workspace URL.
     *
     * @param portalControllerContext portal controller context
     * @param options options
     * @return URL
     * @throws PortletException
     */
    String getWorkspaceUrl(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options) throws PortletException;


    /**
     * Get workspace task creation URL.
     * 
     * @param portalControllerContext portal controller context
     * @param options options
     * @return URL
     * @throws PortletException
     */
    String getTaskCreationUrl(PortalControllerContext portalControllerContext, WorkspaceEditionOptions options) throws PortletException;

}
