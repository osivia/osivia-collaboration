package org.osivia.services.workspace.task.creation.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.workspace.task.creation.portlet.model.TaskCreationForm;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * Workspace task creation repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceTaskCreationRepository
 */
@Repository
public class WorkspaceTaskCreationRepositoryImpl implements WorkspaceTaskCreationRepository, ApplicationContextAware {

    /**
     * Taskbar service.
     */
    @Autowired
    private ITaskbarService taskbarService;

    /**
     * Bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Application context.
     */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public WorkspaceTaskCreationRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document types
        Map<String, DocumentType> documentTypes = nuxeoController.getCMSItemTypes();

        // Workspace document type
        String workspaceType = this.getWorkspaceType(portalControllerContext);
        DocumentType workspaceDocumentType = documentTypes.get(workspaceType);

        // Workspace sub-types
        List<String> workspaceSubTypes;
        if (workspaceDocumentType == null) {
            workspaceSubTypes = new ArrayList<>(0);
        } else {
            workspaceSubTypes = workspaceDocumentType.getSubtypes();
        }

        // Types
        List<DocumentType> types = new ArrayList<>(workspaceSubTypes.size());
        for (String subType : workspaceSubTypes) {
            DocumentType type = documentTypes.get(subType);
            if (type != null) {
                types.add(type);
            }
        }

        return types;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspacePath(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        return window.getProperty(WORKSPACE_PATH_WINDOW_PROPERTY);
    }


    /**
     * Get workspace type.
     *
     * @param portalControllerContext portal controller context
     * @return type
     */
    private String getWorkspaceType(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Window property
        String property = window.getProperty(WORKSPACE_TYPE_WINDOW_PROPERTY);

        return StringUtils.defaultIfEmpty(property, "Workspace");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String create(PortalControllerContext portalControllerContext, TaskCreationForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Workspace path
        String path = this.getWorkspacePath(portalControllerContext);

        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Default taskbar items
        SortedSet<TaskbarItem> items;
        try {
            items = this.taskbarService.getDefaultItems(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(WorkspaceTaskCreationCommand.class, path, form, items, bundle);
        Document document = (Document) nuxeoController.executeNuxeoCommand(command);

        return document.getPath();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
