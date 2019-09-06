package org.osivia.services.widgets.move.plugin.model;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.widgets.move.plugin.service.MovePluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Move menubar module.
 *
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
@Component
public class MoveMenubarModule implements MenubarModule {

    /**
     * Plugin service.
     */
    @Autowired
    private MovePluginService service;


    /**
     * Constructor.
     */
    public MoveMenubarModule() {
        super();
    }


    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext spaceDocumentContext) {
        // Do nothing
    }


    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext) throws PortalException {
        this.service.updateMenubar(portalControllerContext, menubar, documentContext);
    }

}
