package org.osivia.services.calendar.event.edition.portlet.controller;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.CalendarCommonEventForm;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.event.edition.portlet.model.validation.CalendarEventEditionFormValidator;
import org.osivia.services.calendar.event.edition.portlet.service.CalendarEventEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Calendar event edition portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class CalendarEventEditionController extends CMSPortlet implements PortletConfigAware {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private CalendarEventEditionService service;

    /** Calendar event edition form validator. */
    @Autowired
    private CalendarEventEditionFormValidator formValidator;
	

    /** Log. */
    private final Log log;


    /**
     * Constructor.
     */
	public CalendarEventEditionController() {
		super();
        this.log = LogFactory.getLog(this.getClass());
	}


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        try {
            super.init(portletConfig);
        } catch (PortletException e) {
            this.log.error(e);
        }
    }


	/**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param options calendar edition options model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("options") CalendarEditionOptions options) throws PortletException {
        // Portlet title
        String portletTitle = options.getPortletTitle();
        response.setTitle(portletTitle);

        return "view";
    }
    

    /**
     * Upload attachments action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form calendar event edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "upload-attachments")
    public void uploadAttachments(ActionRequest request, ActionResponse response, @ModelAttribute("form") CalendarCommonEventForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadAttachments(portalControllerContext, form);
    }


    /**
     * Delete attachment action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param index attachment index request parameter
     * @param form calendar event edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "delete-attachment")
    public void deleteAttachment(ActionRequest request, ActionResponse response, @RequestParam("delete-attachment") Integer index,
            @ModelAttribute("form") CalendarCommonEventForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteAttachment(portalControllerContext, form, index);
    }


    /**
     * Restore attachment action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param index attachment index request parameter
     * @param form calendar event edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "restore-attachment")
    public void restoreAttachment(ActionRequest request, ActionResponse response, @RequestParam("restore-attachment") Integer index,
            @ModelAttribute("form") CalendarCommonEventForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.restoreAttachment(portalControllerContext, form, index);
    }


    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param options calendar edition options model attribute
     * @param form calendar event edition form model attribute
     * @param result binding result
     * @param status session status
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("options") CalendarEditionOptions options,
            @ModelAttribute("form") @Validated CalendarCommonEventForm form, BindingResult result, SessionStatus status) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        if (!result.hasErrors()) {
            this.service.save(portalControllerContext, options, form);

            status.setComplete();
        } 
    }
    

    /**
     * Cancel action mapping.
     *
     * @param request action request
     * @param response action response
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("cancel")
    public void cancel(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.cancel(portalControllerContext);
    }
    

    /**
     * Get editor properties resource mapping.
     *
     * @param request resource request
     * @param response resource response
     * @param editorId editor identifier required request parameter
     */
    @ResourceMapping("editor")
    public void getEditor(ResourceRequest request, ResourceResponse response, @RequestParam(name = "editorId") String editorId)
            throws PortletException, IOException {
        super.serveResourceEditor(request, response, editorId);
    }


    /**
     * Get calendar edition options model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return calendar edition options
     * @throws PortletException
     */
    @ModelAttribute("options")
    public CalendarEditionOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getEditionOptions(portalControllerContext);
    }
    

    /**
     * Get calendar event edition form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return calendar event edition form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public CalendarCommonEventForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Calendar event edition form init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.setDisallowedFields("calendarColor", "startDate", "endDate");
        binder.addValidators(this.formValidator);
        binder.registerCustomEditor(CalendarColor.class, this.service.getCalendarColorPropertyEditor());
    }

    
    /**
     * Manage upload errors
     * 
     * @param ex
     * @param request
     * @param response
     * @return
     * @throws PortletException
     * @throws IOException
     */
	@ExceptionHandler(MultipartException.class)
	String handleFileException(Throwable ex, RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		return "upload-error";

	}    
	
}
