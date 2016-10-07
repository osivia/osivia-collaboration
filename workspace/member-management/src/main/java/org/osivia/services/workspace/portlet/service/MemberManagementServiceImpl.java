package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
import org.osivia.services.workspace.portlet.model.InvitationRequest;
import org.osivia.services.workspace.portlet.model.InvitationRequestsForm;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.osivia.services.workspace.portlet.model.comparator.InvitationComparator;
import org.osivia.services.workspace.portlet.model.comparator.MemberObjectComparator;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Workspace member management service implementation.
 *
 * @author Cédric Krommenhoek
 * @see MemberManagementService
 * @see ApplicationContextAware
 */
@Service
public class MemberManagementServiceImpl implements MemberManagementService, ApplicationContextAware {

    /** Mail regex. */
    private static final String MAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    /** Application context. */
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


    /** Mail pattern. */
    private final Pattern mailPattern;


    /**
     * Constructor.
     */
    public MemberManagementServiceImpl() {
        super();

        // Mail pattern
        this.mailPattern = Pattern.compile(MAIL_REGEX);
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
            String workspaceId = this.repository.getCurrentWorkspaceId(portalControllerContext);
            options.setWorkspaceId(workspaceId);

            // Workspace type
            WorkspaceType type = this.repository.getCurrentWorkspaceType(portalControllerContext);
            options.setWorkspaceType(type);

            // Invitations count
            int invitationsCount = this.repository.getInvitationsCount(portalControllerContext, workspaceId);
            options.setInvitationsCount(invitationsCount);

            if (!WorkspaceType.INVITATION.equals(type)) {
                // Requests count
                int requestsCount = this.repository.getRequestsCount(portalControllerContext, workspaceId);
                options.setRequestsCount(requestsCount);
            }

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
            String workspaceId = this.repository.getCurrentWorkspaceId(portalControllerContext);

            // Members
            List<Member> members = this.repository.getMembers(portalControllerContext, workspaceId);
            form.setMembers(members);

            // Member identifiers
            Set<String> identifiers = new HashSet<String>();
            for (Member member : members) {
                identifiers.add(member.getId());
            }
            form.setIdentifiers(identifiers);

            form.setLoaded(true);
        }
        
        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sortMembers(PortalControllerContext portalControllerContext, MembersForm form, String sort, boolean alt) throws PortletException {
        MemberObjectComparator comparator = this.applicationContext.getBean(MemberObjectComparator.class, sort, alt);
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
        form.setLoaded(false);
        InvitationsForm invitationForm = this.applicationContext.getBean(InvitationsForm.class);
        invitationForm.setLoaded(false);
        InvitationRequestsForm requestsForm = this.applicationContext.getBean(InvitationRequestsForm.class);
        requestsForm.setLoaded(false);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_MEMBERS_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getMembersHelp(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getHelp(portalControllerContext, MEMBERS_HELP_LOCATION_PROPERTY);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public InvitationsForm getInvitationsForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        InvitationsForm form = this.applicationContext.getBean(InvitationsForm.class);

        if (!form.isLoaded()) {
            // Workspace identifier
            String workspaceId = this.repository.getCurrentWorkspaceId(portalControllerContext);

            // Member idenfiers
            Set<String> identifiers = this.getMembersForm(portalControllerContext).getIdentifiers();

            // Invitations
            List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, workspaceId, identifiers);
            form.setInvitations(invitations);

            form.setLoaded(true);
        }

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
    public JSONArray searchPersons(PortalControllerContext portalControllerContext, MemberManagementOptions options, String filter, boolean tokenizer)
            throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Invitation only indicator
        boolean invitationOnly = WorkspaceType.INVITATION.equals(options.getWorkspaceType());

        // Member idenfiers
        Set<String> memberIdentifiers = this.getMembersForm(portalControllerContext).getIdentifiers();
        // Requests identifiers
        Set<String> requestIdentifiers;
        if (invitationOnly) {
            requestIdentifiers = null;
        } else {
            requestIdentifiers = this.getInvitationRequestsForm(portalControllerContext).getIdentifiers();
        }

        // Results JSON array
        JSONArray array = new JSONArray();

        if (StringUtils.isNotBlank(filter)) {
            String[] parts = StringUtils.split(filter, ",;");
            for (String part : parts) {
                // Persons
                List<Person> persons = this.repository.searchPersons(portalControllerContext, part, tokenizer);
                for (Person person : persons) {
                    // Already member indicator
                    boolean alreadyMember = memberIdentifiers.contains(person.getUid());
                    // Existing request indicator
                    boolean existingRequest = !invitationOnly && requestIdentifiers.contains(person.getUid());

                    // Search result
                    JSONObject object = getSearchResult(person, alreadyMember, existingRequest, bundle);

                    array.add(object);
                }

                // Add person creation
                if (this.enablePersonCreation() && !(tokenizer && !persons.isEmpty())) {
                    this.addPersonCreationSearchResult(persons, array, part, bundle);
                }
            }
        }

        return array;
    }


    /**
     * Get search result JSON Object.
     * 
     * @param person person
     * @param alreadyMember already member indicator
     * @param existingRequest existing request indicator
     * @param bundle internationalization bundle
     * @return JSON object
     */
    protected JSONObject getSearchResult(Person person, boolean alreadyMember, boolean existingRequest, Bundle bundle) {
        JSONObject object = new JSONObject();
        object.put("id", person.getUid());

        // Display name
        String displayName;
        // Extra
        String extra;

        if (StringUtils.isEmpty(person.getDisplayName())) {
            displayName = person.getUid();
            extra = null;
        } else {
            displayName = person.getDisplayName();

            extra = person.getUid();
            if (StringUtils.isNotBlank(person.getMail()) && !StringUtils.equals(person.getUid(), person.getMail())) {
                extra += " – " + person.getMail();
            }
        }

        if (alreadyMember) {
            displayName += " " + bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ALREADY_MEMBER_INDICATOR");
            object.put("disabled", true);
        } else if (existingRequest) {
            displayName += " " + bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_EXISTING_INVITATION_REQUEST_INDICATOR");
            object.put("disabled", true);
        }

        object.put("displayName", displayName);
        object.put("extra", extra);

        object.put("avatar", person.getAvatar().getUrl());

        return object;
    }


    /**
     * Get enable person creation indicator (default = true).
     * This method can be overrided for disable person creation.
     * 
     * @return true if person creation is enabled
     */
    protected boolean enablePersonCreation() {
        return true;
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
    protected void addPersonCreationSearchResult(List<Person> persons, JSONArray array, String filter, Bundle bundle) throws PortletException {
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
                object.put("displayName", bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_CREATE_PERSON"));
                object.put("extra", bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_CREATE_PERSON_EXTRA", filter));
            } else {
                object.put("displayName", bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_CREATE_PERSON_INVALID"));
                object.put("extra", bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATIONS_CREATE_PERSON_INVALID_EXTRA", filter));
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

        // Workspace identifier
        String workspaceId = options.getWorkspaceId();

        // Update
        this.repository.updateInvitations(portalControllerContext, workspaceId, form.getInvitations());

        // Member idenfiers
        MembersForm membersForm = this.applicationContext.getBean(MembersForm.class);
        membersForm.setLoaded(false);
        Set<String> identifiers = this.getMembersForm(portalControllerContext).getIdentifiers();

        // Invitations count
        int count = this.repository.getInvitationsCount(portalControllerContext, workspaceId);

        // Update model
        options.setInvitationsCount(count);
        List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, workspaceId, identifiers);
        form.setInvitations(invitations);
        InvitationRequestsForm requestsForm = this.applicationContext.getBean(InvitationRequestsForm.class);
        requestsForm.setLoaded(false);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validateInvitationsCreationForm(Errors errors, InvitationsCreationForm form) {
        if (CollectionUtils.isNotEmpty(form.getPendingInvitations())) {
            for (Invitation invitation : form.getPendingInvitations()) {
                if (invitation.isUnknownUser()) {
                    // Mail pattern matcher
                    Matcher matcher = this.mailPattern.matcher(StringUtils.trim(invitation.getId()));

                    if (!matcher.matches()) {
                        Object[] errorArgs = new Object[]{invitation.getId()};
                        errors.rejectValue("pendingInvitations", "Invalid", errorArgs, null);
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

        // Pending invitation identifiers
        List<String> invitationIdentifiers = new ArrayList<String>(creationForm.getPendingInvitations().size());
        for (Invitation invitation : creationForm.getPendingInvitations()) {
            if (invitation.isUnknownUser()) {
                invitationIdentifiers.add(invitation.getId());
            }
        }

        if (invitationIdentifiers.isEmpty() || creationForm.isWarning()) {
            // Create invitations
            boolean created = this.repository.createInvitations(portalControllerContext, options.getWorkspaceId(), invitationsForm.getInvitations(),
                    creationForm);

            if (created) {
                // Member idenfiers
                Set<String> memberIdentifiers = this.getMembersForm(portalControllerContext).getIdentifiers();

                // Update model
                List<Invitation> invitations = this.repository.getInvitations(portalControllerContext, options.getWorkspaceId(), memberIdentifiers);
                invitationsForm.setInvitations(invitations);
                options.setInvitationsCount(this.repository.getInvitationsCount(portalControllerContext, options.getWorkspaceId()));

                // Notification
                String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_CREATION_SUCCESS");
                this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
            }

            creationForm.getPendingInvitations().clear();
            creationForm.setWarning(false);
        } else {
            creationForm.setWarning(true);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getInvitationsHelp(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getHelp(portalControllerContext, INVITATIONS_HELP_LOCATION_PROPERTY);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public InvitationRequestsForm getInvitationRequestsForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        InvitationRequestsForm form = this.applicationContext.getBean(InvitationRequestsForm.class);

        if (!form.isLoaded()) {
            // Workspace identifier
            String workspaceId = this.repository.getCurrentWorkspaceId(portalControllerContext);

            // Member idenfiers
            Set<String> memberIdentifiers = this.getMembersForm(portalControllerContext).getIdentifiers();

            // Invitation requests
            List<InvitationRequest> requests = this.repository.getInvitationRequests(portalControllerContext, workspaceId, memberIdentifiers);
            form.setRequests(requests);

            // Invitation requests identifiers
            Set<String> requestIdentifiers = new HashSet<String>();
            for (InvitationRequest request : requests) {
                if (InvitationState.SENT.equals(request.getState())) {
                    requestIdentifiers.add(request.getId());
                }
            }
            form.setIdentifiers(requestIdentifiers);

            form.setLoaded(true);
        }

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sortInvitationRequests(PortalControllerContext portalControllerContext, InvitationRequestsForm form, String sort, boolean alt)
            throws PortletException {
        MemberObjectComparator comparator = this.applicationContext.getBean(MemberObjectComparator.class, sort, alt);
        Collections.sort(form.getRequests(), comparator);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInvitationRequests(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationRequestsForm form)
            throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Workspace identifier
        String workspaceId = options.getWorkspaceId();

        // Update
        this.repository.updateInvitationRequests(portalControllerContext, workspaceId, form.getRequests());

        // Member idenfiers
        MembersForm membersForm = this.applicationContext.getBean(MembersForm.class);
        membersForm.setLoaded(false);
        Set<String> identifiers = this.getMembersForm(portalControllerContext).getIdentifiers();

        // Invitations count
        int count = this.repository.getInvitationsCount(portalControllerContext, workspaceId);

        // Update model
        options.setRequestsCount(count);
        List<InvitationRequest> requests = this.repository.getInvitationRequests(portalControllerContext, workspaceId, identifiers);
        form.setRequests(requests);
        form.setLoaded(false);
        InvitationsForm invitationsForm = this.applicationContext.getBean(InvitationsForm.class);
        invitationsForm.setLoaded(false);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATION_REQUESTS_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getInvitationRequestsHelp(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getHelp(portalControllerContext, INVITATION_REQUESTS_HELP_LOCATION_PROPERTY);
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
