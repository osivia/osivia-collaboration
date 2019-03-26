package org.osivia.services.workspace.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.AbstractChangeRoleForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MemberObject;
import org.osivia.services.workspace.portlet.model.validator.ChangeRoleFormValidator;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Member management change role portlet controller abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @param <M> member type
 * @param <F> form type
 */
@SessionAttributes("changeRoleForm")
public abstract class AbstractMemberManagementChangeRoleController<M extends MemberObject, F extends AbstractChangeRoleForm<M>> {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private MemberManagementService service;

    /** Form validator. */
    @Autowired
    private ChangeRoleFormValidator validator;


    /**
     * Constructor.
     */
    public AbstractMemberManagementChangeRoleController() {
        super();
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @param tab tab request parameter
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @RequestParam("tab") String tab) throws PortletException {
        request.setAttribute("tab", tab);
        request.setAttribute("view", "change-role");

        return "change-role/view";
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
    @ActionMapping("redirectTab")
    public void redirectTab(ActionRequest request, ActionResponse response, @RequestParam("redirection") String redirection, SessionStatus sessionStatus)
            throws PortletException {
        sessionStatus.setComplete();
        response.setRenderParameter("tab", redirection);
    }


    /**
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param tab tab request parameter
     * @param options options model attribute
     * @param form form model attribute
     * @param sessionStatus session status
     * @throws PortletException
     */
    @ActionMapping("save")
    public void save(ActionRequest request, ActionResponse response, @RequestParam("tab") String tab,
            @ModelAttribute("options") MemberManagementOptions options, @Validated @ModelAttribute("changeRoleForm") F form, BindingResult result,
            SessionStatus sessionStatus) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        response.setRenderParameter("tab", tab);

        if (result.hasErrors()) {
            response.setRenderParameter("view", "change-role");
        } else {
            this.service.updateRole(portalControllerContext, options, form);

            sessionStatus.setComplete();
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
    @ModelAttribute("changeRoleForm")
    public F getForm(PortletRequest request, PortletResponse response, @RequestParam(name = "identifiers", required = false) String[] identifiers)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getChangeRoleForm(portalControllerContext, identifiers, this.getMemberType(), this.getFormType());
    }


    /**
     * Form init binder.
     *
     * @param binder portlet request data binder
     */
    @InitBinder("changeRoleForm")
    public void formInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.validator);
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
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }


    /**
     * Get member type.
     * 
     * @return member type
     */
    public abstract Class<M> getMemberType();


    /**
     * Get form type.
     * 
     * @return form type
     */
    public abstract Class<F> getFormType();

}
