package org.osivia.services.workspace.portlet.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.html.AccessibilityRoles;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.AbstractMembersForm;
import org.osivia.services.workspace.portlet.model.AddToGroupForm;
import org.osivia.services.workspace.portlet.model.ChangeRoleForm;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationEditionForm;
import org.osivia.services.workspace.portlet.model.InvitationRequest;
import org.osivia.services.workspace.portlet.model.InvitationRequestsForm;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.osivia.services.workspace.portlet.model.MembersSort;
import org.osivia.services.workspace.portlet.model.comparator.InvitationComparator;
import org.osivia.services.workspace.portlet.model.comparator.LocalGroupComparator;
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

    /** Local group comparator. */
    @Autowired
    private LocalGroupComparator localGroupComparator;


    /** Mail pattern. */
    private final Pattern mailPattern;

    /** Log. */
    private final Log log;


    /**
     * Constructor.
     */
    public MemberManagementServiceImpl() {
        super();

        // Mail pattern
        this.mailPattern = Pattern.compile(MAIL_REGEX);

        // Log
        this.log = LogFactory.getLog(this.getClass());
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

            // Local groups
            List<CollabProfile> localGroups = this.repository.getLocalGroups(portalControllerContext, workspaceId);
            Collections.sort(localGroups, this.localGroupComparator);
            options.setWorkspaceLocalGroups(localGroups);
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
            Set<String> identifiers = new HashSet<>();
            for (Member member : members) {
                identifiers.add(member.getId());
            }
            form.setIdentifiers(identifiers);

            // Sort
            this.sortMembers(portalControllerContext, form, MembersSort.DATE, true);

            form.setLoaded(true);
        }

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sortMembers(PortalControllerContext portalControllerContext, AbstractMembersForm form, MembersSort sort, boolean alt) throws PortletException {
        if (CollectionUtils.isNotEmpty(form.getMembers())) {
            // Comparator
            MemberObjectComparator comparator = this.applicationContext.getBean(MemberObjectComparator.class, sort, alt);

            try {
                Collections.sort(form.getMembers(), comparator);
            } catch (IllegalArgumentException e) {
                // #1718 - catch errors during sort
                this.log.error("Impossible de trier les membres ", e);
            }

            // Update model
            form.setSort(sort);
            form.setAlt(alt);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeMembers(PortalControllerContext portalControllerContext, MemberManagementOptions options, MembersForm form, String[] identifiers)
            throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Workspace identifier
        String workspaceId = options.getWorkspaceId();

        // Sorted identifiers
        Set<String> sortedIdentifiers = new HashSet<>(Arrays.asList(identifiers));

        // Removed members
        List<Member> removedMembers = new ArrayList<>(sortedIdentifiers.size());

        for (Member member : form.getMembers()) {
            if (sortedIdentifiers.contains(member.getId()) && member.isEditable()) {
                member.setDeleted(true);

                this.repository.updateMember(portalControllerContext, workspaceId, member);

                removedMembers.add(member);
            }
        }

        // Update model
        form.getMembers().removeAll(removedMembers);
        this.invalidateLoadedForms();

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_REMOVE_MEMBERS_SUCCESS");
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
    public Element getMembersToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Toolbar container
        Element container = DOM4JUtils.generateDivElement(null);

        // Toolbar
        Element toolbar = DOM4JUtils.generateDivElement("btn-toolbar", AccessibilityRoles.TOOLBAR);
        container.add(toolbar);

        if (CollectionUtils.isNotEmpty(indexes)) {
            // Form
            MembersForm form = this.getMembersForm(portalControllerContext);

            // Members
            List<Member> members = form.getMembers();

            // Selected members
            List<Member> selectedMembers = new ArrayList<>(indexes.size());
            for (String index : indexes) {
                int i = NumberUtils.toInt(index, -1);
                if ((i > -1) && (i < members.size())) {
                    Member member = members.get(i);
                    selectedMembers.add(member);
                }
            }

            if (indexes.size() == selectedMembers.size()) {
                // All editable indicator
                boolean allEditable = true;
                Iterator<Member> iterator = selectedMembers.iterator();
                while (allEditable && iterator.hasNext()) {
                    // Selected member
                    Member member = iterator.next();

                    allEditable &= member.isEditable();
                }
                
                // Selected member identifiers
                String[] identifiers = new String[selectedMembers.size()];
                for (int i = 0; i < selectedMembers.size(); i++) {
                    Member member = selectedMembers.get(i);
                    identifiers[i] = member.getId();
                }


                // Change role
                Element changeRole = this.getChangeRoleToolbarButton(portalControllerContext, identifiers, allEditable, bundle);
                toolbar.add(changeRole);

                // Add to group
                Element addToGroup = this.getAddToGroupToolbarButton(portalControllerContext, identifiers, bundle);
                toolbar.add(addToGroup);

                // Remove member
                Element removeMember = this.getRemoveMemberToolbarButton(portalControllerContext, allEditable, bundle);
                toolbar.add(removeMember);
                
                if (allEditable) {
                    Element removeMemberConfirmationModal = this.getRemoveMemberConfirmationModal(portalControllerContext, selectedMembers, identifiers,
                            bundle);
                    container.add(removeMemberConfirmationModal);
                }
            }
        }

        return container;
    }


    /**
     * Get change role toolbar button DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selected member identifiers
     * @param allEditable all editable indicator
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getChangeRoleToolbarButton(PortalControllerContext portalControllerContext, String[] identifiers, boolean allEditable,
            Bundle bundle) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();
        // MIME response
        MimeResponse mimeResponse;
        if (portletResponse instanceof MimeResponse) {
            mimeResponse = (MimeResponse) portletResponse;
        } else {
            mimeResponse = null;
        }

        // HTML classes
        String htmlClass = "btn btn-default btn-sm";
        // Text
        String text = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_CHANGE_ROLE");
        // Icon
        String icon = "glyphicons glyphicons-shield";
        // URL
        String url;
        if (allEditable && (mimeResponse != null)) {
            // Render URL
            PortletURL renderUrl = mimeResponse.createRenderURL();
            renderUrl.setParameter("tab", "members");
            renderUrl.setParameter("view", "change-role");
            renderUrl.setParameter("identifiers", identifiers);

            url = renderUrl.toString();
        } else {
            url = null;
        }

        if (StringUtils.isEmpty(url)) {
            url = "#";
            htmlClass += " disabled";
        }

        return DOM4JUtils.generateLinkElement(url, null, null, htmlClass, text, icon);
    }


    /**
     * Get add to group toolbar button DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selected member identifiers
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getAddToGroupToolbarButton(PortalControllerContext portalControllerContext, String[] identifiers, Bundle bundle) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();
        // MIME response
        MimeResponse mimeResponse;
        if (portletResponse instanceof MimeResponse) {
            mimeResponse = (MimeResponse) portletResponse;
        } else {
            mimeResponse = null;
        }

        // HTML classes
        String htmlClass = "btn btn-default btn-sm";
        // Text
        String text = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ADD_TO_GROUP");
        // Icon
        String icon = "glyphicons glyphicons-group";

        // URL
        String url;
        if (mimeResponse != null) {
            // Render URL
            PortletURL renderUrl = mimeResponse.createRenderURL();
            renderUrl.setParameter("tab", "members");
            renderUrl.setParameter("view", "add-to-group");
            renderUrl.setParameter("identifiers", identifiers);

            url = renderUrl.toString();
        } else {
            url = null;
        }

        if (StringUtils.isEmpty(url)) {
            url = "#";
            htmlClass += " disabled";
        }

        return DOM4JUtils.generateLinkElement(url, null, null, htmlClass, text, icon);
    }


    /**
     * Get remove member toolbar button DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param allEditable all editable indicator
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getRemoveMemberToolbarButton(PortalControllerContext portalControllerContext, boolean allEditable, Bundle bundle) {
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();
        
        // URL
        String url;
        // HTML classes
        String htmlClass = "btn btn-default btn-sm no-ajax-link";
        // Text
        String text = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_REMOVE_MEMBER");
        // Icon
        String icon = "glyphicons glyphicons-remove";

        if (allEditable) {
            url = "#" + response.getNamespace() + "-remove";
        } else {
            url = "#";
            htmlClass += " disabled";
        }

        Element button = DOM4JUtils.generateLinkElement(url, null, null, htmlClass, text, icon);
        if (allEditable) {
            DOM4JUtils.addDataAttribute(button, "toggle", "modal");
        }

        return button;
    }


    /**
     * Get remove member confirmation modal DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param selectedMembers selected members
     * @param identifiers selected member identifiers
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getRemoveMemberConfirmationModal(PortalControllerContext portalControllerContext, List<Member> selectedMembers, String[] identifiers,
            Bundle bundle) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();
        // MIME response
        MimeResponse mimeResponse;
        if (portletResponse instanceof MimeResponse) {
            mimeResponse = (MimeResponse) portletResponse;
        } else {
            mimeResponse = null;
        }

        // Modal
        Element modal = DOM4JUtils.generateDivElement("modal fade");
        DOM4JUtils.addAttribute(modal, "id", portletResponse.getNamespace() + "-remove");

        // Modal dialog
        Element modalDialog = DOM4JUtils.generateDivElement("modal-dialog");
        modal.add(modalDialog);

        // Modal content
        Element modalContent = DOM4JUtils.generateDivElement("modal-content");
        modalDialog.add(modalContent);

        // Modal header
        Element modalHeader = DOM4JUtils.generateDivElement("modal-header");
        modalContent.add(modalHeader);

        // Modal close button
        Element close = DOM4JUtils.generateElement("button", "close", null, "glyphicons glyphicons-remove", null);
        DOM4JUtils.addAttribute(close, "type", "button");
        DOM4JUtils.addDataAttribute(close, "dismiss", "modal");
        modalHeader.add(close);

        // Modal title
        Element modalTitle = DOM4JUtils.generateElement("h4", "modal-title", bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_REMOVE_MEMBER"));
        modalHeader.add(modalTitle);

        // Modal body
        Element modalBody = DOM4JUtils.generateDivElement("modal-body");
        modalContent.add(modalBody);

        // Modal message
        Element message = DOM4JUtils.generateElement("p", null, bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_REMOVE_MEMBER_MODAL_MESSAGE"));
        modalBody.add(message);
        Element ul = DOM4JUtils.generateElement("ul", null, null);
        modalBody.add(ul);
        for (Member member : selectedMembers) {
            Element li = DOM4JUtils.generateElement("li", null, null);
            ul.add(li);

            Element p = DOM4JUtils.generateElement("p", null, null);
            li.add(p);

            // Avatar
            String avatarUrl;
            if (member.getPerson().getAvatar() == null) {
                avatarUrl = null;
            } else {
                avatarUrl = member.getPerson().getAvatar().getUrl();
            }
            if (StringUtils.isNotEmpty(avatarUrl)) {
                Element avatar = DOM4JUtils.generateElement("img", "avatar", null);
                DOM4JUtils.addAttribute(avatar, "src", avatarUrl);
                DOM4JUtils.addAttribute(avatar, "alt", StringUtils.EMPTY);
                p.add(avatar);
            }

            // Display name
            Element displayName = DOM4JUtils.generateElement("span", null, member.getDisplayName());
            p.add(displayName);
        }

        // Modal footer
        Element modalFooter = DOM4JUtils.generateDivElement("modal-footer");
        modalContent.add(modalFooter);

        // Confirmation button
        String url;
        if (mimeResponse == null) {
            url = "#";
        } else {
            // Action URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, "remove");
            actionUrl.setParameter("tab", "members");
            actionUrl.setParameter("identifiers", identifiers);

            url = actionUrl.toString();
        }

        Element confirm = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-warning no-ajax-link", bundle.getString("CONFIRM"), null);
        modalFooter.add(confirm);

        // Cancel button
        Element cancel = DOM4JUtils.generateElement("button", "btn btn-default", bundle.getString("CANCEL"), null, null);
        DOM4JUtils.addAttribute(cancel, "type", "button");
        DOM4JUtils.addDataAttribute(cancel, "dismiss", "modal");
        modalFooter.add(cancel);

        return modal;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void exportMembersCsv(PortalControllerContext portalControllerContext, MembersForm members, OutputStream outputStream)
            throws PortletException, IOException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Writer
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        // Date format
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

        try {
            // CSV format
            CSVFormat format = CSVFormat.EXCEL;

            // Headers
            List<String> headers = new ArrayList<String>();
            headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_MEMBER"));
            headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_MEMBER_EXTRA"));
            headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_MEMBER_ACKNOWLEDGMENT_DATE"));
            headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ROLE"));
            headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ID"));
            format.withHeader(headers.toArray(new String[headers.size()]));

            // CSV printer
            CSVPrinter printer = format.print(writer);

            try {
                for (Member member : members.getMembers()) {
                    // Date
                    String date;
                    if (member.getDate() == null) {
                        date = StringUtils.EMPTY;
                    } else {
                        date = dateFormat.format(member.getDate());
                    }

                    // Role
                    String role;
                    if (member.getRole() == null) {
                        role = StringUtils.EMPTY;
                    } else {
                        role = bundle.getString(member.getRole().getKey(), member.getRole().getClassLoader());
                    }

                    printer.printRecord(member.getDisplayName(), member.getExtra(), date, role, member.getId());
                }
            } finally {
                IOUtils.closeQuietly(printer);
            }
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ChangeRoleForm getChangeRoleForm(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException {
        List<Member> selectedMembers = this.getSelectedMembers(portalControllerContext, identifiers);

        // Form
        ChangeRoleForm form = this.applicationContext.getBean(ChangeRoleForm.class);
        form.setSelectedMembers(selectedMembers);

        // Sort
        this.sortMembers(portalControllerContext, form, MembersSort.MEMBER_DISPLAY_NAME, false);

        return form;
    }


    /**
     * Get selected members.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selected member identifiers
     * @return selected members
     * @throws PortletException
     */
    private List<Member> getSelectedMembers(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException {
        // Members
        List<Member> members = this.getMembersForm(portalControllerContext).getMembers();

        // Selected members
        List<Member> selectedMembers;
        if (ArrayUtils.isEmpty(identifiers) || CollectionUtils.isEmpty(members)) {
            selectedMembers = null;
        } else {
            // Sorted members
            Map<String, Member> sortedMembers = new HashMap<>(members.size());
            for (Member member : members) {
                sortedMembers.put(member.getId(), member);
            }
            
            selectedMembers = new ArrayList<>(identifiers.length);
            for (String identifier : identifiers) {
                Member member = sortedMembers.get(identifier);
                if (member != null) {
                    selectedMembers.add(member);
                }
            }
        }
        return selectedMembers;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRole(PortalControllerContext portalControllerContext, MemberManagementOptions options, ChangeRoleForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Updated role
        WorkspaceRole role = form.getRole();
        // Workspace identifier
        String workspaceId = options.getWorkspaceId();

        for (Member member : form.getSelectedMembers()) {
            if (member.isEditable()) {
                member.setEdited(true);
                member.setRole(role);

                this.repository.updateMember(portalControllerContext, workspaceId, member);
            }
        }

        // Update model
        this.invalidateLoadedForms();

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_ROLE_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AddToGroupForm getAddToGroupForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Selected member identifiers
        String[] identifiers = request.getParameterValues("identifiers");
        // Selected members
        List<Member> selectedMembers = this.getSelectedMembers(portalControllerContext, identifiers);

        // Form
        AddToGroupForm form = this.applicationContext.getBean(AddToGroupForm.class);
        form.setSelectedMembers(selectedMembers);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addToGroup(PortalControllerContext portalControllerContext, MemberManagementOptions options, AddToGroupForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Workspace identifier
        String workspaceId = options.getWorkspaceId();
        // Selected members
        List<Member> selectedMembers = form.getSelectedMembers();

        for (CollabProfile localGroup : form.getLocalGroups()) {
            this.repository.addToGroup(portalControllerContext, workspaceId, selectedMembers, localGroup);
        }

        // Update model
        this.invalidateLoadedForms();

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_ADD_TO_GROUP_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
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

            // Invitation identifiers
            Set<String> invitationIdentifiers = new HashSet<>();
            // Purge available indicator
            boolean purgeAvailable = false;

            for (Invitation invitation : invitations) {
                if (InvitationState.SENT.equals(invitation.getState())) {
                    invitationIdentifiers.add(invitation.getId());
                } else {
                    purgeAvailable = true;
                }
            }
            form.setIdentifiers(invitationIdentifiers);
            form.setPurgeAvailable(purgeAvailable);

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
    public JSONObject searchPersons(PortalControllerContext portalControllerContext, MemberManagementOptions options, String filter, int page,
            boolean tokenizer) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Invitation only indicator
        boolean invitationOnly = WorkspaceType.INVITATION.equals(options.getWorkspaceType());

        // Member identifiers
        Set<String> memberIdentifiers = this.getMembersForm(portalControllerContext).getIdentifiers();
        // Invitation identifiers
        Set<String> invitationIndentifiers = this.getInvitationsForm(portalControllerContext).getIdentifiers();
        // Request identifiers
        Set<String> requestIdentifiers;
        if (invitationOnly) {
            requestIdentifiers = null;
        } else {
            requestIdentifiers = this.getInvitationRequestsForm(portalControllerContext).getIdentifiers();
        }

        // JSON objects
        List<JSONObject> objects = new ArrayList<>();
        // Total results
        int total = 0;

        String[] parts;
        if (StringUtils.isBlank(filter)) {
            parts = new String[]{StringUtils.EMPTY};
        } else {
            parts = StringUtils.split(filter, ",;");
        }
        for (String part : parts) {
            // Persons
            List<Person> persons = this.repository.searchPersons(portalControllerContext, part, tokenizer);
            for (Person person : persons) {
                // Already member indicator
                boolean alreadyMember = memberIdentifiers.contains(person.getUid());

                // Already invited
                boolean alreadyInvited = invitationIndentifiers.contains(person.getUid());

                // Existing request indicator
                boolean existingRequest = !invitationOnly && requestIdentifiers.contains(person.getUid());

                // Search result
                JSONObject object = this.getSearchResult(person, alreadyMember, alreadyInvited, existingRequest, bundle);

                objects.add(object);
                total++;
            }

            // Add person creation
            if (this.enablePersonCreation() && !(tokenizer && !persons.isEmpty())) {
                this.addPersonCreationSearchResult(persons, objects, part, bundle);
            }
        }


        // Results JSON object
        JSONObject results = new JSONObject();

        // Items JSON array
        JSONArray items = new JSONArray();
        if (tokenizer) {
            items.addAll(objects);
        } else {
            // Message
            if (page == 1) {
                String message = this.getSearchResultsMessage(portalControllerContext, total, bundle);
                JSONObject object = new JSONObject();
                object.put("message", message);
                items.add(object);
            }

            // Paginated results
            int begin = (page - 1) * SELECT2_RESULTS_PAGE_SIZE;
            int end = Math.min(objects.size(), begin + SELECT2_RESULTS_PAGE_SIZE);
            for (int i = begin; i < end; i++) {
                JSONObject object = objects.get(i);
                items.add(object);
            }

            // Pagination informations
            results.put("page", page);
            results.put("pageSize", SELECT2_RESULTS_PAGE_SIZE);
        }
        results.put("items", items);
        results.put("total", objects.size());

        return results;
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
    protected JSONObject getSearchResult(Person person, boolean alreadyMember, boolean alreadyInvited, boolean existingRequest, Bundle bundle) {
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
        } else if (alreadyInvited) {
            displayName += " " + bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ALREADY_INVITED");
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
     * @param persons filtered persons
     * @param objects JSON objects
     * @param filter search filter
     * @param bundle internationalization bundle
     * @throws PortletException
     */
    protected void addPersonCreationSearchResult(List<Person> persons, List<JSONObject> objects, String filter, Bundle bundle) throws PortletException {
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

            objects.add(object);
        }
    }


    /**
     * Get search results message.
     * 
     * @param portalControllerContext portal controller context
     * @param total search results total
     * @param bundle internationalization bundle
     * @return message
     * @throws PortletException
     */
    protected String getSearchResultsMessage(PortalControllerContext portalControllerContext, int total, Bundle bundle) throws PortletException {
        String message;

        if (total == 0) {
            message = bundle.getString("SELECT2_NO_RESULTS");
        } else if (total == 1) {
            message = bundle.getString("SELECT2_ONE_RESULT");
        } else if (total > SELECT2_MAX_RESULTS) {
            message = bundle.getString("SELECT2_TOO_MANY_RESULTS", SELECT2_MAX_RESULTS);
        } else {
            message = bundle.getString("SELECT2_MULTIPLE_RESULTS", total);
        }

        return message;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sortInvitations(PortalControllerContext portalControllerContext, InvitationsForm form, MembersSort sort, boolean alt) throws PortletException {
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
        this.repository.updateInvitations(portalControllerContext, form.getInvitations());

        // Invitations count
        int count = this.repository.getInvitationsCount(portalControllerContext, workspaceId);

        // Update model
        options.setInvitationsCount(count);
        this.invalidateLoadedForms();
        this.getInvitationsForm(portalControllerContext);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeInvitationsHistory(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form)
            throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Invitations
        List<Invitation> invitations = form.getInvitations();

        if (CollectionUtils.isNotEmpty(invitations)) {
            for (Invitation invitation : invitations) {
                if (!InvitationState.SENT.equals(invitation.getState())) {
                    invitation.setDeleted(true);
                }
            }

            // Update invitations
            this.repository.updateInvitations(portalControllerContext, form.getInvitations());

            // Update model
            form.setLoaded(false);
            this.getInvitationsForm(portalControllerContext);

            // Notification
            String message = bundle.getString("MESSAGE_WORKSPACE_INVITATIONS_PURGE_SUCCESS");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        }
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
        List<String> invitationIdentifiers = new ArrayList<>(creationForm.getPendingInvitations().size());
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
                // Update model
                options.setInvitationsCount(this.repository.getInvitationsCount(portalControllerContext, options.getWorkspaceId()));
                invitationsForm.setLoaded(false);
                creationForm.setLocalGroups(null);
                creationForm.setMessage(null);
                this.getInvitationsForm(portalControllerContext);

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
            Set<String> requestIdentifiers = new HashSet<>();
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
    public void sortInvitationRequests(PortalControllerContext portalControllerContext, InvitationRequestsForm form, MembersSort sort, boolean alt)
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

        // Invitations count
        int count = this.repository.getInvitationsCount(portalControllerContext, workspaceId);

        // Update model
        options.setRequestsCount(count);
        this.invalidateLoadedForms();
        this.getInvitationRequestsForm(portalControllerContext);

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
    public InvitationEditionForm getInvitationEditionForm(PortalControllerContext portalControllerContext, String path) throws PortletException {
        return this.repository.getInvitationEditionForm(portalControllerContext, path);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void resendInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException {
        // Resending date
        Date resendingDate = new Date();

        this.repository.resendInvitation(portalControllerContext, form, resendingDate);

        // Update model
        String message = form.getMessage();
        form.setMessage(null);
        form.getInvitation().setMessage(message);
        form.getInvitation().setResendingDate(resendingDate);
        this.invalidateLoadedForms();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Updated invitation
        Invitation invitation = form.getInvitation();
        invitation.setEdited(true);

        // Invitations
        List<Invitation> invitations = new ArrayList<>(1);
        invitations.add(invitation);

        this.repository.updateInvitations(portalControllerContext, invitations);

        // Update model
        this.invalidateLoadedForms();

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATION_UPDATE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Deleted invitation
        Invitation invitation = form.getInvitation();
        invitation.setDeleted(true);

        // Invitations
        List<Invitation> invitations = new ArrayList<>(1);
        invitations.add(invitation);

        this.repository.updateInvitations(portalControllerContext, invitations);

        // Update model
        this.invalidateLoadedForms();

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATION_DELETE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * Invalidate loaded forms.
     */
    protected void invalidateLoadedForms() {
        MembersForm membersForm = this.applicationContext.getBean(MembersForm.class);
        membersForm.setLoaded(false);

        InvitationsForm invitationsForm = this.applicationContext.getBean(InvitationsForm.class);
        invitationsForm.setLoaded(false);

        InvitationRequestsForm requestsForm = this.applicationContext.getBean(InvitationRequestsForm.class);
        requestsForm.setLoaded(false);
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
