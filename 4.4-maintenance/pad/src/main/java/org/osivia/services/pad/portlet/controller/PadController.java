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
package org.osivia.services.pad.portlet.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.Cookie;

import net.sf.json.JSONObject;

import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.pad.portlet.service.PadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * @author Loïc Billon
 *
 */
@Controller
@RequestMapping("VIEW")
public class PadController extends CMSPortlet implements PortletConfigAware, PortletContextAware { 

	@Autowired
	private PadService service;
	
	@Autowired
	private IBundleFactory bundleFactory;
	
	@Autowired
	private INotificationsService notificationService;
	
    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;
	
    /**
     * Constructor.
     */
    public PadController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }
    
    
    @RenderMapping
	public String view(RenderRequest request, RenderResponse response, @ModelAttribute("pad") PadModel pad) {
    	
    	PortalWindow window = WindowFactory.getWindow(request);
    	
    	PortalControllerContext pcc = new PortalControllerContext(getPortletContext(), request, response);
    	JSONObject obj =  service.joinRequest(pcc,pad.getEditionMode());
    	
    	// Errors are transmitted in the json response
    	if(obj.containsKey("error")) {
    		// Notification
    		Bundle bundle = bundleFactory.getBundle(request.getLocale());
            String message = bundle.getString("PAD_UNAVALIABLE");
            this.notificationService.addSimpleNotification(pcc, message, NotificationsType.ERROR);
    		
    		return "paderror";
    	}
    	
    	if(obj.containsKey("sessionId")) {
        	Cookie cookie = new Cookie("sessionID", obj.getString("sessionId"));
    		response.addProperty(cookie);
    	}

		pad.setUrl(obj.getString("url"));
    	
		// Edition state, default is false then a toggle button is clickable for switching in edition mode.
		Boolean canEdit = Boolean.valueOf(window.getProperty(PadService.PAD_CAN_EDIT));
		
		// Edition mode only avaliable if write permissions is set on the pad.
		if(canEdit) {
			addMenubarOptions(request, response, pad);
		}
    	
		return "view";
	}

    @ModelAttribute("pad")
    public PadModel getPad(PortletRequest request, PortletResponse response) {

		PadModel pad = new PadModel();
		return pad;
    }
    
    /**
     * Switch in read/write mode in a pad.
     * @param request
     * @param response
     * @param pad
     */
    private void addMenubarOptions(RenderRequest request, RenderResponse response, PadModel pad) {
    	
    	// Menubar
        List<MenubarItem> menubar = (List<MenubarItem>) request.getAttribute(Constants.PORTLET_ATTR_MENU_BAR);
        Bundle bundle = bundleFactory.getBundle(request.getLocale());
    	
        
    	PortletURL actionURL = response.createActionURL();
    	actionURL.setParameter(ActionRequest.ACTION_NAME, "switchMode");
    	// Write mode button
    	if(!pad.getEditionMode()) {
    		actionURL.setParameter("editionMode", "true");
            MenubarItem menubarItem = new MenubarItem("ENTER_PAD", bundle.getString("ENTER_PAD"), null, MenubarGroup.SPECIFIC, 1, actionURL.toString(),
                    null, null, null);
            menubar.add(menubarItem);
    	}
    	// Read mode button
    	else {
    		actionURL.setParameter("editionMode", "false");
            MenubarItem menubarItem = new MenubarItem("LEAVE_PAD", bundle.getString("LEAVE_PAD"), null, MenubarGroup.SPECIFIC, 1, actionURL.toString(),
                    null, null, null);
            menubar.add(menubarItem);
    	}

    }
    
    @ActionMapping("switchMode")
    public void switchMode(ActionRequest request, @ModelAttribute("pad") PadModel pad) {
    	Boolean editionMode = Boolean.parseBoolean(request.getParameter("editionMode"));
    	pad.setEditionMode(editionMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
