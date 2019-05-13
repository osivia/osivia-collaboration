package org.osivia.services.widgets.rename.plugin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.Permissions;
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
import org.osivia.services.widgets.rename.plugin.model.RenameMenubarModule;
import org.osivia.services.widgets.rename.portlet.service.RenameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.ContextualizationHelper;

/**
 * Rename plugin service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see RenamePluginService
 */
@Service
public class RenamePluginServiceImpl implements RenamePluginService {

    /** Rename menubar module. */
    @Autowired
    private RenameMenubarModule menubarModule;

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
    public RenamePluginServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeMenubarModules(CustomizationContext customizationContext, List<MenubarModule> menubarModules) {
        menubarModules.add(this.menubarModule);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addMenubarItem(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        if (ContextualizationHelper.isCurrentDocContextualized(portalControllerContext) && (documentContext != null)) {
            // Document type
            DocumentType type = documentContext.getDocumentType();
            // Permissions
            Permissions permissions = documentContext.getPermissions();

            if ((type != null) && type.isEditable() && permissions.isEditable()) {
                // HTTP servlet request
                HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
                // Bundle
                Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

                // Window properties
                Map<String, String> properties = new HashMap<>();
                properties.put(RenameService.DOCUMENT_PATH_WINDOW_PROPERTY, documentContext.getCmsPath());

                // Menubar item
                String id = "RENAME";
                String title = bundle.getString("RENAME_MENUBAR_ITEM");
                String icon = "glyphicons glyphicons-edit";
                MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);
                int order = 1;
                String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, RENAME_INSTANCE, properties, PortalUrlType.MODAL);

                MenubarItem menubarItem = new MenubarItem(id, title, icon, parent, order, "javascript:;", null, null, null);
                Map<String, String> data = menubarItem.getData();
                data.put("target", "#osivia-modal");
                data.put("load-url", url);

                menubar.add(menubarItem);
            }
        }
    }

}
