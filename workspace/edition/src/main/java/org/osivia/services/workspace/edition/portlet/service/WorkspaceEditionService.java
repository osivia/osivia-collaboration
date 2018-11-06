package org.osivia.services.workspace.edition.portlet.service;

import java.io.IOException;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

/**
 * Workspace edition service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceEditionService {

    /** Workspace editorial task identifier. */
    String WORKSPACE_EDITORIAL_TASK_ID = "WORKSPACE_EDITORIAL";


    /**
     * Get workspace edition form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    WorkspaceEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


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
     * Create editorial.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @param result binding result
     * @throws PortletException
     */
    void createEditorial(PortalControllerContext portalControllerContext, WorkspaceEditionForm form, BindingResult result) throws PortletException;


    /**
     * Save edition.
     *
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;

    
    /**
     * Validate workspace edition form.
     * 
     * @param errors errors
     * @param form workspace edition form
     */
    void validate(Errors errors, WorkspaceEditionForm form);
    

    /**
     * Delete workspace and return redirection URL.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @return URL
     * @throws PortletException
     */
    String delete(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;


    /**
     * Hide task.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @param index task index
     * @throws PortletException
     */
    void hide(PortalControllerContext portalControllerContext, WorkspaceEditionForm form, int index) throws PortletException;


    /**
     * Show task.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @param index task index
     * @throws PortletException
     */
    void show(PortalControllerContext portalControllerContext, WorkspaceEditionForm form, int index) throws PortletException;


    /**
     * Get workspace URL.
     *
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @return URL
     * @throws PortletException
     */
    String getWorkspaceUrl(PortalControllerContext portalControllerContext, WorkspaceEditionForm form) throws PortletException;

}
