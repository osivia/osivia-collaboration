package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
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
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
 */
@Repository
public class MemberManagementRepositoryImpl implements MemberManagementRepository {

    /** Application context. */
    @Autowired
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
     */
    public MemberManagementRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace document
        Document workspace = this.getWorkspaceDocument(portalControllerContext);

        return workspace.getString("webc:url");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getInvitationsCount(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Model path
        String modelPath = this.getInvitationModelPath(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetInvitationsCommand.class, modelPath, workspaceId, InvitationState.SENT);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        return documents.size();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getRequestsCount(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // TODO Auto-generated method stub
        return 0;
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

        // Members
        List<Member> members = new ArrayList<>(workspaceMembers.size());
        for (WorkspaceMember workspaceMember : workspaceMembers) {
            Member member = this.applicationContext.getBean(Member.class, workspaceMember);

            // Editable member indicator
            boolean editable = !StringUtils.equals(currentUser, member.getId()) && (currentRole.getWeight() >= workspaceMember.getRole().getWeight());
            member.setEditable(editable);

            members.add(member);
        }

        return members;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMember(PortalControllerContext portalControllerContext, String workspaceId, Member member) throws PortletException {
        if (member.isDeleted()) {
            this.workspaceService.removeMember(workspaceId, member.getDn());
        } else {
            this.workspaceService.addOrModifyMember(workspaceId, member.getDn(), member.getRole());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Invitation> getInvitations(PortalControllerContext portalControllerContext, String workspaceId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Model path
        String modelPath = this.getInvitationModelPath(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetInvitationsCommand.class, modelPath, workspaceId);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Invitations
        List<Invitation> invitations = new ArrayList<>(documents.size());
        for (Document document : documents.list()) {
            // Variables
            PropertyMap variables = document.getProperties().getMap("pi:globalVariablesValues");

            // UID
            String uid = variables.getString(PERSON_UID_PROPERTY);

            if (StringUtils.isNotEmpty(uid)) {
                // Person
                Person person = this.personService.getPerson(uid);

                // Invitation
                Invitation invitation = this.applicationContext.getBean(Invitation.class, person);
                invitation.setDocument(document);

                // Date
                Date date = document.getDate("dc:modified");
                invitation.setDate(date);

                // Role
                WorkspaceRole role = WorkspaceRole.fromId(variables.getString(ROLE_PROPERTY));
                if (role == null) {
                    role = WorkspaceRole.READER;
                }
                invitation.setRole(role);

                // Invitations state
                InvitationState state = InvitationState.fromName(variables.getString(INVITATION_STATE_PROPERTY));
                invitation.setState(state);

                invitations.add(invitation);
            }
        }

        return invitations;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> searchPersons(PortalControllerContext portalControllerContext, String filter) throws PortletException {
        String tokenizedFilter = filter + "*";

        // Criteria
        Person search = this.personService.getEmptyPerson();
        search.setUid(tokenizedFilter);
        search.setDisplayName(tokenizedFilter);
        search.setSn(tokenizedFilter);
        search.setGivenName(tokenizedFilter);

        return this.personService.findByCriteria(search);
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
    public void updateInvitations(PortalControllerContext portalControllerContext, String workspaceId, List<Invitation> invitations, boolean pending)
            throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateInvitationsCommand.class, invitations, pending);
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
        Document workspace = this.getWorkspaceDocument(portalControllerContext);
        
        // Model path
        String modelPath = this.getInvitationModelPath(portalControllerContext);


        // Workspace members
        List<Member> members = this.getMembers(portalControllerContext, workspaceId);

        // Workspace admin & owner groups
        List<CollabProfile> groups = new ArrayList<>();
        CollabProfile criteria = this.workspaceService.getEmptyProfile();
        criteria.setWorkspaceId(workspaceId);
        criteria.setRole(WorkspaceRole.ADMIN);
        groups.addAll(this.workspaceService.findByCriteria(criteria));
        criteria.setRole(WorkspaceRole.OWNER);
        groups.addAll(this.workspaceService.findByCriteria(criteria));


        // Existing invitations
        Map<String, Invitation> existingInvitations = new HashMap<>(invitations.size());
        for (Invitation invitation : invitations) {
            existingInvitations.put(invitation.getId(), invitation);
        }

        // Existing members
        Map<String, Member> existingMembers = new HashMap<>(members.size());
        for (Member member : members) {
            existingMembers.put(member.getId(), member);
        }


        // Result
        boolean result = false;

        // Notifications
        Notifications notifications = new Notifications(NotificationsType.WARNING);

        // Updated invitations
        List<Invitation> updatedInvitations = new ArrayList<>(form.getIdentifiers().size());

        for (String uid : form.getIdentifiers()) {
            if (existingInvitations.containsKey(uid)) {
                Invitation existingInvitation = existingInvitations.get(uid);
                if (form.getRole().getWeight() > existingInvitation.getRole().getWeight()) {
                    existingInvitation.setRole(form.getRole());
                    updatedInvitations.add(existingInvitation);
                }
            } else if (existingMembers.containsKey(uid)) {
                Member existingMember = existingMembers.get(uid);

                // Notification
                String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_MEMBER_ALREADY_EXISTS", existingMember.getDisplayName());
                notifications.addMessage(message);
            } else {
                // Variables
                Map<String, String> variables = new HashMap<>();
                variables.put("documentId", workspace.getId());
                variables.put("documentPath", workspace.getPath());
                variables.put(WORKSPACE_IDENTIFIER_PROPERTY, workspaceId);
                variables.put(WORKSPACE_TITLE_PROPERTY, workspace.getTitle());
                variables.put(PERSON_UID_PROPERTY, uid);
                variables.put(INVITATION_STATE_PROPERTY, InvitationState.SENT.name());
                variables.put(ROLE_PROPERTY, form.getRole().getId());

                // Start
                try {
                    this.formsService.start(portalControllerContext, MODEL_ID, variables);
                } catch (PortalException e) {
                    throw new PortletException(e);
                }

                // Update ACL
                INuxeoCommand command = this.applicationContext.getBean(SetProcedureInstanceAcl.class, modelPath, workspaceId, uid, groups);
                nuxeoController.executeNuxeoCommand(command);

                result = true;
            }
        }

        if (!notifications.getMessages().isEmpty()) {
            this.notificationsService.addNotifications(portalControllerContext, notifications);
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptInvitation(PortalControllerContext portalControllerContext, Map<String, String> variables) {
        // Variables
        String workspaceId = variables.get(WORKSPACE_IDENTIFIER_PROPERTY);
        String uid = variables.get(PERSON_UID_PROPERTY);
        WorkspaceRole role = WorkspaceRole.fromId(variables.get(ROLE_PROPERTY));

        // Member
        Name memberDn = this.personService.getEmptyPerson().buildDn(uid);

        this.workspaceService.addOrModifyMember(workspaceId, memberDn, role);
    }


    /**
     * Get workspace document.
     *
     * @param portalControllerContext portal controller context
     * @return document
     */
    private Document getWorkspaceDocument(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Base path
        String basePath = nuxeoController.getBasePath();

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(basePath);

        return documentContext.getDoc();
    }


    /**
     * Get invitation model path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    private String getInvitationModelPath(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Fetch path
        String fetchPath = NuxeoController.webIdToFetchPath(IFormsService.FORMS_WEB_ID_PREFIX + MODEL_ID);

        // Nuxeo document context
        NuxeoDocumentContext documentContext;
        try {
            documentContext = nuxeoController.getDocumentContext(fetchPath);
        } catch (NuxeoException e) {
            if (NuxeoException.ERROR_NOTFOUND == e.getErrorCode()) {
                Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
                String message = bundle.getString("MESSAGE_WORKSPACE_CANNOT_FIND_MODEL_ERROR", MODEL_ID);
                throw new PortletException(message);
            } else {
                throw e;
            }
        }

        // Model Nuxeo document
        Document model = documentContext.getDoc();

        return model.getPath();
    }

}
