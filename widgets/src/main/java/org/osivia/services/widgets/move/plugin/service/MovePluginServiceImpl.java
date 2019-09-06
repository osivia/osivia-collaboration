package org.osivia.services.widgets.move.plugin.service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.widgets.move.plugin.model.MoveMenubarModule;
import org.osivia.services.widgets.move.portlet.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Move plugin service implementation.
 *
 * @author Cédric Krommenhoek
 * @see MovePluginService
 */
@Service
public class MovePluginServiceImpl implements MovePluginService {

    /**
     * Move menubar module.
     */
    @Autowired
    private MoveMenubarModule menubarModule;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     */
    public MovePluginServiceImpl() {
        super();
    }


    @Override
    public void customizeMenubarModules(CustomizationContext customizationContext, List<MenubarModule> menubarModules) {
        menubarModules.add(this.menubarModule);
    }


    @Override
    public void updateMenubar(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext) throws PortalException {
        // Move menubar item
        MenubarItem move = null;
        Iterator<MenubarItem> iterator = menubar.iterator();
        while ((move == null) && iterator.hasNext()) {
            MenubarItem item = iterator.next();
            if (StringUtils.equals("MOVE", item.getId())) {
                move = item;
            }
        }

        if (move != null) {
            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

            // Document type
            DocumentType documentType = documentContext.getDocumentType();

            // Window properties
            Map<String, String> properties = new HashMap<>();
            properties.put(MoveService.DOCUMENT_PATH_WINDOW_PROPERTY, documentContext.getCmsPath());
            properties.put(MoveService.BASE_PATH_WINDOW_PROPERTY, nuxeoController.getBasePath());
            properties.put(MoveService.IGNORED_PATHS_WINDOW_PROPERTY, documentContext.getCmsPath());
            if (documentType != null) {
                properties.put(MoveService.ACCEPTED_TYPES_WINDOW_PROPERTY, documentType.getName());
            }

            // URL
            String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, PORTLET_INSTANCE, properties, PortalUrlType.MODAL);

            // Update move menubar item
            move.setUrl("javascript:");
            move.setHtmlClasses(null);
            Map<String, String> data = move.getData();
            data.put("target", "#osivia-modal");
            data.put("load-url", url);
        }
    }

}
