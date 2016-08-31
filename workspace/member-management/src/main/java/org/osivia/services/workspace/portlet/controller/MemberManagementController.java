package org.osivia.services.workspace.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Workspace member management portlet view controller.
 *
 * @author Cédric Krommenhoek
 * @see ApplicationContextAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
public class MemberManagementController implements ApplicationContextAware, PortletContextAware {

    /** Portlet context. */
    private PortletContext portletContext;

    /** Member management service. */
    @Autowired
    private MemberManagementService service;

    
    /**
     * Constructor.
     */
    public MemberManagementController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param form members form model attribute
     * @param sort sort property request parameter
     * @param alt alternative sort indicator request parameter
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("members") MembersForm form,
            @RequestParam(value = "sort", defaultValue = "name") String sort, @RequestParam(value = "alt", required = false) String alt)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Tab
        request.setAttribute("tab", "members");

        // Sort members
        this.service.sortMembers(portalControllerContext, form, sort, BooleanUtils.toBoolean(alt));
        request.setAttribute("sort", sort);
        request.setAttribute("alt", alt);

        return "view-members";
    }


    /**
     * Portlet exception handler.
     *
     * @param request portlet request
     * @param response portlet response
     * @param exception portlet exception
     * @return error path
     */
    @ExceptionHandler(PortletException.class)
    public String handlePortletException(PortletRequest request, PortletResponse response, PortletException exception) {
        request.setAttribute("exception", exception);
        return "error";
    }


    /**
     * Update members action mapping.
     *
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @param form members form model attribute
     * @throws PortletException
     */
    @ActionMapping("update")
    public void update(ActionRequest request, ActionResponse response, @ModelAttribute("options") MemberManagementOptions options,
            @ModelAttribute("members") MembersForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.updateMembers(portalControllerContext, options, form);

        // Copy render parameters
        String sortParameter = request.getParameter("sort");
        if (StringUtils.isNotEmpty(sortParameter)) {
            response.setRenderParameter("sort", sortParameter);
        }
        String altParameter = request.getParameter("sort");
        if (StringUtils.isNotEmpty(altParameter)) {
            response.setRenderParameter("alt", altParameter);
        }
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
    public MemberManagementOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }


    /**
     * Get members form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @param options options model attribute
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("members")
    public MembersForm getMembersForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getMembersForm(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.setApplicationContext(applicationContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
