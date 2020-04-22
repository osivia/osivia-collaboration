package org.osivia.services.workspace.portlet.controller;

import java.io.File;
import java.io.IOException;
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
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
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


//    /**
//     * Upload document file action mapping.
//     *
//     * @param request  action request
//     * @param response action response
//     * @param form     document edition form model attribute
//     * @throws IOException 
//     */
//    @ActionMapping(name = "submit", params = "upload")
//    public void upload(ActionRequest request, ActionResponse response, @ModelAttribute("form") ImportForm form) 
//    		throws PortletException, IOException {
//        // Portal controller context
//        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
//		
//        // Delete previous temporary file
//        if (form.getTemporaryFile() != null) {
//            form.getTemporaryFile().delete();
//        }
//
//        // Upload
//        MultipartFile upload = form.getUpload();
//        File temporaryFile = File.createTempFile("document-edition-file-", ".tmp");
//        temporaryFile.deleteOnExit();
//        upload.transferTo(temporaryFile);
//        form.setTemporaryFile(temporaryFile);
//        form.setTemporaryFileName(upload.getOriginalFilename());
//    }

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
            @ModelAttribute("import") ImportForm form) throws PortletException, ParseException, PortalException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.prepareImportInvitations(portalControllerContext, options, form);

        // Copy render parameter
        response.setRenderParameter("tab", "members");
    }


    /**
     * Form init binder.
     *
     * @param binder portlet request data binder
     */
    @InitBinder("import")
    public void importFormInitBinder(PortletRequestDataBinder binder) {
        binder.registerCustomEditor(CollabProfile.class, this.localGroupPropertyEditor);
    }

}
