package org.osivia.services.widgets.delete.plugin.service;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.widgets.delete.plugin.model.DeleteMenubarModule;
import org.osivia.services.widgets.delete.portlet.service.DeleteService;
import org.osivia.services.widgets.move.portlet.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Delete plugin service implementation.
 *
 * @author Cédric Krommenhoek
 * @see DeletePluginService
 */
@Service
public class DeletePluginServiceImpl implements DeletePluginService {

    /**
     * Delete menubar module.
     */
    @Autowired
    private DeleteMenubarModule menubarModule;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     */
    public DeletePluginServiceImpl() {
        super();
    }


    @Override
    public void customizeMenubarModules(CustomizationContext customizationContext, List<MenubarModule> menubarModules) {
        menubarModules.add(this.menubarModule);
    }


    @Override
    public void updateMenubar(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext) throws PortalException {
        // Delete menubar item
        MenubarItem delete = null;
        Iterator<MenubarItem> iterator = menubar.iterator();
        while ((delete == null) && iterator.hasNext()) {
            MenubarItem item = iterator.next();
            if (StringUtils.equals("DELETE", item.getId())) {
                delete = item;
            }
        }

        if (delete != null) {
            // Window properties
            Map<String, String> properties = new HashMap<>();
            properties.put(DeleteService.DOCUMENT_PATH_WINDOW_PROPERTY, documentContext.getCmsPath());

            // URL
            String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, DeleteService.PORTLET_INSTANCE, properties, PortalUrlType.MODAL);

            // Update move menubar item
            delete.setUrl("javascript:");
            delete.setHtmlClasses(null);
            delete.setAssociatedHTML(null);
            Map<String, String> data = delete.getData();
            data.remove("fancybox");
            data.remove("src");
            data.put("target", "#osivia-modal");
            data.put("load-url", url);
            data.put("title", delete.getTitle());
        }
    }

}
