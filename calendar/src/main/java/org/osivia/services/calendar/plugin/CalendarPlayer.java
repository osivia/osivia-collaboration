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

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import org.apache.commons.lang.BooleanUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.PublicationInfos;
import org.osivia.portal.api.player.Player;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository;


/**
 * Calendar player.
 *
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see INuxeoPlayerModule
 */
public class CalendarPlayer implements INuxeoPlayerModule {

    /**
     * Constructor.
     *
     * @param context portlet context
     */
    public CalendarPlayer(PortletContext context) {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        // Document
        Document document = documentContext.getDocument();
        // Publication infos
        NuxeoPublicationInfos publicationInfos = documentContext.getPublicationInfos();

        if ("Agenda".equals(document.getType())) {
            Map<String, String> windowProperties = new HashMap<>();
            windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());
            windowProperties.put("osivia.title", document.getTitle());
            windowProperties.put("osivia.hideTitle", "1");
            windowProperties.put("osivia.cms.hideMetaDatas", "1");
            windowProperties.put(CalendarViewRepository.CMS_PATH_WINDOW_PROPERTY, "${contentPath}");
            windowProperties.put(CalendarViewRepository.READ_ONLY_WINDOW_PROPERTY, String.valueOf(!publicationInfos.isLiveSpace()));

            Player props = new Player();
            props.setWindowProperties(windowProperties);
            props.setPortletInstance("osivia-services-calendar-instance");

            return props;
        } else if ("VEVENT".equals(document.getType())) {
            // Window properties
            Map<String, String> windowProperties = new HashMap<>();
            windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());
            windowProperties.put("osivia.title", document.getTitle());

            Player props = new Player();
            props.setWindowProperties(windowProperties);
            props.setPortletInstance("osivia-services-calendar-event-view-instance");

            return props;
        } else {
            return null;
        }
    }

}
