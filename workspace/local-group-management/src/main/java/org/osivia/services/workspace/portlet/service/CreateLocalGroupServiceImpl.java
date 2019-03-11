package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.LocalGroupForm;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummary;
import org.osivia.services.workspace.portlet.repository.LocalGroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Create local group portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroupsServiceImpl
 * @see CreateLocalGroupService
 */
@Service("create")
public class CreateLocalGroupServiceImpl extends LocalGroupsServiceImpl implements CreateLocalGroupService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private LocalGroupsRepository repository;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public CreateLocalGroupServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroupForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.applicationContext.getBean(LocalGroupForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void create(PortalControllerContext portalControllerContext, LocalGroupForm form) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        this.repository.createLocalGroup(portalControllerContext, form);

        // Force summary update
        LocalGroupsSummary summary = this.getSummary(portalControllerContext);
        summary.setLoaded(false);

        // Notification
        String message = bundle.getString("MESSAGE_SUCCESS_CREATE_LOCAL_GROUP", form.getDisplayName());
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }

}
