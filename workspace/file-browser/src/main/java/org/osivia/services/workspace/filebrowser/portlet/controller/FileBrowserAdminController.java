package org.osivia.services.workspace.filebrowser.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortField;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserWindowProperties;
import org.osivia.services.workspace.filebrowser.portlet.service.FileBrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;
import java.util.List;

@Controller
@RequestMapping("ADMIN")
public class FileBrowserAdminController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;
    /**
     * Portlet service.
     */
    @Autowired
    private FileBrowserService service;


    /**
     * Constructor.
     */
    public FileBrowserAdminController() {
        super();
    }


    @RenderMapping
    public String view() {
        return "admin";
    }


    /**
     * Save action mapping.
     *
     * @param request          action request
     * @param response         action response
     * @param windowProperties window properties model attribute
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("windowProperties") FileBrowserWindowProperties windowProperties) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.setWindowProperties(portalControllerContext, windowProperties);

        response.setWindowState(WindowState.NORMAL);
        response.setPortletMode(PortletMode.VIEW);
    }


    /**
     * Get window properties model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return window properties
     */
    @ModelAttribute("windowProperties")
    public FileBrowserWindowProperties getWindowProperties(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getWindowProperties(portalControllerContext);
    }


    /**
     * Get sort fields model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return sort fields
     */
    @ModelAttribute("sortFields")
    public List<FileBrowserSortField> getSortFields(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getSortFields(portalControllerContext, true);
    }

}
