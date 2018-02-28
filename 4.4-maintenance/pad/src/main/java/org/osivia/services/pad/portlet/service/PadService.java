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

/**
 * @author Loïc Billon
 *
 */
public interface PadService {

	/** Path of the document */
	public static final String PAD_REF = "osivia.service.pad.document";
	
	/** Used to switch between read and write mode */
	public static final String PAD_CAN_EDIT = "osivia.service.pad.canEdit";
	
	/**
	 * Call nuxeo to join a pad
	 * @param pcc (contains pad path)
	 * @param editionEnabled edition mode is enabled or not
	 * @return JSonObject with url of the pad, sessionId or errors in case of.
	 */
	public JSONObject joinRequest(PortalControllerContext pcc, Boolean editionEnabled);
	
	
}
