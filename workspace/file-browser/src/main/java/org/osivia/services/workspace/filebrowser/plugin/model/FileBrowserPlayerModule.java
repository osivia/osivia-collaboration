package org.osivia.services.workspace.filebrowser.plugin.model;

import org.osivia.portal.api.player.Player;
import org.osivia.services.workspace.filebrowser.plugin.service.FileBrowserPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

/**
 * File browser player module.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoPlayerModule
 */
@Component
public class FileBrowserPlayerModule implements INuxeoPlayerModule {

    /** Plugin service. */
    @Autowired
    private FileBrowserPluginService service;


    /**
     * Constructor.
     */
    public FileBrowserPlayerModule() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        return this.service.getFileBrowserPlayer(documentContext);
    }

}
