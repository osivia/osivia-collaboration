package org.osivia.services.edition.portlet.controller;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.configuration.DocumentEditionConfiguration;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.FileEditionForm;
import org.osivia.services.edition.portlet.model.validator.DocumentEditionFormValidator;
import org.osivia.services.edition.portlet.service.DocumentEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

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
public class DocumentEditionController {

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
    
	@ExceptionHandler(MultipartException.class)
	String handleFileException(Throwable ex, RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		String uploadMaxSize = StringUtils.defaultIfBlank(System.getProperty("osivia.filebrowser.max.upload.size"), 
				DocumentEditionConfiguration.MAX_UPLOAD_SIZE_PER_FILE_MO);
		
		request.setAttribute("uploadMaxSize", uploadMaxSize.toString());
		
		return "upload-error";

	}
}
