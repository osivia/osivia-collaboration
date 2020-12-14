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

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.player.Player;
import org.osivia.services.calendar.portlet.service.ICalendarService;

import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;


/**
 * @author lbillon
 *
 */
public class CalendarPlayer implements INuxeoPlayerModule {

	/**
	 * 
	 */
	public CalendarPlayer(PortletContext context) {

	}
	
	/* (non-Javadoc)
	 * @see org.osivia.portal.api.cms.IPlayerModule#getCMSPlayer(org.osivia.portal.api.cms.DocumentContext)
	 */
	@Override
	public Player getCMSPlayer(DocumentContext<Document> docCtx) {
        // Document
        Document document = docCtx.getDoc();
        
        if ("Agenda".equals(document.getType())) {
        	
            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());
            windowProperties.put("osivia.title", document.getTitle());
            windowProperties.put("osivia.hideTitle", "1");
            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.cms.hideMetaDatas", "1");
            windowProperties.put("osivia.calendar.cmsPath", "${contentPath}");
            windowProperties.put(ICalendarService.PERIOD_TYPE_PARAMETER , "month");

            Player props = new Player();
            props.setWindowProperties(windowProperties);
            props.setPortletInstance("osivia-services-calendar-instance");

            return props;
        }
        else if ("VEVENT".equals(document.getType())) {
            // Window properties
            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put("osivia.document.dispatch.jsp", "calendar-event");
            windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());

            Player props = new Player();
            props.setWindowProperties(windowProperties);
            props.setPortletInstance("toutatice-portail-cms-nuxeo-viewDocumentPortletInstance");

            return props;
        }
        else {
        	return null;
        }
	}
	
}
