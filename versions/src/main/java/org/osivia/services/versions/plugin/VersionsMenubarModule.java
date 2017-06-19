package org.osivia.services.versions.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
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
 * Versions menubar module.
 * 
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
public class VersionsMenubarModule implements MenubarModule {

    /** Menubar service. */
    private final IMenubarService menubarService;
    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public VersionsMenubarModule() {
        super();

        // Menubar service
        this.menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext spaceDocumentContext)
            throws PortalException {
        // Do nothing
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        if ((request != null) && (request.getRemoteUser() != null) && (documentContext != null)
                && ContextualizationHelper.isCurrentDocContextualized(portalControllerContext)) {
            // Document type
            DocumentType type = documentContext.getDocumentType();

            if ((type != null) && "File".equals(type.getName())) {
                // Internationalization bundle
                Bundle bundle = this.bundleFactory.getBundle(request.getLocale());
                // Nuxeo document
                Document document = (Document) documentContext.getDocument();

                // Window properties
                Map<String, String> properties = new HashMap<>();
                properties.put("osivia.cms.contentPath", document.getId());

                // Menubar item properties
                String id = "VERSIONS";
                String title = bundle.getString("VERSIONS_MENUBAR_ITEM");
                String icon = "glyphicons glyphicons-history";
                MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.OTHER_OPTIONS_DROPDOWN_MENU_ID);
                int order = 10;
                String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-versions-instance", properties,
                        PortalUrlType.POPUP);
                String target = null;
                String onclick = null;
                String htmlClasses = "fancyframe";


                // Menubar item
                MenubarItem item = new MenubarItem(id, title, icon, parent, order, url, target, onclick, htmlClasses);
                item.setAjaxDisabled(true);
                item.setDivider(true);
                menubar.add(item);
            }
        }
    }

}
