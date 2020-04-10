package org.osivia.services.rss.feedRss.portlet.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.services.rss.common.model.FeedRssModel;
import org.osivia.services.rss.common.service.FeedService;
import org.osivia.services.rss.feedRss.portlet.validator.ModifFormValidator;
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
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Edit Container Rss controller.
 *
 * @author Frédéric Boudan
 */
@Controller
@RequestMapping(value = "VIEW", params="edit=feed")
@SessionAttributes("form")
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
    private ModifFormValidator formValidator;    

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
    public final static String INDEX = "index";    
    
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
    	
    	return "editFeed";
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
    public void del(ActionRequest request, ActionResponse response, @RequestParam String id ,@ModelAttribute("form") FeedRssModel form, SessionStatus status) throws PortletException, IOException {

        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

		this.service.delFeed(portalControllerContext, form);
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
			response.setRenderParameter("edit", "feed");
		} else {
			this.service.modFeed(portalControllerContext, form);
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
    public FeedRssModel getForm(PortletRequest request, PortletResponse response, @RequestParam(value=ID, required=false) String id, @RequestParam(value=DISPLAY, required=false) String name, @RequestParam(value=URL, required=false) String url, @RequestParam(value=INDEX, required=false) String index) throws PortletException
    {
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
    	return this.service.getMapFeed(portalControllerContext, id, name, url, Integer.parseInt(index));
    }
    
	/**
	 * Cancel 
	 */
	@ActionMapping("cancel")
	public void cancel(ActionRequest request, ActionResponse response, SessionStatus status) {
		status.setComplete();
	}    
    
    /**
     * Upload visual action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "modif", params = "upload-visual")
    public void uploadVisual(ActionRequest request, ActionResponse response, @ModelAttribute("form") FeedRssModel form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadVisual(portalControllerContext, form);
        response.setRenderParameter("edit", "feed");
    }    
    

    /**
     * Delete visual action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "modif", params = "delete-visual")
    public void deleteVisual(ActionRequest request, ActionResponse response, @ModelAttribute("form") FeedRssModel form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteVisual(portalControllerContext, form);
        response.setRenderParameter("edit", "feed");
    }

    /**
     * Visual preview resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param form workspace edition form model attribute
     * @throws IOException
     */
    @ResourceMapping("visualPreview")
    public void visualPreview(ResourceRequest request, ResourceResponse response, @ModelAttribute("form") FeedRssModel form)
            throws IOException {
    	
        // Temporary file
        File temporaryFile = form.getVisual().getTemporaryFile();

        // Upload size
		Long size = Long.valueOf(temporaryFile.length());
        response.setContentLength(size.intValue());

        // Content type
        String contentType = response.getContentType();
        response.setContentType(contentType);

        // Character encoding
        response.setCharacterEncoding(CharEncoding.UTF_8);

        // No cache
        response.getCacheControl().setExpirationTime(0);

        // Input stream
        InputStream inputSteam = new FileInputStream(temporaryFile);
        // Output stream
        OutputStream outputStream = response.getPortletOutputStream();
        // Copy
        IOUtils.copy(inputSteam, outputStream);
        outputStream.close();
    }    	    
}
