package org.osivia.services.workspace.sharing.plugin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.workspace.sharing.plugin.model.SharingMenubarModule;
import org.osivia.services.workspace.sharing.portlet.service.SharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.ContextualizationHelper;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;

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
        MenubarModule menubarModule = this.applicationContext.getBean(SharingMenubarModule.class);
        menubarModules.add(menubarModule);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMenubarItem(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        if ((documentContext != null) && (documentContext instanceof NuxeoDocumentContext)
                && ContextualizationHelper.isCurrentDocContextualized(portalControllerContext)) {
            // Nuxeo document context
            NuxeoDocumentContext nuxeoDocumentContext = (NuxeoDocumentContext) documentContext;
            // Document
            Document document = nuxeoDocumentContext.getDocument();
            // Document type
            DocumentType type = nuxeoDocumentContext.getDocumentType();

            if ((document != null) && (type != null) && !type.isRoot()) {
                // Publication infos
                NuxeoPublicationInfos publicationInfos = nuxeoDocumentContext.getPublicationInfos();
                // Permissions
                NuxeoPermissions permissions = nuxeoDocumentContext.getPermissions();

                if (publicationInfos.isLiveSpace() && !publicationInfos.isDraft() && permissions.isManageable()) {
                    // HTTP servlet request
                    HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
                    // Bundle
                    Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

                    // Window properties
                    Map<String, String> properties = new HashMap<String, String>();
                    // properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
                    // properties.put("osivia.ajaxLink", "1");
                    properties.put(SharingService.DOCUMENT_PATH_WINDOW_PROPERTY, document.getPath());

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
                    data.put("title", title);
                    data.put("footer", String.valueOf(true));

                    menubar.add(menubarItem);
                }
            }
        }
    }

}
