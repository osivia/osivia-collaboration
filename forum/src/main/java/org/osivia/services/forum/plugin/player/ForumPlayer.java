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
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.player.Player;
import org.osivia.services.forum.plugin.ForumPlugin;

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
     * @param cmsContext CMS context
     * @param cmsService the cms service
     * @return request
     */
	private String createForumPlayerRequest(DocumentContext<Document> docCtx) {
		BasicPublicationInfos navigationInfos = docCtx.getPublicationInfos(BasicPublicationInfos.class);

        StringBuilder request = new StringBuilder();
        request.append("ecm:parentId = '").append(navigationInfos.getLiveId()).append("' ");
        request.append("AND ecm:primaryType IN ('Forum', 'Thread') ");
        request.append("ORDER BY ttcth:lastCommentDate DESC, dc:title ASC");
        return request.toString();
	}



    /**
     * Get forum player.
     *
     * @param ctx CMS context
     * @param cmsService the cms service
     * @return CMS forum player
     */
	private Player getForumPlayer(DocumentContext<Document> docCtx) {
		BasicPublicationInfos navigationInfos = docCtx.getPublicationInfos(BasicPublicationInfos.class);

        Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.nuxeoRequest", this.createForumPlayerRequest(docCtx));
        windowProperties.put("osivia.cms.style", ForumPlugin.FORUM_LIST_TEMPLATE);
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("theme.dyna.partial_refresh_enabled", "false");
        windowProperties.put(Constants.WINDOW_PROP_SCOPE, navigationInfos.getScope());
        windowProperties.put("osivia.ajaxLink", "1");
        windowProperties.put(Constants.WINDOW_PROP_VERSION, navigationInfos.getState().toString());
        if (docCtx.getDoc() != null) {
            windowProperties.put(ViewList.CREATION_PARENT_PATH_WINDOW_PROPERTY, docCtx.getDoc().getPath());
        }

        Player linkProps = new Player();
        linkProps.setWindowProperties(windowProperties);
        linkProps.setPortletInstance("toutatice-portail-cms-nuxeo-viewListPortletInstance");

        return linkProps;
	}


	/**
     * Get forum thread player.
     *
     * @param cmsContext CMS context
     * @return forum thread player
     */
	private Player getForumThreadPlayer(DocumentContext<Document> docCtx) {
	        Document document = docCtx.getDoc();

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
	public Player getCMSPlayer(DocumentContext<Document> docCtx) {
        Document doc = docCtx.getDoc();

        if ("Forum".equals(doc.getType())) {
            return this.getForumPlayer(docCtx);
        }

        if ("Thread".equals(doc.getType())) {
            return this.getForumThreadPlayer(docCtx);
        }

        return null;
	}

}
