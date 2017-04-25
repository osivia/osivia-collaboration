package org.osivia.services.workspace.task.creation.portlet.controller;

import java.util.SortedMap;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.BooleanUtils;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.common.portlet.model.TaskCreationForm;
import org.osivia.services.workspace.common.portlet.model.validator.TaskCreationFormValidator;
import org.osivia.services.workspace.task.creation.portlet.service.WorkspaceTaskCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Workspace task creation controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class WorkspaceTaskCreationController implements PortletContextAware {

    /** Portlet context. */
    private PortletContext portletContext;


    /** Workspace task creation service. */
    @Autowired
    private WorkspaceTaskCreationService service;

    /** Workspace task creation form validator. */
    @Autowired
    private TaskCreationFormValidator taskCreationFormValidator;


    /**
     * Constructor.
     */
    public WorkspaceTaskCreationController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param closeModal close modal indicator request parameter
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @RequestParam(name = "closeModal", required = false) String closeModal) {
        // Close modal indicator
        request.setAttribute("closeModal", BooleanUtils.toBoolean(closeModal));

        return "view";
    }


    /**
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form workspace task creation form model attribute
     * @param result binding result
     * @throws PortletException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("taskCreationForm") @Validated TaskCreationForm form,
            BindingResult result) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.save(portalControllerContext, form);

            response.setRenderParameter("closeModal", String.valueOf(true));
        }
    }


    /**
     * Get workspace task creation form model attribute.
     * 
     * @param request portlet request
     * @param response response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("taskCreationForm")
    public TaskCreationForm getTaskCreationForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        
        return this.service.getTaskCreationForm(portalControllerContext);
    }


    /**
     * Workspace task creation form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("taskCreationForm")
    public void taskCreationFormInitBinder(WebDataBinder binder) {
        binder.addValidators(this.taskCreationFormValidator);
    }


    /**
     * Get task types model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return types
     * @throws PortletException
     */
    @ModelAttribute("types")
    public SortedMap<String, DocumentType> getTaskTypes(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getTaskTypes(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
