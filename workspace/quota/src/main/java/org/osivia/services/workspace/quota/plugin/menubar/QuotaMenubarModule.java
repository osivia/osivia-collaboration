package org.osivia.services.workspace.quota.plugin.menubar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.cms.impl.BasicPermissions;
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
 * Quota menubar module.
 *
 * @author Loïc Billon
 * @see MenubarModule
 */
public class QuotaMenubarModule implements MenubarModule {

    /** Menubar item identifier. */
    public static final String MENUBAR_ITEM_ID = "QUOTA_MANAGEMENT";


    /** Menubar service. */
    private final IMenubarService menubarService;
    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** Bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public QuotaMenubarModule() {
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
            DocumentContext<? extends EcmDocument> spaceDocumentContext) throws PortalException {
        if (spaceDocumentContext != null) {
            // Space document
            Document space = (Document) spaceDocumentContext.getDoc();
            if (space != null) {
                // Check type
                String type = space.getType();
                if ("Workspace".equals(type)) {
                    // Check permissions
                    BasicPermissions permissions = spaceDocumentContext.getPermissions(BasicPermissions.class);
                    if (permissions.isManageableByUser()) {
                        // HTTP servlet request
                        HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
                        // Bundle
                        Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

                        // Window properties
                        Map<String, String> properties = new HashMap<String, String>();
                        properties.put("osivia.title", bundle.getString("QUOTA_MANAGEMENT_TITLE"));
                        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
                        properties.put("osivia.ajaxLink", "1");
                        properties.put("osivia.back.reset", String.valueOf(true));
                        properties.put("osivia.navigation.reset", String.valueOf(true));

                        // Menubar item
                        String title = bundle.getString("QUOTA_MANAGEMENT_MENUBAR_ITEM");
                        String icon = "glyphicons glyphicons-dashboard";
                        MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CONFIGURATION_DROPDOWN_MENU_ID);
                        int order = 4;
                        String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-workspace-quota-instance",
                                properties);
                        String target = null;
                        String onclick = null;
                        String htmlClasses = null;

                        MenubarItem menubarItem = new MenubarItem(MENUBAR_ITEM_ID, title, icon, parent, order, url, target, onclick, htmlClasses);
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
            DocumentContext<? extends EcmDocument> documentContext) throws PortalException {
        // Do nothing
    }

}

