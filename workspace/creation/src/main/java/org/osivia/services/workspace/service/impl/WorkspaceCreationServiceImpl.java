package org.osivia.services.workspace.service.impl;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.model.WorkspaceCreationForm;
import org.osivia.services.workspace.repository.WorkspaceCreationRepository;
import org.osivia.services.workspace.service.WorkspaceCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Workspace creation service implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceCreationService
 */
@Service
public class WorkspaceCreationServiceImpl implements WorkspaceCreationService {

    /** Workspace creation repository. */
    @Autowired
    private WorkspaceCreationRepository repository;


    /**
     * Constructor.
     */
    public WorkspaceCreationServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(PortalControllerContext portalControllerContext, WorkspaceCreationForm form) throws PortletException {
        // Create workspace Nuxeo document
        Document workspace = this.repository.createDocument(portalControllerContext, form);
        // Create LDAP groups
        this.repository.createGroups(portalControllerContext, form, workspace);
    }

}
