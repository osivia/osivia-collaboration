package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.workspace.portlet.model.AbstractLocalGroup;
import org.osivia.services.workspace.portlet.model.LocalGroupMember;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummary;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummaryItem;
import org.osivia.services.workspace.portlet.repository.LocalGroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Local groups portlet service abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroupsService
 */
public abstract class LocalGroupsServiceImpl implements LocalGroupsService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private LocalGroupsRepository repository;


    /**
     * Constructor.
     */
    public LocalGroupsServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalGroupMember> getAllMembers(PortalControllerContext portalControllerContext) throws PortletException {
        // Workspace members
        List<WorkspaceMember> workspaceMembers = this.repository.getWorkspaceMembers(portalControllerContext);

        // Members
        List<LocalGroupMember> members;
        if (CollectionUtils.isEmpty(workspaceMembers)) {
            members = null;
        } else {
            members = new ArrayList<>(workspaceMembers.size());

            for (WorkspaceMember workspaceMember : workspaceMembers) {
                // Person
                Person person = workspaceMember.getMember();

                // Member
                LocalGroupMember member = this.applicationContext.getBean(LocalGroupMember.class);
                member.setUid(person.getUid());
                member.setDisplayName(StringUtils.defaultIfBlank(person.getDisplayName(), person.getUid()));
                if (person.getAvatar() != null) {
                    member.setAvatar(person.getAvatar().getUrl());
                }
                member.setExtra(person.getMail());
                if (StringUtils.equals(member.getExtra(), member.getDisplayName())) {
                    member.setExtra(null);
                }

                members.add(member);
            }
        }

        return members;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroupMember convertPersonToMember(Person person) {
        LocalGroupMember member = this.applicationContext.getBean(LocalGroupMember.class);

        // Identifier
        String uid = person.getUid();
        member.setUid(uid);

        // Display name
        String displayName = StringUtils.defaultIfBlank(person.getDisplayName(), uid);
        member.setDisplayName(displayName);

        // Avatar URL
        String avatar;
        if (person.getAvatar() == null) {
            avatar = null;
        } else {
            avatar = person.getAvatar().getUrl();
        }
        member.setAvatar(avatar);

        // Extra informations;
        String extra = person.getMail();
        if (StringUtils.equals(extra, displayName)) {
            extra = null;
        }
        member.setExtra(extra);

        return member;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroupsSummary getSummary(PortalControllerContext portalControllerContext) throws PortletException {
        // Summary
        LocalGroupsSummary summary = this.applicationContext.getBean(LocalGroupsSummary.class);

        if (!summary.isLoaded()) {
            // Local groups
            List<LocalGroupsSummaryItem> groups = this.repository.getLocalGroupsSummaryItems(portalControllerContext);
            summary.setGroups(groups);

            summary.setLoaded(true);
        }

        return summary;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractLocalGroup> getSelection(PortalControllerContext portalControllerContext, LocalGroupsSummary summary, String[] identifiers)
            throws PortletException {
        // Sorted local groups
        Map<String, AbstractLocalGroup> sortedGroups;
        if (CollectionUtils.isEmpty(summary.getGroups())) {
            sortedGroups = null;
        } else {
            sortedGroups = new HashMap<>(summary.getGroups().size());
            for (AbstractLocalGroup group : summary.getGroups()) {
                sortedGroups.put(group.getId(), group);
            }
        }

        // Selection
        List<AbstractLocalGroup> selection;
        if (ArrayUtils.isEmpty(identifiers) || MapUtils.isEmpty(sortedGroups)) {
            selection = null;
        } else {
            selection = new ArrayList<>(identifiers.length);
            for (String identifier : identifiers) {
                AbstractLocalGroup group = sortedGroups.get(identifier);
                if (group != null) {
                    selection.add(group);
                }
            }
        }

        return selection;
    }

}
