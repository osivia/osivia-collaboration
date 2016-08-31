package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
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
import org.osivia.services.workspace.portlet.model.InvitationState;
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
    protected ApplicationContext applicationContext;

    /** Member management repository. */
    @Autowired
    protected MemberManagementRepository repository;


    @Autowired
    protected MembersForm membersFormInSession;
    
    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    protected INotificationsService notificationsService;


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

        if (options.getWorkspaceId() == null) {
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
        }

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

        // Pending & history
        List<Invitation> pending = new ArrayList<>(invitations.size());
        List<Invitation> history = new ArrayList<>(invitations.size());
        for (Invitation invitation : invitations) {
            if (InvitationState.SENT.equals(invitation.getState())) {
                pending.add(invitation);
            } else {
                history.add(invitation);
            }
        }
        form.setPending(pending);
        form.setHistory(history);

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
            
            List<String> alreadyMembers = new ArrayList<String>();
            for(Member m : membersFormInSession.getMembers()) {
            	alreadyMembers.add(m.getPerson().getUid());
            }

            for (Person person : persons) {
            	if (!alreadyMembers.contains(person.getUid())) {
	                toJson(array, person);
            	}
            }
        }

        return array;
    }


    /**
     * Format a person in json array
     */
	protected void toJson(JSONArray array, Person person) {
		JSONObject object = new JSONObject();
		object.put("id", person.getUid());
		object.put("displayName", person.getDisplayName());
		object.put("extra", person.getMail());
		object.put("avatar", person.getAvatar().getUrl());

		array.add(object);
	}


    /**
     * {@inheritDoc}
     */
    @Override
    public void sortInvitations(PortalControllerContext portalControllerContext, InvitationsForm form, String sort, boolean alt, String sort2, boolean alt2)
            throws PortletException {
        InvitationComparator comparator = this.applicationContext.getBean(InvitationComparator.class, sort, alt);
        Collections.sort(form.getPending(), comparator);

        InvitationComparator comparator2 = this.applicationContext.getBean(InvitationComparator.class, sort2, alt2);
        Collections.sort(form.getHistory(), comparator2);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePendingInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
            throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        this.repository.updateInvitations(portalControllerContext, options.getWorkspaceId(), form.getPending(), true);

        // Update model
        List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, options.getWorkspaceId());
        List<Invitation> pending = new ArrayList<>(invitations.size());
        for (Invitation invitation : invitations) {
            if (InvitationState.SENT.equals(invitation.getState())) {
                pending.add(invitation);
            }
        }
        options.setInvitationsCount(pending.size());
        form.setPending(pending);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateHistoryInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
            throws PortletException {
        this.repository.updateInvitations(portalControllerContext, options.getWorkspaceId(), form.getHistory(), false);

        // Update model
        List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, options.getWorkspaceId());
        List<Invitation> history = new ArrayList<>(invitations.size());
        for (Invitation invitation : invitations) {
            if (!InvitationState.SENT.equals(invitation.getState())) {
                history.add(invitation);
            }
        }
        form.setHistory(history);
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
        boolean created = this.repository.createInvitations(portalControllerContext, options.getWorkspaceId(), invitationsForm.getPending(), creationForm);

        if (created) {
            // Update model
            List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, options.getWorkspaceId());
            List<Invitation> pending = new ArrayList<>(invitations.size());
            List<Invitation> history = new ArrayList<>(invitations.size());
            for (Invitation invitation : invitations) {
                if (InvitationState.SENT.equals(invitation.getState())) {
                    pending.add(invitation);
                } else {
                    history.add(invitation);
                }
            }
            options.setInvitationsCount(pending.size());
            invitationsForm.setPending(pending);
            invitationsForm.setHistory(history);

            // Notification
            String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_CREATION_SUCCESS");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        }
    }

}
