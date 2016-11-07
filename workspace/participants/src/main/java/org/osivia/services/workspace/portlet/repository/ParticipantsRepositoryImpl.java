package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.Name;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSPublicationInfos;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.workspace.portlet.model.Configuration;
import org.osivia.services.workspace.portlet.model.Group;
import org.osivia.services.workspace.portlet.model.Member;
import org.osivia.services.workspace.portlet.model.View;
import org.osivia.services.workspace.portlet.model.comparator.PersonComparator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoServiceFactory;
import fr.toutatice.portail.cms.nuxeo.api.services.tag.INuxeoTagService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Participants portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see ParticipantsRepository
 * @see ApplicationContextAware
 */
@Repository
public class ParticipantsRepositoryImpl implements ParticipantsRepository, ApplicationContextAware {

    /** Person comparator. */
    @Autowired
    private PersonComparator personComparator;

    /** CMS service locator. */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;

    /** Person service. */
    @Autowired
    private PersonService personService;

    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;


    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public ParticipantsRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // View
        View view = View.get(window.getProperty(VIEW_WINDOW_PROPERTY));

        // Display
        WorkspaceRole[] roles = WorkspaceRole.values();
        Map<WorkspaceRole, Boolean> display = new LinkedHashMap<>(roles.length);
        for (WorkspaceRole role : roles) {
            Boolean value = BooleanUtils.toBooleanObject(window.getProperty(DISPLAY_WINDOW_PROPERTY_PREFIX + role.getId()));
            if (value == null) {
                value = (role.getWeight() >= WorkspaceRole.ADMIN.getWeight());
            }
            display.put(role, value);
        }

        // Max
        int max = NumberUtils.toInt(window.getProperty(MAX_WINDOW_PROPERTY), MAX_DEFAULT_VALUE);

        // Configuration
        Configuration configuration = this.applicationContext.getBean(Configuration.class);
        configuration.setView(view);
        configuration.setDisplay(display);
        configuration.setMax(max);

        return configuration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // View
        View view = configuration.getView();
        window.setProperty(VIEW_WINDOW_PROPERTY, view.toString());

        // Display
        for (Entry<WorkspaceRole, Boolean> entry : configuration.getDisplay().entrySet()) {
            WorkspaceRole role = entry.getKey();
            boolean value = BooleanUtils.toBoolean(entry.getValue());
            window.setProperty(DISPLAY_WINDOW_PROPERTY_PREFIX + role.getId(), String.valueOf(value));
        }

        // Max
        int max = configuration.getMax();
        window.setProperty(MAX_WINDOW_PROPERTY, String.valueOf(max));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Group> getGroups(PortalControllerContext portalControllerContext) throws PortletException {
        // Space document
        Document space = this.getSpace(portalControllerContext);

        // Participant groups
        List<Group> groups;

        if (space != null) {
            // Room indicator
            boolean isRoom = "Room".equals(space.getType());

            if (isRoom) {
                groups = this.getRoomGroups(portalControllerContext, space);
            } else {
                groups = this.getWorkspaceGroups(portalControllerContext, space);
            }
        } else {
            groups = new ArrayList<>(0);
        }

        return groups;
    }



    /**
     * Get space document.
     * 
     * @param portalControllerContext portal controller context
     * @return Nuxeo document
     * @throws PortletException
     */
    private Document getSpace(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS service context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Base path
        String basePath = nuxeoController.getBasePath();

        // Space document
        Document space;
        if (StringUtils.isEmpty(basePath)) {
            space = null;
        } else {
            try {
                // Space config
                CMSItem spaceConfig = cmsService.getSpaceConfig(cmsContext, basePath);

                space = (Document) spaceConfig.getNativeItem();
            } catch (CMSException e) {
                throw new PortletException(e);
            }
        }

        return space;
    }


    /**
     * Get workspace identifier.
     * 
     * @param portalControllerContext portal controller context
     * @param space space document
     * @return identifier
     * @throws PortletException
     */
    private String getWorkspaceId(PortalControllerContext portalControllerContext, Document space) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS service context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Workspace Nuxeo document
        Document workspace = null;

        try {
            // Publication infos
            CMSPublicationInfos publicationInfos = cmsService.getPublicationInfos(cmsContext, space.getPath());
            // Parent path
            String parentPath = publicationInfos.getParentSpaceID();

            while ((workspace == null) && StringUtils.isNotBlank(parentPath)) {
                // Space config
                CMSItem spaceConfig = cmsService.getSpaceConfig(cmsContext, parentPath);
                // Document type
                DocumentType documentType = spaceConfig.getType();

                if ((documentType != null) && ("Workspace".equals(documentType.getName()))) {
                    workspace = (Document) spaceConfig.getNativeItem();
                } else {
                    // Loop on parent path
                    parentPath = publicationInfos.getParentSpaceID();
                }
            }
        } catch (CMSException e) {
            throw new PortletException(e);
        }

        // Workspace identifier
        String workspaceId;
        if (workspace == null) {
            workspaceId = null;
        } else {
            workspaceId = workspace.getString("webc:url");
        }

        return workspaceId;
    }


    /**
     * Get workspace groups.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace document
     * @return groups
     * @throws PortletException
     */
    private List<Group> getWorkspaceGroups(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);

        // Workspace identifier
        String workspaceId = workspace.getString("webc:url");

        // Joined dates
        Map<String, Date> joinedDates = this.getJoinedDates(portalControllerContext, workspace);

        // Workspace security profiles
        CollabProfile profileCriteria = this.workspaceService.getEmptyProfile();
        profileCriteria.setWorkspaceId(workspaceId);
        profileCriteria.setType(WorkspaceGroupType.security_group);
        List<CollabProfile> profiles = this.workspaceService.findByCriteria(profileCriteria);

        // Participant groups
        List<Group> groups = new ArrayList<>(profiles.size());

        // Max
        int max;
        if (View.FULL.equals(configuration.getView())) {
            max = Integer.MAX_VALUE;
        } else {
            max = configuration.getMax();
        }

        for (CollabProfile profile : profiles) {
            // Workspace role
            WorkspaceRole role = profile.getRole();

            if (View.FULL.equals(configuration.getView()) || configuration.getDisplay().get(role)) {
                // Group
                Group group = this.applicationContext.getBean(Group.class);
                group.setRole(role);

                // Group members search
                Person criteria = this.personService.getEmptyPerson();
                criteria.setProfiles(Arrays.asList(new Name[]{profile.getDn()}));
                List<Person> persons = this.personService.findByCriteria(criteria);

                if (persons.size() > max) {
                    Collections.sort(persons, this.personComparator);
                }

                // Group members
                List<Member> members = new ArrayList<>(Math.min(persons.size(), max));

                for (int i = 0; i < Math.min(persons.size(), max); i++) {
                    // Person
                    Person person = persons.get(i);

                    // Joined date
                    Date joinedDate = joinedDates.get(person.getUid());

                    Member member = this.getMember(portalControllerContext, person);
                    member.setJoinedDate(joinedDate);
                    members.add(member);
                }

                group.setMembers(members);

                // More
                int more = Math.max(persons.size() - max, 0);
                group.setMore(more);

                groups.add(group);
            }
        }

        return groups;
    }


    /**
     * Get room groups.
     * 
     * @param portalControllerContext portal controller context
     * @param room room document
     * @return groups
     * @throws PortletException
     */
    private List<Group> getRoomGroups(PortalControllerContext portalControllerContext, Document room) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetPermissionsCommand.class, room);
        JSONObject result = (JSONObject) nuxeoController.executeNuxeoCommand(command);

        // Workspace identifier
        String workspaceId = this.getWorkspaceId(portalControllerContext, room);

        // Participant groups
        List<Group> groups;

        if (result == null) {
            groups = new ArrayList<>(0);
        } else {
            // JSON array
            JSONArray array = result.getJSONArray("local");
            if (array.isEmpty()) {
                array = result.getJSONArray("inherited");
            }


            // Workspace profiles
            List<CollabProfile> profiles = this.workspaceService.findByWorkspaceId(workspaceId);
            Map<String, CollabProfile> profileMap = new HashMap<>(profiles.size());
            for (CollabProfile profile : profiles) {
                profileMap.put(profile.getCn(), profile);
            }

            // Workspace members
            List<WorkspaceMember> members = this.workspaceService.getAllMembers(workspaceId);
            Map<String, WorkspaceMember> memberMap = new HashMap<>(members.size());
            for (WorkspaceMember member : members) {
                Person person = member.getMember();
                memberMap.put(person.getUid(), member);
            }


            // Permissions
            Map<String, Permission> permissions = new HashMap<>(array.size());
            for (int i = 0; i < array.size(); i++) {
                JSONObject object = array.getJSONObject(i);

                if (object.getBoolean("isGranted")) {
                    // Name
                    String name = object.getString("username");
                    // Group indicator
                    boolean group = object.getBoolean("isGroup");

                    // Permission
                    Permission permission = permissions.get(name);
                    if (permission == null) {
                        permission = new Permission();
                        permission.setName(name);
                        permission.setValues(new ArrayList<String>());
                        permission.setGroup(group);
                        permissions.put(name, permission);
                    }
                    permission.getValues().add(object.getString("permission"));
                }
            }

            // Users
            Map<String, WorkspaceRole> users = new HashMap<>(permissions.size());
            for (Permission permission : permissions.values()) {
                // Permission values
                String[] values = permission.getValues().toArray(new String[permission.getValues().size()]);
                // Workspace role
                WorkspaceRole workspaceRole = WorkspaceRole.fromPermissions(values);
                if (workspaceRole == null) {
                    // Unknown workspace role
                    continue;
                }

                if (permission.isGroup()) {
                    CollabProfile profile = profileMap.get(permission.getName());
                    if (profile != null) {
                        List<Name> names = profile.getUniqueMember();
                        for (Name name : names) {
                            String uid = StringUtils.substringAfter(name.get(name.size() - 1), "=");
                            WorkspaceRole role = users.get(uid);
                            if ((role == null) || role.getWeight() < workspaceRole.getWeight()) {
                                users.put(uid, workspaceRole);
                            }
                        }
                    }
                } else {
                    WorkspaceRole role = users.get(permission.getName());
                    if ((role == null) || role.getWeight() < workspaceRole.getWeight()) {
                        users.put(permission.getName(), workspaceRole);
                    }
                }
            }

            // Workspace roles
            Map<WorkspaceRole, Group> roles = new HashMap<>(WorkspaceRole.values().length);
            for (Entry<String, WorkspaceRole> user : users.entrySet()) {
                WorkspaceRole role = user.getValue();
                Group group = roles.get(role);
                if (group == null) {
                    group = this.applicationContext.getBean(Group.class);
                    group.setRole(role);
                    group.setMembers(new ArrayList<Member>());
                    roles.put(role, group);
                }

                WorkspaceMember workspaceMember = memberMap.get(user.getKey());
                if (workspaceMember != null) {
                    Person person = workspaceMember.getMember();
                    Member member = this.getMember(portalControllerContext, person);
                    group.getMembers().add(member);
                }
            }

            groups = new ArrayList<>(roles.values());
        }

        return groups;
    }


    /**
     * Get participant group member.
     * 
     * @param portalControllerContext portal controller context
     * @param person person
     * @return member
     * @throws PortletException
     */
    private Member getMember(PortalControllerContext portalControllerContext, Person person) throws PortletException {
        // Tag service
        INuxeoTagService tagService = NuxeoServiceFactory.getTagService();

        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Identifier
        String id = person.getUid();
        // Display name
        String displayName = person.getDisplayName();
        if (StringUtils.isBlank(displayName)) {
            displayName = id;
        }
        // URL
        String url = tagService.getUserProfileLink(nuxeoController, id, displayName).getUrl();
        // Avatar URL
        String avatarUrl = person.getAvatar().getUrl();
        // Email
        String email = person.getMail();

        // Member
        Member member = this.applicationContext.getBean(Member.class);
        member.setId(id);
        member.setUrl(url);
        member.setAvatarUrl(avatarUrl);
        member.setDisplayName(displayName);
        member.setEmail(email);

        return member;
    }


    /**
     * Get member joined dates.
     * 
     * @param portalControllerContext portal controller context
     * @param workspace workspace document
     * @return member dates
     * @throws PortletException
     */
    private Map<String, Date> getJoinedDates(PortalControllerContext portalControllerContext, Document workspace) throws PortletException {
        // Members
        PropertyList members = workspace.getProperties().getList("ttcs:spaceMembers");

        // Dates
        Map<String, Date> dates = new HashMap<>(members.size());

        for (int i = 0; i < members.size(); i++) {
            PropertyMap member = members.getMap(i);
            String user = member.getString("login");
            Date date = member.getDate("joinedDate");

            dates.put(user, date);
        }

        return dates;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
