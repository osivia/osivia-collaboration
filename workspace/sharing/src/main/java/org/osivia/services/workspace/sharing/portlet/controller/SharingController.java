package org.osivia.services.workspace.sharing.portlet.controller;

import java.io.IOException;
import java.util.Arrays;
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
import org.osivia.services.workspace.sharing.portlet.model.SharingForm;
import org.osivia.services.workspace.sharing.portlet.model.SharingPermission;
import org.osivia.services.workspace.sharing.portlet.service.SharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Sharing portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class SharingController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private SharingService service;


    /**
     * Constructor.
     */
    public SharingController() {
        super();
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @param form form model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("form") SharingForm form) throws PortletException {
        String view;

        if (form.isEnabled()) {
            view = "enabled";
        } else {
            view = "disabled";
        }

        return view;
    }


    /**
     * Enable sharing action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "enable")
    public void enable(ActionRequest request, ActionResponse response, @ModelAttribute("form") SharingForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.enableSharing(portalControllerContext, form);
    }


    /**
     * Disable sharing action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "disable")
    public void disable(ActionRequest request, ActionResponse response, @ModelAttribute("form") SharingForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.disableSharing(portalControllerContext, form);
    }


    /**
     * Update permissions action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "update-permissions")
    public void updatePermissions(ActionRequest request, ActionResponse response, @ModelAttribute("form") SharingForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.updatePermissions(portalControllerContext, form);
    }


    /**
     * Remove user permission action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param user removed user request parameter
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping("remove-user")
    public void removeUser(ActionRequest request, ActionResponse response, @RequestParam("user") String user, @ModelAttribute("form") SharingForm form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.removeUser(portalControllerContext, form, user);
    }


    /**
     * Restore user permission action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param user restored user request parameter
     * @param form form model attribute
     * @throws PortletException
     */
    @ActionMapping("restore-user")
    public void restoreUser(ActionRequest request, ActionResponse response, @RequestParam("user") String user, @ModelAttribute("form") SharingForm form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.restoreUser(portalControllerContext, form, user);
    }


    /**
     * Close modal action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("close")
    public void close(ActionRequest request, ActionResponse response, @ModelAttribute("form") SharingForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.close(portalControllerContext, form);
    }


    /**
     * Get form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public SharingForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        return this.service.getForm(portalControllerContext);
    }


    /**
     * Form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("form")
    public void editionFormInitBinder(PortletRequestDataBinder binder) {
        binder.setDisallowedFields("link.id", "link.liveEditable", "initialEnabled", "close");
    }


    /**
     * Get permissions model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return permissions
     * @throws PortletException
     */
    @ModelAttribute("permissions")
    public List<SharingPermission> getPermissions(PortletRequest request, PortletResponse response) throws PortletException {
        return Arrays.asList(SharingPermission.values());
    }

}
