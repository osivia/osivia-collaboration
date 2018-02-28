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
package fr.toutatice.faq.plugin;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.player.Player;

import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

/**
 * @author lbillon
 *
 */
public class FaqPlayer implements INuxeoPlayerModule {


	/* (non-Javadoc)
	 * @see org.osivia.portal.api.cms.IPlayerModule#getCMSPlayer(org.osivia.portal.api.cms.DocumentContext)
	 */
	@Override
	public Player getCMSPlayer(DocumentContext<Document> docCtx) {
		Document doc = docCtx.getDoc();
		
        if ("FaqFolder".equals(doc.getType()) || "Question".equals(doc.getType())) {
        	
            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, doc.getPath());
            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.hideDecorators", "1");
            Player linkProps = new Player();
            linkProps.setWindowProperties(windowProperties);
            linkProps.setPortletInstance("osivia-services-faqInstance");

            return linkProps;
        }
        else return null;
	}

}
