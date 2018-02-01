package org.osivia.services.pins.edition.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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

import org.apache.commons.lang.math.NumberUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.pins.edition.portlet.model.PinnedDocument;
import org.osivia.services.pins.edition.portlet.model.PinsEditionForm;
import org.osivia.services.pins.edition.portlet.service.PinsEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import net.sf.json.JSONObject;

/**
 * Pins edition controller
 * @author jbarberet
 *
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class PinsEditionController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Service */
    @Autowired
    private PinsEditionService service;
    
    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;
    
    /**
     * Constructor.
     */
	public PinsEditionController() {
		super();
	}
	
	/**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param options calendar edition options model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portlet title
        response.setTitle("Pins");

        return "view";
    }

    /**
     * Delete pin action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute("form") PinsEditionForm form, @RequestParam("index") Integer index) throws PortletException {
        form.getListPins().remove(index.intValue());
        
        form.setToSave(true);
    }
    
    /**
     * Sort pins action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "sort")
    public void sort(ActionRequest request, ActionResponse response, @ModelAttribute("form") PinsEditionForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sort(portalControllerContext, form);
        
        form.setToSave(true);
    }
    
    /**
     * Add pin action mapping.
     * 
     * @param request portlet request
     * @param response portlet response
     * @param session session status
     */
    @ActionMapping(name = "save", params="addPin")
    public void addPin(ActionRequest request, ActionResponse response, @ModelAttribute("form") PinsEditionForm form) throws PortletException{
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        this.service.addPin(portalControllerContext, form);
        
        form.setToSave(true);
    }
    
    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @param result binding result
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params="save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("form") @Validated PinsEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        this.service.save(portalControllerContext, form);

        // Notification
        String message = bundle.getString("MESSAGE_WORKSPACE_SAVED");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        
        form.setToSave(false);
    }


    /**
     * Cancel action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "cancel")
    public void cancel(ActionRequest request, ActionResponse response, @ModelAttribute("form") PinsEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        form = this.service.getForm(portalControllerContext, true);
        form.setToSave(false);
        
//        // Redirection
//        String url = this.service.getWorkspaceUrl(portalControllerContext, form);
//        response.sendRedirect(url);
    }
    
    /**
     * Search action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     * @throws IOException 
     */
    @ResourceMapping("search")
    public void search(ResourceRequest request, ResourceResponse response, @ModelAttribute("form") PinsEditionForm form, @RequestParam(value = "filter", required = false) String filter, @RequestParam
            (value = "page", required = false) String page) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search results
        JSONObject results = this.service.searchDocuments(portalControllerContext, form.getWorkspace(), filter, NumberUtils.toInt(page, 0), getMapPinnedDocument(form));

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(results.toString());
        printWriter.close();
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
    public PinsEditionForm getForm(PortletRequest request, PortletResponse response)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext, false);
    }
    
    /**
     * Get map of pinned document, used to know if a document is already pinned
     * @param form
     * @return
     */
    private HashMap<String, String> getMapPinnedDocument(PinsEditionForm form)
    {
    	HashMap<String, String> map = new HashMap<>();
    	for (PinnedDocument pinned: form.getListPins())
    	{
    		map.put(pinned.getWebId(), "");
    	}
    	return map;
    }
    
}
