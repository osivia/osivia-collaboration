package org.osivia.services.widgets.rename.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.rename.portlet.model.RenameForm;
import org.osivia.services.widgets.rename.portlet.model.validation.RenameFormValidator;
import org.osivia.services.widgets.rename.portlet.service.RenameService;
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

import javax.portlet.*;
import java.io.IOException;

/**
 * Rename portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class RenameController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private RenameService service;

    /**
     * Rename form validator.
     */
    @Autowired
    private RenameFormValidator validator;


    /**
     * Constructor.
     */
    public RenameController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        return "view";
    }


    /**
     * Save action mapping.
     *
     * @param request       action request
     * @param response      action response
     * @param form          form model attribute
     * @param result        binding result
     * @param sessionStatus session status
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") RenameForm form, BindingResult result,
                     SessionStatus sessionStatus) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            sessionStatus.setComplete();

            this.service.save(portalControllerContext, form);
        }
    }


    /**
     * Get rename form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public RenameForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
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
