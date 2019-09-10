package org.osivia.services.workspace.quota.portlet.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.services.workspace.quota.portlet.model.QuotaForm;
import org.osivia.services.workspace.quota.portlet.model.UpdateForm;
import org.osivia.services.workspace.quota.portlet.model.UpdateOptions;
import org.osivia.services.workspace.quota.portlet.service.QuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * View quota portlet controller.
 *
 * @author Jean-Sébastien Steux
 * @see CMSPortlet
 */
@Controller
@RequestMapping("VIEW")
public class ViewQuotaController extends CMSPortlet {

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
    
    /**
     * Taskbar service.
     */
    @Autowired
    private ITaskbarService taskbarService;
    
    
    
    @Autowired
    private InternalResourceViewResolver viewResolver;

    /** Log. */
    private final Log log;
    
    
    /**
     * Constructor.
     */
    public ViewQuotaController() {
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
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);        

        try {
            TaskbarTask task = this.taskbarService.getTask(portalControllerContext,nuxeoController.getBasePath(),"TRASH");
            if( task == null)   {
                request.setAttribute("osivia.emptyResponse", "1");
                return "empty";
            }
        } catch (PortalException e) {
            throw new PortletException(e);
        }      
        
        return "view";
    }






    /**
     * Get quota form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return quota form
     */
    @ModelAttribute("quotaForm")
    public QuotaForm getQuotaForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getQuotaForm(portalControllerContext);
    }

    

    /**
     * Refresh resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param QuotaForm form model attribute
     * @throws Exception 
     */
    @ResourceMapping("refresh")
    public void refresh(ResourceRequest request, ResourceResponse response,  @ModelAttribute("quotaForm") QuotaForm form)
            throws Exception {
    	
    	
    	View view = this.viewResolver.resolveViewName("refresh", null); 
    	JstlView jstlView = (JstlView) view;
    	String path = jstlView.getUrl();
    	PortletRequestDispatcher dispatcher = this.portletContext.getRequestDispatcher(path);
    	
    	request.setAttribute("quotaForm", form);
    	
    	dispatcher.include(request, response);   
    	
    	response.setContentType("text/html");

    }
    



    


}
