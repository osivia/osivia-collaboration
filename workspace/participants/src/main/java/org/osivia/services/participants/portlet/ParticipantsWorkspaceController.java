/*
 * (C) Copyright 2016 OSIVIA (http://www.osivia.com) 
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
package org.osivia.services.participants.portlet;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

@Controller
@RequestMapping("VIEW")
@SessionAttributes({"members"})
public class ParticipantsWorkspaceController implements ApplicationContextAware, PortletContextAware {


    /** Portlet context. */
    private PortletContext portletContext;
    
    @Autowired
    private WorkspaceService service;

    @RenderMapping
    public String view() {
    	// TODO LBI 
    	// - traiter cas d'erreur
    	// - traiter limitations de résultats
    	
    	return "participants";
    }
    
    /**
     * Get members form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @param options options model attribute
     * @return form
     * @throws PortletException
     * @throws PortalException 
     */
    @ModelAttribute("members")
    public List<WorkspaceMember> getMembersForm(PortletRequest request, PortletResponse response) throws PortletException, PortalException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Base path
        String basePath = nuxeoController.getBasePath();
        
        List<WorkspaceMember> allMembers = new ArrayList<WorkspaceMember>();
        
        if(basePath != null) {
            Document workspace = nuxeoController.fetchDocument(basePath);
            allMembers = service.getAllMembers(portalControllerContext, workspace.getProperties().getString("webc:url"));
        }
        
        
        return allMembers;
    }
    
	
	@Override
	public void setPortletContext(PortletContext portletContext) {
		this.portletContext = portletContext;
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ApplicationContextProvider.setApplicationContext(applicationContext);
		
	}

	
}
