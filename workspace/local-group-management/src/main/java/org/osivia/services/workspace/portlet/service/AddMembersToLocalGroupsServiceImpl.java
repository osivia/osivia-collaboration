package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.AbstractLocalGroup;
import org.osivia.services.workspace.portlet.model.AddMembersToLocalGroupsForm;
import org.osivia.services.workspace.portlet.model.LocalGroupMember;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummary;
import org.osivia.services.workspace.portlet.repository.LocalGroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Add members to local groups portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroupsServiceImpl
 * @see AddMembersToLocalGroupsService
 */
@Service("addMembers")
public class AddMembersToLocalGroupsServiceImpl extends LocalGroupsServiceImpl implements AddMembersToLocalGroupsService {

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
    public AddMembersToLocalGroupsServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AddMembersToLocalGroupsForm getForm(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException {
        // Local groups summary
        LocalGroupsSummary summary = this.getSummary(portalControllerContext);

        // Selection
        List<AbstractLocalGroup> selection = this.getSelection(portalControllerContext, summary, identifiers);

        // Form
        AddMembersToLocalGroupsForm form = this.applicationContext.getBean(AddMembersToLocalGroupsForm.class);
        form.setGroups(selection);
        
        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMembers(PortalControllerContext portalControllerContext, AddMembersToLocalGroupsForm form) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        if (CollectionUtils.isNotEmpty(form.getMembers()) && CollectionUtils.isNotEmpty(form.getGroups())) {
            // Member identifiers
            List<String> members = new ArrayList<>(form.getMembers().size());
            for (LocalGroupMember member : form.getMembers()) {
                members.add(member.getUid());
            }

            // Group identifiers
            List<String> groups = new ArrayList<>(form.getGroups().size());
            for (AbstractLocalGroup group : form.getGroups()) {
                groups.add(group.getId());
            }

            this.repository.addMembersToLocalGroups(portalControllerContext, members, groups);

            // Force summary update
            LocalGroupsSummary summary = this.getSummary(portalControllerContext);
            summary.setLoaded(false);

            // Notification
            String message = bundle.getString("MESSAGE_SUCCESS_ADD_MEMBERS_TO_LOCAL_GROUPS");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        }
    }

}
