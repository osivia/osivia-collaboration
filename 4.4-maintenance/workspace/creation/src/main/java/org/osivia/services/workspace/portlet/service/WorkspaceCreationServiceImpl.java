package org.osivia.services.workspace.portlet.service;

import java.util.Locale;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;
import org.osivia.services.workspace.portlet.repository.WorkspaceCreationRepository;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace creation service implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceCreationService
 * @see ApplicationContextAware
 */
@Service
public class WorkspaceCreationServiceImpl implements WorkspaceCreationService, ApplicationContextAware {

    /** Application context. */
    private ApplicationContext applicationContext;

    /** Workspace creation repository. */
    @Autowired
    private WorkspaceCreationRepository repository;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


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
    public WorkspaceCreationForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.applicationContext.getBean(WorkspaceCreationForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkTitleAvailability(PortalControllerContext portalControllerContext, String modelWebId, String procedureInstanceUuid, String title,
            String titleVariableName) throws PortletException {
        return this.repository.checkTitleAvailability(portalControllerContext, modelWebId, procedureInstanceUuid, title, titleVariableName);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(PortalControllerContext portalControllerContext, WorkspaceCreationForm form) throws PortletException {
        // Locale
        Locale locale = portalControllerContext.getHttpServletRequest().getLocale();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Set invitation only workspace type if empty
        if (form.getType() == null) {
            form.setType(WorkspaceType.INVITATION);
        }

        // Create workspace Nuxeo document
        Document workspace = this.repository.createDocument(portalControllerContext, form);
        // Create LDAP groups
        this.repository.createGroups(portalControllerContext, form, workspace);
        // Update permissions
        this.repository.updatePermissions(portalControllerContext, form, workspace);

        // Update form
        form.setTitle(null);
        form.setDescription(null);
        form.setType(null);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_CREATION_SUCCESS", workspace.getTitle());
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }

}
