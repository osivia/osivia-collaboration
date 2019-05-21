package org.osivia.services.widgets.move.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.move.portlet.model.MoveForm;
import org.osivia.services.widgets.move.portlet.model.validation.MoveFormValidator;
import org.osivia.services.widgets.move.portlet.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Move portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class MoveController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private MoveService service;

    /**
     * Move form validator.
     */
    @Autowired
    private MoveFormValidator validator;


    /**
     * Constructor.
     */
    public MoveController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @return view path
     */
    @RenderMapping
    public String view() {
        return "view";
    }


    /**
     * Move action mapping.
     *
     * @param request       action request
     * @param response      action response
     * @param form          move form model attribute
     * @param result        binding result
     * @param sessionStatus session status
     */
    @ActionMapping("move")
    public void move(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") MoveForm form, BindingResult result, SessionStatus sessionStatus) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            sessionStatus.setComplete();

            this.service.move(portalControllerContext, form);
        }
    }


    /**
     * Browse resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     */
    @ResourceMapping("browse")
    public void browse(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Browse
        String data = this.service.browse(portalControllerContext);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(data);
        printWriter.close();
    }


    /**
     * Get move form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public MoveForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Form init binder.
     *
     * @param binder portlet request data binder
     */
    @InitBinder("form")
    public void formInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.validator);
    }

}
