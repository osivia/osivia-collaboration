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
package org.osivia.services.forum.plugin.player;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.lang.StringUtils;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.player.Player;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.core.cms.BinaryDescription;
import org.osivia.portal.core.constants.InternalConstants;
import org.osivia.services.forum.plugin.ForumPlugin;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.portlet.ViewList;

/**
 * Forum player.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoPlayerModule
 */
public class ForumPlayer implements INuxeoPlayerModule {

    /** Forum portlet instance. */
    private static final String FORUM_PORTLET_INSTANCE = "toutatice-portail-cms-nuxeo-viewListPortletInstance";
    /** Forum thread portlet instance. */
    private static final String FORUM_THREAD_PORTLET_INSTANCE = "osivia-services-forum-thread-instance";


    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public ForumPlayer(PortletContext portletContext) {
        super();
    }


    /**
     * Utility method used to create forum player request.
     *
     * @param documentContext document context
     * @return request
     */
    private String createForumPlayerRequest(NuxeoDocumentContext documentContext) {
        NuxeoPublicationInfos navigationInfos = documentContext.getPublicationInfos();

        StringBuilder request = new StringBuilder();
        request.append("ecm:parentId = '").append(navigationInfos.getLiveId()).append("' ");
        request.append("AND ecm:primaryType IN ('Forum', 'Thread') ");
        request.append("ORDER BY ttcth:lastCommentDate DESC, dc:title ASC");
        return request.toString();
    }


    /**
     * Get forum player.
     *
     * @param documentContext Nuxeo document context
     * @return forum player
     */
    private Player getForumPlayer(NuxeoDocumentContext documentContext) {
        Document document = documentContext.getDocument();

        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, document.getPath());
        properties.put("osivia.hideDecorators", "1");
        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
        properties.put("osivia.ajaxLink", "1");
        properties.put(Constants.WINDOW_PROP_SCOPE, documentContext.getScope());
        properties.put(Constants.WINDOW_PROP_VERSION, documentContext.getDocumentState().toString());
        properties.put(ViewList.NUXEO_REQUEST_WINDOW_PROPERTY, this.createForumPlayerRequest(documentContext));
        properties.put(ViewList.TEMPLATE_WINDOW_PROPERTY, ForumPlugin.FORUM_LIST_TEMPLATE);
        properties.put(ViewList.CREATION_PARENT_PATH_WINDOW_PROPERTY, document.getPath());
        properties.put(InternalConstants.PROP_WINDOW_TITLE, document.getTitle());
        properties.put(InternalConstants.PROP_WINDOW_SUB_TITLE, document.getString("dc:description"));

        // Vignette
        PropertyMap vignetteProperties = document.getProperties().getMap("ttc:vignette");
        if ((vignetteProperties != null) && StringUtils.isNotEmpty(vignetteProperties.getString("data"))) {
            properties.put(InternalConstants.PROP_WINDOW_VIGNETTE_DISPLAY, String.valueOf(true));
        }

        // Player
        Player player = new Player();
        player.setWindowProperties(properties);
        player.setPortletInstance(FORUM_PORTLET_INSTANCE);

        return player;
    }


    /**
     * Get forum thread player.
     *
     * @param documentContext Nuxeo document context
     * @return forum thread player
     */
    private Player getForumThreadPlayer(NuxeoDocumentContext documentContext) {
        Document document = documentContext.getDocument();

        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, document.getPath());
        properties.put("osivia.hideDecorators", "1");
        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
        properties.put("osivia.ajaxLink", "1");
        properties.put(InternalConstants.PROP_WINDOW_TITLE, document.getTitle());
        properties.put(InternalConstants.PROP_WINDOW_SUB_TITLE, document.getString("dc:description"));

        // Vignette
        PropertyMap vignetteProperties = document.getProperties().getMap("ttc:vignette");
        if ((vignetteProperties != null) && StringUtils.isNotEmpty(vignetteProperties.getString("data"))) {
            properties.put(InternalConstants.PROP_WINDOW_VIGNETTE_DISPLAY, String.valueOf(true));
        }


        // Player
        Player player = new Player();
        player.setWindowProperties(properties);
        player.setPortletInstance(FORUM_THREAD_PORTLET_INSTANCE);

        return player;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        Document doc = documentContext.getDocument();

        if ("Forum".equals(doc.getType())) {
            return this.getForumPlayer(documentContext);
        } else if ("Thread".equals(doc.getType())) {
            return this.getForumThreadPlayer(documentContext);
        }

        return null;
    }

}
