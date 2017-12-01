package org.osivia.services.calendar.synchronization.edition.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarOptions;
import org.osivia.services.calendar.synchronization.edition.portlet.model.CalendarSynchronizationEditionForm;
import org.osivia.services.calendar.synchronization.edition.portlet.model.validation.CalendarSynchronizationEditionFormValidator;
import org.osivia.services.calendar.synchronization.edition.portlet.service.CalendarSynchronizationEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Calendar synchronization source edition portlet controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class CalendarSynchronizationEditionController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private CalendarSynchronizationEditionService service;

    /** Synchronization source edition form validator. */
    @Autowired
    private CalendarSynchronizationEditionFormValidator formValidator;


    /**
     * Constructor.
     */
    public CalendarSynchronizationEditionController() {
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
        return "view";
    }


    /**
     * Submit action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form synchronization source edition form model attribute
     * @param result binding result
     * @param status session status
     * @throws PortletException
     */
    @ActionMapping("submit")
    public void submit(ActionRequest request, ActionResponse response, @Validated @ModelAttribute("form") CalendarSynchronizationEditionForm form,
            BindingResult result, SessionStatus status) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.submit(portalControllerContext, form);

            status.setComplete();
        }
    }


    /**
     * Get calendar options model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return options
     * @throws PortletException
     */
    @ModelAttribute("options")
    public CalendarOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }


    /**
     * Get synchronization source edition form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return synchronization source edition form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public CalendarSynchronizationEditionForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Calendar form init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.formValidator);
        binder.setDisallowedFields("sourceId", "done");
    }

}
