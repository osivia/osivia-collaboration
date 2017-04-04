/**
 * 
 */
package org.osivia.services.versions.portlet.controller;

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

import org.osivia.services.versions.portlet.model.Version;
import org.osivia.services.versions.portlet.model.Versions;
import org.osivia.services.versions.portlet.service.VersionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;


/**
 * @author david
 *
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewListVersionsController extends CMSPortlet implements PortletContextAware, PortletConfigAware {
    
    @Autowired
    private VersionsService service;
    
    private PortletContext portletContext;
    
    private PortletConfig portletConfig;

    /**
     * Constructor.
     */
    public ViewListVersionsController(){
        super();
    }
    
    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(portletConfig);
    }
    
    /**
     * Get versions model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return versions
     * @throws PortletException
     */
    @ModelAttribute(value = "versions")
    public Versions getVersions(PortletRequest request, PortletResponse response) throws PortletException {
        return service.getListVersions(request, response, portletContext);
    }
    
    /**
     * Get Version creation form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return creation form
     */
    @ModelAttribute(value = "creationForm")
    public Version getCreationForm(PortletRequest request, PortletResponse response) {
        return new Version();
    }
    
    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }
    
    /**
     * Restore a given version.
     *
     * @param request action request
     * @param response action response
     * @param id local group identifier
     * @param status session status
     */
    @ActionMapping(value = "restore")
    public void restore(ActionRequest request, ActionResponse response, @RequestParam String versionId, SessionStatus status) {
        service.restoreVersion(request, response, portletContext, versionId);
        response.setRenderParameter("view", "view");
    }
    
    /**
     * Create a version.
     *
     * @param request action request
     * @param response action response
     * @param localGroup local group model attribute
     * @throws PortletException
     */
    @ActionMapping(value = "create")
    public void create(ActionRequest request, ActionResponse response, @ModelAttribute Versions versions, 
            @ModelAttribute(value = "creationForm") Version form) throws PortletException {
        this.service.createVersion(request, response, portletContext, versions, form);
        // Re-initialize form
        form.setComment(null);
        response.setRenderParameter("view", "view");
    }
    
    
    //=================//
    
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

}
