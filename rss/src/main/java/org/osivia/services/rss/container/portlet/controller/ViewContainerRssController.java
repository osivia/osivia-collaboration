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

import org.jboss.portal.common.http.HttpRequest.Form;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.rss.container.portlet.model.ContainerRssModel;
import org.osivia.services.rss.container.portlet.service.ContainerRssService;
import org.osivia.services.rss.container.portlet.validator.ContainerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
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
@SessionAttributes("form")
public class ViewContainerRssController {

    /** Error JSP path. */
    private static final String ERROR_PATH = "view";
    /** Error message request attribute. */
    private static final String ERROR_MESSAGE_ATTRIBUTE = "message";

    /** Portlet context. */
    @Autowired
    protected PortletContext portletContext;

    /** Container RSS service. */
    @Autowired
    protected ContainerRssService service;
    
    /** Validator. */
    @Autowired
    private ContainerFormValidator formValidator;    

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
        
        if(containers != null) {
            request.setAttribute("containers", containers);
        }
        
        return "view";
    }

     /**
     * Add container 
     * 
     * @param request
     * @param response
     * @param session
     * @throws PortletException
     * @throws IOException
     */
	@ActionMapping(value = "add")
    public void add(ActionRequest request, ActionResponse response, PortletSession session) throws PortletException, IOException {

        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        ContainerRssModel container = new ContainerRssModel();
        // container and path Name
        container.setName(request.getParameter("name"));
		container.setPath(request.getParameter("path"));
        
        this.service.creatContainer(portalControllerContext, container);

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
    
    /**
     * Containerform init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.formValidator);
    }    
    
    @ModelAttribute("form")
    public ContainerRssModel getForm(PortletRequest request, PortletResponse response)
    {
        PortalWindow window = WindowFactory.getWindow(request);

        ContainerRssModel form = new ContainerRssModel();
        form.setName(window.getProperty("name"));
        form.setPath(window.getProperty("path"));
        return form;
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
}
