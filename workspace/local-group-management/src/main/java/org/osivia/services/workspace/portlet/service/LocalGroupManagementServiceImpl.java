package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.LocalGroup;
import org.osivia.services.workspace.portlet.model.LocalGroupEditionForm;
import org.osivia.services.workspace.portlet.model.LocalGroupListItem;
import org.osivia.services.workspace.portlet.model.LocalGroups;
import org.osivia.services.workspace.portlet.model.Member;
import org.osivia.services.workspace.portlet.model.comparator.LocalGroupsComparator;
import org.osivia.services.workspace.portlet.repository.LocalGroupManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Workspace local group management service implementation.
 *
 * @author Cédric Krommenhoek
 * @see LocalGroupManagementService
 */
@Service
public class LocalGroupManagementServiceImpl implements LocalGroupManagementService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Local group management repository. */
    @Autowired
    private LocalGroupManagementRepository repository;

    /** Local groups comparator. */
    @Autowired
    private LocalGroupsComparator localGroupsComparator;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public LocalGroupManagementServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroups getLocalGroups(PortalControllerContext portalControllerContext) throws PortletException {
        // Local groups container
        LocalGroups container = applicationContext.getBean(LocalGroups.class);

        // Workspace identifier
        String workspaceId = this.repository.getWorkspaceId(portalControllerContext);
        container.setWorkspaceId(workspaceId);

        // Local groups
        List<LocalGroup> localGroups = this.repository.getLocalGroups(portalControllerContext, workspaceId);
        Collections.sort(localGroups, this.localGroupsComparator);
        container.setGroups(localGroups);

        return container;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroup getAddLocalGroupForm(PortalControllerContext portalControllerContext) throws PortletException {
        return applicationContext.getBean(LocalGroupListItem.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroupEditionForm getLocalGroupEditionForm(PortalControllerContext portalControllerContext, String id) throws PortletException {
        return this.repository.getLocalGroupEditionForm(portalControllerContext, id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveLocalGroups(PortalControllerContext portalControllerContext, LocalGroups localGroups) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        this.repository.setLocalGroups(portalControllerContext, localGroups);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_LOCAL_GROUPS_SAVE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createLocalGroup(PortalControllerContext portalControllerContext, LocalGroups localGroups, LocalGroup form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        LocalGroup localGroup = this.repository.createLocalGroup(portalControllerContext, localGroups, form);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_LOCAL_GROUP_ADD_SUCCESS", form.getDisplayName());
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);

        // Update model
        localGroups.getGroups().add(localGroup);
        form.setDisplayName(null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Member> getMembers(PortalControllerContext portalControllerContext, LocalGroupEditionForm form) throws PortletException {
        return this.repository.getAllMembers(portalControllerContext, form.getWorkspaceId());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMembersToLocalGroup(PortalControllerContext portalControllerContext, LocalGroupEditionForm form) throws PortletException {
        // Local group members
        List<Member> members = form.getMembers();
        if (members == null) {
            members = new ArrayList<Member>();
            form.setMembers(members);
        }

        // Added member
        Member addedMember = form.getAddedMember();
        addedMember.setAdded(true);
        int index = members.indexOf(addedMember);
        if (index != -1) {
            Member member = members.get(index);
            member.setDeleted(false);
        } else {
            members.add(addedMember);
        }

        // Update model
        form.setAddedMember(null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveLocalGroup(PortalControllerContext portalControllerContext, LocalGroupEditionForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        this.repository.setLocalGroup(portalControllerContext, form);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_LOCAL_GROUP_SAVE_SUCCESS", form.getDisplayName());
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLocalGroup(PortalControllerContext portalControllerContext, LocalGroupEditionForm form) throws PortletException {
        this.repository.deleteLocalGroup(portalControllerContext, form.getWorkspaceId(), form.getId());
    }

}
