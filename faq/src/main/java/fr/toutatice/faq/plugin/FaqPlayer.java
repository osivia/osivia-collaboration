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
import org.osivia.portal.api.player.Player;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

/**
 * FAQ player.
 * 
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see INuxeoPlayerModule
 */
public class FaqPlayer implements INuxeoPlayerModule {

    /**
     * Constructor.
     */
    public FaqPlayer() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        Document document = documentContext.getDocument();

        if ("FaqFolder".equals(document.getType()) || "Question".equals(document.getType())) {
            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());
            windowProperties.put("osivia.hideDecorators", "1");
            Player linkProps = new Player();
            linkProps.setWindowProperties(windowProperties);
            linkProps.setPortletInstance("osivia-services-faqInstance");

            return linkProps;
        } else {
            return null;
        }
    }

}
