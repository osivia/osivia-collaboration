package org.osivia.services.workspace.portlet.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.SortedSet;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace creation repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceCreationRepository
 * @see ApplicationContextAware
 * @see PortletContextAware
 */
@Repository
public class WorkspaceCreationRepositoryImpl implements WorkspaceCreationRepository, ApplicationContextAware, PortletContextAware {

    /** Properties file name. */
    private static final String PROPERTIES_FILE_NAME = "workspace-creation.properties";

    /** Workspace parent path property. */
    private static final String PARENT_PATH_PROPERTY = "workspace.parentPath";


    /** Person service. */
    @Autowired
    private PersonService personService;

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
    /** Portlet context. */
    private PortletContext portletContext;


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
        // HTTP servlet request
        HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(this.portletContext);
        nuxeoController.setServletRequest(servletRequest);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Locale
        Locale locale = servletRequest.getLocale();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Workspace parent path
        String parentPath = this.properties.getProperty(PARENT_PATH_PROPERTY);


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
    public void createGroups(PortalControllerContext portalControllerContext, WorkspaceCreationForm form, Document workspace) throws PortletException {
        // Owner
        Person owner;
        if (StringUtils.isEmpty(form.getOwner())) {
            String user = portalControllerContext.getHttpServletRequest().getRemoteUser();
            owner = this.personService.getPerson(user);
        } else {
            owner = this.personService.getPerson(form.getOwner());
        }

        // Workspace identifier
        String identifier = workspace.getString("webc:url");

        // LDAP groups creation
        this.workspaceService.create(identifier, owner);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePermissions(PortalControllerContext portalControllerContext, WorkspaceCreationForm form, Document workspace) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(this.portletContext);
        nuxeoController.setServletRequest(portalControllerContext.getHttpServletRequest());
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
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
    public boolean checkTitleAvailability(PortalControllerContext portalControllerContext, String modelWebId, String procedureInstanceUuid, String title,
            String titleVariableName) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CheckTitleAvailabilityCommand.class, modelWebId, procedureInstanceUuid, title,
                titleVariableName);
        Boolean available = (Boolean) nuxeoController.executeNuxeoCommand(command);

        return BooleanUtils.isTrue(available);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
