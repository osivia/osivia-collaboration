package org.osivia.services.workspace.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

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
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.validator.InvitationsCreationFormValidator;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import net.sf.json.JSONArray;

/**
 * Member management portlet view invitations controller.
 * 
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping(value = "VIEW", params = "tab=invitations")
@SessionAttributes({"options", "invitations"})
public class MemberManagementInvitationsController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;


    /** Member management service. */
    @Autowired
    private MemberManagementService service;

    /** Invitations creation form validator. */
    @Autowired
    private InvitationsCreationFormValidator creationFormValidator;


    /**
     * Constructor.
     */
    public MemberManagementInvitationsController() {
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
     * @param form invitations form model attribute
     * @param sort sort property request parameter
     * @param alt alternative sort indicator request parameter
     * @param sort2 history sort property request parameter
     * @param alt2 history alternative sort indicator request parameter
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("invitations") InvitationsForm form,
            @RequestParam(value = "sort", defaultValue = "name") String sort, @RequestParam(value = "alt", required = false) String alt,
            @RequestParam(value = "sort2", defaultValue = "name") String sort2, @RequestParam(value = "alt2", required = false) String alt2)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Tab
        request.setAttribute("tab", "invitations");

        // Sort members
        this.service.sortInvitations(portalControllerContext, form, sort, BooleanUtils.toBoolean(alt), sort2, BooleanUtils.toBoolean(alt2));
        request.setAttribute("sort", sort);
        request.setAttribute("alt", alt);
        request.setAttribute("sort2", sort2);
        request.setAttribute("alt2", alt2);

        return "view-invitations";
    }


    /**
     * Update invitations.
     * 
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @param form invitations form model attribute
     * @throws PortletException
     */
    @ActionMapping("update")
    public void update(ActionRequest request, ActionResponse response, @ModelAttribute("options") MemberManagementOptions options,
            @ModelAttribute("invitations") InvitationsForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.updatePendingInvitations(portalControllerContext, options, form);

        // Copy render parameter
        copyRenderParameters(request, response);
    }


    /**
     * Update invitations history.
     * 
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @param form invitations form model attribute
     * @throws PortletException
     */
    @ActionMapping("updateHistory")
    public void updateHistory(ActionRequest request, ActionResponse response, @ModelAttribute("options") MemberManagementOptions options,
            @ModelAttribute("invitations") InvitationsForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.updateHistoryInvitations(portalControllerContext, options, form);

        // Copy render parameter
        copyRenderParameters(request, response);
    }


    /**
     * Create invitations action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @param invitationsForm invitations form model attribute
     * @param creationForm invitations creation form model attribute
     * @param result binding result
     * @throws PortletException
     */
    @ActionMapping("create")
    public void create(ActionRequest request, ActionResponse response, @ModelAttribute("options") MemberManagementOptions options,
            @ModelAttribute("invitations") InvitationsForm invitationsForm, @ModelAttribute("creation") @Validated InvitationsCreationForm creationForm,
            BindingResult result) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.createInvitations(portalControllerContext, options, invitationsForm, creationForm);
        }

        // Copy render parameter
        copyRenderParameters(request, response);
    }


    /**
     * Copy render parameters.
     * 
     * @param request action request
     * @param response action response
     */
    private void copyRenderParameters(ActionRequest request, ActionResponse response) {
        response.setRenderParameter("tab", "invitations");

        String sortParameter = request.getParameter("sort");
        if (StringUtils.isNotEmpty(sortParameter)) {
            response.setRenderParameter("sort", sortParameter);
        }

        String altParameter = request.getParameter("alt");
        if (StringUtils.isNotEmpty(altParameter)) {
            response.setRenderParameter("alt", altParameter);
        }

        String sort2Parameter = request.getParameter("sort2");
        if (StringUtils.isNotEmpty(sort2Parameter)) {
            response.setRenderParameter("sort2", sort2Parameter);
        }

        String alt2Parameter = request.getParameter("alt2");
        if (StringUtils.isNotEmpty(alt2Parameter)) {
            response.setRenderParameter("alt2", alt2Parameter);
        }
    }


    /**
     * Search persons resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param filter search filter request parameter
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("search")
    public void search(ResourceRequest request, ResourceResponse response, @RequestParam(value = "filter", required = false) String filter)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search results
        JSONArray results = this.service.searchPersons(portalControllerContext, filter);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(results.toString());
        printWriter.close();
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
     * Get invitations form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("invitations")
    public InvitationsForm getInvitationsForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getInvitationsForm(portalControllerContext);
    }


    /**
     * Get invitations creation form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("creation")
    public InvitationsCreationForm getInvitationsCreationForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getInvitationsCreationForm(portalControllerContext);
    }


    /**
     * Invitations creation form init binder.
     * 
     * @param binder portlet request data binder
     */
    @InitBinder("creation")
    public void invitationsCreationFormInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.creationFormValidator);
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
