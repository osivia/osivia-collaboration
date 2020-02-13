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
package org.osivia.services.rss.plugin;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.Plugin;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.services.rss.common.repository.ContainerRepository;
import org.osivia.services.rss.plugin.player.RssPlayer;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;


/**
 * Rss plugin 
 *
 * @author Frédéric Boudan
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
@Plugin("rss.plugin")
public class RssPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "rss.plugin";

    /**
     * Constructor.
     */
    public RssPlugin() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(CustomizationContext context) {
        // Document types
        this.customizeDocumentTypes(context);
        // Players
        this.customizePlayers(context);
    }

    /**
     * Customize document types.
     *
     * @param context customization context
     */
    private void customizeDocumentTypes(CustomizationContext context) {
        // Document types
        Map<String, DocumentType> types = this.getDocTypes(context);

        // Rss
        DocumentType rss = DocumentType.createNode(ContainerRepository.DOCUMENT_TYPE_CONTENEUR);
        rss.setIcon("glyphicons glyphicon-folder-open");
        rss.setForceContextualization(true);
        rss.setEditable(true);
        rss.setMovable(true);
        types.put(ContainerRepository.DOCUMENT_TYPE_CONTENEUR, rss);
    }

    /**
     * Customize players.
     *
     * @param context customize players
     */
    private void customizePlayers(CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = this.getPortletContext();

        // Players
        @SuppressWarnings("rawtypes")
        List<IPlayerModule> players = this.getPlayers(context);

        // RSS container
        RssPlayer rssContainer = new RssPlayer(portletContext);
        players.add(rssContainer);
    }

}
