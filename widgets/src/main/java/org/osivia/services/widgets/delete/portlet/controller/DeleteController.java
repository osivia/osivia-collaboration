package org.osivia.services.widgets.delete.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.delete.portlet.model.DeleteForm;
import org.osivia.services.widgets.delete.portlet.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;
import java.io.IOException;

/**
 * Delete portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class DeleteController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private DeleteService service;


    /**
     * Constructor.
     */
    public DeleteController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }


    /**
     * Delete action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     form model attribute
     */
    @ActionMapping("delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute("form") DeleteForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.delete(portalControllerContext, form);
    }


    /**
     * Get form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public DeleteForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }

}
