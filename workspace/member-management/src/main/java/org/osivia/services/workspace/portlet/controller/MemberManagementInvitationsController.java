package org.osivia.services.workspace.portlet.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.Member;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.osivia.services.workspace.portlet.model.converter.InvitationPropertyEditor;
import org.osivia.services.workspace.portlet.model.converter.LocalGroupPropertyEditor;
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

import net.sf.json.JSONObject;

/**
 * Member management portlet view invitations controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = "tab=invitations")
@SessionAttributes("creation")
public class MemberManagementInvitationsController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Member management service. */
    @Autowired
    private MemberManagementService service;

    /** Invitations creation form validator. */
    @Autowired
    private InvitationsCreationFormValidator creationFormValidator;

    /** Invitation property editor. */
    @Autowired
    private InvitationPropertyEditor invitationPropertyEditor;

    /** Local group property editor. */
    @Autowired
    private LocalGroupPropertyEditor localGroupPropertyEditor;

    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public MemberManagementInvitationsController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param form invitations form model attribute
     * @param sort sort property request parameter
     * @param alt alternative sort indicator request parameter
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("invitations") InvitationsForm form,
            @RequestParam(value = "sort", defaultValue = "date") String sort, @RequestParam(value = "alt", defaultValue = "true") String alt)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Tab
        request.setAttribute("tab", "invitations");

        // Sort invitations
        this.service.sortInvitations(portalControllerContext, form, sort, BooleanUtils.toBoolean(alt));
        request.setAttribute("sort", sort);
        request.setAttribute("alt", alt);

        return "invitations/view";
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

        this.service.updateInvitations(portalControllerContext, options, form);

        // Copy render parameter
        this.copyRenderParameters(request, response);
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

        if (result.hasErrors()) {
            creationForm.setWarning(false);
        } else {
            this.service.createInvitations(portalControllerContext, options, invitationsForm, creationForm);
        }

        // Copy render parameter
        this.copyRenderParameters(request, response);
    }


    /**
     * Purge invitations history action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @param form invitations form model attribute
     * @throws PortletException
     */
    @ActionMapping("purge")
    public void purge(ActionRequest request, ActionResponse response, @ModelAttribute("options") MemberManagementOptions options,
            @ModelAttribute("invitations") InvitationsForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.purgeInvitationsHistory(portalControllerContext, options, form);
        
        // Copy render parameter
        this.copyRenderParameters(request, response);
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
    }


    /**
     * Search persons resource mapping.
     *
     * @param request resource request
     * @param response resource response
     * @param options options model attribute
     * @param filter search filter request parameter
     * @param page pagination page number request parameter
     * @param tokenizer tokenizer indicator request parameter
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("search")
    public void search(ResourceRequest request, ResourceResponse response, @ModelAttribute("options") MemberManagementOptions options,
            @RequestParam(value = "filter", required = false) String filter, @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "tokenizer", required = false) String tokenizer) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search results
        JSONObject results = this.service.searchPersons(portalControllerContext, options, filter, NumberUtils.toInt(page, 1),
                BooleanUtils.toBoolean(tokenizer));

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
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

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
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

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
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

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
        binder.registerCustomEditor(Invitation.class, this.invitationPropertyEditor);
        binder.registerCustomEditor(CollabProfile.class, this.localGroupPropertyEditor);
    }


    /**
     * Get help model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return help
     * @throws PortletException
     */
    @ModelAttribute("help")
    public String getHelp(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getInvitationsHelp(portalControllerContext);
    }


    /**
     * Export invitation table (CSV)
     * 
     * @param request
     * @param response
     * @param invitations
     * @throws IOException
     */
    @ResourceMapping("exportCsv")
    public void exportCsv(ResourceRequest request, ResourceResponse response, @ModelAttribute("invitations") InvitationsForm invitations, 
    		@ModelAttribute("options") MemberManagementOptions options) throws IOException {
    	
    	response.setContentType("text/csv");
        response.setProperty("Content-disposition", "attachment; filename=\"" + "invitations_"+options.getWorkspaceId()+".csv" + "\"");
    	
    	OutputStreamWriter writer = new OutputStreamWriter(response.getPortletOutputStream());
    	List<String> headers = new ArrayList<String>();
    	
    	Bundle bundle = bundleFactory.getBundle(null);
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION"));
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_MEMBER_EXTRA"));
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_DATE"));
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_RESENDING_DATE_EXP"));
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ROLE"));
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_INVITATION_STATE"));

    	
    	CSVPrinter printer = CSVFormat.EXCEL.withHeader(headers.toArray(new String[headers.size()])).print(writer);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
    	
    	for(Invitation m : invitations.getInvitations()) {
    		String date = "";
    		if(m.getDate() != null) {
				date = sdf.format(m.getDate());
			}
    		
    		String resendingDate = "";
    		if(m.getResendingDate() != null) {
    			resendingDate = sdf.format(m.getResendingDate());
			}

			String role = "";
			if(m.getRole() != null) { 
				role = bundle.getString(m.getRole().getKey(), m.getRole().getClassLoader());
			
			}
			String state = "";
			if(m.getState() != null) { 
				state = bundle.getString(m.getState().getKey());
			
			}
			
			printer.printRecord(m.getDisplayName(),m.getExtra(), date, resendingDate, role, state);

    	}
    	
    	printer.close();
    	writer.close();
    }
    
    
}
