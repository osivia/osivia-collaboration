package org.osivia.services.statistics.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.statistics.portlet.model.StatisticsForm;
import org.osivia.services.statistics.portlet.service.StatisticsPortletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import net.sf.json.JSONObject;

/**
 * Statistics view controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes("form")
public class StatisticsViewController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Statistics service. */
    @Autowired
    private StatisticsPortletService service;


    /**
     * Constructor.
     */
    public StatisticsViewController() {
        super();
    }


    /**
     * Creations view render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String viewCreations(RenderRequest request, RenderResponse response) {
        return "view/view-creations";
    }


    /**
     * Visits view render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping(params = "tab=visits")
    public String viewVisits(RenderRequest request, RenderResponse response) {
        return "view/view-visits";
    }


    /**
     * Change view action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping("changeView")
    public void changeView(ActionRequest request, ActionResponse response, @ModelAttribute("form") StatisticsForm form) throws PortletException {
        // Copy render parameter
        response.setRenderParameter("tab", request.getParameter("tab"));
    }


    /**
     * Load creations resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param form form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("creations")
    public void loadCreations(ResourceRequest request, ResourceResponse response, @ModelAttribute("form") StatisticsForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        JSONObject data = this.service.getCreations(portalControllerContext, form.getCreationsView());

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(data.toString());
        printWriter.close();
    }


    /**
     * Load visits resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param form form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("visits")
    public void loadVisits(ResourceRequest request, ResourceResponse response, @ModelAttribute("form") StatisticsForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        JSONObject data = this.service.getVisits(portalControllerContext, form.getVisitsView());

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(data.toString());
        printWriter.close();
    }


    /**
     * Get form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public StatisticsForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        return this.service.getForm(portalControllerContext);
    }

}
