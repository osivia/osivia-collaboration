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

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.player.Player;
import org.osivia.services.pad.portlet.service.PadService;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.plugin.PluginModule;

/**
 * PAD player.
 * 
 * @author Loïc Billon
 * @see PluginModule
 * @see INuxeoPlayerModule
 */
public class PadPlayer extends PluginModule implements INuxeoPlayerModule {

	/**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
	public PadPlayer(PortletContext portletContext) {
		super(portletContext);
	}


    /**
     * {@inheritDoc}
     */
	@Override
	public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        // Document type
        DocumentType documentType = documentContext.getDocumentType();

        // Player
        Player player;

        if ((documentType != null) && StringUtils.equals(documentType.getName(), PadPlugin.TOUTATICE_PAD)) {
            // Document
            Document document = documentContext.getDocument();

            // Permissions
            NuxeoPermissions permissions = documentContext.getPermissions();

            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(PadService.PAD_REF, document.getPath());
            windowProperties.put(PadService.PAD_CAN_EDIT, Boolean.toString(permissions.isEditable()));
            windowProperties.put("osivia.title", document.getTitle());
            
            player = new Player();
            player.setWindowProperties(windowProperties);
            player.setPortletInstance("osivia-services-pad-instance");
        } else {
            player = null;
        }

        return player;
	}

}
