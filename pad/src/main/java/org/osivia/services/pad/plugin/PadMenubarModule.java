/**
 * 
 */
package org.osivia.services.pad.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.cms.PublicationInfos;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;

import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoConnectionProperties;

/**
 * @author Loïc Billon
 *
 */
public class PadMenubarModule implements MenubarModule {

    /** Menubar service. */
    private final IMenubarService menubarService;
    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** Bundle factory. */
    private final IBundleFactory bundleFactory;

    
    /**
     * Constructor.
     */
    public PadMenubarModule() {
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
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext spaceDocumentContext)
            throws PortalException {
        // do nothing
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext documentContext) throws PortalException {


        if (documentContext.getDocumentType().getName().equals(PadPlugin.TOUTATICE_PAD)) {
            Permissions permissions = documentContext.getPermissions();
            if (permissions.isEditable()) {
                // HTTP servlet request
                HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
                // Bundle
                Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());


                String title = bundle.getString("JOIN_PAD");
                String icon = "glyphicons glyphicons-pencil";
                MenubarDropdown parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);
                int order = 1;

                Map<String, String> requestParameters = new HashMap<String, String>();
                PublicationInfos publicationInfos = documentContext.getPublicationInfos();
                String url = getEcmPadUrl(publicationInfos.getPath(), requestParameters);

                MenubarItem joinPad = new MenubarItem("JOIN_PAD", title, icon, parent, order, url, null, null, "fancyframe_refresh");
                menubar.add(joinPad);


                // Suppression du lien éditer par défaut
                MenubarItem edit = null;
                for (MenubarItem item : menubar) {
                    if (item.getId().equals("EDIT")) {
                        edit = item;
                    }
                }
                if (edit != null) {
                    menubar.remove(edit);
                }
            }
        }

    }

    private String getEcmPadUrl(String path, Map<String, String> requestParameters) {

        // get the default domain and app name
        String uri = NuxeoConnectionProperties.getPublicBaseUri().toString();

        // if (requestParameters == null) {
        // requestParameters = new HashMap<String, String>();
        // }

        String url = uri.toString() + "/nxpath/default" + path + "@toutapad_join_pad?";

        return url;
    }

}
