package org.osivia.services.editor.image.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.image.portlet.model.EditorImageForm;
import org.osivia.services.editor.image.portlet.model.ImageSourceType;
import org.osivia.services.editor.image.portlet.model.validation.EditorImageFormValidator;
import org.osivia.services.editor.image.portlet.service.EditorImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;
import java.util.List;

/**
 * Editor image portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class EditorImageController {

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
    private EditorImageService service;

    /**
     * Form validator.
     */
    @Autowired
    private EditorImageFormValidator formValidator;


    /**
     * Constructor.
     */
    public EditorImageController() {
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
     * Save action mapping.
     *
     * @param request       action request
     * @param response      action response
     * @param form          form model attribute
     * @param bindingResult binding result
     */
    @ActionMapping("submit")
    public void save(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") EditorImageForm form, BindingResult bindingResult) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!bindingResult.hasErrors()) {
            this.service.save(portalControllerContext, form);
        }
    }


    /**
     * Get form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public EditorImageForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Form init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(PortletRequestDataBinder binder) {
        binder.setDisallowedFields("done");
        binder.setValidator(this.formValidator);
    }


    /**
     * Get image source types model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return image source types
     */
    @ModelAttribute("sourceTypes")
    public List<ImageSourceType> getSourceTypes(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getSourceTypes(portalControllerContext);
    }

}
