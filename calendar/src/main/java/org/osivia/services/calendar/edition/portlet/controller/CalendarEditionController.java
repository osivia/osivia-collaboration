package org.osivia.services.calendar.edition.portlet.controller;

import java.io.IOException;

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

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.model.ICalendarColor;
import org.osivia.services.calendar.common.model.converter.CalendarColorPropertyEditor;
import org.osivia.services.calendar.edition.portlet.model.CalendarEditionForm;
import org.osivia.services.calendar.edition.portlet.model.validation.CalendarFormValidator;
import org.osivia.services.calendar.edition.portlet.service.CalendarEditionService;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Calendar edition portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class CalendarEditionController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private CalendarEditionService service;

    /** Calendar form validator. */
    @Autowired
    private CalendarFormValidator formValidator;

    /** Calendar color property editor. */
    @Autowired
    private CalendarColorPropertyEditor calendarColorPropertyEditor;


    /**
     * Constructor.
     */
    public CalendarEditionController() {
        super();
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
     * Upload vignette action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form calendar form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "upload-vignette")
    public void uploadVignette(ActionRequest request, ActionResponse response, @ModelAttribute("form") CalendarEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadVignette(portalControllerContext, form);
    }


    /**
     * Delete vignette action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form calendar form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "delete-vignette")
    public void deleteVignette(ActionRequest request, ActionResponse response, @ModelAttribute("form") CalendarEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteVignette(portalControllerContext, form);
    }


    /**
     * Edit synchronization source.
     * 
     * @param request action request
     * @param response action response
     * @param form calendar form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "edit-synchronization-source")
    public void editSynchronizationSource(ActionRequest request, ActionResponse response, @ModelAttribute("form") CalendarEditionForm form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.editSynchronizationSource(portalControllerContext, form);
    }


    /**
     * Add synchronization source.
     * 
     * @param request action request
     * @param response action response
     * @param form calendar edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "add-synchronization-source")
    public void addSynchronizationSource(ActionRequest request, ActionResponse response, @ModelAttribute("form") CalendarEditionForm form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.addSynchronizationSource(portalControllerContext, form);
    }


    /**
     * Save action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param options calendar edition options model attribute
     * @param form calendar edition form model attribute
     * @param result binding result
     * @param status session status
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("options") CalendarEditionOptions options,
            @Validated @ModelAttribute("form") CalendarEditionForm form, BindingResult result, SessionStatus status) throws PortletException, IOException {
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
     * @param status session status
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("cancel")
    public void cancel(ActionRequest request, ActionResponse response, SessionStatus status) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.cancel(portalControllerContext);

        status.setComplete();
    }


    /**
     * Remove synchronization source.
     * 
     * @param request action request
     * @param response action response
     * @param sourceId removed synchronization source identifier request parameter
     * @param form calendar edition form model attribute
     * @throws PortletException
     */
    @ActionMapping("remove-synchronization-source")
    public void removeSynchronisationSource(ActionRequest request, ActionResponse response, @RequestParam("sourceId") String sourceId,
            @ModelAttribute("form") CalendarEditionForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.removeSynchronizationSource(portalControllerContext, form, sourceId);
    }


    /**
     * Vignette preview resource mapping.
     *
     * @param request resource request
     * @param response resource response
     * @param form calendar edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("vignette-preview")
    public void vignettePreview(ResourceRequest request, ResourceResponse response, @ModelAttribute("form") CalendarEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.vignettePreview(portalControllerContext, form);
    }


    /**
     * Synchronization source edition URL.
     * 
     * @param request resource request
     * @param response resource response
     * @param sourceId synchronization source identifier request parameter, null in case of creation
     * @param form calendar edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("synchronization-source-edition-url")
    public void synchronizationSourceEditionUrl(ResourceRequest request, ResourceResponse response,
            @RequestParam(name = "sourceId", required = false) String sourceId, @ModelAttribute("form") CalendarEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.synchronizationSourceEditionUrl(portalControllerContext, form, sourceId);
    }


    /**
     * Get calendar edition options model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return options
     * @throws PortletException
     */
    @ModelAttribute("options")
    public CalendarEditionOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getEditionOptions(portalControllerContext);
    }


    /**
     * Get calendar edition form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public CalendarEditionForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Calendar edition form init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.formValidator);
        binder.registerCustomEditor(ICalendarColor.class, this.calendarColorPropertyEditor);
    }

}
