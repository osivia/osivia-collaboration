package org.osivia.services.workspace.sharing.plugin.model;

import java.util.List;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.workspace.sharing.plugin.service.SharingPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sharing menubar module.
 * 
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
@Component
public class SharingMenubarModule implements MenubarModule {

    /** Plugin service. */
    @Autowired
    private SharingPluginService service;


    /**
     * Constructor.
     */
    public SharingMenubarModule() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        this.service.addMenubarItems(portalControllerContext, menubar, documentContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        this.service.clearMenubarItems(portalControllerContext, menubar, documentContext);
    }

}
