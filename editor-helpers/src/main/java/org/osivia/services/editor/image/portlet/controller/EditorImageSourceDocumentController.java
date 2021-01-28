package org.osivia.services.editor.image.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.image.portlet.model.EditorImageSourceDocumentForm;
import org.osivia.services.editor.image.portlet.service.EditorImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.IOException;

/**
 * Editor document image source portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = "view=document")
public class EditorImageSourceDocumentController {

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
    public EditorImageSourceDocumentController() {
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
        return "source-document";
    }


    /**
     * Filter documents action mapping.
     *
     * @param request      action request
     * @param response     action response
     * @param documentForm image document form
     */
    @ActionMapping(name = "submit")
    public void filter(ActionRequest request, ActionResponse response, @ModelAttribute("documentForm") EditorImageSourceDocumentForm documentForm) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.filterDocuments(portalControllerContext, documentForm);

        response.setRenderParameter("view", "document");
    }


    /**
     * Select document action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param path     document path request parameter
     */
    @ActionMapping("select")
    public void select(ActionRequest request, ActionResponse response, @RequestParam("path") String path) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.selectDocument(portalControllerContext, path);
    }


    /**
     * Search resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param filter   search filter request parameter
     * @param scope    search scope request parameter
     */
    @ResourceMapping("search")
    public void search(ResourceRequest request, ResourceResponse response, @RequestParam(name = "filter", required = false) String filter, @RequestParam(name = "scope", required = false) String scope) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Content type
        response.setContentType("text/html");

        this.service.serveSearchResults(portalControllerContext, filter, scope);
    }


    /**
     * Get image document form.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return image document form
     */
    @ModelAttribute("documentForm")
    public EditorImageSourceDocumentForm getDocumentForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getDocumentForm(portalControllerContext);
    }

}
