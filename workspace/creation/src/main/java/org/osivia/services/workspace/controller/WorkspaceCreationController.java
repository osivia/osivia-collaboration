package org.osivia.services.workspace.controller;

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

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.model.WorkspaceCreationForm;
import org.osivia.services.workspace.service.WorkspaceCreationService;
import org.osivia.services.workspace.validator.WorkspaceCreationFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Workspace creation controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletContextAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
public class WorkspaceCreationController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;

    /** Form validator. */
    @Autowired
    private WorkspaceCreationFormValidator formValidator;

    /** Workspace creation service. */
    @Autowired
    private WorkspaceCreationService service;


    /**
     * Constructor.
     */
    public WorkspaceCreationController() {
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
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @param result binding result
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("form") @Validated WorkspaceCreationForm form, BindingResult result)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.create(portalControllerContext, form);
        }
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
    public WorkspaceCreationForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
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
