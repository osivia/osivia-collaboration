package org.osivia.services.rss.feedRss.portlet.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.feedRss.portlet.service.FeedService;
import org.osivia.services.rss.feedRss.portlet.validator.FeedFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping(value = "VIEW", params="view=add")
@SessionAttributes({"form"})
public class AddFeedController {

    /** Portlet context. */
    @Autowired
    protected PortletContext portletContext;
    
    /** Application context. */
    @Autowired
    public ApplicationContext applicationContext;    

    /** Container RSS service. */
    @Autowired
    protected FeedService service;
    
    /** Validator. */
    @Autowired
    private FeedFormValidator formValidator;    

    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    protected INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public AddFeedController() {
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

        return "viewAdd";
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
    public void add(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") FeedRssModel form, BindingResult status) throws PortletException, IOException {

        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if(status.hasErrors()) {
            response.setRenderParameter("view", "add");
        } else {
        	ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
        	List<FeedRssModel> list = new ArrayList<FeedRssModel>();
        	list.add(form);
        	container.setFeedSources(list);        	
        	
        	this.service.creatFeed(portalControllerContext, container);
        }
       	
    }

    /**
     * Container form init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.formValidator);
    }    
    
    @ModelAttribute("form")
    public FeedRssModel getForm(PortletRequest request, PortletResponse response)
    {
    	return applicationContext.getBean(FeedRssModel.class);
    }
    
}
