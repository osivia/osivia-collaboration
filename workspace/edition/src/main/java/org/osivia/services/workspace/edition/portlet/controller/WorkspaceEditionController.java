package org.osivia.services.workspace.edition.portlet.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.PostConstruct;
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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.model.validator.WorkspaceEditionFormValidator;
import org.osivia.services.workspace.edition.portlet.service.WorkspaceEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Workspace edition controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes({"options", "editionForm"})
public class WorkspaceEditionController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;

    /** Workspace edition form validator. */
    @Autowired
    private WorkspaceEditionFormValidator editionFormValidator;

    /** Workspace edition service. */
    @Autowired
    private WorkspaceEditionService service;

    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public WorkspaceEditionController() {
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
        request.setAttribute("back", StringUtils.isNotEmpty(request.getParameter(ActionRequest.ACTION_NAME)));
        return "error";
    }


    @ExceptionHandler(MultipartException.class)
    public String handleMultipartException(PortletRequest request, PortletResponse response, MultipartException exception) {
        request.setAttribute("exception", exception);
        // TODO
        return "error";
    }


    @ExceptionHandler(Exception.class)
    public String handleException(PortletRequest request, PortletResponse response, Exception exception) {
        request.setAttribute("exception", exception);
        // TODO
        return "error";
    }


    /**
     * Sort tasks action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "sort")
    public void sort(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") WorkspaceEditionForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.sort(portalControllerContext, form);
    }


    /**
     * Upload visual action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "upload-visual")
    public void uploadVisual(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") WorkspaceEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadVisual(portalControllerContext, form);
    }


    /**
     * Delete visual action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "delete-visual")
    public void deleteVisual(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") WorkspaceEditionForm form)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteVisual(portalControllerContext, form);
    }


    /**
     * Create editorial.
     * 
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @param result binding result
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "create-editorial")
    public void createEditorial(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") WorkspaceEditionForm form, BindingResult result)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.createEditorial(portalControllerContext, form, result);
    }


    /**
     * Save action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @param result binding result
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") @Validated WorkspaceEditionForm form, BindingResult result)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        if (result.hasErrors()) {
            // Notification
            String message = bundle.getString("MESSAGE_WORKSPACE_EDITION_ERROR");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);
        } else {
            this.service.save(portalControllerContext, form);

            // Redirection
            String url = this.service.getWorkspaceUrl(portalControllerContext, form);
            response.sendRedirect(url);
        }
    }


    /**
     * Cancel action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "cancel")
    public void cancel(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") WorkspaceEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Redirection
        String url = this.service.getWorkspaceUrl(portalControllerContext, form);
        response.sendRedirect(url);
    }


    /**
     * Hide task action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param index task index request parameter
     * @param form workspace edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "hide")
    public void hide(ActionRequest request, ActionResponse response, @RequestParam("index") String index,
            @ModelAttribute("editionForm") WorkspaceEditionForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.hide(portalControllerContext, form, NumberUtils.toInt(index));
    }


    /**
     * Show task action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param index task index request parameter
     * @param form workspace edition form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "show")
    public void show(ActionRequest request, ActionResponse response, @RequestParam("index") String index,
            @ModelAttribute("editionForm") WorkspaceEditionForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.show(portalControllerContext, form, NumberUtils.toInt(index));
    }


    /**
     * Delete workspace action mapping.
     *
     * @param request action request
     * @param response action response
     * @param form workspace edition form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping("delete")
    public void delete(ActionRequest request, ActionResponse response, @ModelAttribute("editionForm") WorkspaceEditionForm form)
            throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        String url = this.service.delete(portalControllerContext, form);

        // Redirection
        response.sendRedirect(url);
    }


    /**
     * Visual preview resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param form workspace edition form model attribute
     * @throws IOException
     */
    @ResourceMapping("visualPreview")
    public void visualPreview(ResourceRequest request, ResourceResponse response, @ModelAttribute("editionForm") WorkspaceEditionForm form)
            throws IOException {
        // Temporary file
        File temporaryFile = form.getVisual().getTemporaryFile();

        // Upload size
        Long size = new Long(temporaryFile.length());
        response.setContentLength(size.intValue());

        // Content type
        String contentType = response.getContentType();
        response.setContentType(contentType);

        // Character encoding
        response.setCharacterEncoding(CharEncoding.UTF_8);

        // No cache
        response.getCacheControl().setExpirationTime(0);


        // Input steam
        InputStream inputSteam = new FileInputStream(temporaryFile);
        // Output stream
        OutputStream outputStream = response.getPortletOutputStream();
        // Copy
        IOUtils.copy(inputSteam, outputStream);
        outputStream.close();
    }


    /**
     * Get edition form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return edition form
     * @throws PortletException
     */
    @ModelAttribute("editionForm")
    public WorkspaceEditionForm getEditionForm(PortletRequest request, PortletResponse response)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Workspace edition form init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("editionForm")
    public void editionFormInitBinder(PortletRequestDataBinder binder) {
        binder.addValidators(this.editionFormValidator);
        binder.setDisallowedFields("root", "initialWorkspaceType");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
