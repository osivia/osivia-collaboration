package org.osivia.services.workspace.portlet.repository;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;

/**
 * Workspace creation repository.
 *
 * @author Cédric Krommenhoek
 */
public interface WorkspaceCreationRepository {

    /**
     * Create Nuxeo document.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @return Nuxeo document
     * @throws PortletException
     */
    Document createDocument(PortalControllerContext portalControllerContext, WorkspaceCreationForm form) throws PortletException;


    /**
     * Create LDAP groups.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @param workspace workspace Nuxeo document
     * @throws PortletException
     */
    void createGroups(PortalControllerContext portalControllerContext, WorkspaceCreationForm form, Document workspace) throws PortletException;


    /**
     * Update permissions.
     *
     * @param portalControllerContext portal controller context
     * @param form form
     * @param workspace workspace Nuxeo document
     * @throws PortletException
     */
    void updatePermissions(PortalControllerContext portalControllerContext, WorkspaceCreationForm form, Document workspace) throws PortletException;


    /**
     * Check workspace title availability.
     * 
     * @param portalControllerContext portal controller context
     * @param modelWebId model webId
     * @param procedureInstanceUuid procedure instance UUID
     * @param title workspace title
     * @param titleVariableName workspace title variable name
     * @return true if workspace title is available
     * @throws PortletException
     */
    boolean checkTitleAvailability(PortalControllerContext portalControllerContext, String modelWebId, String procedureInstanceUuid, String title,
            String titleVariableName) throws PortletException;

}
