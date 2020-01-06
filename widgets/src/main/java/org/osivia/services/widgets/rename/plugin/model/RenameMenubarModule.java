package org.osivia.services.widgets.rename.plugin.model;

import java.util.List;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.widgets.rename.plugin.service.RenamePluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Rename menubar module.
 * 
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
@Component
public class RenameMenubarModule implements MenubarModule {

    /** Plugin service. */
    @Autowired
    private RenamePluginService service;


    /**
     * Constructor.
     */
    public RenameMenubarModule() {
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
