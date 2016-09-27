package org.osivia.services.workspace.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.services.workspace.model.WorkspaceCreationForm;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace creation repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceCreationRepository
 * @see ApplicationContextAware
 */
@Repository
public class WorkspaceCreationRepositoryImpl implements WorkspaceCreationRepository, ApplicationContextAware {

    /** Properties file name. */
    private static final String PROPERTIES_FILE_NAME = "workspace-creation.properties";

    /** Workspace parent path property. */
    private static final String PARENT_PATH_PROPERTY = "workspace.parentPath";


    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;

    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /** Application context. */
    private ApplicationContext applicationContext;


    /** Properties. */
    private final Properties properties;


    /**
     * Constructor.
     *
     * @throws IOException
     */
    public WorkspaceCreationRepositoryImpl() throws IOException {
        super();

        // Generator properties
        this.properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (inputStream != null) {
            this.properties.load(inputStream);
        } else {
            throw new FileNotFoundException(PROPERTIES_FILE_NAME);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Document createDocument(PortalControllerContext portalControllerContext, WorkspaceCreationForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Workspace parent path
        String parentPath = this.properties.getProperty(PARENT_PATH_PROPERTY);

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
        INuxeoCommand command = this.applicationContext.getBean(WorkspaceCreationCommand.class, form, parentPath, items, bundle);
        return (Document) nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createGroups(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Person
        Person person = (Person) request.getAttribute(Constants.ATTR_LOGGED_PERSON_2);

        // Workspace identifier
        String identifier = workspace.getString("webc:url");

        // LDAP groups creation
        this.workspaceService.create(identifier, person);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePermissions(PortalControllerContext portalControllerContext, WorkspaceCreationForm form, Document workspace) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Workspace identifier
        String identifier = workspace.getString("webc:url");

        // Groups
        List<CollabProfile> groups = this.workspaceService.findByWorkspaceId(identifier);

        // Permissions
        List<Permission> permissions = new ArrayList<>(groups.size());
        permissions.add(Permission.getInheritanceBlocking());

        if (WorkspaceType.PUBLIC.equals(form.getType())) {
            // Public permission
            permissions.add(Permission.getPublicPermission());
        }

        for (CollabProfile group : groups) {
            // Role
            WorkspaceRole role = group.getRole();

            if (role != null) {
                Permission permission = new Permission();
                permission.setName(group.getCn());
                permission.setValues(Arrays.asList(role.getPermissions()));
                permissions.add(permission);
            }
        }

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(WorkspacePermissionsCommand.class, workspace, permissions);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
