package org.osivia.services.statistics.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.statistics.portlet.model.StatisticsVersion;
import org.osivia.services.statistics.portlet.model.StatisticsWindowSettings;
import org.osivia.services.statistics.portlet.service.StatisticsPortletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Statistics admin controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(value = "ADMIN")
public class StatisticsAdminController {

    /** Portlet service. */
    @Autowired
    private StatisticsPortletService service;
    
    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public StatisticsAdminController() {
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
        // Statistics versions
        request.setAttribute("versions", StatisticsVersion.values());

        return "admin/view";
    }


    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param windowSettings window settings model attribute
     * @throws PortletException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("windowSettings") StatisticsWindowSettings windowSettings)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.saveWindowSettings(portalControllerContext, windowSettings);

        response.setWindowState(WindowState.NORMAL);
        response.setPortletMode(PortletMode.VIEW);
    }


    /**
     * Get window settings model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return window settings
     * @throws PortletException
     */
    @ModelAttribute("windowSettings")
    public StatisticsWindowSettings getWindowSettings(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getWindowSettings(portalControllerContext);
    }

}
