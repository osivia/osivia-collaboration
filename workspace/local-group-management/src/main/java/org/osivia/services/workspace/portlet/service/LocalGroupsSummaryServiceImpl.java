package org.osivia.services.workspace.portlet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.AccessibilityRoles;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.AbstractLocalGroup;
import org.osivia.services.workspace.portlet.model.LocalGroupsSort;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummary;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummaryItem;
import org.osivia.services.workspace.portlet.model.comparator.LocalGroupsSummaryItemComparator;
import org.osivia.services.workspace.portlet.repository.LocalGroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Local groups summary portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroupsServiceImpl
 * @see LocalGroupsSummaryService
 */
@Service("summary")
public class LocalGroupsSummaryServiceImpl extends LocalGroupsServiceImpl implements LocalGroupsSummaryService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private LocalGroupsRepository repository;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public LocalGroupsSummaryServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalGroupsSummary getSummary(PortalControllerContext portalControllerContext) throws PortletException {
        // Summary
        LocalGroupsSummary summary = this.applicationContext.getBean(LocalGroupsSummary.class);

        if (!summary.isLoaded()) {
            summary = super.getSummary(portalControllerContext);

            // Sort
            if (summary.getSort() == null) {
                this.sort(portalControllerContext, summary, LocalGroupsSort.DISPLAY_NAME, false);
            } else {
                this.sort(portalControllerContext, summary, summary.getSort(), summary.isAlt());
            }
        }

        return summary;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sort(PortalControllerContext portalControllerContext, LocalGroupsSummary summary, LocalGroupsSort sort, boolean alt) throws PortletException {
        if (CollectionUtils.isNotEmpty(summary.getGroups())) {
            // Comparator
            LocalGroupsSummaryItemComparator comparator = this.applicationContext.getBean(LocalGroupsSummaryItemComparator.class, sort, alt);

            Collections.sort(summary.getGroups(), comparator);

            // Update model
            summary.setSort(sort);
            summary.setAlt(alt);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, LocalGroupsSummary summary, String[] identifiers) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Selection
        List<AbstractLocalGroup> selection = this.getSelection(portalControllerContext, summary, identifiers);

        if (CollectionUtils.isNotEmpty(selection)) {
            // Filtered identifiers
            List<String> filteredIdentifiers = new ArrayList<>(selection.size());
            for (AbstractLocalGroup group : selection) {
                filteredIdentifiers.add(group.getId());
            }
            
            this.repository.deleteLocalGroups(portalControllerContext, filteredIdentifiers);

            // Update model
            summary.getGroups().removeAll(selection);

            // Notification
            String message = bundle.getString("MESSAGE_SUCCESS_DELETE_LOCAL_GROUPS");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Element getToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException, IOException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Toolbar container
        Element container = DOM4JUtils.generateDivElement(null);

        // Toolbar
        Element toolbar = DOM4JUtils.generateDivElement("btn-toolbar", AccessibilityRoles.TOOLBAR);
        container.add(toolbar);

        if (CollectionUtils.isNotEmpty(indexes)) {
            // Summary
            LocalGroupsSummary summary = this.getSummary(portalControllerContext);

            // Local groups
            List<LocalGroupsSummaryItem> groups = summary.getGroups();

            // Selection
            List<AbstractLocalGroup> selection = new ArrayList<>(indexes.size());
            for (String index : indexes) {
                int i = NumberUtils.toInt(index, -1);
                if ((i > -1) && (i < groups.size())) {
                    AbstractLocalGroup group = groups.get(i);
                    selection.add(group);
                }
            }

            if (indexes.size() == selection.size()) {
                // Selection identifiers
                String[] identifiers = new String[selection.size()];
                for (int i = 0; i < selection.size(); i++) {
                    AbstractLocalGroup group = selection.get(i);
                    identifiers[i] = group.getId();
                }

                // Edit
                if (selection.size() == 1) {
                    AbstractLocalGroup group = selection.get(0);
                    Element edit = this.getEditToolbarButton(portalControllerContext, group, bundle);
                    toolbar.add(edit);
                }

                // Add members
                Element addMembers = this.getAddMembersToolbarButton(portalControllerContext, identifiers, bundle);
                toolbar.add(addMembers);

                // Delete
                Element delete = this.getDeleteToolbarButton(portalControllerContext, identifiers, bundle);
                toolbar.add(delete);

                // Delete confirmation modal
                Element deleteConfirmationModal = this.getDeleteConfirmationModal(portalControllerContext, selection, identifiers, bundle);
                container.add(deleteConfirmationModal);
            }
        }

        return container;
    }


    /**
     * Get edit local group toolbar button.
     * 
     * @param portalControllerContext portal controller context
     * @param group edited local group
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getEditToolbarButton(PortalControllerContext portalControllerContext, AbstractLocalGroup group, Bundle bundle) {
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
        String text = bundle.getString("TOOLBAR_BUTTON_EDIT_LOCAL_GROUP");
        // Icon
        String icon = "glyphicons glyphicons-pencil";
        // URL
        String url;
        if (mimeResponse != null) {
            // Render URL
            PortletURL renderUrl = mimeResponse.createRenderURL();
            renderUrl.setParameter("view", "edit");
            renderUrl.setParameter("id", group.getId());

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
     * Get add members to local groups toolbar button.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selection identifiers
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getAddMembersToolbarButton(PortalControllerContext portalControllerContext, String[] identifiers, Bundle bundle) {
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
        String text = bundle.getString("TOOLBAR_BUTTON_ADD_MEMBER_TO_LOCAL_GROUPS");
        // Icon
        String icon = "glyphicons glyphicons-user-add";
        // URL
        String url;
        if (mimeResponse != null) {
            // Render URL
            PortletURL renderUrl = mimeResponse.createRenderURL();
            renderUrl.setParameter("view", "add-members");
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
     * Get delete local groups toolbar button.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers selection identifiers
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getDeleteToolbarButton(PortalControllerContext portalControllerContext, String[] identifiers, Bundle bundle) {
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // HTML classes
        String htmlClass = "btn btn-default btn-sm no-ajax-link";
        // Text
        String text = bundle.getString("TOOLBAR_BUTTON_DELETE_LOCAL_GROUPS");
        // Icon
        String icon = "glyphicons glyphicons-bin";
        // URL
        String url = "#" + response.getNamespace() + "-delete";

        Element button = DOM4JUtils.generateLinkElement(url, null, null, htmlClass, text, icon);
        DOM4JUtils.addDataAttribute(button, "toggle", "modal");

        return button;
    }


    /**
     * Get delete local groups confirmation modal.
     * 
     * @param portalControllerContext portal controller context
     * @param selection selection
     * @param identifiers selection identifiers
     * @param bundle internationalization bundle
     * @return DOM element
     */
    protected Element getDeleteConfirmationModal(PortalControllerContext portalControllerContext, List<AbstractLocalGroup> selection, String[] identifiers,
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
        String modalId = portletResponse.getNamespace() + "-delete";
        // Modal title
        String title = bundle.getString("MODAL_TITLE_DELETE_LOCAL_GROUPS");

        // Modal body content
        Element content = DOM4JUtils.generateDivElement(null);
        Element message = DOM4JUtils.generateElement("p", null, bundle.getString("MODAL_CONTENT_DELETE_LOCAL_GROUPS"));
        content.add(message);
        Element ul = DOM4JUtils.generateElement("ul", null, null);
        content.add(ul);
        for (AbstractLocalGroup group : selection) {
            Element li = DOM4JUtils.generateElement("li", null, null);
            ul.add(li);
            Element p = DOM4JUtils.generateElement("p", null, null);
            li.add(p);
            Element displayName = DOM4JUtils.generateElement("span", null, group.getDisplayName(), "glyphicons glyphicons-group", null);
            p.add(displayName);
            if (StringUtils.isNotBlank(group.getDescription())) {
                Element br = DOM4JUtils.generateElement("br", null, null);
                p.add(br);
                Element description = DOM4JUtils.generateElement("small", "text-pre-wrap text-muted", group.getDescription());
                p.add(description);
            }
        }

        // Confirmation button
        String url;
        if (mimeResponse == null) {
            url = "#";
        } else {
            // Action URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, "delete");
            actionUrl.setParameter("identifiers", identifiers);

            url = actionUrl.toString();
        }
        Element confirm = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-warning no-ajax-link", bundle.getString("CONFIRM"), null);

        return this.getConfirmationModal(portalControllerContext, modalId, title, content, confirm, bundle);
    }


    /**
     * Get confirmation modal.
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

}
