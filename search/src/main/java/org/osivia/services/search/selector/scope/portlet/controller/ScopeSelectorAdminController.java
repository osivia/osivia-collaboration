package org.osivia.services.search.selector.scope.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.search.selector.scope.portlet.model.ScopeSelectorSettings;
import org.osivia.services.search.selector.scope.portlet.service.ScopeSelectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Scope selector portlet administration controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("ADMIN")
public class ScopeSelectorAdminController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private ScopeSelectorService service;


    /**
     * Constructor.
     */
    public ScopeSelectorAdminController() {
        super();
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "admin";
    }


    /**
     * Save portlet settings action mapping.
     * @param request action request
     * @param response action response
     * @param settings portlet settings model attribute
     * @throws PortletException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("settings") ScopeSelectorSettings settings) throws PortletException {
     // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        this.service.save(portalControllerContext, settings);
        
        response.setWindowState(WindowState.NORMAL);
        response.setPortletMode(PortletMode.VIEW);
    }


    /**
     * Get portlet settings model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return portlet settings
     * @throws PortletException
     */
    @ModelAttribute("settings")
    public ScopeSelectorSettings getSettings(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        return this.service.getSettings(portalControllerContext);
    }

}
