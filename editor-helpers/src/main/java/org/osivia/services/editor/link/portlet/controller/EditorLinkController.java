package org.osivia.services.editor.link.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.osivia.services.editor.link.portlet.model.validator.EditorLinkFormValidator;
import org.osivia.services.editor.link.portlet.service.EditorLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;

/**
 * Editor link portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class EditorLinkController {

    /**
     * Portlet context.
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private EditorLinkService service;

    /**
     * Editor link form validator.
     */
    @Autowired
    private EditorLinkFormValidator validator;


    /**
     * Constructor.
     */
    public EditorLinkController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }


    /**
     * Document link source redirection.
     *
     * @param request  action request
     * @param response action response
     * @param form     form model attribute
     */
    @ActionMapping(name = "submit", params = "source-document")
    public void sourceDocumentRedirection(ActionRequest request, ActionResponse response, @ModelAttribute("form") EditorLinkForm form) {
        response.setRenderParameter("view", "document");
    }


    /**
     * Submit action mapping.
     *
     * @param request       action request
     * @param response      action response
     * @param form          form model attribute
     * @param bindingResult binding result
     */
    @ActionMapping(name = "submit", params = "save")
    public void submit(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") EditorLinkForm form, BindingResult bindingResult) throws
            PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!bindingResult.hasErrors()) {
            this.service.save(portalControllerContext, form);
        }
    }


    /**
     * Unlink action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     editor link form
     */
    @ActionMapping("unlink")
    public void unlink(ActionRequest request, ActionResponse response, @ModelAttribute("form") EditorLinkForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.unlink(portalControllerContext, form);
    }


    /**
     * Get editor link form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public EditorLinkForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Editor link form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.validator);
        binder.setDisallowedFields("done", "loaded");
    }

}
