package org.osivia.services.workspace.task.creation.portlet.service;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.task.creation.portlet.model.TaskCreationForm;

import javax.portlet.PortletException;
import java.util.SortedMap;

/**
 * Workspace task creation service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceTaskCreationService {

    /**
     * Get workspace task creation form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     */
    TaskCreationForm getTaskCreationForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get task types, sorted by display name.
     *
     * @param portalControllerContext portal controller context
     * @return task types
     */
    SortedMap<String, DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save task creation.
     *
     * @param portalControllerContext portal controller context
     * @param form                    workspace task creation form
     * @return redirection URL
     */
    String save(PortalControllerContext portalControllerContext, TaskCreationForm form) throws PortletException;


    /**
     * Get workspace URL.
     *
     * @param portalControllerContext portal controller context
     * @return URL
     */
    String getWorkspaceUrl(PortalControllerContext portalControllerContext) throws PortletException;

}
