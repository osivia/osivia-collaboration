package org.osivia.services.editor.link.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.osivia.services.editor.link.portlet.model.FilterType;
import org.osivia.services.editor.link.portlet.model.UrlType;
import org.osivia.services.editor.link.portlet.model.validator.EditorLinkFormValidator;
import org.osivia.services.editor.link.portlet.service.EditorLinkService;
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
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import net.sf.json.JSONObject;

/**
 * Editor link portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class EditorLinkController {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private EditorLinkService service;

    /**
     * Editor link form validator.
     */
    @Autowired
    private EditorLinkFormValidator validator;


    /**
     * Constructor.
     */
    public EditorLinkController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }


    /**
     * Submit action mapping.
     *
     * @param request       action request
     * @param response      action response
     * @param form          form model attribute
     * @param bindingResult binding result
     */
    @ActionMapping("submit")
    public void submit(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") EditorLinkForm form, BindingResult bindingResult) throws
            PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!bindingResult.hasErrors()) {
            this.service.save(portalControllerContext, form);
        }
    }


    /**
     * Unlink action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     editor link form
     */
    @ActionMapping("unlink")
    public void unlink(ActionRequest request, ActionResponse response, @ModelAttribute("form") EditorLinkForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.unlink(portalControllerContext, form);
    }


    /**
     * Search documents resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param filter   search filter
     * @param page     search pagination page number
     */
    @ResourceMapping("search")
    public void search(ResourceRequest request, ResourceResponse response, @RequestParam(value = "filter", required = false) String filter, @RequestParam
            (value = "page", required = false) String page) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Search results
        JSONObject results = this.service.searchDocuments(portalControllerContext, filter, NumberUtils.toInt(page, 1));

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(results.toString());
        printWriter.close();
    }


    /**
     * Get editor link form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public EditorLinkForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Editor link form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.validator);
        binder.setDisallowedFields("done", "url", "onlyText");
    }


    /**
     * Get URL types model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return URL types
     */
    @ModelAttribute("urlTypes")
    public List<UrlType> getUrlTypes(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getUrlTypes(portalControllerContext);
    }

}
