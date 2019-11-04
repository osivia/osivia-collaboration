package org.osivia.services.workspace.quota.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.portlet.PortalGenericPortlet;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.workspace.quota.portlet.model.AskQuotaForm;
import org.osivia.services.workspace.quota.portlet.model.AskQuotaValidator;
import org.osivia.services.workspace.quota.portlet.service.QuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.transform.impl.AddStaticInitTransformer;
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

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;

/**
 * View used to ask for quota
 * @author Loïc Billon
 *
 */
@Controller
@RequestMapping("VIEW")
public class RequestQuotaController extends PortalGenericPortlet {


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
    private AskQuotaValidator validator;
    
    /**
     * Quota request view
     * 
     * @param request action request
     * @param response action response
     * @param redirection redirection request parameter
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("askQuota")
    public void askQuota(ActionRequest request, ActionResponse response, SessionStatus sessionStatus)
            throws PortletException {
        sessionStatus.setComplete();        
        response.setRenderParameter("view", "askQuota");
    }
    

    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping(params = {"view=askQuota"})
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        return "askQuota";
    }
    
    
    
    @ModelAttribute("askForm")
    public AskQuotaForm getAskForm(PortletRequest request, PortletResponse response) {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
    	return service.getAskForm(portalControllerContext);
    	
    	
    }
    
    /**
     * Form init binder.
     *
     * @param binder portlet request data binder
     */
    @InitBinder("askForm")
    public void formInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.validator);
    }
    
    @ActionMapping("ask")
    public void ask(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("askForm") AskQuotaForm form,
    		BindingResult result) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        try {
        	
        	if(!result.hasErrors()) {
	        	service.ask(portalControllerContext, form);
	        	
	        	Bundle bundle = getBundleFactory().getBundle(portalControllerContext.getRequest().getLocale());
	        	String message = bundle.getString("QUOTA_ASK_SUCCESS");
				getNotificationsService().addSimpleNotification(portalControllerContext, message , NotificationsType.SUCCESS);
        	}
        	else {
                response.setRenderParameter("view", "askQuota");
        	}
        	
        }
        catch(CMSException e) {
        	throw new PortletException(e);
        } catch (PortalException e) {
        	throw new PortletException(e);
		} catch (FormFilterException e) {
        	throw new PortletException(e);
		}
    }
    

    /**
     * Cancel quota
     *
     * @param request  action request
     * @param response action response
     * @param form     update form model attribute
     */
    @ActionMapping("cancel")
    public void cancel(ActionRequest request, ActionResponse response) throws PortletException {
    	
    }
    
}
