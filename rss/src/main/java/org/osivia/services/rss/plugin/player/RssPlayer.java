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
 *
 *
 *
 */
package org.osivia.services.rss.plugin.player;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.player.Player;
import org.osivia.services.rss.common.repository.ContainerRepository;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

/**
 * Rss player.
 *
 * @author Frédéric Boudan
 * @author Cédric Krommenhoek
 * @see INuxeoPlayerModule
 */
public class RssPlayer implements INuxeoPlayerModule {

    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public RssPlayer(PortletContext portletContext) {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        // Document
        Document document = documentContext.getDocument();

        Player player = null;
     
        if ("container".equals(document.getType())) {
            Map<String, String> windowProperties = new HashMap<>();
            windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());
            windowProperties.put("osivia.title", document.getTitle());
            windowProperties.put("osivia.hideTitle", "1");
            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.cms.hideMetaDatas", "1");
            windowProperties.put(ContainerRepository.CMS_PATH_WINDOW_PROPERTY, "${contentPath}");

            player = new Player();
            player.setWindowProperties(windowProperties);
            player.setPortletInstance("osivia-services-rss-container-instance");

        } else if ("feed".equals(document.getType())) {
            // Window properties
            Map<String, String> windowProperties = new HashMap<>();
            windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());
            windowProperties.put("osivia.title", document.getTitle());
            windowProperties.put("osivia.ajaxLink", "1");

            player = new Player();
            player.setWindowProperties(windowProperties);
            player.setPortletInstance("osivia-services-rss-feed-instance");

        }       

        return player;
    }

}
