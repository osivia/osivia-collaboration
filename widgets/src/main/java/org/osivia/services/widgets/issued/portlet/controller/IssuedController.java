package org.osivia.services.widgets.issued.portlet.controller;

import java.io.IOException;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.issued.portlet.model.IssuedForm;
import org.osivia.services.widgets.issued.portlet.model.converter.DatePropertyEditor;
import org.osivia.services.widgets.issued.portlet.model.validation.IssuedFormValidator;
import org.osivia.services.widgets.issued.portlet.service.IssuedService;
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

/**
 * Issued date portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class IssuedController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private IssuedService service;

    /** Date property editor. */
    @Autowired
    private DatePropertyEditor datePropertyEditor;

    /** Form validator. */
    @Autowired
    private IssuedFormValidator validator;


    /**
     * Constructor.
     */
    public IssuedController() {
        super();
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        return "view";
    }


    /**
     * Warning render mapping.
     * 
     * @param request render request
     * @param response render response
     * @return warning path
     * @throws PortletException
     */
    @RenderMapping(params = "warning=true")
    public String warning(RenderRequest request, RenderResponse response) throws PortletException {
        return "warning";
    }


    /**
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @param result binding result
     * @param sessionStatus session status
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") IssuedForm form, BindingResult result,
            SessionStatus sessionStatus) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            sessionStatus.setComplete();

            this.service.save(portalControllerContext, form);
        }
    }


    /**
     * Get issued date form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public IssuedForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
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
        binder.registerCustomEditor(Date.class, this.datePropertyEditor);
        binder.addValidators(this.validator);
        binder.setDisallowedFields("displayInformations");
    }

}
