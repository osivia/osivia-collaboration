package org.osivia.services.workspace.repository;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.model.WorkspaceCreationForm;

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

}
