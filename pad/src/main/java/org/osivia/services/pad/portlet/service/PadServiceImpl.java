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
package org.osivia.services.pad.portlet.service;

import net.sf.json.JSONObject;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * @author Loïc Billon
 *
 */
@Service
public class PadServiceImpl implements PadService {


	/* (non-Javadoc)
	 * @see org.osivia.services.pad.portlet.service.PadService#joinRequest(org.osivia.portal.api.context.PortalControllerContext, java.lang.String)
	 */
	@Override
	public JSONObject joinRequest(PortalControllerContext pcc, Boolean editionEnabled) {

		NuxeoController controller = new NuxeoController(pcc);
		
		// Récupération du sessionID
        PortalWindow window = WindowFactory.getWindow(pcc.getRequest());
		String padPath = window.getProperty(PAD_REF);
		
		JSONObject ret = (JSONObject) controller.executeNuxeoCommand(new JoinRequestCommand(padPath, editionEnabled));

		// Contextualisation
		NuxeoDocumentContext documentContext = controller.getDocumentContext(padPath);
		controller.setCurrentDoc(documentContext.getDocument());
		controller.insertContentMenuBarItems();
		
		return ret;
		
	}

}
