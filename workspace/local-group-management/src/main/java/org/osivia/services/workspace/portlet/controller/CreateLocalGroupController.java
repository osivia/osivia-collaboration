package org.osivia.services.workspace.portlet.controller;

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
import org.osivia.services.workspace.portlet.model.LocalGroupForm;
import org.osivia.services.workspace.portlet.model.LocalGroupMember;
import org.osivia.services.workspace.portlet.model.converter.LocalGroupMemberPropertyEditor;
import org.osivia.services.workspace.portlet.model.validator.LocalGroupFormValidator;
import org.osivia.services.workspace.portlet.service.CreateLocalGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Create local group portlet controller
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = "view=create")
@SessionAttributes("form")
public class CreateLocalGroupController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Create local group service. */
    @Autowired
    private CreateLocalGroupService service;

    /** Member property editor. */
    @Autowired
    private LocalGroupMemberPropertyEditor memberPropertyEditor;

    /** Form validator. */
    @Autowired
    private LocalGroupFormValidator formValidator;


    /**
     * Constructor.
     */
    public CreateLocalGroupController() {
        super();
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException {
        return "create/view";
    }


    /**
     * Submit creation action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form form model attribute
     * @param result binding result
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("submit")
    public void submit(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") LocalGroupForm form, BindingResult result,
            SessionStatus sessionStatus) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (result.hasErrors()) {
            response.setRenderParameter("view", "create");
        } else {
            this.service.create(portalControllerContext, form);
            
            sessionStatus.setComplete();
        }
    }


    /**
     * Cancel creation action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("cancel")
    public void cancel(ActionRequest request, ActionResponse response, SessionStatus sessionStatus) throws PortletException {
        sessionStatus.setComplete();
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
    public LocalGroupForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Form init binder.
     *
     * @param binder portlet request data binder
     */
    @InitBinder("form")
    public void formInitBinder(PortletRequestDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.registerCustomEditor(LocalGroupMember.class, this.memberPropertyEditor);
        binder.addValidators(this.formValidator);
    }


    /**
     * Get workspace members model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return members
     * @throws PortletException
     */
    @ModelAttribute("members")
    public List<LocalGroupMember> getWorkspaceMembers(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getAllMembers(portalControllerContext);
    }

}
