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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Edit Container Rss controller.
 *
 * @author Frédéric Boudan
 */
@Controller
@RequestMapping(value = "VIEW", params="view=edit")
@SessionAttributes({"form"})
public class EditFeedController {

    /** Portlet context. */
    @Autowired
    protected PortletContext portletContext;
    
    /** Application context. */
    @Autowired
    public ApplicationContext applicationContext;    

    /** Feed RSS service. */
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
    
    /** Feed parameter. */
    public final static String DISPLAY = "displayName";
    public final static String URL = "url";
    public final static String ID = "id";
    
    /** Feed request attribute. */
    protected static final String FEED_ATTRIBUTE = "form";    

    /**
     * Constructor.
     */
    public EditFeedController() {
        super();
    }

    /**
     * Edit container render mapping
     *
     * @param request render request
     * @param response render response
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute FeedRssModel form)
            throws PortletException {
    	
    	return "viewEdit";
    }

     /**
     * Delete feed
     * 
     * @param request
     * @param response
     * @param form
     * @param status
     * @throws PortletException
     * @throws IOException
     */
	@ActionMapping("del")
    public void del(ActionRequest request, ActionResponse response, @ModelAttribute("form") FeedRssModel form, SessionStatus status) throws PortletException, IOException {

        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
		ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
		List<FeedRssModel> list = new ArrayList<FeedRssModel>();
		list.add(form);
		container.setFeedSources(list);

		this.service.delFeed(portalControllerContext, container);
		status.setComplete();
    }
	
	/**
	 * Modification feed
	 * 
	 * @param request
	 * @param response
	 * @param form
	 * @param status
	 * @throws PortletException
	 * @throws IOException
	 */
	@ActionMapping("modif")
	public void modif(ActionRequest request, ActionResponse response,
			@Validated @ModelAttribute("form") FeedRssModel form, BindingResult result, SessionStatus status)
			throws PortletException, IOException {

		// Portal controller context
		PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request,
				response);
		if (result.hasErrors()) {
			response.setRenderParameter("view", "edit");
		} else {
			ContainerRssModel container = applicationContext.getBean(ContainerRssModel.class);
			List<FeedRssModel> list = new ArrayList<FeedRssModel>();
			list.add(form);
			container.setFeedSources(list);

			this.service.modFeed(portalControllerContext, container);
			status.setComplete();
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
    public FeedRssModel getForm(PortletRequest request, PortletResponse response, @RequestParam(value = ID, required = false) String id ) throws PortletException
    {
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        FeedRssModel feed = applicationContext.getBean(FeedRssModel.class);
        
    	return this.service.getMapFeed(portalControllerContext, id, feed);
    }
}
