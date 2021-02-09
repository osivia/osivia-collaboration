package org.osivia.services.workspace.portlet.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.batch.IBatchService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.html.AccessibilityRoles;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.batch.ImportInvitationsBatch;
import org.osivia.services.workspace.portlet.batch.ImportObject;
import org.osivia.services.workspace.portlet.model.AbstractAddToGroupForm;
import org.osivia.services.workspace.portlet.model.AbstractChangeRoleForm;
import org.osivia.services.workspace.portlet.model.AbstractMembersForm;
import org.osivia.services.workspace.portlet.model.AddInvitationsToGroupForm;
import org.osivia.services.workspace.portlet.model.AddMembersToGroupForm;
import org.osivia.services.workspace.portlet.model.ChangeInvitationRequestsRoleForm;
import org.osivia.services.workspace.portlet.model.ChangeInvitationsRoleForm;
import org.osivia.services.workspace.portlet.model.ChangeMembersRoleForm;
import org.osivia.services.workspace.portlet.model.ImportForm;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationEditionForm;
import org.osivia.services.workspace.portlet.model.InvitationObject;
import org.osivia.services.workspace.portlet.model.InvitationRequest;
import org.osivia.services.workspace.portlet.model.InvitationRequestsForm;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MemberObject;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.osivia.services.workspace.portlet.model.MembersSort;
import org.osivia.services.workspace.portlet.model.ResendInvitationsForm;
import org.osivia.services.workspace.portlet.model.comparator.InvitationObjectComparator;
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
import org.springframework.web.multipart.MultipartFile;

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
    
    /** Batch service. */
    @Autowired
    private IBatchService batchService;

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
    public void sortMembers(PortalControllerContext portalControllerContext, AbstractMembersForm<? extends MemberObject> form, MembersSort sort, boolean alt)
            throws PortletException {
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

                String tab = "members";


                // Change role
                Element changeRole = this.getChangeRoleToolbarButton(portalControllerContext, identifiers, allEditable, tab, bundle);
                toolbar.add(changeRole);

                // Add to group
                Element addToGroup = this.getAddToGroupToolbarButton(portalControllerContext, identifiers, tab, bundle);
                toolbar.add(addToGroup);

                // Remove member
                Element removeMember = this.getRemoveMemberToolbarButton(portalControllerContext, allEditable, bundle);
                toolbar.add(removeMember);
                
                if (allEditable) {
                    // Remove member confirmation modal
                    Element removeMemberConfirmationModal = getRemoveMemberConfirmationModal(portalControllerContext, selectedMembers, identifiers, bundle);
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
     * @param tab tab
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getChangeRoleToolbarButton(PortalControllerContext portalControllerContext, String[] identifiers, boolean allEditable,
            String tab, Bundle bundle) {
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
            renderUrl.setParameter("tab", tab);
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
     * @param tab tab
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getAddToGroupToolbarButton(PortalControllerContext portalControllerContext, String[] identifiers, String tab, Bundle bundle) {
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
            renderUrl.setParameter("tab", tab);
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
     * @param identifiers identifiers
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

        // Modal identifier
        String modalId = portletResponse.getNamespace() + "-remove";
        // Modal title
        String title = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_REMOVE_MEMBER");
        // Modal body content
        Element content = DOM4JUtils.generateDivElement(null);
        Element message = DOM4JUtils.generateElement("p", null, bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_REMOVE_MEMBER_MODAL_MESSAGE"));
        content.add(message);
        Element ul = getSelectionModalBodyContent(selectedMembers, bundle);
        content.add(ul);
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
        Element confirm = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-danger no-ajax-link", bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_REMOVE_MEMBER"), null);

        return this.getConfirmationModal(portalControllerContext, modalId, title, content, confirm, bundle);
    }


    /**
     * Get selection modal body content DOM element.
     * 
     * @param selection selection
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getSelectionModalBodyContent(List<? extends MemberObject> selection, Bundle bundle) {
        Element ul = DOM4JUtils.generateElement("ul", null, null);

        for (MemberObject object : selection) {
            Element li = DOM4JUtils.generateElement("li", null, null);
            ul.add(li);

            Element p = DOM4JUtils.generateElement("p", null, null);
            li.add(p);

            // Avatar
            String avatarUrl;
            if (object.getPerson().getAvatar() == null) {
                avatarUrl = null;
            } else {
                avatarUrl = object.getPerson().getAvatar().getUrl();
            }
            if (StringUtils.isNotEmpty(avatarUrl)) {
                Element avatar = DOM4JUtils.generateElement("img", "avatar", null);
                DOM4JUtils.addAttribute(avatar, "src", avatarUrl);
                DOM4JUtils.addAttribute(avatar, "alt", StringUtils.EMPTY);
                p.add(avatar);
            }

            // Display name
            Element displayName = DOM4JUtils.generateElement("span", null, object.getDisplayName());
            p.add(displayName);

            // Role
            if (object.getRole() != null) {
                String text = bundle.getString(object.getRole().getKey(), object.getRole().getClassLoader());
                Element role = DOM4JUtils.generateElement("small", "text-muted", text);
                p.add(role);
            }

            // Message
            String message;
            if (object instanceof Invitation) {
                Invitation invitation = (Invitation) object;
                message = invitation.getMessage();
            } else if (object instanceof InvitationRequest) {
                InvitationRequest request = (InvitationRequest) object;
                message = request.getUserMessage();
            } else {
                message = null;
            }
            if (StringUtils.isNotBlank(message)) {
                Element blockquote = DOM4JUtils.generateElement("blockquote", null, null);
                li.add(blockquote);

                Element userMessage = DOM4JUtils.generateElement("p", "text-pre-wrap", message);
                blockquote.add(userMessage);
            }
        }

        return ul;
    }


    /**
     * Get confirmation modal DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param modalId modal identifier
     * @param title modal title
     * @param content modal body content DOM element
     * @param confirm confirmation button DOM element
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getConfirmationModal(PortalControllerContext portalControllerContext, String modalId, String title, Element content, Element confirm,
            Bundle bundle) {
        // Modal
        Element modal = DOM4JUtils.generateDivElement("modal fade");
        DOM4JUtils.addAttribute(modal, "id", modalId);

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
        Element modalTitle = DOM4JUtils.generateElement("h4", "modal-title", title);
        modalHeader.add(modalTitle);

        // Modal body
        Element modalBody = DOM4JUtils.generateDivElement("modal-body");
        modalContent.add(modalBody);

        // Modal body content
        modalBody.add(content);

        // Modal footer
        Element modalFooter = DOM4JUtils.generateDivElement("modal-footer");
        modalContent.add(modalFooter);

        // Confirmation button
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
     * Get selected members.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selected member identifiers
     * @return selected members
     * @throws PortletException
     */
    protected List<Member> getSelectedMembers(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException {
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

            // Sort
            this.sortInvitations(portalControllerContext, form, MembersSort.DATE, true);

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
        InvitationObjectComparator comparator = this.applicationContext.getBean(InvitationObjectComparator.class, sort, alt);
        Collections.sort(form.getInvitations(), comparator);

        // Update model
        form.setSort(sort);
        form.setAlt(alt);
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
    public void deleteInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm form, String[] identifiers)
            throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Sorted identifiers
        Set<String> sortedIdentifiers = new HashSet<>(Arrays.asList(identifiers));

        // Deleted invitations
        List<Invitation> deletedInvitations = new ArrayList<>(sortedIdentifiers.size());

        for (Invitation invitation : form.getInvitations()) {
            if (sortedIdentifiers.contains(invitation.getDocument().getId()) && (invitation.getState() != null) && invitation.getState().isEditable()) {
                invitation.setDeleted(true);

                deletedInvitations.add(invitation);
            }
        }

        this.repository.updateInvitations(portalControllerContext, deletedInvitations);

        // Update model
        form.getInvitations().removeAll(deletedInvitations);
        this.invalidateLoadedForms();

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_INVITATION_DELETE_SUCCESS");
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
    public Element getInvitationsToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Toolbar container
        Element container = DOM4JUtils.generateDivElement(null);

        // Toolbar
        Element toolbar = DOM4JUtils.generateDivElement("btn-toolbar", AccessibilityRoles.TOOLBAR);
        container.add(toolbar);

        if (CollectionUtils.isNotEmpty(indexes)) {
            // Form
            InvitationsForm form = this.getInvitationsForm(portalControllerContext);

            // Invitations
            List<Invitation> invitations = form.getInvitations();

            // Selected members
            List<Invitation> selectedInvitations = new ArrayList<>(indexes.size());
            for (String index : indexes) {
                int i = NumberUtils.toInt(index, -1);
                if ((i > -1) && (i < invitations.size())) {
                    Invitation invitation = invitations.get(i);
                    if (invitation.getDocument() != null) {
                        selectedInvitations.add(invitation);
                    }
                }
            }

            if (indexes.size() == selectedInvitations.size()) {
                // All editable indicator
                boolean allEditable = true;
                Iterator<Invitation> iterator = selectedInvitations.iterator();
                while (allEditable && iterator.hasNext()) {
                    // Selected invitation
                    Invitation invitation = iterator.next();

                    // Editable indicator
                    boolean editable = (invitation.getState() != null) && invitation.getState().isEditable();

                    allEditable &= editable;
                }

                // Selected invitation identifiers
                String[] identifiers = new String[selectedInvitations.size()];
                for (int i = 0; i < selectedInvitations.size(); i++) {
                    Invitation invitation = selectedInvitations.get(i);
                    identifiers[i] = invitation.getDocument().getId();
                }

                String tab = "invitations";


                // Manage
                if (selectedInvitations.size() == 1) {
                    Invitation invitation = selectedInvitations.get(0);
                    Element manage = this.getManageInvitationToolbarButton(portalControllerContext, invitation, bundle);
                    toolbar.add(manage);
                }

                // Resend invitation
                Element resend = this.getResendInvitationsToolbarButton(portalControllerContext, identifiers, allEditable, bundle);
                toolbar.add(resend);

                // Change role
                Element changeRole = this.getChangeRoleToolbarButton(portalControllerContext, identifiers, allEditable, tab, bundle);
                toolbar.add(changeRole);

                // Add to group
                Element addToGroup = this.getAddToGroupToolbarButton(portalControllerContext, identifiers, tab, bundle);
                toolbar.add(addToGroup);

                // Delete
                Element delete = this.getDeleteInvitationsToolbarButton(portalControllerContext, allEditable, bundle);
                toolbar.add(delete);
                
                // #2071 Admins can delete the workflow
                Boolean administrator = Boolean.TRUE.equals(portalControllerContext.getRequest().getAttribute("osivia.isAdministrator"));
                if(selectedInvitations.size() == 1 && administrator) {
                    Invitation invitation = selectedInvitations.get(0);

                    Element drop = this.getDropInvitationToolbarButton(portalControllerContext, invitation, bundle, "invitations");
                    toolbar.add(drop);
                }

                if (allEditable) {
                    // Delete confirmation modal
                    Element deleteConfirmationModal = this.getDeleteInvitationsConfirmationModal(portalControllerContext, selectedInvitations, identifiers,
                            bundle);
                    container.add(deleteConfirmationModal);
                }
            }
        }

        return container;
    }


    /**
     * Get manage invitation toolbar button DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param invitation invitation
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getManageInvitationToolbarButton(PortalControllerContext portalControllerContext, Invitation invitation, Bundle bundle) {
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
        String text = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDIT");
        // Icon
        String icon = "glyphicons glyphicons-pencil";
        // URL
        String url;
        if ((invitation.getState() != null) && invitation.getState().isEditable() && (mimeResponse != null)) {
            // Render URL
            PortletURL renderUrl = mimeResponse.createRenderURL();
            renderUrl.setParameter("view", "invitation-edition");
            renderUrl.setParameter("invitationPath", invitation.getDocument().getPath());

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
     * Get drop invitation toolbar button DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param invitation invitation
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getDropInvitationToolbarButton(PortalControllerContext portalControllerContext, InvitationObject invitation, 
    		Bundle bundle, String tab) {
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
        String htmlClass = "btn btn-danger btn-sm";
        // Text
        String text = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_DROP");
        // Icon
        String icon = "glyphicons glyphicons-remove";
        // URL
        String url;
        if ((invitation.getState() != null) && invitation.getState().isEditable() && (mimeResponse != null)) {
            // Render URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, "drop");
            actionUrl.setParameter("invitationPath", invitation.getDocument().getPath());
            actionUrl.setParameter("view", "tab=invitations");
            actionUrl.setParameter("fromtab", tab);


            url = actionUrl.toString();
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
     * Get resend invitations toolbar button DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selected item identifiers
     * @param allEditable all editable indicator
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getResendInvitationsToolbarButton(PortalControllerContext portalControllerContext, String[] identifiers, boolean allEditable,
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
        String text = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_EDITION_RESEND");
        // Icon
        String icon = "glyphicons glyphicons-message-out";
        // URL
        String url;
        if (allEditable && (mimeResponse != null)) {
            // Render URL
            PortletURL renderUrl = mimeResponse.createRenderURL();
            renderUrl.setParameter("tab", "invitations");
            renderUrl.setParameter("view", "resend");
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
     * Get delete invitations toolbar button DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param allEditable all editable indicator
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getDeleteInvitationsToolbarButton(PortalControllerContext portalControllerContext, boolean allEditable, Bundle bundle) {
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // URL
        String url;
        // HTML classes
        String htmlClass = "btn btn-default btn-sm no-ajax-link";
        // Text
        String text = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_DELETE");
        // Icon
        String icon = "glyphicons glyphicons-bin";

        if (allEditable) {
            url = "#" + response.getNamespace() + "-delete";
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
     * Get delete invitations confirmation modal DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param selectedInvitations selected invitation requests
     * @param identifiers selected invitation request identifiers
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getDeleteInvitationsConfirmationModal(PortalControllerContext portalControllerContext, List<Invitation> selectedInvitations,
            String[] identifiers, Bundle bundle) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();
        // MIME response
        MimeResponse mimeResponse;
        if (portletResponse instanceof MimeResponse) {
            mimeResponse = (MimeResponse) portletResponse;
        } else {
            mimeResponse = null;
        }

        // Modal identifier
        String modalId = portletResponse.getNamespace() + "-delete";
        // Modal title
        String title = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_DELETE_CONFIRMATION_TITLE");
        // Modal body content
        Element content = DOM4JUtils.generateDivElement(null);
        Element message = DOM4JUtils.generateElement("p", null,
                bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_DELETE_CONFIRMATION_MESSAGE"));
        content.add(message);
        Element ul = getSelectionModalBodyContent(selectedInvitations, bundle);
        content.add(ul);
        // Confirmation button
        String url;
        if (mimeResponse == null) {
            url = "#";
        } else {
            // Action URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, "delete");
            actionUrl.setParameter("tab", "invitations");
            actionUrl.setParameter("identifiers", identifiers);

            url = actionUrl.toString();
        }
        Element confirm = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-warning no-ajax-link",
                bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_DELETE"), null);

        return this.getConfirmationModal(portalControllerContext, modalId, title, content, confirm, bundle);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptInvitationRequests(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationRequestsForm form,
            String[] identifiers) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Workspace identifier
        String workspaceId = options.getWorkspaceId();

        // Sorted identifiers
        Set<String> sortedIdentifiers = new HashSet<>(Arrays.asList(identifiers));

        // Invitation requests
        List<InvitationRequest> requests = form.getRequests();

        for (InvitationRequest request : requests) {
            if ((request.getDocument() != null) && sortedIdentifiers.contains(request.getDocument().getId()) && (request.getState() != null)
                    && request.getState().isEditable()) {
                request.setAccepted(true);
            }
        }

        this.repository.updateInvitationRequests(portalControllerContext, workspaceId, requests);

        // Update model
        this.invalidateLoadedForms();
        List<InvitationRequest> updatedRequests = this.getInvitationRequestsForm(portalControllerContext).getRequests();
        form.setRequests(updatedRequests);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_ACCEPT_REQUEST_INVITATIONS_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void declineInvitationRequests(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationRequestsForm form,
            String[] identifiers) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Workspace identifier
        String workspaceId = options.getWorkspaceId();

        // Sorted identifiers
        Set<String> sortedIdentifiers = new HashSet<>(Arrays.asList(identifiers));

        // Invitation requests
        List<InvitationRequest> requests = form.getRequests();

        for (InvitationRequest request : requests) {
            if ((request.getDocument() != null) && sortedIdentifiers.contains(request.getDocument().getId()) && (request.getState() != null)
                    && request.getState().isEditable()) {
                request.setDeleted(true);
            }
        }

        this.repository.updateInvitationRequests(portalControllerContext, workspaceId, requests);

        // Update model
        this.invalidateLoadedForms();
        List<InvitationRequest> updatedRequests = this.getInvitationRequestsForm(portalControllerContext).getRequests();
        form.setRequests(updatedRequests);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_DECLINE_REQUEST_INVITATIONS_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
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

            // Sort
            this.sortInvitationRequests(portalControllerContext, form, MembersSort.DATE, true);

            form.setLoaded(true);
        }

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Element getInvitationRequestsToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Toolbar container
        Element container = DOM4JUtils.generateDivElement(null);

        // Toolbar
        Element toolbar = DOM4JUtils.generateDivElement("btn-toolbar", AccessibilityRoles.TOOLBAR);
        container.add(toolbar);

        if (CollectionUtils.isNotEmpty(indexes)) {
            // Form
            InvitationRequestsForm form = this.getInvitationRequestsForm(portalControllerContext);

            // Invitation requests
            List<InvitationRequest> requests = form.getRequests();

            // Selected invitation requests
            List<InvitationRequest> selectedRequests = new ArrayList<>(indexes.size());
            for (String index : indexes) {
                int i = NumberUtils.toInt(index, -1);
                if ((i > -1) && (i < requests.size())) {
                    InvitationRequest request = requests.get(i);
                    if (request.getDocument() != null) {
                        selectedRequests.add(request);
                    }
                }
            }

            if (indexes.size() == selectedRequests.size()) {
                // All editable indicator
                boolean allEditable = true;
                Iterator<InvitationRequest> iterator = selectedRequests.iterator();
                while (allEditable && iterator.hasNext()) {
                    // Selected invitation request
                    InvitationRequest request = iterator.next();

                    // Editable indicator
                    boolean editable = (request.getState() != null) && request.getState().isEditable();
                    
                    allEditable &= editable;
                }

                // Selected invitation request identifiers
                String[] identifiers = new String[selectedRequests.size()];
                for (int i = 0; i < selectedRequests.size(); i++) {
                    InvitationRequest request = selectedRequests.get(i);
                    identifiers[i] = request.getDocument().getId();
                }

                // Change role
                Element changeRole = this.getChangeRoleToolbarButton(portalControllerContext, identifiers, allEditable, "requests", bundle);
                toolbar.add(changeRole);

                // Accept
                Element accept = this.getAcceptInvitationRequestToolbarButton(portalControllerContext, allEditable, bundle);
                toolbar.add(accept);
                
                // Decline
                Element decline = this.getDeclineInvitationRequestToolbarButton(portalControllerContext, allEditable, bundle);
                toolbar.add(decline);
                
                // #2071 Admins can delete the workflow
                Boolean administrator = Boolean.TRUE.equals(portalControllerContext.getRequest().getAttribute("osivia.isAdministrator"));
                if(selectedRequests.size() == 1 && administrator) {
                    InvitationRequest request = selectedRequests.get(0);

                    Element drop = this.getDropInvitationToolbarButton(portalControllerContext, request, bundle, "requests");
                    toolbar.add(drop);
                }
                

                if (allEditable) {
                    // Accept confirmation modal
                    Element acceptConfirmationModal = this.getAcceptInvitationRequestConfirmationModal(portalControllerContext, selectedRequests, identifiers, bundle);
                    container.add(acceptConfirmationModal);

                    // Decline confirmation modal
                    Element declineConfirmationModal = this.getDeclineInvitationRequestConfirmationModal(portalControllerContext, selectedRequests, identifiers,
                            bundle);
                    container.add(declineConfirmationModal);
                }
            }
        }

        return container;
    }


    /**
     * Get accept invitation request toolbar button DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param allEditable all editable indicator
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getAcceptInvitationRequestToolbarButton(PortalControllerContext portalControllerContext, boolean allEditable, Bundle bundle) {
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // URL
        String url;
        // HTML classes
        String htmlClass = "btn btn-success btn-sm no-ajax-link";
        // Text
        String text = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ACCEPT_INVITATION_REQUEST");
        // Icon
        String icon = "glyphicons glyphicons-ok-sign";

        if (allEditable) {
            url = "#" + response.getNamespace() + "-accept";
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
     * Get decline invitation request toolbar button DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param allEditable all editable indicator
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getDeclineInvitationRequestToolbarButton(PortalControllerContext portalControllerContext, boolean allEditable, Bundle bundle) {
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // URL
        String url;
        // HTML classes
        String htmlClass = "btn btn-danger btn-sm no-ajax-link";
        // Text
        String text = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_DECLINE_INVITATION_REQUEST");
        // Icon
        String icon = "glyphicons glyphicons-remove-sign";

        if (allEditable) {
            url = "#" + response.getNamespace() + "-decline";
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
     * Get accept invitation request confirmation modal DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param selectedRequests selected invitation requests
     * @param identifiers selected invitation request identifiers
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getAcceptInvitationRequestConfirmationModal(PortalControllerContext portalControllerContext, List<InvitationRequest> selectedRequests,
            String[] identifiers, Bundle bundle) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();
        // MIME response
        MimeResponse mimeResponse;
        if (portletResponse instanceof MimeResponse) {
            mimeResponse = (MimeResponse) portletResponse;
        } else {
            mimeResponse = null;
        }

        // Modal identifier
        String modalId = portletResponse.getNamespace() + "-accept";
        // Modal title
        String title = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ACCEPT_INVITATION_REQUEST_CONFIRMATION_MODAL_TITLE");
        // Modal body content
        Element content = DOM4JUtils.generateDivElement(null);
        Element message = DOM4JUtils.generateElement("p", null,
                bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ACCEPT_INVITATION_REQUEST_CONFIRMATION_MODAL_MESSAGE"));
        content.add(message);
        Element ul = getSelectionModalBodyContent(selectedRequests, bundle);
        content.add(ul);
        // Confirmation button
        String url;
        if (mimeResponse == null) {
            url = "#";
        } else {
            // Action URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, "accept");
            actionUrl.setParameter("tab", "requests");
            actionUrl.setParameter("identifiers", identifiers);

            url = actionUrl.toString();
        }
        Element confirm = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-success no-ajax-link",
                bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ACCEPT_INVITATION_REQUEST"), null);

        return this.getConfirmationModal(portalControllerContext, modalId, title, content, confirm, bundle);
    }


    /**
     * Get decline invitation request confirmation modal DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param selectedRequests selected invitation requests
     * @param identifiers selected invitation request identifiers
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getDeclineInvitationRequestConfirmationModal(PortalControllerContext portalControllerContext, List<InvitationRequest> selectedRequests,
            String[] identifiers, Bundle bundle) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();
        // MIME response
        MimeResponse mimeResponse;
        if (portletResponse instanceof MimeResponse) {
            mimeResponse = (MimeResponse) portletResponse;
        } else {
            mimeResponse = null;
        }

        // Modal identifier
        String modalId = portletResponse.getNamespace() + "-decline";
        // Modal title
        String title = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_DECLINE_INVITATION_REQUEST_CONFIRMATION_MODAL_TITLE");
        // Modal body content
        Element content = DOM4JUtils.generateDivElement(null);
        Element message = DOM4JUtils.generateElement("p", null,
                bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_DECLINE_INVITATION_REQUEST_CONFIRMATION_MODAL_MESSAGE"));
        content.add(message);
        Element ul = getSelectionModalBodyContent(selectedRequests, bundle);
        content.add(ul);
        // Confirmation button
        String url;
        if (mimeResponse == null) {
            url = "#";
        } else {
            // Action URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, "decline");
            actionUrl.setParameter("tab", "requests");
            actionUrl.setParameter("identifiers", identifiers);

            url = actionUrl.toString();
        }
        Element confirm = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-danger no-ajax-link",
                bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_DECLINE_INVITATION_REQUEST"), null);

        return this.getConfirmationModal(portalControllerContext, modalId, title, content, confirm, bundle);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sortInvitationRequests(PortalControllerContext portalControllerContext, InvitationRequestsForm form, MembersSort sort, boolean alt)
            throws PortletException {
        InvitationObjectComparator comparator = this.applicationContext.getBean(InvitationObjectComparator.class, sort, alt);
        Collections.sort(form.getRequests(), comparator);

        // Update model
        form.setSort(sort);
        form.setAlt(alt);
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
     * {@inheritDoc}
     */
    @Override
    public <M extends MemberObject, F extends AbstractChangeRoleForm<M>> F getChangeRoleForm(PortalControllerContext portalControllerContext,
            String[] identifiers, Class<M> memberType, Class<F> formType) throws PortletException {
        // Selection
        List<M> selection = getSelection(portalControllerContext, identifiers, memberType);

        // Form
        F form = this.applicationContext.getBean(formType);
        form.setMembers(selection);

        // Sort
        this.sortMembers(portalControllerContext, form, MembersSort.MEMBER_DISPLAY_NAME, false);

        return form;
    }


    /**
     * Get selection.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selected item identifiers
     * @param memberType member type
     * @return selection
     * @throws PortletException
     */
    protected <M extends MemberObject> List<M> getSelection(PortalControllerContext portalControllerContext, String[] identifiers, Class<M> memberType)
            throws PortletException {
        // Parent form
        AbstractMembersForm<?> parentForm;
        if (Member.class.isAssignableFrom(memberType)) {
            parentForm = this.getMembersForm(portalControllerContext);
        } else if (Invitation.class.isAssignableFrom(memberType)) {
            parentForm = this.getInvitationsForm(portalControllerContext);
        } else if (InvitationRequest.class.isAssignableFrom(memberType)) {
            parentForm = this.getInvitationRequestsForm(portalControllerContext);
        } else {
            parentForm = null;
        }

        // Members
        List<? extends MemberObject> members;
        if (parentForm == null) {
            members = null;
        } else {
            members = parentForm.getMembers();
        }

        // Selection
        List<M> selection;
        if (ArrayUtils.isEmpty(identifiers) || CollectionUtils.isEmpty(members)) {
            selection = null;
        } else {
            // Sorted members
            Map<String, MemberObject> sortedMembers = new HashMap<>(members.size());
            for (MemberObject member : members) {
                String id;
                if (member instanceof InvitationObject) {
                    InvitationObject invitation = (InvitationObject) member;
                    if (invitation.getDocument() == null) {
                        id = null;
                    } else {
                        id = invitation.getDocument().getId();
                    }
                } else {
                    id = member.getId();
                }

                if (StringUtils.isNotEmpty(id)) {
                    sortedMembers.put(id, member);
                }
            }

            selection = new ArrayList<>(identifiers.length);
            for (String identifier : identifiers) {
                M member;
                try {
                    member = memberType.cast(sortedMembers.get(identifier));
                } catch (ClassCastException e) {
                    member = null;
                }

                if (member != null) {
                    selection.add(member);
                }
            }
        }
        return selection;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <M extends MemberObject, F extends AbstractChangeRoleForm<M>> void updateRole(PortalControllerContext portalControllerContext,
            MemberManagementOptions options, F form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Updated role
        WorkspaceRole role = form.getRole();
        // Workspace identifier
        String workspaceId = options.getWorkspaceId();

        // Selection
        List<M> selection = form.getMembers();

        if (form instanceof ChangeMembersRoleForm) {
            // Members
            for (MemberObject object : selection) {
                if (object instanceof Member) {
                    Member member = (Member) object;
                    if (member.isEditable()) {
                        member.setEdited(true);
                        member.setRole(role);

                        this.repository.updateMember(portalControllerContext, workspaceId, member);
                    }
                }
            }
        } else if (form instanceof ChangeInvitationsRoleForm) {
            // Invitations
            List<Invitation> invitations = new ArrayList<>(selection.size());
            for (MemberObject object : selection) {
                if (object instanceof Invitation) {
                    Invitation invitation = (Invitation) object;
                    if ((invitation.getState() != null) && invitation.getState().isEditable()) {
                        invitation.setEdited(true);
                        invitation.setRole(role);

                        invitations.add(invitation);
                    }
                }
            }
            this.repository.updateInvitations(portalControllerContext, invitations);
        } else if (form instanceof ChangeInvitationRequestsRoleForm) {
            // Invitation requests
            List<InvitationRequest> requests = new ArrayList<>(selection.size());
            for (MemberObject object : selection) {
                if (object instanceof InvitationRequest) {
                    InvitationRequest request = (InvitationRequest) object;
                    if ((request.getState() != null) && request.getState().isEditable()) {
                        request.setEdited(true);
                        request.setRole(role);

                        requests.add(request);
                    }
                }
            }
            this.repository.updateInvitationRequests(portalControllerContext, workspaceId, requests);
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
    public <M extends MemberObject, F extends AbstractAddToGroupForm<M>> F getAddToGroupForm(PortalControllerContext portalControllerContext,
            String[] identifiers, Class<M> memberType, Class<F> formType) throws PortletException {
        // Selection
        List<M> selection = getSelection(portalControllerContext, identifiers, memberType);

        // Form
        F form = this.applicationContext.getBean(formType);
        form.setMembers(selection);

        // Sort
        this.sortMembers(portalControllerContext, form, MembersSort.MEMBER_DISPLAY_NAME, false);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <M extends MemberObject, F extends AbstractAddToGroupForm<M>> void addToGroup(PortalControllerContext portalControllerContext,
            MemberManagementOptions options, F form) throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Workspace identifier
        String workspaceId = options.getWorkspaceId();

        // Selection
        List<M> selection = form.getMembers();

        if (form instanceof AddMembersToGroupForm) {
            // Members
            List<MemberObject> members = new ArrayList<>(form.getMembers().size());
            for (MemberObject member : form.getMembers()) {
                members.add(member);
            }
            for (CollabProfile localGroup : form.getLocalGroups()) {
                this.repository.addToGroup(portalControllerContext, workspaceId, members, localGroup);
            }
        } else if (form instanceof AddInvitationsToGroupForm) {
            // Invitations
            List<Invitation> invitations = new ArrayList<>(selection.size());
            for (MemberObject object : selection) {
                if (object instanceof Invitation) {
                    Invitation invitation = (Invitation) object;
                    if ((invitation.getState() != null) && invitation.getState().isEditable()) {
                        // Updated local groups
                        Set<CollabProfile> updatedLocalGroups = new HashSet<CollabProfile>();
                        if (CollectionUtils.isNotEmpty(invitation.getLocalGroups())) {
                            updatedLocalGroups.addAll(invitation.getLocalGroups());
                        }
                        if (CollectionUtils.isNotEmpty(form.getLocalGroups())) {
                            updatedLocalGroups.addAll(form.getLocalGroups());
                        }

                        invitation.setEdited(true);
                        invitation.setLocalGroups(new ArrayList<>(updatedLocalGroups));

                        invitations.add(invitation);
                    }
                }
            }
            this.repository.updateInvitations(portalControllerContext, invitations);
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
    public ResendInvitationsForm getResendInvitationsForm(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException {
        // Selection
        List<Invitation> selection = getSelection(portalControllerContext, identifiers, Invitation.class);

        // Form
        ResendInvitationsForm form = this.applicationContext.getBean(ResendInvitationsForm.class);
        form.setMembers(selection);

        // Sort
        this.sortMembers(portalControllerContext, form, MembersSort.MEMBER_DISPLAY_NAME, false);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void resendInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, ResendInvitationsForm form)
            throws PortletException {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Invitations resending date
        Date date = new Date();

        boolean status = this.repository.resendInvitations(portalControllerContext, form.getMembers(), form.getMessage(), date);

        // Update model
        this.invalidateLoadedForms();

        // Notification
        if (status) {
            String message = bundle.getString("MESSAGE_WORKSPACE_INVITATION_RESENDING_SUCCESS");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } else {
            String message = bundle.getString("MESSAGE_WORKSPACE_INVITATION_RESENDING_ERROR");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);
        }
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
        
        ImportForm importForm = this.applicationContext.getBean(ImportForm.class);
        importForm.setLoaded(false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }


	@Override
	public void dropInvitation(PortalControllerContext portalControllerContext, String invitationPath) {
		this.repository.dropInvitation(portalControllerContext, invitationPath);
	    
	    // Update model
	    this.invalidateLoadedForms();
	
	    Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
	    
	    // Notification
	    String message = bundle.getString("MESSAGE_WORKSPACE_DROP_WF_SUCCESS");
	    this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
		
	}


	@Override
	public ImportForm getImportForm(PortalControllerContext portalControllerContext) {
		
        // Form
		ImportForm form = this.applicationContext.getBean(ImportForm.class);
		
		if (!form.isLoaded()) {
			form.setRole(WorkspaceRole.READER);
			
			Person person = (Person) portalControllerContext.getRequest().getAttribute(Constants.ATTR_LOGGED_PERSON_2);
			form.setInitiator(person.getUid());
			
			form.setLocalGroups(null);
			form.setMessage(null);
//			form.setTemporaryFile(null);
			form.setUpload(null);

            form.setLoaded(true);
           
        
		}
        return form;
	}


	@Override
	public void prepareImportInvitations(PortalControllerContext portalControllerContext,
			MemberManagementOptions options, ImportForm form) throws ParseException, PortalException {
		
		String batchId = "importmembers_"+options.getWorkspaceId()+"_"+new Date().getTime();
		
    	// Temporary file
        MultipartFile upload = form.getUpload();
        
        ImportObject dto = applicationContext.getBean(ImportObject.class);
        
        File temporaryFile;
		try {
			temporaryFile = File.createTempFile(batchId, ".tmp");

	        upload.transferTo(temporaryFile);

	        dto.setTemporaryFile(temporaryFile);
	        dto.setInitiator(form.getInitiator());
	        dto.setLocalGroups(form.getLocalGroups());
	        dto.setMessage(form.getMessage());
	        dto.setRole(form.getRole());
	        dto.setWorkspaceId(options.getWorkspaceId());

			Document currentWorkspace = repository.getCurrentWorkspace(portalControllerContext);
			dto.setCurrentWorkspace(currentWorkspace);
	        
		} catch (IOException e) {
			throw new PortalException(e);
		}
		
		ImportInvitationsBatch batch = applicationContext.getBean(ImportInvitationsBatch.class, portalControllerContext.getPortletCtx(), dto);

		batchService.addBatch(batch);
		

	    // Update model
	    this.invalidateLoadedForms();
	
	    Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
	    
	    // Notification
	    String message = bundle.getString("MESSAGE_IMPORT_IN_PROGRESS");
	    this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.INFO);
		
	}


	@Override
	public String getImportHelp(PortalControllerContext portalControllerContext) throws PortletException { 
        return this.repository.getHelp(portalControllerContext, IMPORT_HELP_LOCATION_PROPERTY);

	}


}
