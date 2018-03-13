package fr.toutatice.collaboratif.purgeworkspaces.portlet.controller;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.BooleanUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.PurgeWorkspaceForm;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.PurgeWorkspaceOptions;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.service.PurgeWorkspacesService;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.service.PurgeWorkspacesServiceImpl;

/**
 * Purge workspace controller
 * @author Julien Barberet
 *
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class PurgeWorkspacesController {

	private static final String DEFAULT_PAGE_SIZE = "10";
	
    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;
    
	/** Service */
    @Autowired
    private PurgeWorkspacesService service;
    
    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;
	
	public PurgeWorkspacesController() {
		super();
	}

	/**
	 * 
	 * View render mapping.
     *
	 * @param request render request
	 * @param response render response
	 * @param sort sort property request parameter
	 * @param alt alternative sort indicator request parameter
	 * @return
	 * @throws PortletException
	 */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, 
    		@ModelAttribute("form") PurgeWorkspaceForm form,
    		@ModelAttribute("options") PurgeWorkspaceOptions options) throws PortletException {
        
    	// Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        String page = "error";
        if (isAdministrator(portalControllerContext)) 
        {
        	page = "view";
        	form.setList(this.service.loadList(portalControllerContext, options));
            
        	form.setDisplayPurgeButton(this.service.existWorkspaceInBin(portalControllerContext));
        	
        	// Portlet title
            response.setTitle("Purge");
        }
        
        

        return page;
    }
    
    /**
     * Restore workspace action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @param result binding result
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "restore")
    public void restore(ActionRequest request, ActionResponse response, 
    		@ModelAttribute("form") PurgeWorkspaceForm form,
    		@ModelAttribute("options") PurgeWorkspaceOptions options,
    		@RequestParam(name = "uid") String uid)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        this.service.restore(portalControllerContext, uid);
        
        // Notification
        String message = bundle.getString("WORKSPACE_RESTORE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }
    
    /**
     * Put in trash action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @param result binding result
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "trash")
    public void putInTrash(ActionRequest request, ActionResponse response, 
    		@ModelAttribute("form") PurgeWorkspaceForm form,
    		@ModelAttribute("options") PurgeWorkspaceOptions options)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        this.service.putInTrash(portalControllerContext, form);
        
        // Notification
        String message = bundle.getString("WORKSPACE_IN_BIN_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }

    /**
     * Purge workspace in bin action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @param result binding result
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "purge")
    public void purge(ActionRequest request, ActionResponse response, 
    		@ModelAttribute("form") PurgeWorkspaceForm form,
    		@ModelAttribute("options") PurgeWorkspaceOptions options)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

    	int numberPurged = this.service.purge(portalControllerContext);

    	//Reinit the page number
    	options.setPageNumber("1");
    	
    	String message;
    	if (numberPurged == 0)
    	{
            message = bundle.getString("NO_WORKSPACE_TO_PURGE");
        } else if (numberPurged ==1)
        {
        	message = bundle.getString("WORKSPACE_PURGE_SUCCESS");
        } else {
            message = bundle.getString("WORKSPACES_PURGE_SUCCESS",numberPurged);
            
        }
    	// Notification
    	this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
    }
    
    /**
     * Get form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return edition form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public PurgeWorkspaceForm getForm(PortletRequest request, PortletResponse response)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }
    
    /**
     * Get options model attribute
     * @param request
     * @param response
     * @param sort
     * @param alt
     * @param pageNumber
     * @param pageSize
     * @param way
     * @return
     * @throws PortletException
     */
    @ModelAttribute("options")
    public PurgeWorkspaceOptions getOptions(PortletRequest request, PortletResponse response,
    		@RequestParam(name = "sort", defaultValue = "title") String sort,
    		@RequestParam(name = "alt", defaultValue = "false") String alt,
    		@RequestParam(name = "pageNumber", defaultValue = "1") String pageNumber,
    		@RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) String pageSize,
    		@RequestParam(name = "way", defaultValue = PurgeWorkspacesServiceImpl.OTHER_WAY) String way) throws PortletException
    {
    	// Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        return this.service.getOptions(portalControllerContext, sort, alt, pageNumber, pageSize);
    }
    
	/**
	 * Return true if the user has administrator role
	 * @param portalControllerContext
	 * @return true if the user has administrator role
	 */
	private boolean isAdministrator(PortalControllerContext portalControllerContext) {
		PortletRequest request = portalControllerContext.getRequest();
		if(request != null) 
			return BooleanUtils.isTrue((Boolean) request.getAttribute("osivia.isAdministrator")); 
		else return false;
	}
}
