package org.osivia.services.rss.container.portlet.controller;

import java.io.IOException;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.services.rss.container.portlet.model.ContainerRssModel;
import org.osivia.services.rss.container.portlet.service.ContainerRssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * View Container Rss controller.
 *
 * @author Cédric Krommenhoek
 * @author Frédéric Boudan
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewContainerRssController {

    /** Error JSP path. */
    private static final String ERROR_PATH = "view";
    /** Error message request attribute. */
    private static final String ERROR_MESSAGE_ATTRIBUTE = "message";

    /** Portlet context. */
    @Autowired
    protected PortletContext portletContext;

    /** Calendar service. */
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
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        List<ContainerRssModel> containers = this.service.getListContainer(portalControllerContext);
        
        request.setAttribute("containers", containers);
        
        return "view";
    }

    /**
     * remove container
     * 
     * @param request
     * @param response
     * @param session
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(value = "remove")
    public void remove(ActionRequest request, ActionResponse response, PortletSession session, @RequestParam("doc_id") String docid) throws PortletException, IOException {

        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

    }

    /**
     * Add 
     * 
     * @param request
     * @param response
     * @param session
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(value = "add")
    public void add(ActionRequest request, ActionResponse response, PortletSession session, @RequestParam("url") String url) throws PortletException, IOException {

        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

    }

    /**
     * Exception handler.
     *
     * @param request portlet request
     * @param response portlet response
     * @param exception current exception
     * @return error path
     */
    @ExceptionHandler(value = Exception.class)
    public String handleException(PortletRequest request, PortletResponse response, Exception exception) {
        // Error message
        request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, exception.getMessage());

        return ERROR_PATH;
    }
    
    @ActionMapping(name="submit", params="synchro")
    public void reloadImage(ActionRequest request, ActionResponse response, @ModelAttribute("form") ContainerRssModel containerRss) {
		// Synchronisation du conteneur avec le flux RSS
    }
    
    /**
     * Get containers model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return containers
     * @throws PortletException
     */
    @ModelAttribute("containers")
    public ContainerRssModel getContainers(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return (ContainerRssModel) this.service.getListContainer(portalControllerContext);
    }    

}
