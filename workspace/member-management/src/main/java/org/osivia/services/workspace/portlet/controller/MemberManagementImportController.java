package org.osivia.services.workspace.portlet.controller;

import java.text.ParseException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.ImportForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.converter.LocalGroupPropertyEditor;
import org.osivia.services.workspace.portlet.model.validator.ImportValidator;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
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
 * Member management portlet view import csv controller.
 *
 * @author Loïc Billon
 */
@Controller
@RequestMapping(path = "VIEW", params = "tab=importCsv")
@SessionAttributes("import")
public class MemberManagementImportController {


    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;
	

    /** Member management service. */
    @Autowired
    private MemberManagementService service;    
    
    /** Local group property editor. */
    @Autowired
    private LocalGroupPropertyEditor localGroupPropertyEditor;
    
    @Autowired
    private ImportValidator validator;
    
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
        // Tab
        request.setAttribute("tab", "importCsv");

        return "importcsv/view";
    }

    /**
     * Get import form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("import")
    public ImportForm getInvitationsForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getImportForm(portalControllerContext);
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
     * Import invitations.
     *
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @param form invitations form model attribute
     * @throws PortletException
     * @throws PortalException 
     * @throws ParseException 
     */
    @ActionMapping("launchImport")
    public void launchImport(ActionRequest request, ActionResponse response, @ModelAttribute("options") MemberManagementOptions options,
    		@Validated  @ModelAttribute("import") ImportForm form,  BindingResult result,
            SessionStatus sessionStatus) throws PortletException, ParseException, PortalException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
        	this.service.prepareImportInvitations(portalControllerContext, options, form);

            response.setRenderParameter("tab", "members");

            sessionStatus.setComplete();
        }
        else {
            response.setRenderParameter("tab", "importCsv");
    	}

    }


    /**
     * Form init binder.
     *
     * @param binder portlet request data binder
     */
    @InitBinder("import")
    public void importFormInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.validator);
        binder.registerCustomEditor(CollabProfile.class, this.localGroupPropertyEditor);
    }

}
