package org.osivia.services.workspace.portlet.controller;

import java.util.Arrays;
import java.util.List;

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
import org.osivia.services.workspace.portlet.model.Configuration;
import org.osivia.services.workspace.portlet.model.View;
import org.osivia.services.workspace.portlet.service.ParticipantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Participants portlet admin controller.
 * 
 * @author Cédric Krommenhoek
 * @see PortletContextAware
 */
@Controller
@RequestMapping("ADMIN")
public class AdminParticipantsController implements PortletContextAware {

    /** Portlet service. */
    @Autowired
    private ParticipantsService service;


    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public AdminParticipantsController() {
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
        return "admin";
    }


    /**
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param configuration configuration model attribute
     * @throws PortletException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute Configuration configuration) throws PortletException {
     // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        this.service.saveConfiguration(portalControllerContext, configuration);

        response.setWindowState(WindowState.NORMAL);
        response.setPortletMode(PortletMode.VIEW);
    }


    /**
     * Get configuration model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return configuration
     * @throws PortletException
     */
    @ModelAttribute("configuration")
    public Configuration getConfiguration(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getConfiguration(portalControllerContext);
    }


    /**
     * Get views model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return views
     * @throws PortletException
     */
    @ModelAttribute("views")
    public List<View> getViews(PortletRequest request, PortletResponse response) throws PortletException {
        return Arrays.asList(View.values());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
