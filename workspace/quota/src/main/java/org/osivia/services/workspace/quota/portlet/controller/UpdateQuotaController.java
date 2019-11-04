package org.osivia.services.workspace.quota.portlet.controller;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.services.workspace.quota.portlet.model.UpdateForm;
import org.osivia.services.workspace.quota.portlet.model.UpdateValidator;
import org.osivia.services.workspace.quota.portlet.service.QuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * View quota portlet controller.
 *
 * @author Jean-Sébastien Steux, Loïc Billon
 * @see CMSPortlet
 */
@Controller
@RequestMapping("VIEW")
public class UpdateQuotaController extends CMSPortlet {

    /**
     * Portlet config.
     */
    @Autowired
    private PortletConfig portletConfig;

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private QuotaService service;

    @Autowired
    private UpdateValidator validator;
    
    
    /** Log. */
    private final Log log;
    
    
    /**
     * Constructor.
     */
    public UpdateQuotaController() {
        super();

        
        this.log = LogFactory.getLog(this.getClass());        
    }


    /**
     * Post-construct.
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }
    
    /**
     * Quota update view
     * 
     * @param request action request
     * @param response action response
     * @param redirection redirection request parameter
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("redirectUpdate")
    public void redirectUpdate(ActionRequest request, ActionResponse response, SessionStatus sessionStatus)
            throws PortletException {
        sessionStatus.setComplete();        
        response.setRenderParameter("view", "update");
    }
    

    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping(params = {"view=update"})
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        return "update";
    }
    
    
    
    @ModelAttribute("updateForm")
    public UpdateForm getAskForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
    	return service.getUpdateForm(portalControllerContext);
    	
    }
    
    
    /**
     * Form init binder.
     *
     * @param binder portlet request data binder
     */
    @InitBinder("updateForm")
    public void formInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.validator);
    }
        

    /**
     * Update quota
     *
     * @param request  action request
     * @param response action response
     * @param form     update form model attribute
     */
    @ActionMapping("save-quota")
    public void updateQuota(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("updateForm") UpdateForm form,
    		BindingResult result) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if(!result.hasErrors()) {
            this.service.updateQuota(portalControllerContext, form);
            
        	Bundle bundle = getBundleFactory().getBundle(portalControllerContext.getRequest().getLocale());

        	String message = bundle.getString("QUOTA_MODIFIED");
        	if(StringUtils.isBlank(form.getSize())) {
        		message = bundle.getString("QUOTA_REMOVED");
        	}
        	
        	if(form.isStepRequest()) {
            	message = message.concat(bundle.getString("QUOTA_ASK_APPROUVED"));
            }

    		getNotificationsService().addSimpleNotification(portalControllerContext, message , NotificationsType.SUCCESS);
        	
        }
        else {
            response.setRenderParameter("view", "update");
        }
        

    }
  
    /**
     * Refuse quota
     *
     * @param request  action request
     * @param response action response
     * @param form     update form model attribute
     */
    @ActionMapping("refuse-quota")
    public void refuseQuota(ActionRequest request, ActionResponse response, @ModelAttribute("updateForm") UpdateForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.refuseQuota(portalControllerContext, form);

    	Bundle bundle = getBundleFactory().getBundle(portalControllerContext.getRequest().getLocale());
    	String message = bundle.getString("QUOTA_ASK_REFUSED");
		getNotificationsService().addSimpleNotification(portalControllerContext, message , NotificationsType.SUCCESS);
    	
    }
  

    /**
     * Cancel quota
     *
     * @param request  action request
     * @param response action response
     * @param form     update form model attribute
     */
    @ActionMapping("cancel-quota")
    public void cancelQuota(ActionRequest request, ActionResponse response) throws PortletException {
   }


    /**
     * Get update form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return quota form
     */
    @ModelAttribute("updateForm")
    public UpdateForm getUpdateForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getUpdateForm(portalControllerContext);
    }
    
}
