package org.osivia.services.workspace.portlet.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.Configuration;
import org.osivia.services.workspace.portlet.model.Participants;
import org.osivia.services.workspace.portlet.model.View;
import org.osivia.services.workspace.portlet.service.ParticipantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Participants portlet view controller.
 * 
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
public class ViewParticipantsController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet service. */
    @Autowired
    private ParticipantsService service;


    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public ViewParticipantsController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @param configuration configuration model attribute
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute Configuration configuration) {
        // View
        View view = configuration.getView();

        return view.getPath();
    }


    /**
     * More action mapping.
     * 
     * @param request action request
     * @param response action response
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("more")
    public void more(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Redirection URL
        String url = this.service.getMoreUrl(portalControllerContext);

        if (StringUtils.isNotEmpty(url)) {
            response.sendRedirect(url);
        }
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
     * Get workspace participants model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return participants
     * @throws PortletException
     */
    @ModelAttribute("participants")
    public Participants getParticipants(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getParticipants(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
