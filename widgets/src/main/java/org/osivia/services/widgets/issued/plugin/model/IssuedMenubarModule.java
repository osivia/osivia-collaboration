package org.osivia.services.widgets.issued.plugin.model;

import java.util.List;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.widgets.issued.plugin.service.IssuedPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Issued date menubar module.
 * 
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
@Component
public class IssuedMenubarModule implements MenubarModule {

    /** Plugin service. */
    @Autowired
    private IssuedPluginService service;


    /**
     * Constructor.
     */
    public IssuedMenubarModule() {
        super();
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
        this.service.addMenubarItem(portalControllerContext, menubar, documentContext);
    }

}
