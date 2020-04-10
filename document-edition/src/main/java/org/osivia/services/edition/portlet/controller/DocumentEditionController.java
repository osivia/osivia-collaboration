package org.osivia.services.edition.portlet.controller;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.NoteEditionForm;
import org.osivia.services.edition.portlet.model.validator.DocumentEditionFormValidator;
import org.osivia.services.edition.portlet.service.DocumentEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;

import javax.portlet.*;
import java.io.IOException;

/**
 * Document edition portlet controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class DocumentEditionController extends CMSPortlet implements PortletConfigAware {

    /**
     * Portlet context.
     */
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private DocumentEditionService service;


    /**
     * Document edition form validator.
     */
    @Autowired
    private DocumentEditionFormValidator validator;


    /**
     * Constructor.
     */
    public DocumentEditionController() {
        super();
    }


    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        try {
            super.init(portletConfig);
        } catch (PortletException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getViewPath(portalControllerContext);
    }


    /**
     * Upload document file action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     document edition form model attribute
     */
    @ActionMapping(name = "submit", params = "upload")
    public void upload(ActionRequest request, ActionResponse response, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.upload(portalControllerContext, form);
    }


    /**
     * Restore document file action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     document edition form model attribute
     */
    @ActionMapping(name = "submit", params = "restore")
    public void restore(ActionRequest request, ActionResponse response, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.restore(portalControllerContext, form);
    }


    /**
     * Save document edition action mapping.
     *
     * @param request       action request
     * @param response      action response
     * @param form          document edition form model attribute
     * @param result        binding result
     * @param sessionStatus session status
     */
    @ActionMapping(name = "submit", params = "save")
    public void save(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") AbstractDocumentEditionForm form, BindingResult result, SessionStatus sessionStatus) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            sessionStatus.setComplete();


            // FIXME
            if (form instanceof NoteEditionForm) {
                NoteEditionForm noteForm = (NoteEditionForm) form;
                System.out.println("Content: \"" + noteForm.getContent() + "\"");
            } else {
                System.out.println("Form class: " + form.getClass());
            }


            this.service.save(portalControllerContext, form, result);
        }
    }


    /**
     * Cancel document edition action mapping.
     *
     * @param request       action request
     * @param response      action response
     * @param sessionStatus session status
     */
    @ActionMapping("cancel")
    public void cancel(ActionRequest request, ActionResponse response, SessionStatus sessionStatus) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        sessionStatus.setComplete();

        this.service.cancel(portalControllerContext);
    }


    /**
     * Editor resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param editorId editor identifier request parameter
     */
    @ResourceMapping("editor")
    public void editorResourceMapping(ResourceRequest request, ResourceResponse response, @RequestParam("editorId") String editorId) throws PortletException, IOException {
        super.serveResourceEditor(request, response, editorId);
    }


    /**
     * Get document edition form model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return form
     */
    @ModelAttribute("form")
    public AbstractDocumentEditionForm getForm(PortletRequest request, PortletResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Document edition form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("form")
    public void editionFormInitBinder(WebDataBinder binder) {
        binder.addValidators(this.validator);
        binder.setDisallowedFields("name", "creation", "path", "originalTitle", "breadcrumb");
    }

}
