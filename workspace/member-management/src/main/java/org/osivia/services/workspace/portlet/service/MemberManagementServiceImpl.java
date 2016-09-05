package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.validation.Errors;

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

    /** Mail pattern. */
    private static final String MAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    /** Mail pattern. */
    private final Pattern mailPattern;


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

        // Mail pattern
        this.mailPattern = Pattern.compile(MAIL_PATTERN);
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
        
        if (!form.isLoaded()) {
            // Workspace identifier
            String workspaceId = this.repository.getWorkspaceId(portalControllerContext);

            // Members
            List<Member> members = this.repository.getMembers(portalControllerContext, workspaceId);
            form.setMembers(members);

            form.setLoaded(true);
        }
        
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
    public JSONArray searchPersons(PortalControllerContext portalControllerContext, String filter, boolean tokenizer) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Results JSON array
        JSONArray array = new JSONArray();

        if (StringUtils.isNotBlank(filter)) {
            String[] parts = StringUtils.split(filter, " ,;");
            for (String part : parts) {
                // Persons
                List<Person> persons = this.repository.searchPersons(portalControllerContext, part, tokenizer);

                // Member idenfiers
                Set<String> identifiers = this.getMembersForm(portalControllerContext).getIdentifiers();

                for (Person person : persons) {
                    JSONObject object = new JSONObject();
                    object.put("id", person.getUid());
                    object.put("mail", person.getMail());
                    object.put("avatar", person.getAvatar().getUrl());

                    if (identifiers.contains(person.getUid())) {
                        object.put("displayName", person.getDisplayName() + " " + bundle.getString("ALREADY_MEMBER_INDICATOR"));
                        object.put("disabled", true);
                    } else {
                        object.put("displayName", person.getDisplayName());
                    }

                    array.add(object);
                }

                // Add person creation
                if (!tokenizer || persons.isEmpty()) {
                    this.addPersonCreationSearchResult(persons, array, part, bundle);
                }
            }
        }

        return array;
    }


    /**
     * Add person creation search result.
     * 
     * @param filtered persons
     * @param array search result JSON array
     * @param filter search filter
     * @param bundle internationalization bundle
     * @throws PortletException
     */
    private void addPersonCreationSearchResult(List<Person> persons, JSONArray array, String filter, Bundle bundle) throws PortletException {
        // Mail pattern matcher
        Matcher matcher = this.mailPattern.matcher(filter);

        // Check if person mail already exists
        boolean exists = false;
        if (matcher.matches()) {
            for (Person person : persons) {
                if (filter.equalsIgnoreCase(person.getMail())) {
                    exists = true;
                    break;
                }
            }
        }

        if (!exists) {
            JSONObject object = new JSONObject();
            object.put("id", filter);
            object.put("create", true);

            if (matcher.matches()) {
                object.put("displayName", bundle.getString("INVITATIONS_CREATE_PERSON"));
                object.put("extra", bundle.getString("INVITATIONS_CREATE_PERSON_EXTRA", filter));
            } else {
                object.put("displayName", bundle.getString("INVITATIONS_CREATE_PERSON_INVALID"));
                object.put("extra", bundle.getString("INVITATIONS_CREATE_PERSON_INVALID_EXTRA", filter));
                object.put("disabled", true);
            }

            array.add(object);
        }
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
        List<Invitation> pending = new ArrayList<>(invitations.size());
        for (Invitation invitation : invitations) {
            if (InvitationState.SENT.equals(invitation.getState())) {
                pending.add(invitation);
            }
        }
        options.setInvitationsCount(pending.size());

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validateInvitationsCreationForm(Errors errors, InvitationsCreationForm form) {
        if (CollectionUtils.isNotEmpty(form.getInvitations())) {
            for (Invitation invitation : form.getInvitations()) {
                if (invitation.isUnknownUser()) {
                    // Mail pattern matcher
                    Matcher matcher = this.mailPattern.matcher(invitation.getId());

                    if (!matcher.matches()) {
                        Object[] errorArgs = new Object[]{invitation.getId()};
                        errors.rejectValue("invitations", "Invalid", errorArgs, "Invalid value");
                    }
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm invitationsForm,
            InvitationsCreationForm creationForm) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Identifiers to create
        List<String> identifiers = new ArrayList<>(creationForm.getInvitations().size());
        for (Invitation invitation : creationForm.getInvitations()) {
            if (invitation.isUnknownUser()) {
                identifiers.add(invitation.getId());
            }
        }

        if (identifiers.isEmpty() || creationForm.isWarning()) {
            // Create invitations
            boolean created = this.repository.createInvitations(portalControllerContext, options.getWorkspaceId(), invitationsForm.getInvitations(),
                    creationForm);

            if (created) {
                // Update model
                List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, options.getWorkspaceId());
                invitationsForm.setInvitations(invitations);
                options.setInvitationsCount(this.repository.getInvitationsCount(portalControllerContext, options.getWorkspaceId()));

                // Notification
                String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_CREATION_SUCCESS");
                this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
            }

            creationForm.getInvitations().clear();
            creationForm.setWarning(false);
        } else {
            creationForm.setWarning(true);
        }
    }

}
