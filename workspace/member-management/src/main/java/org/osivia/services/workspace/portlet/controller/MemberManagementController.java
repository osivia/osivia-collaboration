package org.osivia.services.workspace.portlet.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
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
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.workspace.portlet.model.Member;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.model.MembersForm;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Workspace member management portlet view controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class MemberManagementController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Member management service. */
    @Autowired
    private MemberManagementService service;

    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public MemberManagementController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param form members form model attribute
     * @param sort sort property request parameter
     * @param alt alternative sort indicator request parameter
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("members") MembersForm form, @ModelAttribute("options") MemberManagementOptions options,
            @RequestParam(value = "sort", defaultValue = "date") String sort, @RequestParam(value = "alt", defaultValue = "true") String alt)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Tab
        request.setAttribute("tab", "members");

        // Sort members
        this.service.sortMembers(portalControllerContext, form, sort, BooleanUtils.toBoolean(alt));
        request.setAttribute("sort", sort);
        request.setAttribute("alt", alt);

        return "members/view";
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
        return "error";
    }


    /**
     * Update members action mapping.
     *
     * @param request action request
     * @param response action response
     * @param options options model attribute
     * @param form members form model attribute
     * @throws PortletException
     */
    @ActionMapping("update")
    public void update(ActionRequest request, ActionResponse response, @ModelAttribute("options") MemberManagementOptions options,
            @ModelAttribute("members") MembersForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.updateMembers(portalControllerContext, options, form);

        // Copy render parameters
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
     * Get members form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @param options options model attribute
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("members")
    public MembersForm getMembersForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getMembersForm(portalControllerContext);
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
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getMembersHelp(portalControllerContext);
    }
    
    /**
     * Export members table (CSV)
     * 
     * @param request
     * @param response
     * @param members
     * @throws IOException
     */
    @ResourceMapping("exportCsv")
    public void exportCsv(ResourceRequest request, ResourceResponse response, @ModelAttribute("members") MembersForm members, 
    		@ModelAttribute("options") MemberManagementOptions options) throws IOException {
    	
    	response.setContentType("text/csv");
        response.setProperty("Content-disposition", "attachment; filename=\"" + "members_"+options.getWorkspaceId()+".csv" + "\"");
    	
    	OutputStreamWriter writer = new OutputStreamWriter(response.getPortletOutputStream());
    	List<String> headers = new ArrayList<String>();
    	
    	Bundle bundle = bundleFactory.getBundle(null);
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_MEMBER"));
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_MEMBER_EXTRA"));
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_MEMBER_ACKNOWLEDGMENT_DATE"));
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ROLE"));
    	headers.add(bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_ID"));  	
    	
    	CSVPrinter printer = CSVFormat.EXCEL.withHeader(headers.toArray(new String[headers.size()])).print(writer);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
    	
    	for(Member m : members.getMembers()) {
    		String date = "";
    		if(m.getDate() != null) {
				date = sdf.format(m.getDate());
			}
    		
			String role = "";
			if(m.getRole() != null) { 
				role = bundle.getString(m.getRole().getKey(), m.getRole().getClassLoader());
			
			}
			printer.printRecord(m.getDisplayName(),m.getExtra(), date, role, m.getId());

    	}
    	
    	printer.close();
    	writer.close();
    }

}
