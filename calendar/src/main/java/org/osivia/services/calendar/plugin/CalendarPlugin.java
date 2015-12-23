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
package org.osivia.services.calendar.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.Plugin;
import org.osivia.portal.api.player.IPlayerModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Add integration in toutatice-cms :
 * Défine Calendar and calendar events, enable calendar to be played with the specific player
 * @author lbillon
 *
 */
@Plugin("calendar.plugin")
public class CalendarPlugin extends AbstractPluginPortlet  {

	private static final String PLUGIN_NAME = "calendar.plugin";

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.CMSCustomizerPortlet#
	 * customizeCMSProperties(java.lang.String,
	 * org.osivia.portal.api.customization.CustomizationContext)
	 */
	@Override
	protected void customizeCMSProperties(String customizationID,
			CustomizationContext context) {

		// Players
        List<IPlayerModule> modules = this.getPlayers(context);

		modules.add(0, new CalendarPlayer(this.getPortletContext()));

		// Doc types
		Map<String, DocumentType> docTypes = this.getDocTypes(context);
        // Agenda
		docTypes.put("Agenda",new DocumentType("Agenda", true, true, false, false, true, true, Arrays.asList("VEVENT"), null, "glyphicons glyphicons-calendar"));
        // Agenda event
		docTypes.put("VEVENT",new DocumentType("VEVENT", false, false, false, false, true, true, new ArrayList<String>(0), null, "glyphicons glyphicons-calendar", false, false));




	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet#getPluginName()
	 */
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}

}
