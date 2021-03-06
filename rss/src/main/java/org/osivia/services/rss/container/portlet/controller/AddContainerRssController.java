package org.osivia.services.rss.container.portlet.controller;

import java.io.IOException;

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
import org.osivia.services.rss.common.service.ContainerRssService;
import org.osivia.services.rss.common.validator.ContainerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Add Container Rss controller.
 *
 * @author Frédéric Boudan
 */
@Controller
@RequestMapping(value = "VIEW", params="add=container")
public class AddContainerRssController {

    /** Portlet context. */
    @Autowired
    protected PortletContext portletContext;
    
    /** Application context. */
    @Autowired
    public ApplicationContext applicationContext;    

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
    public AddContainerRssController() {
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

        return "addContainer";
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
	@ActionMapping(value = "add")
    public void add(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") ContainerRssModel form, BindingResult result, SessionStatus status) throws PortletException, IOException {

        // Portal controller context
//        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if(result.hasErrors()) {
        	response.setRenderParameter("add", "container");
        } else {
        	//    	this.service.creatContainer(portalControllerContext, form);
            status.setComplete();
        }
       	
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
    public ContainerRssModel getForm(PortletRequest request, PortletResponse response) throws PortletException
    {
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
        container.setMap(this.service.getMapContainer(portalControllerContext));    	

        return container;
        		
    }
    
}
