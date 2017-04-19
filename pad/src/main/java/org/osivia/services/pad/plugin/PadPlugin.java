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
 */
package org.osivia.services.pad.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.Plugin;
import org.osivia.portal.api.player.IPlayerModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Add integration in toutatice-cms: define pads.
 *
 * @author Loïc Billon
 * @see AbstractPluginPortlet
 */
@Plugin("pad.plugin")
public class PadPlugin extends AbstractPluginPortlet  {

    /**
	 * Nx type
	 */
	public static final String TOUTATICE_PAD = "ToutaticePad";
	
	
	/** Plugin name. */
	private static final String PLUGIN_NAME = "pad.plugin";


    /**
     * Constructor.
     */
    public PadPlugin() {
        super();
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
        
        // ToutaticePad
        DocumentType pad = new DocumentType(TOUTATICE_PAD, false, false, false, false, true, true, new ArrayList<String>(0), null, "glyphicons glyphicons-blackboard");
        types.put(pad.getName(), pad);
        this.addSubType(context, "Folder", pad.getName());
        
        
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

        // Calendar player
        PadPlayer padPlayer = new PadPlayer(portletContext);
        players.add(padPlayer);
    }


    /**
     * {@inheritDoc}
     */
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}

}
