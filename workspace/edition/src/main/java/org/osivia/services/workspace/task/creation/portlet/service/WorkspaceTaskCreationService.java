package org.osivia.services.workspace.task.creation.portlet.service;

import java.util.SortedMap;

import javax.portlet.PortletException;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;

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
     * @throws PortletException
     */
    TaskCreationForm getTaskCreationForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get task types, sorted by display name.
     * 
     * @param portalControllerContext portal controller context
     * @return task types
     * @throws PortletException
     */
    SortedMap<String, DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save task creation.
     * 
     * @param portalControllerContext portal controller context
     * @param form workspace task creation form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, TaskCreationForm form) throws PortletException;

}
