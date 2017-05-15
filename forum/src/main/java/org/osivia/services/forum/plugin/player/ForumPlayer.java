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

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.player.Player;
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
     * @param documentContext document context
     * @return CMS forum player
     */
    private Player getForumPlayer(NuxeoDocumentContext documentContext) {
        Document document = documentContext.getDocument();

        Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.nuxeoRequest", this.createForumPlayerRequest(documentContext));
        windowProperties.put("osivia.cms.style", ForumPlugin.FORUM_LIST_TEMPLATE);
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("theme.dyna.partial_refresh_enabled", "false");
        windowProperties.put(Constants.WINDOW_PROP_SCOPE, documentContext.getScope());
        windowProperties.put("osivia.ajaxLink", "1");
        windowProperties.put(Constants.WINDOW_PROP_VERSION, documentContext.getDocumentState().toString());
        windowProperties.put(ViewList.CREATION_PARENT_PATH_WINDOW_PROPERTY, document.getPath());

        Player linkProps = new Player();
        linkProps.setWindowProperties(windowProperties);
        linkProps.setPortletInstance("toutatice-portail-cms-nuxeo-viewListPortletInstance");

        return linkProps;
    }


    /**
     * Get forum thread player.
     *
     * @param documentContext document context
     * @return forum thread player
     */
    private Player getForumThreadPlayer(NuxeoDocumentContext documentContext) {
        Document document = documentContext.getDocument();

        Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("osivia.ajaxLink", "1");

        Player linkProps = new Player();
        linkProps.setWindowProperties(windowProperties);
        linkProps.setPortletInstance("osivia-services-forum-portletInstance");

        return linkProps;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        Document doc = documentContext.getDocument();

        if ("Forum".equals(doc.getType())) {
            return this.getForumPlayer(documentContext);
        }

        if ("Thread".equals(doc.getType())) {
            return this.getForumThreadPlayer(documentContext);
        }

        return null;
    }

}
