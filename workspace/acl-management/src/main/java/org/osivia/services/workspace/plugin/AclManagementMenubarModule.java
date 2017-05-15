package org.osivia.services.workspace.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.cms.PublicationInfos;
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
import org.osivia.portal.api.urls.PortalUrlType;

import fr.toutatice.portail.cms.nuxeo.api.ContextualizationHelper;

/**
 * Workspace ACL management menubar module.
 *
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
public class AclManagementMenubarModule implements MenubarModule {

    /** Menubar service. */
    private final IMenubarService menubarService;
    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** Bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public AclManagementMenubarModule() {
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
        // Do nothing
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext documentContext) throws PortalException {
        if ((documentContext != null) && ContextualizationHelper.isCurrentDocContextualized(portalControllerContext)) {
            // Check type
            DocumentType type = documentContext.getDocumentType();
            if ((type != null) && !type.isRootType()) {
                // Document
                Document document = (Document) documentContext.getDocument();
                if (document != null) {
                    // Check permissions
                    Permissions permissions = documentContext.getPermissions();
                    PublicationInfos publicationInfos = documentContext.getPublicationInfos();
                    if (publicationInfos.isLiveSpace() && !publicationInfos.isDraft() && permissions.isManageable()) {
                        // HTTP servlet request
                        HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
                        // Bundle
                        Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

                        // Window properties
                        Map<String, String> properties = new HashMap<String, String>();
                        properties.put("osivia.title", bundle.getString("ACL_MANAGEMENT_TITLE"));
                        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
                        properties.put("osivia.ajaxLink", "1");
                        properties.put("osivia.back.reset", String.valueOf(true));
                        properties.put(Constants.WINDOW_PROP_URI, document.getPath());

                        // Menubar item
                        String id = "WORKSPACE_ACL_MANAGEMENT";
                        String title = bundle.getString("ACL_MANAGEMENT_MENUBAR_ITEM");
                        String icon = "glyphicons glyphicons-shield";
                        MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.OTHER_OPTIONS_DROPDOWN_MENU_ID);
                        int order = 3;
                        String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-workspace-acl-management-instance",
                                properties, PortalUrlType.POPUP);
                        String target = null;
                        String onclick = null;
                        String htmlClasses = "fancyframe_refresh";

                        MenubarItem menubarItem = new MenubarItem(id, title, icon, parent, order, url, target, onclick, htmlClasses);
                        menubar.add(menubarItem);
                    }
                }
            }
        }
    }

}

