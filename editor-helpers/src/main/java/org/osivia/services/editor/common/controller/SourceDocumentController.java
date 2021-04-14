package org.osivia.services.editor.common.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.common.model.SourceDocumentForm;
import org.osivia.services.editor.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.IOException;

/**
 * Source document portlet controller abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class SourceDocumentController {


    /**
     * Portlet context.
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public SourceDocumentController() {
        super();
    }


    /**
     * Get portlet service.
     *
     * @return portlet service
     */
    protected abstract CommonService getService();


    /**
     * View render mapping.
     *
     * @return view path
     */
    @RenderMapping
    public String view() {
        return "source-document";
    }


    /**
     * Select document action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param path     document path request parameter
     */
    @ActionMapping("select")
    public void select(ActionRequest request, ActionResponse response, @RequestParam("path") String path) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.getService().selectDocument(portalControllerContext, path);
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

        this.getService().serveSearchResults(portalControllerContext, filter, scope);
    }


    /**
     * Get source document form.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return source document form
     */
    @ModelAttribute("documentForm")
    public SourceDocumentForm getDocumentForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.getService().getSourceDocumentForm(portalControllerContext);
    }


}
