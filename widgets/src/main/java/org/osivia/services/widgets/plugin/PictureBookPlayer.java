/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.osivia.services.widgets.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.player.Player;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.constants.InternalConstants;

import fr.toutatice.portail.cms.nuxeo.api.FileBrowserView;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.plugin.PluginModule;


/**
 * Picture book player.
 *
 * @author Jean-Sébastien Steux
 * @see PluginModule
 * @see INuxeoPlayerModule
 */
public class PictureBookPlayer extends PluginModule implements INuxeoPlayerModule {

    /**
     * Instantiates a new player module.
     *
     * @param portletContext the portlet context
     */
    public PictureBookPlayer(PortletContext portletContext) {
        super(portletContext);

    }


    /**
     * Get picturebook player.
     *
     * @param documentContext CMS context
     * @return player properties
     * @throws CMSException
     */
    public Player getCMSPictureBookPlayer(DocumentContext<Document> documentContext) {
        // Document
        Document doc = documentContext.getDoc();
        // Publication infos
        BasicPublicationInfos navigationInfos = documentContext.getPublicationInfos(BasicPublicationInfos.class);

        Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.title", doc.getTitle());
        windowProperties.put("osivia.cms.uri", doc.getPath());
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("osivia.ajaxLink", "1");
        windowProperties.put("osivia.cms.displayLiveVersion", navigationInfos.getState().toString());
        windowProperties.put("osivia.cms.style", WidgetsPlugin.STYLE_PICTUREBOOK);
        windowProperties.put("osivia.nuxeoRequest", NuxeoController.createFolderRequest(documentContext, false));
        windowProperties.put("osivia.cms.pageSize", "24");
        windowProperties.put("osivia.cms.pageSizeMax", "96");
        windowProperties.put("osivia.cms.maxItems", "96");

        Player player = new Player();
        player.setWindowProperties(windowProperties);
        player.setPortletInstance("toutatice-portail-cms-nuxeo-viewListPortletInstance");

        return player;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(DocumentContext<Document> documentContext) {
        // Document
        Document document = documentContext.getDoc();

        if ("PictureBook".equals(document.getType())) {
            // Publication infos
            BasicPublicationInfos navigationInfos = documentContext.getPublicationInfos(BasicPublicationInfos.class);
            // Workspace indicator
            boolean workspace = (navigationInfos.isContextualized() && navigationInfos.isLiveSpace());

            if (workspace) {
                // File browser
                Player properties = this.getNuxeoCustomizer().getCMSFileBrowser(documentContext);
                Map<String, String> windowProperties = properties.getWindowProperties();
                windowProperties.put(InternalConstants.PROP_WINDOW_TITLE, document.getTitle());
                windowProperties.put(InternalConstants.DEFAULT_VIEW_WINDOW_PROPERTY, FileBrowserView.THUMBNAILS.getName());
                return properties;
            } else {
                // Picture book
                return this.getCMSPictureBookPlayer(documentContext);
            }
        }

        return null;
    }

}
