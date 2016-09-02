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
import org.osivia.services.workspace.portlet.model.LocalGroup;
import org.osivia.services.workspace.portlet.model.LocalGroups;
import org.osivia.services.workspace.portlet.model.validator.LocalGroupValidator;
import org.osivia.services.workspace.portlet.service.LocalGroupManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Workspace local group management view controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(value = "VIEW")
@SessionAttributes(value = "localGroups")
public class ViewLocalGroupManagementController implements PortletContextAware {

    /** Local group management service. */
    @Autowired
    private LocalGroupManagementService service;

    /** Local group validator. */
    @Autowired
    private LocalGroupValidator localGroupValidator;

    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public ViewLocalGroupManagementController() {
        super();
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
     * Save local groups action mapping.
     *
     * @param request action request
     * @param response action response
     * @param localGroups local groups model attribute
     * @throws PortletException
     */
    @ActionMapping(value = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute LocalGroups localGroups) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.saveLocalGroups(portalControllerContext, localGroups);
    }


    /**
     * Add local group action mapping.
     *
     * @param request action request
     * @param response action response
     * @param localGroups local groups model attribute
     * @param form local group creation form model attribute
     * @param result binding result
     * @throws PortletException
     */
    @ActionMapping(value = "add", params = "save")
    public void add(ActionRequest request, ActionResponse response, @ModelAttribute LocalGroups localGroups,
            @ModelAttribute(value = "addForm") @Validated LocalGroup form, BindingResult result) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.createLocalGroup(portalControllerContext, localGroups, form);
        }
    }


    /**
     * Cancel add local group action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form local group creation form model attribute
     */
    @ActionMapping(value = "add", params = "cancel")
    public void cancelAdd(ActionRequest request, ActionResponse response, @ModelAttribute(value = "addForm") LocalGroup form) {
        form.setDisplayName(null);
    }


    /**
     * Edit local group action mapping.
     *
     * @param request action request
     * @param response action response
     * @param id local group identifier
     * @param status session status
     */
    @ActionMapping(value = "edit")
    public void edit(ActionRequest request, ActionResponse response, @RequestParam String id, SessionStatus status) {
        status.setComplete();

        response.setRenderParameter("view", "edit");
        response.setRenderParameter("id", id);
    }


    /**
     * Get local groups model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return local groups
     * @throws PortletException
     */
    @ModelAttribute(value = "localGroups")
    public LocalGroups getLocalGroups(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getLocalGroups(portalControllerContext);
    }


    /**
     * Local group init binder.
     *
     * @param binder web data binder
     */
    @InitBinder(value = "localGroups")
    protected void localGroupsInitBinder(WebDataBinder binder) {
        binder.setDisallowedFields("workspaceId");
    }


    /**
     * Get add local group form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute(value = "addForm")
    public LocalGroup getAddLocalGroupForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getAddLocalGroupForm(portalControllerContext);
    }


    /**
     * Local group init binder.
     *
     * @param binder web data binder
     */
    @InitBinder(value = "addForm")
    protected void addLocalGroupFormInitBinder(WebDataBinder binder) {
        binder.addValidators(this.localGroupValidator);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}