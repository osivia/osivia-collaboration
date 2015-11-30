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
 */
package org.osivia.services.widgets.plugin;

import java.io.IOException;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;

import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;

/**
 * Slider template module.
 *
 * @author david chevrier
 * @see PortletModule
 */
public class SliderTemplateModule extends PortletModule {

    /** Type of document displayed in slider. */
    public static final String SLIDER_DOC_TYPE = "docType";
    /** Temporisation of slider */
    public static final String SLIDER_TIMER = "timer";


    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public SliderTemplateModule(PortletContext portletContext) {
        super(portletContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        request.setAttribute(SLIDER_TIMER, window.getProperty(SLIDER_TIMER));
    }

}
