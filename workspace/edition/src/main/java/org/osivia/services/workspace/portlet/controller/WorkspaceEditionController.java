package org.osivia.services.workspace.portlet.controller;

import java.io.IOException;

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
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.portlet.model.validator.WorkspaceEditionFormValidator;
import org.osivia.services.workspace.portlet.service.WorkspaceEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Workspace edition controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
public class WorkspaceEditionController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;

    /** Form validator. */
    @Autowired
    private WorkspaceEditionFormValidator formValidator;

    /** Workspace edition service. */
    @Autowired
    private WorkspaceEditionService service;


    /**
     * Constructor.
     */
    public WorkspaceEditionController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
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
        request.setAttribute("back", StringUtils.isNotEmpty(request.getParameter(ActionRequest.ACTION_NAME)));
        return "error";
    }


    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form form
     * @param result binding result
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("form") @Validated WorkspaceEditionForm form, BindingResult result)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.save(portalControllerContext, form);

            // Redirection
            String url = this.service.getWorkspaceUrl(portalControllerContext, form);
            response.sendRedirect(url);
        }
    }


    /**
     * Cancel action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form form
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "cancel")
    public void cancel(ActionRequest request, ActionResponse response, @ModelAttribute("form") WorkspaceEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Redirection
        String url = this.service.getWorkspaceUrl(portalControllerContext, form);
        response.sendRedirect(url);
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
    public WorkspaceEditionForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
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
    public void localGroupInitBinder(WebDataBinder binder) {
        binder.setDisallowedFields("path");
        binder.addValidators(this.formValidator);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
