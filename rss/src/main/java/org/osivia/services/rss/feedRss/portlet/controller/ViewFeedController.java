package org.osivia.services.rss.feedRss.portlet.controller;

import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.feedRss.portlet.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * View Flux Rss controller.
 *
 * @author Cédric Krommenhoek
 * @author Frédéric Boudan
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewFeedController {

    /** Portlet context. */
    @Autowired
    protected PortletContext portletContext;

    /** Container RSS service. */
    @Autowired
    protected FeedService service;
    
    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    protected INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public ViewFeedController() {
        super();
    }


    /**
     * View container render mapping
     *
     * @param request render request
     * @param response render response
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response)
            throws PortletException {

        return "viewFlux";
    }

    @ModelAttribute("flux")
    public List<ContainerRssModel> getContainers(PortletRequest request, PortletResponse response) throws PortletException
    {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        return this.service.getListFeed(portalControllerContext);
    }
}
