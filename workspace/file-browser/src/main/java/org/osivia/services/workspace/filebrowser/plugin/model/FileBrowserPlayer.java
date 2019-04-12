package org.osivia.services.workspace.filebrowser.plugin.model;

import java.util.Map;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.player.Player;
import org.osivia.services.workspace.filebrowser.plugin.service.FileBrowserPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser player.
 * 
 * @author Cédric Krommenhoek
 * @see Player
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserPlayer extends Player {

    /** Plugin service. */
    @Autowired
    private FileBrowserPluginService service;


    /** Nuxeo document. */
    private final Document document;


    /**
     * Constructor.
     * 
     * @param document Nuxeo document
     */
    public FileBrowserPlayer(Document document) {
        super();
        this.document = document;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getPortletInstance() {
        return FileBrowserPluginService.FILE_BROWSER_INSTANCE;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getWindowProperties() {
        return this.service.getFileBrowserWindowProperties(this.document);
    }

}
