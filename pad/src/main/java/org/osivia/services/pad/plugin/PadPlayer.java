/*
 * (C) Copyright 2016 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
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
package org.osivia.services.pad.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.player.Player;
import org.osivia.services.pad.portlet.service.PadService;

import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.plugin.PluginModule;

/**
 * @author Loïc Billon
 *
 */
public class PadPlayer extends PluginModule implements INuxeoPlayerModule {

	/**
	 * @param portletContext
	 */
	public PadPlayer(PortletContext portletContext) {
		super(portletContext);
	}

	/* (non-Javadoc)
	 * @see org.osivia.portal.api.player.IPlayerModule#getCMSPlayer(org.osivia.portal.api.cms.DocumentContext)
	 */
	@Override
	public Player getCMSPlayer(DocumentContext<Document> docCtx) {
				
        // Document
        Document document = docCtx.getDoc();

        if(docCtx.getType().getName().equals(PadPlugin.TOUTATICE_PAD)) {    
            
            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(PadService.PAD_REF, document.getPath());
            
            BasicPermissions permissions = docCtx.getPermissions(BasicPermissions.class);
            windowProperties.put(PadService.PAD_CAN_EDIT, Boolean.toString(permissions.isEditableByUser()));
            
            windowProperties.put("osivia.title", document.getTitle());
            
            Player player = new Player();
            player.setWindowProperties(windowProperties);
            
            player.setPortletInstance("osivia-services-pad-instance");

            return player;
        }

		return null;
	}

}
