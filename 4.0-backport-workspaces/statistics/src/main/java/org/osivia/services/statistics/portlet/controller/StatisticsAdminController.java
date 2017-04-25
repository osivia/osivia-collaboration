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
import org.osivia.services.statistics.portlet.model.StatisticsConfiguration;
import org.osivia.services.statistics.portlet.model.StatisticsVersion;
import org.osivia.services.statistics.portlet.repository.IStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Statistics admin controller.
 *
 * @author Cédric Krommenhoek
 * @see PortletContextAware
 */
@Controller
@RequestMapping(value = "ADMIN")
public class StatisticsAdminController implements PortletContextAware {

    /** Statistics repository. */
    @Autowired
    private IStatisticsRepository repository;

    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public StatisticsAdminController() {
        super();
    }


    /**
     * Admin render mapping.
     *
     * @param request render request
     * @param response render response
     * @return admin path
     */
    @RenderMapping
    public String admin(RenderRequest request, RenderResponse response) {
        // Statistics versions
        request.setAttribute("versions", StatisticsVersion.values());

        return "admin";
    }


    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param configuration statistics configuration model attribute
     * @throws PortletException
     */
    @ActionMapping(value = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute(value = "configuration") StatisticsConfiguration configuration)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.repository.saveConfiguration(portalControllerContext, configuration);

        response.setWindowState(WindowState.NORMAL);
        response.setPortletMode(PortletMode.VIEW);
    }


    /**
     * Get statistics configuration model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return configuration
     * @throws PortletException
     */
    @ModelAttribute(value = "configuration")
    public StatisticsConfiguration getConfiguration(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.repository.getConfiguration(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
