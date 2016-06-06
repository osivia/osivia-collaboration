package org.osivia.services.workspace.repository.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.SortedSet;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.nuxeo.ecm.automation.client.model.Document;
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
import org.osivia.services.workspace.repository.WorkspaceCreationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Workspace creation repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceCreationRepository
 */
@Repository
public class WorkspaceCreationRepositoryImpl implements WorkspaceCreationRepository {

    /** Properties file name. */
    private static final String PROPERTIES_FILE_NAME = "workspace-creation.properties";

    /** Workspace parent path property. */
    private static final String PARENT_PATH_PROPERTY = "workspace.parentPath";


    /** Properties. */
    private final Properties properties;


    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;

    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


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
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
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
        INuxeoCommand command = new WorkspaceCreationCommand(form, parentPath, items, bundle);
        return (Document) nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createGroups(PortalControllerContext portalControllerContext, WorkspaceCreationForm form, Document workspace) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Person
        Person person = (Person) request.getAttribute(Constants.ATTR_LOGGED_PERSON_2);

        // Workspace identifier
        String identifier = workspace.getString("webc:url");

        // LDAP groups creation
        this.workspaceService.create(identifier, form.getDescription(), person);
    }

}
