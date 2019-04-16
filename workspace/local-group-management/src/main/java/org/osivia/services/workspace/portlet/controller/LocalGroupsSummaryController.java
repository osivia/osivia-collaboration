package org.osivia.services.workspace.portlet.controller;

import java.io.IOException;
import java.util.Arrays;

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

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.LocalGroupsSort;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummary;
import org.osivia.services.workspace.portlet.service.LocalGroupsSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * View local groups portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class LocalGroupsSummaryController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Local groups summary service. */
    @Autowired
    private LocalGroupsSummaryService service;


    /**
     * Constructor.
     */
    public LocalGroupsSummaryController() {
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
        return "summary/view";
    }


    /**
     * Sort action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param sortId sort property identifier request parameter
     * @param alt alternative sort indicator request parameter
     * @param summary summary model attribute
     * @throws PortletException
     */
    @ActionMapping("sort")
    public void sort(ActionRequest request, ActionResponse response, @RequestParam("sortId") String sortId, @RequestParam("alt") String alt,
            @ModelAttribute("summary") LocalGroupsSummary summary) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sort(portalControllerContext, summary, LocalGroupsSort.fromId(sortId), BooleanUtils.toBoolean(alt));
    }


    /**
     * Delete local groups action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param identifiers selection identifiers request parameter
     * @param summary summary model attribute
     * @throws PortletException
     */
    @ActionMapping("delete")
    public void delete(ActionRequest request, ActionResponse response, @RequestParam("identifiers") String[] identifiers,
            @ModelAttribute("summary") LocalGroupsSummary summary) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.delete(portalControllerContext, summary, identifiers);
    }


    /**
     * Get local groups summary model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return summary
     * @throws PortletException
     */
    @ModelAttribute("summary")
    public LocalGroupsSummary getSummary(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getSummary(portalControllerContext);
    }


    /**
     * Get local groups table toolbar resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param indexes selected row indexes
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("toolbar")
    public void getToolbar(ResourceRequest request, ResourceResponse response, @RequestParam("indexes") String indexes) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Toolbar
        Element toolbar = this.service.getToolbar(portalControllerContext, Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(indexes), ",")));

        // Content type
        response.setContentType("text/html");

        // Content
        HTMLWriter htmlWriter = new HTMLWriter(response.getPortletOutputStream());
        htmlWriter.write(toolbar);
        htmlWriter.close();
    }

}
