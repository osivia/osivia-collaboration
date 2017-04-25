package org.osivia.services.statistics.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

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
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.statistics.portlet.model.StatisticsConfiguration;
import org.osivia.services.statistics.portlet.model.StatisticsView;
import org.osivia.services.statistics.portlet.repository.IStatisticsRepository;
import org.osivia.services.statistics.portlet.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Statistics view controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletContextAware
 * @see PortletConfigAware
 */
@Controller
@RequestMapping(value = "VIEW")
public class StatisticsViewController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    /** Statistics service. */
    @Autowired
    private IStatisticsService service;

    /** Statistics repository. */
    @Autowired
    private IStatisticsRepository repository;

    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;


    /**
     * Constructor.
     */
    public StatisticsViewController() {
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
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }


    /**
     * Change view action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param configuration configuration model attribute
     * @throws PortletException
     */
    @ActionMapping(value = "changeView")
    public void changeView(ActionRequest request, ActionResponse response, @ModelAttribute(value = "configuration") StatisticsConfiguration configuration)
            throws PortletException {
        // Do nothing
    }


    /**
     * Load statistics resource mapping.
     *
     * @param request resource request
     * @param response resource response
     * @param configuration statistics configuration model attribute
     * @param value view value request parameter
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping(value = "loadStatistics")
    public void loadStatistics(ResourceRequest request, ResourceResponse response,
            @ModelAttribute(value = "configuration") StatisticsConfiguration configuration, @RequestParam(value = "view", required = false) String value)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Data
        String data = this.service.loadData(portalControllerContext, configuration);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(data);
        printWriter.close();
    }


    /**
     * Get statistics configuration model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @param value view value request parameter
     * @return configuration
     * @throws PortletException
     */
    @ModelAttribute(value = "configuration")
    public StatisticsConfiguration getConfiguration(PortletRequest request, PortletResponse response,
            @RequestParam(value = "view", required = false) String value) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // View
        StatisticsView view = StatisticsView.fromValue(value);

        // Configuration
        StatisticsConfiguration configuration = this.repository.getConfiguration(portalControllerContext);
        configuration.setView(view);

        return configuration;
    }


    /**
     * Get statistics views model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return views
     * @throws PortletException
     */
    @ModelAttribute(value = "views")
    public List<StatisticsView> getViews(PortletRequest request, PortletResponse response) throws PortletException {
        return Arrays.asList(StatisticsView.values());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

}
