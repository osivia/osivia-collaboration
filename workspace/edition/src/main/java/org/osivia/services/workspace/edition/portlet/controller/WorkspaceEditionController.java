package org.osivia.services.workspace.edition.portlet.controller;

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
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionOptions;
import org.osivia.services.workspace.edition.portlet.model.validator.WorkspaceEditionFormValidator;
import org.osivia.services.workspace.edition.portlet.service.WorkspaceEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
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
@SessionAttributes({"options", "editionForm"})
public class WorkspaceEditionController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;

    /** Workspace edition form validator. */
    @Autowired
    private WorkspaceEditionFormValidator editionFormValidator;

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
     * @param options options model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("options") WorkspaceEditionOptions options)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Task creation URL
        String taskCreationUrl = this.service.getTaskCreationUrl(portalControllerContext, options);
        request.setAttribute("taskCreationUrl", taskCreationUrl);

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
     * Sort tasks action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "sort")
    public void sort(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") WorkspaceEditionForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sort(portalControllerContext, form);
    }


    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param options options
     * @param form workspace edition form model attribute
     * @param result binding result
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("options") WorkspaceEditionOptions options,
            @ModelAttribute("editionForm") @Validated WorkspaceEditionForm form, BindingResult result) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.save(portalControllerContext, options, form);

            // Redirection
            String url = this.service.getWorkspaceUrl(portalControllerContext, options);
            response.sendRedirect(url);
        }
    }


    /**
     * Create task action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "create")
    public void createTask(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") WorkspaceEditionForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.createTask(portalControllerContext, form);
    }


    /**
     * Cancel action mapping.
     *
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "cancel")
    public void cancel(ActionRequest request, ActionResponse response, @ModelAttribute("options") WorkspaceEditionOptions options)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Redirection
        String url = this.service.getWorkspaceUrl(portalControllerContext, options);
        response.sendRedirect(url);
    }


    /**
     * Delete workspace action mapping.
     *
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute("options") WorkspaceEditionOptions options)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        String url = this.service.delete(portalControllerContext, options);

        // Redirection
        response.sendRedirect(url);
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
    public WorkspaceEditionOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }


    /**
     * Get edition form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @param options options model attribute
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("editionForm")
    public WorkspaceEditionForm getForm(PortletRequest request, PortletResponse response, @ModelAttribute("options") WorkspaceEditionOptions options)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext, options);
    }


    /**
     * Workspace edition form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("editionForm")
    public void editionFormInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.editionFormValidator);
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
