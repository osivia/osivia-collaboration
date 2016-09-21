package org.osivia.services.workspace.portlet.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Name;
import javax.portlet.PortletException;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.Notifications;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationRequest;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Member management repository implementations.
 *
 * @author Cédric Krommenhoek
 * @see MemberManagementRepository
 * @see ApplicationContextAware
 */
@Repository
public class MemberManagementRepositoryImpl implements MemberManagementRepository, ApplicationContextAware {

    /** Application context. */
    private ApplicationContext applicationContext;

    /** Person service. */
    @Autowired
    private PersonService personService;

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;

    /** Forms service. */
    @Autowired
    private IFormsService formsService;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     *
     * @throws IOException
     */
    public MemberManagementRepositoryImpl() throws IOException {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace document
        Document workspace = this.getCurrentWorkspace(portalControllerContext);

        return workspace.getString("webc:url");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getInvitationsCount(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetInvitationsCommand.class, workspaceId, InvitationState.SENT);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        return documents.size();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getRequestsCount(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetInvitationsCommand.class, workspaceId, true, InvitationState.SENT);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        return documents.size();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkspaceRole> getRoles(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        List<WorkspaceRole> roles = new ArrayList<WorkspaceRole>();

        // Current workspace member
        String currentUser = portalControllerContext.getHttpServletRequest().getUserPrincipal().getName();
        WorkspaceMember currentMember = this.workspaceService.getMember(workspaceId, currentUser);
        WorkspaceRole currentRole = currentMember.getRole();

        // Profiles search criteria
        CollabProfile criteria = this.workspaceService.getEmptyProfile();
        criteria.setWorkspaceId(workspaceId);
        criteria.setType(WorkspaceGroupType.security_group);

        List<CollabProfile> profiles = this.workspaceService.findByCriteria(criteria);

        for (CollabProfile profile : profiles) {
            WorkspaceRole role = profile.getRole();
            if (currentRole.getWeight() >= role.getWeight()) {
                roles.add(role);
            }
        }

        return roles;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Member> getMembers(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Workspace members
        List<WorkspaceMember> workspaceMembers = this.workspaceService.getAllMembers(workspaceId);

        // Current workspace member
        String currentUser = portalControllerContext.getHttpServletRequest().getUserPrincipal().getName();
        WorkspaceMember currentMember = this.workspaceService.getMember(workspaceId, currentUser);
        WorkspaceRole currentRole = currentMember.getRole();

        // Member dates
        Map<String, Date> dates = getMemberDates(portalControllerContext, workspaceId, workspaceMembers);

        // Members
        List<Member> members = new ArrayList<>(workspaceMembers.size());
        for (WorkspaceMember workspaceMember : workspaceMembers) {
            // Date
            Date date = dates.get(workspaceMember.getMember().getUid());
            // Editable member indicator
            boolean editable = !StringUtils.equals(currentUser, workspaceMember.getMember().getUid())
                    && (currentRole.getWeight() >= workspaceMember.getRole().getWeight());

            // Member
            Member member = getMember(workspaceMember, date, editable);

            members.add(member);
        }

        return members;
    }


    /**
     * Get member dates.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param workspaceMembers workspace members
     * @return member dates
     * @throws PortletException
     */
    private Map<String, Date> getMemberDates(PortalControllerContext portalControllerContext, String workspaceId, List<WorkspaceMember> workspaceMembers)
            throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Member identifiers
        Set<String> identifiers = new HashSet<>(workspaceMembers.size());
        for (WorkspaceMember member : workspaceMembers) {
            identifiers.add(member.getMember().getUid());
        }

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetInvitationsCommand.class, workspaceId, InvitationState.ACCEPTED, identifiers);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Dates
        Map<String, Date> dates = new HashMap<>(workspaceMembers.size());
        for (Document document : documents) {
            // Variables
            PropertyMap variables = document.getProperties().getMap("pi:globalVariablesValues");

            // Member identifier
            String uid = variables.getString(PERSON_UID_PROPERTY);

            if (!dates.containsKey(uid)) {
                // Date
                Long date = variables.getLong(ACKNOWLEDGMENT_DATE_PROPERTY);

                if (date != null) {
                    dates.put(uid, new Date(date));
                }
            }
        }

        return dates;
    }


    /**
     * Get member.
     * 
     * @param workspaceMember workspace member
     * @param date member date
     * @param editable editable indicator
     * @return member
     */
    protected Member getMember(WorkspaceMember workspaceMember, Date date, boolean editable) {
        Member member = this.applicationContext.getBean(Member.class, workspaceMember);

        // Display name
        String displayName = StringUtils.defaultIfBlank(workspaceMember.getMember().getDisplayName(), workspaceMember.getMember().getUid());
        member.setDisplayName(displayName);

        // Extra
        String extra = workspaceMember.getMember().getMail();
        if (StringUtils.equals(extra, displayName)) {
            extra = null;
        }
        member.setExtra(extra);

        // Date
        member.setDate(date);

        // Role
        member.setRole(workspaceMember.getRole());

        // Editable indicator
        member.setEditable(editable);

        return member;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMember(PortalControllerContext portalControllerContext, String workspaceId, Member member) throws PortletException {
        // DN
        Name dn = member.getPerson().getDn();

        if (member.isDeleted()) {
            this.workspaceService.removeMember(workspaceId, dn);
        } else if (member.isEdited()) {
            this.workspaceService.addOrModifyMember(workspaceId, dn, member.getRole());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Invitation> getInvitations(PortalControllerContext portalControllerContext, String workspaceId, Set<String> memberIdentifiers)
            throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetInvitationsCommand.class, workspaceId);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Invitations
        List<Invitation> invitations = new ArrayList<>(documents.size());
        for (Document document : documents.list()) {
            // Variables
            PropertyMap variables = document.getProperties().getMap("pi:globalVariablesValues");

            // User identifier
            String uid = variables.getString(PERSON_UID_PROPERTY);

            if (StringUtils.isNotEmpty(uid) && !memberIdentifiers.contains(uid)) {
                // Invitation
                Invitation invitation = getInvitation(uid, document, variables);
                invitation.setDocument(document);

                invitations.add(invitation);
            }
        }

        return invitations;
    }


    /**
     * Get invitation.
     * 
     * @param uid user identifier
     * @param document invitation Nuxeo document
     * @param variables invitation variables
     * @return invitation
     */
    protected Invitation getInvitation(String uid, Document document, PropertyMap variables) {
        // Person
        Person person = this.personService.getPerson(uid);

        // Invitation
        Invitation invitation;
        if (person == null) {
            invitation = this.applicationContext.getBean(Invitation.class, uid);
            invitation.setDisplayName(uid);
        } else {
            invitation = this.applicationContext.getBean(Invitation.class, person);
            invitation.setDisplayName(StringUtils.defaultIfBlank(person.getDisplayName(), uid));
            if (!StringUtils.equals(person.getMail(), invitation.getDisplayName())) {
                invitation.setExtra(person.getMail());
            }
        }

        // Date
        Date date;
        Long dateProperty = variables.getLong(ACKNOWLEDGMENT_DATE_PROPERTY);
        if (dateProperty == null) {
            date = document.getDate("dc:created");
        } else {
            date = new Date(dateProperty);
        }
        invitation.setDate(date);

        // Role
        WorkspaceRole role = WorkspaceRole.fromId(variables.getString(ROLE_PROPERTY));
        if (role == null) {
            role = WorkspaceRole.READER;
        }
        invitation.setRole(role);

        // Invitation state
        InvitationState state = InvitationState.fromName(variables.getString(INVITATION_STATE_PROPERTY));
        invitation.setState(state);

        // Acknowledgment date
        Long acknowledgmentDateProperty = variables.getLong(ACKNOWLEDGMENT_DATE_PROPERTY);
        if (acknowledgmentDateProperty != null) {
            invitation.setAcknowledgmentDate(new Date(acknowledgmentDateProperty));
        }

        return invitation;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> searchPersons(PortalControllerContext portalControllerContext, String filter, boolean tokenizer) throws PortletException {
        // Criteria
        Person criteria = this.personService.getEmptyPerson();

        if (tokenizer) {
            criteria.setMail(filter);
        } else {
            String tokenizedFilter = filter + "*";

            criteria.setUid(tokenizedFilter);
            criteria.setDisplayName(tokenizedFilter);
            criteria.setSn(tokenizedFilter);
            criteria.setGivenName(tokenizedFilter);
            criteria.setMail(tokenizedFilter);
        }

        return this.personService.findByCriteria(criteria);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Person getPerson(PortalControllerContext portalControllerContext, String uid) throws PortletException {
        return this.personService.getPerson(uid);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInvitations(PortalControllerContext portalControllerContext, String workspaceId, List<Invitation> invitations) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateInvitationsCommand.class, invitations);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createInvitations(PortalControllerContext portalControllerContext, String workspaceId, List<Invitation> invitations,
            InvitationsCreationForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());


        // Workspace
        Document workspace = this.getCurrentWorkspace(portalControllerContext);


        // Workspace members
        List<Member> members = this.getMembers(portalControllerContext, workspaceId);


        // Existing invitations
        Map<String, Invitation> existingInvitations = new HashMap<>(invitations.size());
        for (Invitation invitation : invitations) {
            if (InvitationState.SENT.equals(invitation.getState())) {
                existingInvitations.put(invitation.getId(), invitation);
            }
        }

        // Member identifiers
        Set<String> memberIdentifiers = new HashSet<>(members.size());
        for (Member member : members) {
            memberIdentifiers.add(member.getId());
        }


        // Result
        boolean result = false;

        // Notifications
        Notifications errors = new Notifications(NotificationsType.ERROR);
        Notifications warnings = new Notifications(NotificationsType.WARNING);


        // Updated invitations
        List<Invitation> updatedInvitations = new ArrayList<>(form.getPendingInvitations().size());

        for (Invitation pendingInvitation : form.getPendingInvitations()) {
            // User identifier
            String uid = pendingInvitation.getId();

            if (existingInvitations.containsKey(uid)) {
                Invitation existingInvitation = existingInvitations.get(uid);
                if (form.getRole().getWeight() > existingInvitation.getRole().getWeight()) {
                    existingInvitation.setRole(form.getRole());
                    updatedInvitations.add(existingInvitation);
                }
            } else if (memberIdentifiers.contains(uid)) {
                // Warning notification
                String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_MEMBER_ALREADY_EXISTS", pendingInvitation.getDisplayName());
                warnings.addMessage(message);
            } else {
                try {
                    // Variables
                    Map<String, String> variables = new HashMap<>();
                    variables.put("documentId", workspace.getId());
                    variables.put("documentPath", workspace.getPath());
                    variables.put(WORKSPACE_IDENTIFIER_PROPERTY, workspaceId);
                    variables.put(WORKSPACE_TITLE_PROPERTY, workspace.getTitle());
                    variables.put(PERSON_UID_PROPERTY, uid);
                    variables.put(INVITATION_STATE_PROPERTY, InvitationState.SENT.name());
                    variables.put(ROLE_PROPERTY, form.getRole().getId());

                    if (pendingInvitation.isUnknownUser()) {
                        variables.put(NEW_USER_PROPERTY, String.valueOf(true));

                        // Generated password
                        String password = RandomStringUtils.randomAlphanumeric(8);
                        variables.put(GENERATED_PASSWORD_PROPERTY, password);

                        // User creation
                        this.createUser(portalControllerContext, uid, password);
                    }

                    // Start
                    this.formsService.start(portalControllerContext, INVITATION_MODEL_ID, variables);

                    // Update ACL
                    this.updateInvitationAcl(portalControllerContext, workspaceId, false, uid);

                    result = true;
                } catch (Exception e) {
                    // Error notification
                    String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_CREATION_ERROR", pendingInvitation.getDisplayName());
                    errors.addMessage(message);
                }
            }
        }

        if (!updatedInvitations.isEmpty()) {
            this.updateInvitations(portalControllerContext, workspaceId, updatedInvitations);
        }

        if (!errors.getMessages().isEmpty()) {
            this.notificationsService.addNotifications(portalControllerContext, errors);
        }
        if (!warnings.getMessages().isEmpty()) {
            this.notificationsService.addNotifications(portalControllerContext, warnings);
        }

        return result;
    }


    /**
     * Create user.
     *
     * @param portalControllerContext portal controller context
     * @param email user email
     * @param password generated password
     * @throws PortletException
     */
    protected void createUser(PortalControllerContext portalControllerContext, String email, String password) throws PortletException {
        // Person
        Person person = this.personService.getEmptyPerson();
        person.setUid(email);
        person.setCn(email);
        person.setSn(email);
        person.setMail(email);

        // Creation
        this.personService.create(person);

        // Generated password
        this.personService.updatePassword(person, password);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<InvitationRequest> getInvitationRequests(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetInvitationsCommand.class, workspaceId, true);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);


        // Invitation requests
        List<InvitationRequest> requests = new ArrayList<>(documents.size());
        for (Document document : documents.list()) {
            // Variables
            PropertyMap variables = document.getProperties().getMap("pi:globalVariablesValues");

            // User identifier
            String uid = variables.getString(PERSON_UID_PROPERTY);

            if (StringUtils.isNotEmpty(uid)) {
                // Invitation request
                InvitationRequest request = getInvitationRequest(uid, document, variables);
                request.setDocument(document);

                requests.add(request);
            }
        }

        return requests;
    }


    /**
     * Get invitation request.
     * 
     * @param uid user identifier
     * @param variables invitation request variables
     * @return invitation request
     */
    protected InvitationRequest getInvitationRequest(String uid, Document document, PropertyMap variables) {
        // Person
        Person person = this.personService.getPerson(uid);

        // Invitation request
        InvitationRequest request;
        if (person == null) {
            request = this.applicationContext.getBean(InvitationRequest.class, uid);
            request.setDisplayName(uid);
        } else {
            request = this.applicationContext.getBean(InvitationRequest.class, person);
            request.setDisplayName(StringUtils.defaultIfBlank(person.getDisplayName(), uid));
            if (!StringUtils.equals(person.getMail(), request.getDisplayName())) {
                request.setExtra(person.getMail());
            }
        }

        // Date
        Date date = document.getDate("dc:created");
        request.setDate(date);

        // Role
        WorkspaceRole role = WorkspaceRole.fromId(variables.getString(ROLE_PROPERTY));
        if (role == null) {
            role = WorkspaceRole.READER;
        }
        request.setRole(role);

        return request;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInvitationRequests(PortalControllerContext portalControllerContext, String workspaceId, List<InvitationRequest> invitationRequests)
            throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateInvitationRequestsCommand.class, portalControllerContext, invitationRequests);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getHelp(PortalControllerContext portalControllerContext, String property) throws PortletException {
        // Help location
        String location = null;
        if (StringUtils.isNotBlank(property)) {
            location = System.getProperty(property);
        }

        // Help content
        String help;

        if (StringUtils.isBlank(location)) {
            help = null;
        } else {
            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

            // Path
            String path;
            if (location.startsWith("/")) {
                path = location;
            } else {
                path = NuxeoController.webIdToFetchPath(location);
            }

            try {
                // Document context
                NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);

                // Document
                Document document = documentContext.getDoc();

                // Transformation
                help = nuxeoController.transformHTMLContent(StringUtils.trimToEmpty(document.getString("note:note")));
            } catch (NuxeoException e) {
                if (e.getErrorCode() == NuxeoException.ERROR_NOTFOUND) {
                    help = null;
                } else {
                    throw e;
                }
            }
        }

        return help;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptInvitation(PortalControllerContext portalControllerContext, Map<String, String> variables) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Variables
        String workspaceId = variables.get(WORKSPACE_IDENTIFIER_PROPERTY);
        String uid = variables.get(PERSON_UID_PROPERTY);
        WorkspaceRole role = WorkspaceRole.fromId(variables.get(ROLE_PROPERTY));

        // Add member to workspace
        Name memberDn = this.personService.getEmptyPerson().buildDn(uid);
        this.workspaceService.addOrModifyMember(workspaceId, memberDn, role);

        // Reload Nuxeo session
        INuxeoCommand command = this.applicationContext.getBean(ReloadNuxeoSessionCommand.class);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPendingInvitation(PortalControllerContext portalControllerContext, String workspaceId, String uid, boolean request)
            throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);

        // Identifiers
        Set<String> identifier = new HashSet<>(1);
        identifier.add(uid);

        // Nuxeo command
        INuxeoCommand command = applicationContext.getBean(GetInvitationsCommand.class, workspaceId, request, identifier);

        // Nuxeo documents
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Pending invitation
        boolean pending;

        if (documents.isEmpty()) {
            pending = false;
        } else {
            // Nuxeo document
            Document document = documents.get(0);

            // Variables
            PropertyMap variables = document.getProperties().getMap("pi:globalVariablesValues");

            // Invitation state
            InvitationState state = InvitationState.fromName(variables.getString(MemberManagementRepository.INVITATION_STATE_PROPERTY));

            pending = InvitationState.SENT.equals(state);
        }

        return pending;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createInvitationRequest(PortalControllerContext portalControllerContext, String workspaceId, String uid) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        try {
            // Workspace Nuxeo document
            Document workspace = this.getWorkspace(portalControllerContext, workspaceId);

            // Variables
            Map<String, String> variables = new HashMap<>();
            variables.put("documentId", workspace.getId());
            variables.put("documentPath", workspace.getPath());
            variables.put(WORKSPACE_IDENTIFIER_PROPERTY, workspaceId);
            variables.put(WORKSPACE_TITLE_PROPERTY, workspace.getTitle());
            variables.put(PERSON_UID_PROPERTY, uid);
            variables.put(INVITATION_STATE_PROPERTY, InvitationState.SENT.name());

            this.formsService.start(portalControllerContext, REQUEST_MODEL_ID, variables);

            // Update ACL
            this.updateInvitationAcl(portalControllerContext, workspaceId, true, uid);
        } catch (Exception e) {
            // Error notification
            String message = bundle.getString("MESSAGE_WORKSPACE_REQUEST_CREATION_ERROR");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);
        }
    }


    /**
     * Get current workspace Nuxeo document.
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo document
     */
    private Document getCurrentWorkspace(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Base path
        String basePath = nuxeoController.getBasePath();

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(basePath);

        return documentContext.getDoc();
    }


    /**
     * Get workspace Nuxeo document.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @return Nuxeo document
     */
    private Document getWorkspace(PortalControllerContext portalControllerContext, String workspaceId) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetWorkspaceCommand.class, workspaceId);

        // Nuxeo documents
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Workspace Nuxeo document
        Document workspace;
        if (documents.size() == 1) {
            workspace = documents.get(0);
        } else {
            workspace = null;
        }

        return workspace;
    }


    /**
     * Update invitation ACL.
     * 
     * @param portalControllerContext portal controller context
     * @param workspaceId workspace identifier
     * @param request request indicator
     * @param uid user identifier
     */
    private void updateInvitationAcl(PortalControllerContext portalControllerContext, String workspaceId, boolean request, String uid) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Workspace admin & owner groups
        List<CollabProfile> groups = new ArrayList<>();
        CollabProfile criteria = this.workspaceService.getEmptyProfile();
        criteria.setWorkspaceId(workspaceId);
        criteria.setRole(WorkspaceRole.ADMIN);
        groups.addAll(this.workspaceService.findByCriteria(criteria));
        criteria.setRole(WorkspaceRole.OWNER);
        groups.addAll(this.workspaceService.findByCriteria(criteria));

        // Update ACL
        INuxeoCommand command = this.applicationContext.getBean(SetProcedureInstanceAcl.class, workspaceId, request, uid, groups);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
