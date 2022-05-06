package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.Name;
import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.services.workspace.portlet.model.LocalGroupForm;
import org.osivia.services.workspace.portlet.model.LocalGroupMember;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummaryItem;
import org.osivia.services.workspace.portlet.repository.command.UpdateRemovedLocalGroupLinkedInvitationsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Local groups portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see LocalGroupsRepository
 */
@Repository
public class LocalGroupsRepositoryImpl implements LocalGroupsRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;

    /** Person service. */
    @Autowired
    private PersonService personService;


    /**
     * Constructor.
     */
    public LocalGroupsRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceId(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Base path
        String basePath = nuxeoController.getBasePath();

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(basePath);

        // Nuxeo document
        Document document = documentContext.getDocument();

        return document.getString("webc:url");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalGroupsSummaryItem> getLocalGroupsSummaryItems(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace identifier
        String workspaceId = this.getWorkspaceId(portalControllerContext);

        // Criteria
        CollabProfile criteria = this.workspaceService.getEmptyProfile();
        criteria.setWorkspaceId(workspaceId);
        criteria.setType(WorkspaceGroupType.local_group);

        // Search
        List<CollabProfile> groups = this.workspaceService.findByCriteria(criteria);

        // Local groups
        List<LocalGroupsSummaryItem> localGroups;
        if (CollectionUtils.isEmpty(groups)) {
            localGroups = null;
        } else {
            localGroups = new ArrayList<LocalGroupsSummaryItem>(groups.size());
            for (CollabProfile group : groups) {
                LocalGroupsSummaryItem localGroup = this.applicationContext.getBean(LocalGroupsSummaryItem.class);
                localGroup.setId(group.getCn());
                localGroup.setDisplayName(group.getDisplayName());
                localGroup.setDescription(group.getDescription());

                // Members count
                List<?> names = group.getUniqueMember();
                localGroup.setMembersCount(names.size());

                localGroups.add(localGroup);
            }
        }

        return localGroups;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkspaceMember> getWorkspaceMembers(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace identifier
        String workspaceId = this.getWorkspaceId(portalControllerContext);

        return this.workspaceService.getAllMembers(workspaceId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CollabProfile getLocalGroup(PortalControllerContext portalControllerContext, String id) throws PortletException {
        return this.workspaceService.getProfile(id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Person> getLocalGroupMembers(PortalControllerContext portalControllerContext, String id) throws PortletException {
        // Local group DN
        Name groupDn = this.workspaceService.getEmptyProfile().buildDn(id);

        // Search criteria
        Person criteria = this.personService.getEmptyPerson();
        criteria.setProfiles(Arrays.asList(new Name[]{groupDn}));
        return this.personService.findByCriteria(criteria);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void editLocalGroup(PortalControllerContext portalControllerContext, LocalGroupForm form) throws PortletException {
        // Workspace identifier
        String workspaceId = this.getWorkspaceId(portalControllerContext);

        // Local group DN
        Name groupDn = this.workspaceService.getEmptyProfile().buildDn(form.getId());

        // Update local group properties
        CollabProfile group = this.workspaceService.getProfile(groupDn);
        group.setDisplayName(form.getDisplayName());
        group.setDescription(StringUtils.trimToNull(form.getDescription()));

        this.workspaceService.modifyLocalGroup(group);

        // Members
        List<Person> persons = this.getLocalGroupMembers(portalControllerContext, form.getId());
        List<String> removedIdentifiers;
        if (CollectionUtils.isEmpty(persons)) {
            removedIdentifiers = new ArrayList<>(0);
        } else {
            removedIdentifiers = new ArrayList<>(persons.size());
            for (Person person : persons) {
                removedIdentifiers.add(person.getUid());
            }
        }
        List<String> addedIdentifiers;
        if (CollectionUtils.isEmpty(form.getMembers())) {
            addedIdentifiers = new ArrayList<>(0);
        } else {
            addedIdentifiers = new ArrayList<>(form.getMembers().size());
            for (LocalGroupMember member : form.getMembers()) {
                // Member identifier
                String uid = member.getUid();

                if (removedIdentifiers.contains(uid)) {
                    removedIdentifiers.remove(uid);
                } else {
                    addedIdentifiers.add(uid);
                }
            }
        }
        for (String uid : removedIdentifiers) {
            // Member DN
            Name memberDn = this.personService.getEmptyPerson().buildDn(uid);

            this.workspaceService.removeMemberFromLocalGroup(workspaceId, groupDn, memberDn);
        }
        for (String uid : addedIdentifiers) {
            // Member DN
            Name memberDn = this.personService.getEmptyPerson().buildDn(uid);

            this.workspaceService.addMemberToLocalGroup(workspaceId, groupDn, memberDn);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createLocalGroup(PortalControllerContext portalControllerContext, LocalGroupForm form) throws PortletException {
        // Workspace identifier
        String workspaceId = this.getWorkspaceId(portalControllerContext);

        // Local group
        CollabProfile group = this.workspaceService.createLocalGroup(workspaceId, form.getDisplayName(), form.getDescription());

        if (CollectionUtils.isNotEmpty(form.getMembers())) {
            // Local group DN
            Name groupDn = group.getDn();

            for (LocalGroupMember member : form.getMembers()) {
                // Member DN
                Name memberDn = this.personService.getEmptyPerson().buildDn(member.getUid());

                this.workspaceService.addMemberToLocalGroup(workspaceId, groupDn, memberDn);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteLocalGroups(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        nuxeoController.setAsynchronousCommand(true);

        // Workspace identifier
        String workspaceId = this.getWorkspaceId(portalControllerContext);

        for (String identifier : identifiers) {
            this.workspaceService.removeLocalGroup(workspaceId, identifier);
            
            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(UpdateRemovedLocalGroupLinkedInvitationsCommand.class, workspaceId, identifier);
            nuxeoController.executeNuxeoCommand(command);
        }


    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMembersToLocalGroups(PortalControllerContext portalControllerContext, List<String> members, List<String> groups) throws PortletException {
        // Workspace identifier
        String workspaceId = this.getWorkspaceId(portalControllerContext);

        for (String groupCn : groups) {
            // Local group DN
            Name groupDn = this.workspaceService.getEmptyProfile().buildDn(groupCn);

            for (String memberUid : members) {
                // Member DN
                Name memberDn = this.personService.getEmptyPerson().buildDn(memberUid);

                this.workspaceService.addMemberToLocalGroup(workspaceId, groupDn, memberDn);
            }
        }
    }

}
