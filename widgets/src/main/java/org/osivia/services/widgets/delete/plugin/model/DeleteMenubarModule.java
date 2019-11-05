package org.osivia.services.widgets.delete.plugin.model;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.widgets.delete.plugin.service.DeletePluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Delete menubar module.
 *
 * @author Cédric Krommenhoek
 */
@Component
public class DeleteMenubarModule implements MenubarModule {

    /**
     * Plugin service.
     */
    @Autowired
    private DeletePluginService service;


    /**
     * Constructor.
     */
    public DeleteMenubarModule() {
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
