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

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author Loïc Billon
 *
 */
public class JoinRequestCommand implements INuxeoCommand {

	String path;
	
	Boolean editionEnabled;
	
	public JoinRequestCommand(String path, Boolean editionEnabled) {
		super();
		this.path = path;
		this.editionEnabled = editionEnabled;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		OperationRequest request = nuxeoSession.newRequest("Document.ToutapadJoinRequest");
		request.set("path", path);
		request.set("editionEnabled", editionEnabled);
				
		Blob infosAsBlob = (Blob) request.execute();
		
		if (infosAsBlob != null) {
			
			String infosContentStr = IOUtils.toString(infosAsBlob.getStream(), "UTF-8");
			return JSONObject.fromObject(infosContentStr);
            
		}
		
		return null;
            		
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {

		return null;
	}

}
