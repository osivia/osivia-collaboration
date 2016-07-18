package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

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
        INuxeoCommand command = this.applicationContext.getBean(GetInvitationsCommand.class, modelPath, workspaceId);
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

        List<CollabProfile> profiles = this.workspaceService.findByWorkspaceId(workspaceId);
        for (CollabProfile profile : profiles) {
            if (WorkspaceGroupType.security_group.equals(profile.getType())) {
                roles.add(profile.getRole());
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

        // Members
        List<Member> members = new ArrayList<>(workspaceMembers.size());
        for (WorkspaceMember workspaceMember : workspaceMembers) {
            Member member = this.applicationContext.getBean(Member.class, workspaceMember);
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

                // Invitations state
                InvitationState state = InvitationState.fromName(variables.getString(INVITATION_STATE_PROPERTY));
                invitation.setState(state);

                // Role
                WorkspaceRole role = WorkspaceRole.fromId(variables.getString(ROLE_PROPERTY));
                if (role == null) {
                    role = WorkspaceRole.READER;
                }
                invitation.setRole(role);

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
    public void updateInvitations(PortalControllerContext portalControllerContext, String workspaceId, List<Invitation> invitations)
            throws PortletException {
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
    public void createInvitations(PortalControllerContext portalControllerContext, String workspaceId, List<Invitation> invitations,
            InvitationsCreationForm form) throws PortletException {
        // Workspace document
        Document workspace = this.getWorkspaceDocument(portalControllerContext);


        // Existing invitations
        Map<String, Invitation> existingInvitations = new HashMap<>(invitations.size());
        for (Invitation invitation : invitations) {
            existingInvitations.put(invitation.getId(), invitation);
        }

        // Updated invitations
        List<Invitation> updatedInvitations = new ArrayList<>(form.getIdentifiers().size());

        for (String identifier : form.getIdentifiers()) {
            if (existingInvitations.containsKey(identifier)) {
                Invitation existingInvitation = existingInvitations.get(identifier);
                if (form.getRole().getWeight() > existingInvitation.getRole().getWeight()) {
                    existingInvitation.setRole(form.getRole());
                    updatedInvitations.add(existingInvitation);
                }
            } else {
                // Variables
                Map<String, String> variables = new HashMap<>();
                variables.put(WORKSPACE_IDENTIFIER_PROPERTY, workspaceId);
                variables.put(WORKSPACE_TITLE_PROPERTY, workspace.getTitle());
                variables.put(PERSON_UID_PROPERTY, identifier);
                variables.put(INVITATION_STATE_PROPERTY, InvitationState.SENT.name());
                variables.put(ROLE_PROPERTY, form.getRole().getId());

                try {
                    this.formsService.start(portalControllerContext, MODEL_ID, variables);
                } catch (PortalException e) {
                    throw new PortletException(e);
                }
            }
        }

        if (!updatedInvitations.isEmpty()) {
            this.updateInvitations(portalControllerContext, workspaceId, updatedInvitations);
        }
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
     */
    private String getInvitationModelPath(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Fetch path
        String fetchPath = NuxeoController.webIdToFetchPath(IFormsService.FORMS_WEB_ID_PREFIX + "invitation");

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(fetchPath);

        // Model Nuxeo document
        Document model = documentContext.getDoc();

        return model.getPath();
    }

}
