package org.osivia.services.workspace.filebrowser.plugin.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.portal.api.player.Player;
import org.osivia.portal.core.constants.InternalConstants;
import org.osivia.services.workspace.filebrowser.plugin.model.FileBrowserPlayer;
import org.osivia.services.workspace.filebrowser.plugin.model.FileBrowserPlayerModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * File browser plugin service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see FileBrowserPluginService
 */
@Service
public class FileBrowserPluginServiceImpl implements FileBrowserPluginService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** File browser player module. */
    @Autowired
    private FileBrowserPlayerModule fileBrowserPlayerModule;


    /** File browser types. */
    private final List<String> fileBrowserTypes;


    /**
     * Constructor.
     */
    public FileBrowserPluginServiceImpl() {
        super();

        // File browser types
        this.fileBrowserTypes = Arrays.asList("Folder", "OrderedFolder");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizePlayerModules(CustomizationContext customizationContext, @SuppressWarnings("rawtypes") List<IPlayerModule> players) {
        players.add(this.fileBrowserPlayerModule);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getFileBrowserPlayer(NuxeoDocumentContext documentContext) {
        // Document
        Document document = documentContext.getDocument();

        // Player
        Player player;

        if ((document != null) && this.fileBrowserTypes.contains(document.getType())) {
            player = this.applicationContext.getBean(FileBrowserPlayer.class, document);
        } else {
            player = null;
        }

        return player;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getFileBrowserWindowProperties(Document document) {
        Map<String, String> properties = new HashMap<>();

        // Decorators
        properties.put("osivia.hideDecorators", "1");

        // AJAX
        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
        properties.put("osivia.ajaxLink", "1");

        if (document != null) {
            // Path
            properties.put(Constants.WINDOW_PROP_URI, document.getPath());
            // Title
            properties.put(InternalConstants.PROP_WINDOW_TITLE, document.getTitle());
        }

        return properties;
    }

}
