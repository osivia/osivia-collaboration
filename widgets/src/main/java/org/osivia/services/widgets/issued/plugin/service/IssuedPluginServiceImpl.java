package org.osivia.services.widgets.issued.plugin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.cms.PublicationInfos;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.contribution.IContributionService.EditionState;
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
import org.osivia.services.widgets.issued.plugin.model.IssuedMenubarModule;
import org.osivia.services.widgets.issued.portlet.service.IssuedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.ContextualizationHelper;

/**
 * Issued date plugin service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see IssuedPluginService
 */
@Service
public class IssuedPluginServiceImpl implements IssuedPluginService {

    /** Menubar module. */
    @Autowired
    private IssuedMenubarModule menubarModule;

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
    public IssuedPluginServiceImpl() {
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
        if (this.accept(portalControllerContext, documentContext)) {
            // HTTP servlet request
            HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
            // Bundle
            Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

            // Window properties
            Map<String, String> properties = new HashMap<String, String>(1);
            properties.put(IssuedService.DOCUMENT_PATH_WINDOW_PROPERTY, documentContext.getPath());

            // Menubar item
            String id = "UPDATE_ISSUED_DATE";
            String title = bundle.getString("UPDATE_ISSUED_DATE_MENUBAR_ITEM");
            String icon = "glyphicons glyphicons-calendar";
            MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);
            int order = 0;
            String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, INSTANCE, properties, PortalUrlType.MODAL);

            MenubarItem menubarItem = new MenubarItem(id, title, icon, parent, order, "javascript:;", null, null, null);
            Map<String, String> data = menubarItem.getData();
            data.put("target", "#osivia-modal");
            data.put("load-url", url);

            menubar.add(menubarItem);
        }
    }


    /**
     * Accept document indicator.
     * @param portalControllerContext portal controller context
     * @param documentContext document context
     * @return true if the document is accepted
     */
    private boolean accept(PortalControllerContext portalControllerContext, DocumentContext documentContext) {
        boolean accept;
        
        if (ContextualizationHelper.isCurrentDocContextualized(portalControllerContext) && (documentContext != null)) {
            // Publication infos
            PublicationInfos publicationInfos = documentContext.getPublicationInfos();
            // Permissions
            Permissions permissions = documentContext.getPermissions();
            
            if (!publicationInfos.isLiveSpace() && permissions.isEditable()) {
                if (this.isLive(portalControllerContext, documentContext)) {
                    // Live
                    accept = true;
                } else if (StringUtils.equals(publicationInfos.getPath(), documentContext.getPath())) {
                    // Remote proxy
                    accept = false;
                } else {
                    accept = true;
                }
            } else {
                accept = false;
            }
        } else {
            accept = false;
        }
        
        return accept;
    }


    /**
     * Check if document is live.
     * 
     * @param portalControllerContext portal controller context
     * @param documentContext document context
     * @return true if document is live
     */
    private boolean isLive(PortalControllerContext portalControllerContext, DocumentContext documentContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Current edition state
        EditionState editionState = (EditionState) request.getAttribute("osivia.editionState");
        
        return (editionState != null) && (documentContext != null)
                && StringUtils.equals(EditionState.CONTRIBUTION_MODE_EDITION, editionState.getContributionMode())
                && StringUtils.equals(documentContext.getPath(), editionState.getDocPath());
    }

}
