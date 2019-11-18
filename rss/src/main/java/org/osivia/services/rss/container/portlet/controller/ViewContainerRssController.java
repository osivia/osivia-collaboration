package org.osivia.services.rss.container.portlet.controller;

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
import org.osivia.services.rss.container.portlet.service.ContainerRssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * View Container Rss controller.
 *
 * @author Frédéric Boudan
 */
@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes(value= "ContainerRssModel")
public class ViewContainerRssController {

    /** Portlet context. */
    @Autowired
    protected PortletContext portletContext;
    
    /** Application context. */
    @Autowired
    public ApplicationContext applicationContext;    

    /** Container RSS service. */
    @Autowired
    protected ContainerRssService service;
    
    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    protected INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public ViewContainerRssController() {
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

        return "viewContainer";
    }

    @ModelAttribute("containers")
    public List<ContainerRssModel> getContainers(PortletRequest request, PortletResponse response) throws PortletException
    {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        List<ContainerRssModel> container = this.service.getListContainer(portalControllerContext);
        return container;
    }    
    
}
