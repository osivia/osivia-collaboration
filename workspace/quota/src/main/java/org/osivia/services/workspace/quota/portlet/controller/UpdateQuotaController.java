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
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping(params = {"tab=update"})
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
      
        return "update";
    }



    /**
     * Update quota
     *
     * @param request  action request
     * @param response action response
     * @param form     update form model attribute
     */
    @ActionMapping("save-quota")
    public void updateQuota(ActionRequest request, ActionResponse response, @ModelAttribute("updateForm") UpdateForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.updateQuota(portalControllerContext, form);

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
     * Redirect tab action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param redirection redirection request parameter
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("redirect-update")
    public void redirectTab(ActionRequest request, ActionResponse response, SessionStatus sessionStatus)
            throws PortletException {
        sessionStatus.setComplete();        
        response.setRenderParameter("tab", "update");
    }

    
    
    /**
     * Get options model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return options
     * @throws PortletException
     */
    @ModelAttribute("options")
    public UpdateOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.updateOptions(portalControllerContext);
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
