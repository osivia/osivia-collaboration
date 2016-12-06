package org.osivia.services.workspace.portlet.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.services.workspace.portlet.model.ParentDocument;
import org.osivia.services.workspace.portlet.model.TrashForm;
import org.osivia.services.workspace.portlet.model.TrashedDocument;
import org.osivia.services.workspace.portlet.model.comparator.TrashedDocumentComparator;
import org.osivia.services.workspace.portlet.repository.TrashRepository;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Trash portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see TrashService
 * @see ApplicationContextAware
 */
@Service
public class TrashServiceImpl implements TrashService, ApplicationContextAware {

    /** Portlet repository. */
    @Autowired
    private TrashRepository repository;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public TrashServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TrashForm getTrashForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Trash form
        TrashForm form = this.applicationContext.getBean(TrashForm.class);

        if (!form.isLoaded()) {
            // Trashed documents
            List<TrashedDocument> trashedDocuments = this.repository.getTrashedDocuments(portalControllerContext);
            form.setTrashedDocuments(trashedDocuments);

            form.setLoaded(true);
        }

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sort(PortalControllerContext portalControllerContext, TrashForm form, String sort, boolean alt) throws PortletException {
        // Comparator
        Comparator<TrashedDocument> comparator = this.applicationContext.getBean(TrashedDocumentComparator.class, sort, alt);

        Collections.sort(form.getTrashedDocuments(), comparator);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMenubarItems(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();
        // Portlet namespace
        String namespace = response.getNamespace();

        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Menubar
        @SuppressWarnings("unchecked")
        List<MenubarItem> menubar = (List<MenubarItem>) request.getAttribute(Constants.PORTLET_ATTR_MENU_BAR);

        
        // Restore all
        String restoreAllTitle = bundle.getString("TRASH_MENUBAR_RESTORE_ALL");
        String restoreAllUrl = "#" + namespace + "-restore-all";
        MenubarItem restoreAllItem = new MenubarItem("TRASH_RESTORE_ALL", restoreAllTitle, "glyphicons glyphicons-history", MenubarGroup.SPECIFIC, 1, restoreAllUrl, null,
                null, null);
        restoreAllItem.getData().put("toggle", "modal");
        menubar.add(restoreAllItem);
        
        // Empty trash
        String emptyTrashTitle = bundle.getString("TRASH_MENUBAR_EMPTY_TRASH");
        String emptyTrashUrl = "#" + namespace + "-empty-trash";
        MenubarItem emptyTrashItem = new MenubarItem("TRASH_EMPTY_TRASH", emptyTrashTitle, "glyphicons glyphicons-bin", MenubarGroup.SPECIFIC, 2, emptyTrashUrl,
                null, null, null);
        emptyTrashItem.getData().put("toggle", "modal");
        menubar.add(emptyTrashItem);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void emptyTrash(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException {
        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);
        
        this.repository.deleteAll(portalControllerContext);

        // Update model
        form.getTrashedDocuments().clear();

        // Notification
        String message = bundle.getString("TRASH_EMPTY_TRASH_MESSAGE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreAll(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException {
        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);

        this.repository.restoreAll(portalControllerContext);

        // Update model
        form.getTrashedDocuments().clear();

        // Notification
        String message = bundle.getString("TRASH_RESTORE_ALL_MESSAGE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException {
        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Selected documents
        List<TrashedDocument> selection = this.getSelection(form);
        // Selected paths
        List<String> selectedPaths = this.getSelectedPaths(selection);

        this.repository.delete(portalControllerContext, selectedPaths);

        // Update model
        form.getTrashedDocuments().removeAll(selection);

        // Notification
        String message = bundle.getString("TRASH_DELETE_SELECTION_MESSAGE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void restore(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException {
        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Selected documents
        List<TrashedDocument> selection = this.getSelection(form);
        // Selected paths
        List<String> selectedPaths = this.getSelectedPaths(selection);

        this.repository.restore(portalControllerContext, selectedPaths);

        // Update model
        form.getTrashedDocuments().removeAll(selection);

        // Notification
        String message = bundle.getString("TRASH_RESTORE_SELECTION_MESSAGE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }


    /**
     * Get selected documents.
     * 
     * @param form trash form
     * @return selected paths
     */
    private List<TrashedDocument> getSelection(TrashForm form) {
        // Trashed documents
        List<TrashedDocument> trashedDocuments;
        if (form == null) {
            trashedDocuments = null;
        } else {
            trashedDocuments = form.getTrashedDocuments();
        }

        // Selected documents
        List<TrashedDocument> selection;
        if (CollectionUtils.isEmpty(trashedDocuments)) {
            selection = new ArrayList<>(0);
        } else {
            selection = new ArrayList<>(trashedDocuments.size());
            for (TrashedDocument document : trashedDocuments) {
                if (document.isSelected()) {
                    selection.add(document);
                }
            }
        }

        return selection;
    }


    /**
     * Get selected document paths.
     * 
     * @param selection selected documents
     * @return paths
     */
    private List<String> getSelectedPaths(List<TrashedDocument> selection) {
        // Selected paths
        List<String> paths = new ArrayList<>(selection.size());
        for (TrashedDocument document : selection) {
            paths.add(document.getPath());
        }

        return paths;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolbarMessage(PortalControllerContext portalControllerContext, int count) throws PortletException {
        // Internationalization bundle
        Locale locale = portalControllerContext.getRequest().getLocale();
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Toolbar message
        String message;
        if (count == 1) {
            message = bundle.getString("TRASH_TOOLBAR_MESSAGE_SINGLE");
        } else if (count > 1) {
            message = bundle.getString("TRASH_TOOLBAR_MESSAGE_MULTIPLE", count);
        } else {
            message = null;
        }

        return message;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Element getLocationBreadcrumb(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Parent documents
        List<ParentDocument> parents = this.repository.getLocationParents(portalControllerContext, path);

        // Breadcrumb container
        Element breadcrumb = DOM4JUtils.generateElement("ol", "breadcrumb", StringUtils.EMPTY);

        for (ParentDocument parent : parents) {
            String htmlClass;
            if (StringUtils.equals(path, parent.getPath())) {
                htmlClass = "active";
            } else {
                htmlClass = null;
            }

            // Breadcrumb item
            Element item = DOM4JUtils.generateElement("li", htmlClass, parent.getTitle(), parent.getIcon(), null);
            breadcrumb.add(item);
        }

        return breadcrumb;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PanelPlayer getPlayer(PortalControllerContext portalControllerContext) throws PortletException {
        // Player
        PanelPlayer player = new PanelPlayer();

        // Instance
        player.setInstance("osivia-services-workspace-trash-instance");

        // Properties
        Map<String, String> properties = new HashMap<>();
        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
        properties.put("osivia.ajaxLink", "1");
        player.setProperties(properties);

        return player;
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
