package org.osivia.services.edition.portlet.controller;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.validator.DocumentEditionFormValidator;
import org.osivia.services.edition.portlet.service.DocumentEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletContextAware;

import javax.portlet.*;
import java.io.IOException;

/**
 * Document edition portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class DocumentEditionController implements PortletContextAware {

    /**
     * Portlet context.
     */
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
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
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
     * Upload document attachments action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     document edition form model attribute
     */
    @ActionMapping(name = "submit", params = "upload-attachments")
    public void uploadAttachments(ActionRequest request, ActionResponse response, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadAttachments(portalControllerContext, form);
    }


    /**
     * Delete document attachment action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param value    deleted attachment request parameter value
     * @param form     document edition form model attribute
     */
    @ActionMapping(name = "submit", params = "delete-attachment")
    public void deleteAttachment(ActionRequest request, ActionResponse response, @RequestParam("delete-attachment") String value, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteAttachment(portalControllerContext, form, value);
    }


    /**
     * Restore document attachment action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param value    restored attachment request parameter value
     * @param form     document edition form model attribute
     */
    @ActionMapping(name = "submit", params = "restore-attachment")
    public void restoreAttachment(ActionRequest request, ActionResponse response, @RequestParam("restore-attachment") String value, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.restoreAttachment(portalControllerContext, form, value);
    }


    /**
     * Upload document picture action mapping.
     *
     * @param request     action request
     * @param response    action response
     * @param pictureType picture type request parameter
     * @param form        document edition form model attribute
     */
    @ActionMapping(name = "submit", params = "upload-picture")
    public void uploadPicture(ActionRequest request, ActionResponse response, @RequestParam("upload-picture") String pictureType, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadPicture(portalControllerContext, form, pictureType);
    }


    /**
     * Delete document picture action mapping.
     *
     * @param request     action request
     * @param response    action response
     * @param pictureType picture type request parameter
     * @param form        document edition form model attribute
     */
    @ActionMapping(name = "submit", params = "delete-picture")
    public void deletePicture(ActionRequest request, ActionResponse response, @RequestParam("delete-picture") String pictureType, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deletePicture(portalControllerContext, form, pictureType);
    }


    /**
     * Restore document picture action mapping.
     *
     * @param request     action request
     * @param response    action response
     * @param pictureType picture type request parameter
     * @param form        document edition form model attribute
     */
    @ActionMapping(name = "submit", params = "restore-picture")
    public void restorePicture(ActionRequest request, ActionResponse response, @RequestParam("restore-picture") String pictureType, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.restorePicture(portalControllerContext, form, pictureType);
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

            this.service.save(portalControllerContext, form);
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
     * Picture preview ressource mapping.
     *
     * @param request     resource request
     * @param response    resource response
     * @param pictureType picture type request parameter
     * @param form        document edition form model attribute
     */
    @ResourceMapping("picture-preview")
    public void picturePreview(ResourceRequest request, ResourceResponse response, @RequestParam("type") String pictureType, @ModelAttribute("form") AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.picturePreview(portalControllerContext, form, pictureType);
    }


    /**
     * Editor resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param editorId editor identifier request parameter
     */
    @ResourceMapping("editor")
    public void editor(ResourceRequest request, ResourceResponse response, @RequestParam("editorId") String editorId) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.serveEditor(portalControllerContext, editorId);
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
        binder.setDisallowedFields("name", "creation", "path", "originalTitle");
    }

}
