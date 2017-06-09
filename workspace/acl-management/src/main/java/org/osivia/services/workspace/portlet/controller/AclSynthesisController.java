package org.osivia.services.workspace.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.service.AclManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * ACL synthesis portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see PortletContextAware
 */
@Controller
@RequestMapping(path = "VIEW", params = "synthesis")
public class AclSynthesisController implements PortletContextAware {

    /** Portlet service. */
    @Autowired
    private AclManagementService service;


    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public AclSynthesisController() {
        super();
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "synthesis";
    }


    /**
     * Lazy loading resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("lazyLoading")
    public void lazyLoading(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // JSON data
        String data = this.service.getSynthesisData(portalControllerContext);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(data);
        printWriter.close();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
