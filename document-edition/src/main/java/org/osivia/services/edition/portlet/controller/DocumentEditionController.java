package org.osivia.services.edition.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.validator.DocumentEditionFormValidator;
import org.osivia.services.edition.portlet.service.DocumentEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;
import java.io.IOException;

/**
 * Document edition portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class DocumentEditionController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private DocumentEditionService service;


    /**
     * Document edition form validator.
     */
    @Autowired
    private DocumentEditionFormValidator validator;


    /**
     * Constructor.
     */
    public DocumentEditionController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @param form     document edition form model attribute
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getViewPath(portalControllerContext, form);
    }


    /**
     * Save document action mapping.
     *
     * @param request       action request
     * @param response      action response
     * @param form          document edition form model attribute
     * @param result        binding result
     * @param sessionStatus session status
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") AbstractDocumentEditionForm form, BindingResult result, SessionStatus sessionStatus) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            sessionStatus.setComplete();

            this.service.save(portalControllerContext, form);
        }
    }


    /**
     * Get document edition form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public AbstractDocumentEditionForm getForm(PortletRequest request, PortletResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Document edition form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("form")
    public void editionFormInitBinder(WebDataBinder binder) {
        binder.addValidators(this.validator);
        binder.setDisallowedFields("name", "creation", "path");
    }

}
