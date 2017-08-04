package org.osivia.services.forum.thread.portlet.controller;

import org.dom4j.io.HTMLWriter;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.forum.thread.portlet.model.ForumThreadForm;
import org.osivia.services.forum.thread.portlet.model.ForumThreadOptions;
import org.osivia.services.forum.thread.portlet.model.validator.ForumThreadFormValidator;
import org.osivia.services.forum.thread.portlet.service.ForumThreadService;
import org.osivia.services.forum.util.controller.AbstractForumController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import javax.swing.*;
import java.io.IOException;

/**
 * Forum thread portlet controller.
 *
 * @author Cédric Krommenhoek
 * @see AbstractForumController
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes({"form", "options"})
public class ThreadForumController extends AbstractForumController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private ForumThreadService service;

    /** Forum thread form validator. */
    @Autowired
    private ForumThreadFormValidator formValidator;


    /**
     * Constructor.
     */
    public ThreadForumController() {
        super();
    }


    /**
     * View render mapping.
     *
     * @param request  render request
     * @param response render response
     * @param options  forum thread options model attribute
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("options") ForumThreadOptions options) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.view(portalControllerContext, options);
    }


    /**
     * Edit forum thread post action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "post-edit")
    public void editPost(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumThreadForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.editPost(portalControllerContext, form);
    }


    /**
     * Upload post attachment action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "post-upload-attachment")
    public void uploadPostAttachment(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumThreadForm form) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadPostAttachment(portalControllerContext, form);
    }


    /**
     * Delete post attachment action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "post-delete-attachment")
    public void deletePostAttachment(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumThreadForm form) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deletePostAttachment(portalControllerContext, form);
    }


    /**
     * Save forum thread post edition action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @param result   binding result
     * @param options  forum thread options model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "post-edition-save")
    public void savePostEdition(ActionRequest request, ActionResponse response, @ModelAttribute("form") @Validated ForumThreadForm form, BindingResult
            result, @ModelAttribute("options") ForumThreadOptions options) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.savePostEdition(portalControllerContext, form, options);
        }
    }


    /**
     * Cancel forum thread post edition action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "post-edition-cancel")
    public void cancelPostEdition(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumThreadForm form) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.cancelPostEdition(portalControllerContext, form);
    }


    /**
     * Delete forum thread post action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @param options  forum thread options model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "save", params = "post-delete")
    public void deletePost(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumThreadForm form, @ModelAttribute("options")
            ForumThreadOptions options) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deletePost(portalControllerContext, form, options);
    }


    /**
     * Upload reply attachment action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "reply-upload-attachment")
    public void uploadReplyAttachment(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumThreadForm form) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.uploadReplyAttachment(portalControllerContext, form);
    }


    /**
     * Delete reply attachment action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "reply-delete-attachment")
    public void deleteReplyAttachment(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumThreadForm form) throws PortletException,
            IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.deleteReplyAttachment(portalControllerContext, form);
    }


    /**
     * Reply action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @param result   binding result
     * @param options  forum thread options model attribute
     * @throws PortletException
     * @throws IOException
     */
    @ActionMapping(name = "save", params = "reply-save")
    public void reply(ActionRequest request, ActionResponse response, @ModelAttribute("form") @Validated ForumThreadForm form, BindingResult result,
                      @ModelAttribute("options") ForumThreadOptions options) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        if (!result.hasErrors()) {
            this.service.reply(portalControllerContext, form, options);
        }
    }


    /**
     * Close forum thread action mapping.
     *
     * @param request  action request
     * @param response action response
     * @param form     forum thread form model attribute
     * @param options  forum thread options model attribute
     * @throws PortletException
     */
    @ActionMapping("close")
    public void closeThread(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumThreadForm form, @ModelAttribute("options")
            ForumThreadOptions options) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.closeThread(portalControllerContext, form, options);
    }


    /**
     * Reopen forum thread action mapping.
     *
     * @param request  action request
     * @param response action respons
     * @param form     forum thread form model attribute
     * @param options  forum thread options model attribute
     * @throws PortletException
     */
    @ActionMapping("reopen")
    public void reopenThread(ActionRequest request, ActionResponse response, @ModelAttribute("form") ForumThreadForm form, @ModelAttribute("options")
            ForumThreadOptions options) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.reopenThread(portalControllerContext, form, options);
    }


    /**
     * Quote resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param form     forum thread form model attribute
     * @param id       source identifier request parameter
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("quote")
    public void quote(ResourceRequest request, ResourceResponse response, @ModelAttribute("form") ForumThreadForm form, @RequestParam("id") String id) throws
            PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.quote(portalControllerContext, form, id);
    }


    /**
     * Get forum thread form.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return forum thread form
     * @throws PortletException
     * @throws IOException
     */
    @ModelAttribute("form")
    public ForumThreadForm getForm(PortletRequest request, PortletResponse response) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * Forum thread form init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.formValidator);
        binder.setDisallowedFields("commentable");
    }


    /**
     * Get forum thread options model attribute.
     *
     * @param request  portlet request
     * @param response portlet response
     * @return forum thread options
     * @throws PortletException
     */
    @ModelAttribute("options")
    public ForumThreadOptions getOptions(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getOptions(portalControllerContext);
    }

}
