package org.osivia.services.rss.feedRss.portlet.controller;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
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

        return "viewFeed";
    }

    @ModelAttribute("Feed")
    public List<ContainerRssModel> getContainers(PortletRequest request, PortletResponse response) throws PortletException
    {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        return this.service.getListFeed(portalControllerContext);
    }
    
	/**
	 * Add container
	 * 
	 * @param request
	 * @param response
	 * @param form
	 * @param status
	 * @throws PortletException
	 * @throws IOException
	 */
	@ActionMapping(value = "synchro")
	public void add(ActionRequest request, ActionResponse response,
			@Validated @ModelAttribute("form") ContainerRssModel form, BindingResult status)
			throws PortletException, IOException {

		// Portal controller context
		PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request,
				response);

		this.service.synchro(portalControllerContext, form);

	}
}
