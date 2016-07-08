package org.osivia.services.workspace.portlet.service;

import java.util.Collections;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.osivia.services.workspace.portlet.model.comparator.InvitationComparator;
import org.osivia.services.workspace.portlet.model.comparator.MemberComparator;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Workspace member management service implementation.
 *
 * @author Cédric Krommenhoek
 * @see MemberManagementService
 */
@Service
public class MemberManagementServiceImpl implements MemberManagementService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Member management repository. */
    @Autowired
    private MemberManagementRepository repository;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public MemberManagementServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MemberManagementOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException {
        // Options
        MemberManagementOptions options = this.applicationContext.getBean(MemberManagementOptions.class);

        // Workspace identifier
        String workspaceId = this.repository.getWorkspaceId(portalControllerContext);
        options.setWorkspaceId(workspaceId);

        // Invitations count
        int invitationsCount = this.repository.getInvitationsCount(portalControllerContext, workspaceId);
        options.setInvitationsCount(invitationsCount);

        // Requests count
        int requestsCount = this.repository.getRequestsCount(portalControllerContext, workspaceId);
        options.setRequestsCount(requestsCount);

        // Roles
        List<WorkspaceRole> roles = this.repository.getRoles(portalControllerContext, workspaceId);
        options.setRoles(roles);

        return options;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MembersForm getMembersForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        MembersForm form = this.applicationContext.getBean(MembersForm.class);
        
        // Workspace identifier
        String workspaceId = this.repository.getWorkspaceId(portalControllerContext);

        // Members
        List<Member> members = this.repository.getMembers(portalControllerContext, workspaceId);
        form.setMembers(members);
        
        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sortMembers(PortalControllerContext portalControllerContext, MembersForm form, String sort, boolean alt) throws PortletException {
        MemberComparator comparator = this.applicationContext.getBean(MemberComparator.class, sort, alt);
        Collections.sort(form.getMembers(), comparator);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMembers(PortalControllerContext portalControllerContext, MemberManagementOptions options, MembersForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        for (Member member : form.getMembers()) {
            this.repository.updateMember(portalControllerContext, options.getWorkspaceId(), member);
        }

        // Update model
        List<Member> members = this.repository.getMembers(portalControllerContext, options.getWorkspaceId());
        form.setMembers(members);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_MEMBERS_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public InvitationsForm getInvitationsForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        InvitationsForm form = this.applicationContext.getBean(InvitationsForm.class);

        // Workspace identifier
        String workspaceId = this.repository.getWorkspaceId(portalControllerContext);

        // Invitations
        List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, workspaceId);
        form.setInvitations(invitations);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public InvitationsCreationForm getInvitationsCreationForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Invitations creation form
        InvitationsCreationForm form = this.applicationContext.getBean(InvitationsCreationForm.class);
        form.setRole(WorkspaceRole.READER);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray searchPersons(PortalControllerContext portalControllerContext, String filter) throws PortletException {
        // Results JSON array
        JSONArray array = new JSONArray();

        if (StringUtils.isNotBlank(filter)) {
            // Persons
            List<Person> persons = this.repository.searchPersons(portalControllerContext, filter);

            for (Person person : persons) {
                JSONObject object = new JSONObject();
                object.put("id", person.getUid());
                object.put("displayName", person.getDisplayName());
                object.put("mail", person.getMail());
                object.put("avatar", person.getAvatar().getUrl());

                array.add(object);
            }
        }

        return array;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sortInvitations(PortalControllerContext portalControllerContext, InvitationsForm form, String sort, boolean alt) throws PortletException {
        InvitationComparator comparator = this.applicationContext.getBean(InvitationComparator.class, sort, alt);
        Collections.sort(form.getInvitations(), comparator);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
            throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        this.repository.updateInvitations(portalControllerContext, options.getWorkspaceId(), form.getInvitations());

        // Update model
        List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, options.getWorkspaceId());
        options.setInvitationsCount(invitations.size());
        form.setInvitations(invitations);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm invitationsForm,
            InvitationsCreationForm creationForm) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Create invitations
        this.repository.createInvitations(portalControllerContext, options.getWorkspaceId(), invitationsForm.getInvitations(), creationForm);

        // Update model
        List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, options.getWorkspaceId());
        options.setInvitationsCount(invitations.size());
        invitationsForm.setInvitations(invitations);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_CREATION_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }

}
