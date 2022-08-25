package org.osivia.services.workspace.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;

/**
 * Workspace local group management menubar module.
 *
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
public class LocalGroupManagementMenubarModule implements MenubarModule {

    /** Menubar service. */
    private final IMenubarService menubarService;
    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** Bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public LocalGroupManagementMenubarModule() {
        super();

        // Menubar service
        this.menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // Bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext spaceDocumentContext) throws PortalException {
        if (spaceDocumentContext != null) {
            // Space document
            Document space = (Document) spaceDocumentContext.getDocument();
            if (space != null) {
                // Check type
                String type = space.getType();
                if ("Workspace".equals(type)) {
                    // Check permissions
                    Permissions permissions = spaceDocumentContext.getPermissions();
                    if (permissions.isManageable()) {
                        // HTTP servlet request
                        HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
                        // Bundle
                        Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

                        // Window properties
                        Map<String, String> properties = new HashMap<String, String>();
                        properties.put("osivia.title", bundle.getString("WORKSPACE_LOCAL_GROUP_MANAGEMENT_TITLE"));

                        properties.put("osivia.back.reset", String.valueOf(true));
                        properties.put("osivia.navigation.reset", String.valueOf(true));

                        // Menubar item
                        String id = "WORKSPACE_LOCAL_GROUP_MANAGEMENT";
                        String title = bundle.getString("WORKSPACE_LOCAL_GROUP_MANAGEMENT_MENUBAR_ITEM");
                        String icon = "glyphicons glyphicons-group";
                        MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CONFIGURATION_DROPDOWN_MENU_ID);
                        int order = 3;
                        String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext,
                                "osivia-services-workspace-local-group-management-instance", properties);
                        String target = null;
                        String onclick = null;
                        String htmlClasses = null;

                        MenubarItem menubarItem = new MenubarItem(id, title, icon, parent, order, url, target, onclick, htmlClasses);
                        menubar.add(menubarItem);
                    }
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext documentContext) throws PortalException {
        // Do nothing
    }

}

