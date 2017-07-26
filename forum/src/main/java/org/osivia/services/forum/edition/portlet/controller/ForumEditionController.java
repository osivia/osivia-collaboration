package org.osivia.services.forum.edition.portlet.controller;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.CharEncoding;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.services.forum.edition.portlet.model.ForumEditionForm;
import org.osivia.services.forum.edition.portlet.model.ForumEditionMode;
import org.osivia.services.forum.edition.portlet.model.ForumEditionOptions;
import org.osivia.services.forum.edition.portlet.model.validator.ForumEditionFormValidator;
import org.osivia.services.forum.edition.portlet.service.ForumEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import javax.annotation.PostConstruct;
import javax.portlet.*;
import java.io.*;

/**
 * Forum edition portlet controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes({"form", "options"})
public class ForumEditionController extends CMSPortlet {

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private ForumEditionService service;

    /** Forum edition form validator. */
    @Autowired
    private ForumEditionFormValidator formValidator;


    /**
     * Constructor.
     */
    public ForumEditionController() {
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
     * @param request  render request
     * @param response render response
     * @param options  forum edition options model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("options") ForumEditionOptions options) throws PortletException {
        // Title
        String title = options.getTitle();
        response.setTitle(title);

        return options.getViewPath();
    }


    /**
     * Upload vignette action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "upload-vignette")
    public void uploadVignette(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumEditionForm form) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadVignette(portalControllerContext, form);
    }


    /**
     * Delete vignette action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "delete-vignette")
    public void deleteVignette(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumEditionForm form) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteVignette(portalControllerContext, form);
    }


    /**
     * Upload attachment action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "upload-attachment")
    public void uploadAttachment(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumEditionForm form) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadAttachment(portalControllerContext, form);
    }


    /**
     * Delete attachment action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "delete-attachment")
    public void deleteAttachment(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumEditionForm form) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteAttachment(portalControllerContext, form);
    }


    /**
     * Save action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum edition form model attribute
     * @param options  forum edition options model attribute
     * @param result   binding result
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("form") @Validated ForumEditionForm form, BindingResult result,
                     @ModelAttribute("options") ForumEditionOptions options) throws
            PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.save(portalControllerContext, form, options);
        }
    }


    /**
     * Cancel action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param options  forum edition options model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("cancel")
    public void cancel(ActionRequest request, ActionResponse response, @ModelAttribute("options") ForumEditionOptions options) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.cancel(portalControllerContext, options);
    }


    /**
     * Vignette preview resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param form     form edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("vignettePreview")
    public void vignettePreview(ResourceRequest request, ResourceResponse response, @ModelAttribute("form") ForumEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.vignettePreview(portalControllerContext, form);
    }


    /**
     * Get editor properties resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param editorId editor identifier required request parameter
     */
    @ResourceMapping("editor")
    public void getEditor(ResourceRequest request, ResourceResponse response, @RequestParam(name = "editorId") String editorId) throws PortletException,
            IOException {
        super.serveResourceEditor(request, response, editorId);
    }


    /**
     * Get forum edition form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return forum edition form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public ForumEditionForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Forum edition form init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.formValidator);
        binder.setDisallowedFields("documentType");
    }


    /**
     * Get forum edition options model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return forum edition options
     * @throws PortletException
     */
    @ModelAttribute("options")
    public ForumEditionOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }

}
