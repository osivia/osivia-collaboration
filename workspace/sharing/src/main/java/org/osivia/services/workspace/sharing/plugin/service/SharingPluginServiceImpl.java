package org.osivia.services.workspace.sharing.plugin.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.workspace.sharing.plugin.model.SharingMenubarModule;
import org.osivia.services.workspace.sharing.plugin.repository.SharingPluginRepository;
import org.osivia.services.workspace.sharing.portlet.service.SharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.ContextualizationHelper;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;

/**
 * Sharing plugin service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SharingPluginService
 */
@Service
public class SharingPluginServiceImpl implements SharingPluginService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Plugin repository. */
    @Autowired
    private SharingPluginRepository repository;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Menubar service. */
    @Autowired
    private IMenubarService menubarService;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public SharingPluginServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeMenubarModules(CustomizationContext customizationContext, List<MenubarModule> menubarModules) {
        // Sharing menubar module
        MenubarModule sharingMenubarModule = this.applicationContext.getBean(SharingMenubarModule.class);
        menubarModules.add(sharingMenubarModule);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeListTemplates(CustomizationContext customizationContext, Map<String, ListTemplate> listTemplates) {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(customizationContext.getLocale());

        // Sharing list template
        ListTemplate sharingListTemplate = new ListTemplate("sharing", bundle.getString("SHARING_LIST_TEMPLATE"), "dublincore, toutatice, ottc-sharing");
        listTemplates.put(sharingListTemplate.getKey(), sharingListTemplate);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMenubarItems(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        if ((documentContext != null) && (documentContext instanceof NuxeoDocumentContext)) {
            // Nuxeo document context
            NuxeoDocumentContext nuxeoDocumentContext = (NuxeoDocumentContext) documentContext;
            // Document
            Document document = nuxeoDocumentContext.getDocument();
            // Document type
            DocumentType type = nuxeoDocumentContext.getDocumentType();

            if ((document != null) && (type != null) && !type.isRoot()) {
                this.addIndicatorMenubarItem(portalControllerContext, menubar, nuxeoDocumentContext);
                this.addPortletMenubarItem(portalControllerContext, menubar, nuxeoDocumentContext);
            }
        }
    }


    /**
     * Add sharing indicator menubar item.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     * @param documentContext Nuxeo document context
     * @throws PortalException
     */
    private void addIndicatorMenubarItem(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, NuxeoDocumentContext documentContext)
            throws PortalException {
        // Sharing root
        Document sharingRoot = this.repository.getSharingRoot(portalControllerContext, documentContext);

        if (sharingRoot != null) {
            // HTTP servlet request
            HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
            // Bundle
            Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

            // Sharing author
            String author = this.repository.getSharingAuthor(portalControllerContext, sharingRoot);

            // Label
            String label;
            if (StringUtils.isEmpty(author) || StringUtils.equals(servletRequest.getRemoteUser(), author)) {
                label = bundle.getString("SHARED_MENUBAR_LABEL");
            } else {
                PersonService personService = DirServiceFactory.getService(PersonService.class);
                Person person = personService.getPerson(author);
                String displayName;
                if (person == null) {
                    displayName = author;
                } else {
                    displayName = StringUtils.defaultIfBlank(person.getDisplayName(), author);
                }
                label = bundle.getString("SHARED_BY_MENUBAR_LABEL", displayName);
            }

            // Menubar item
            MenubarItem indicator = new MenubarItem("SHARED", label, MenubarGroup.CMS, -1, "label label-default");
            indicator.setGlyphicon("glyphicons glyphicons-group");
            indicator.setState(true);
            menubar.add(indicator);
        }
    }


    /**
     * Add sharing portlet menubar item.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     * @param documentContext Nuxeo document context
     * @throws PortalException
     */
    private void addPortletMenubarItem(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, NuxeoDocumentContext documentContext)
            throws PortalException {
        if (ContextualizationHelper.isCurrentDocContextualized(portalControllerContext)) {
            // Document type
            DocumentType documentType = documentContext.getDocumentType();
            // Publication infos
            NuxeoPublicationInfos publicationInfos = documentContext.getPublicationInfos();
            // Permissions
            NuxeoPermissions permissions = documentContext.getPermissions();

            if ((documentType != null) && (documentType.isFile() || StringUtils.equals("Note", documentType.getName())) && publicationInfos.isLiveSpace()
                    && !publicationInfos.isDraft() && permissions.isManageable()) {
                // Path
                String path = documentContext.getCmsPath();

                if (this.repository.isInCurrentUserWorkspace(portalControllerContext, path)) {
                    // HTTP servlet request
                    HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
                    // Bundle
                    Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

                    // Window properties
                    Map<String, String> properties = new HashMap<String, String>();
                    properties.put(SharingService.DOCUMENT_PATH_WINDOW_PROPERTY, path);

                    // Menubar item
                    String id = "SHARING";
                    String title = bundle.getString("SHARING_MENUBAR_ITEM");
                    String icon = "glyphicons glyphicons-share-alt";
                    MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.SHARE_DROPDOWN_MENU_ID);
                    int order = 1;
                    String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, SHARING_INSTANCE, properties, PortalUrlType.MODAL);

                    MenubarItem menubarItem = new MenubarItem(id, title, icon, parent, order, "javascript:;", null, null, null);
                    Map<String, String> data = menubarItem.getData();
                    data.put("target", "#osivia-modal");
                    data.put("load-url", url);
                    data.put("backdrop", "static");

                    menubar.add(menubarItem);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void clearMenubarItems(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        if ((documentContext != null) && (documentContext instanceof NuxeoDocumentContext)) {
            // Nuxeo document context
            NuxeoDocumentContext nuxeoDocumentContext = (NuxeoDocumentContext) documentContext;

            // Sharing root
            Document sharingRoot = this.repository.getSharingRoot(portalControllerContext, nuxeoDocumentContext);

            if (sharingRoot != null) {
                // HTTP servlet request
                HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();

                // Sharing author
                String author = this.repository.getSharingAuthor(portalControllerContext, sharingRoot);

                if (!StringUtils.equals(author, servletRequest.getRemoteUser())) {
                    Iterator<MenubarItem> iterator = menubar.iterator();
                    while (iterator.hasNext()) {
                        MenubarItem item = iterator.next();
                        if (!StringUtils.equals("SHARED", item.getId())) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

}
