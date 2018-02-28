package org.osivia.services.workspace.task.creation.portlet.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.workspace.task.creation.portlet.model.TaskCreationForm;
import org.osivia.services.workspace.task.creation.portlet.model.comparator.AlphaOrderComparator;
import org.osivia.services.workspace.task.creation.portlet.repository.WorkspaceTaskCreationRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Workspace task creation service implementation.
 *
 * @author Cédric Krommenhoek
 * @see WorkspaceTaskCreationService
 * @see ApplicationContextAware
 */
@Service
public class WorkspaceTaskCreationServiceImpl implements WorkspaceTaskCreationService, ApplicationContextAware {

    /** Workspace task creation repository. */
    @Autowired
    private WorkspaceTaskCreationRepository repository;

    /** Alpha order comparator. */
    @Autowired
    private AlphaOrderComparator alphaOrderComparator;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public WorkspaceTaskCreationServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TaskCreationForm getTaskCreationForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.applicationContext.getBean(TaskCreationForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Task document types
        List<DocumentType> documentTypes = this.repository.getTaskTypes(portalControllerContext);

        // Types
        SortedMap<String, DocumentType> types = new TreeMap<>(this.alphaOrderComparator);
        for (DocumentType type : documentTypes) {
            String displayName = bundle.getString(StringUtils.upperCase(type.getName()), type.getCustomizedClassLoader());

            types.put(displayName, type);
        }

        return types;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, TaskCreationForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Task creation
        this.repository.create(portalControllerContext, form);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_TASK_CREATION_SUCCESS", form.getTitle());
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceUrl(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace path
        String path = this.repository.getWorkspacePath(portalControllerContext);

        return this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
