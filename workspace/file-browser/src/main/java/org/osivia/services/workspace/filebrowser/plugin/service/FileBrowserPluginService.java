package org.osivia.services.workspace.filebrowser.plugin.service;

import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.portal.api.player.Player;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * File browser plugin service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface FileBrowserPluginService {

    /** Plugin name. */
    String PLUGIN_NAME = "file-browser.plugin";

    /** File browser portlet instance. */
    String FILE_BROWSER_INSTANCE = "osivia-services-workspace-file-browser-instance";


    /**
     * Customize player modules.
     * 
     * @param customizationContext customization context
     * @param modules player modules
     */
    void customizePlayerModules(CustomizationContext customizationContext, @SuppressWarnings("rawtypes") List<IPlayerModule> modules);


    /**
     * Get file browser player.
     * 
     * @param documentContext Nuxeo document context
     * @return player, or null if the document doesn't match
     */
    Player getFileBrowserPlayer(NuxeoDocumentContext documentContext);


    /**
     * Get file browser window properties.
     * 
     * @param document document
     * @return window properties
     */
    Map<String, String> getFileBrowserWindowProperties(Document document);

}
