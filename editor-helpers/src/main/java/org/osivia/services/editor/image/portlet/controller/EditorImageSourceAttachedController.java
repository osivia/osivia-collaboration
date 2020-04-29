package org.osivia.services.editor.image.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.image.portlet.model.EditorImageSourceAttachedForm;
import org.osivia.services.editor.image.portlet.service.EditorImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;

/**
 * Editor attached image source portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = "view=attached")
public class EditorImageSourceAttachedController {

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
     * Constructor.
     */
    public EditorImageSourceAttachedController() {
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
        return "source-attached";
    }


    /**
     * Select action mapping.
     *
     * @param request      action request
     * @param response     action response
     * @param attachedForm attached image form model attribute
     */
    @ActionMapping(name = "submit", params = "select")
    public void select(ActionRequest request, ActionResponse response, @ModelAttribute("attachedForm") EditorImageSourceAttachedForm attachedForm) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.selectAttached(portalControllerContext, attachedForm);
    }


    /**
     * Get attached image form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return attached image form
     */
    @ModelAttribute("attachedForm")
    public EditorImageSourceAttachedForm getAttachedForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getAttachedForm(portalControllerContext);
    }

}
