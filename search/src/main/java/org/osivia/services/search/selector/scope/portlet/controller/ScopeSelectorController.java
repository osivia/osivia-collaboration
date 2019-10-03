package org.osivia.services.search.selector.scope.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.search.selector.scope.portlet.model.ScopeSelectorForm;
import org.osivia.services.search.selector.scope.portlet.service.ScopeSelectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Scope selector portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class ScopeSelectorController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private ScopeSelectorService service;


    /**
     * Constructor.
     */
    public ScopeSelectorController() {
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
        return "view";
    }


    /**
     * Select action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping("select")
    public void select(ActionRequest request, ActionResponse response, @ModelAttribute("form") ScopeSelectorForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.select(portalControllerContext, form);
    }
    

    /**
     * Get form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public ScopeSelectorForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }

}
