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
 * @author lbillon
 *
 */
@Plugin("faq.plugin")
public class FaqPlugin extends AbstractPluginPortlet{

	private static final String PLUGIN_NAME = "faq.plugin";

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.CMSCustomizerPortlet#customizeCMSProperties(java.lang.String, org.osivia.portal.api.customization.CustomizationContext)
	 */
	@Override
	protected void customizeCMSProperties(String customizationID,
			CustomizationContext context) {

		Map<String, DocumentType> docTypes = getDocTypes(context);
		// ==== doc types
        // FAQ folder
		docTypes.put("FaqFolder", new DocumentType("FaqFolder", true, false, false, true, false, true, Arrays.asList("Question"), null,
                "glyphicons glyphicons-question-sign"));
        // FAQ question
		docTypes.put("Question", new DocumentType("Question", false, false, false, false, false, true, new ArrayList<String>(0), null,
                "glyphicons glyphicons-question-sign"));
		
		
		// ==== players
		List<IPlayerModule> players = getPlayers(context);
		players.add(new FaqPlayer());
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet#getPluginName()
	 */
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}

}
