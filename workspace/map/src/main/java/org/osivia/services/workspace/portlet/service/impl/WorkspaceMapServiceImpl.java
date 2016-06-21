package org.osivia.services.workspace.portlet.service.impl;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.WorkspaceMapOptions;
import org.osivia.services.workspace.portlet.repository.WorkspaceMapRepository;
import org.osivia.services.workspace.portlet.service.WorkspaceMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Workspace map service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see WorkspaceMapService
 */
@Service
public class WorkspaceMapServiceImpl implements WorkspaceMapService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Workspace map repository. */
    @Autowired
    private WorkspaceMapRepository repository;


    /**
     * Constructor.
     */
    public WorkspaceMapServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public WorkspaceMapOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace map options
        WorkspaceMapOptions options = this.applicationContext.getBean(WorkspaceMapOptions.class);
        
        // Workspace path
        String workspacePath = this.repository.getWorkspacePath(portalControllerContext);
        options.setWorkspacePath(workspacePath);

        // Navigation path
        String navigationPath = this.repository.getNavigationPath(portalControllerContext);
        options.setNavigationPath(navigationPath);

        return options;
    }

}
