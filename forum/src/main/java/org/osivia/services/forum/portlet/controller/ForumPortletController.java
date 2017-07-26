package org.osivia.services.forum.portlet.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.*;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.forum.portlet.model.Thread;
import org.osivia.services.forum.portlet.model.ThreadPost;
import org.osivia.services.forum.portlet.model.ThreadPostReplyForm;
import org.osivia.services.forum.portlet.service.IForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Forum portlet controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletContextAware
 * @see PortletConfigAware
 */
@Controller
@RequestMapping("VIEW")
public class ForumPortletController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    /** View path. */
    private static final String VIEW_PATH = "view";

    /** Forum service. */
    @Autowired
    private IForumService forumService;

    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;


    /**
     * Default constructor.
     */
    public ForumPortletController() {
        super();
    }


    /**
     * Portlet initialization.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * View page render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view page path
     * @throws PortletException
     * @throws CMSException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);
        // Current Nuxeo document
        Document document = this.forumService.getDocument(nuxeoController);

        // Title
        response.setTitle(document.getTitle());

        // Menubar
        nuxeoController.setCurrentDoc(document);
        nuxeoController.insertContentMenuBarItems();

        return VIEW_PATH;
    }


    /**
     * Add comment action.
     *
     * @param request action request
     * @param response action response
     * @param posts thread posts
     * @param replyForm thread post reply form
     * @param parentId parent thread post identifier, may be null
     * @throws PortletException
     */
    @ActionMapping(value = "add")
    public void addCommentAction(ActionRequest request, ActionResponse response, @ModelAttribute(value = "posts") List<ThreadPost> posts, @ModelAttribute(
            value = "replyForm") ThreadPostReplyForm replyForm, @RequestParam(value = "parentId", required = false) String parentId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);

        this.forumService.addThreadPost(nuxeoController, posts, replyForm, parentId);
    }


    /**
     * Delete comment action.
     *
     * @param request action request
     * @param response action response
     * @param posts thread posts
     * @param id comment identifier
     * @throws PortletException
     */
    @ActionMapping(value = "delete")
    public void deleteCommentAction(ActionRequest request, ActionResponse response, @ModelAttribute(value = "posts") List<ThreadPost> posts, @RequestParam(
            value = "id", required = true) String id) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);

        this.forumService.deleteThreadPost(nuxeoController, posts, id);
    }


    /**
     * Get current thread.
     *
     * @param request portlet request
     * @param response portlet response
     * @return current thread
     * @throws PortletException
     */
    @ModelAttribute(value = "thread")
    public Thread getThread(PortletRequest request, PortletResponse response) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);

        return this.forumService.getThread(nuxeoController);
    }


    /**
     * Get thread posts.
     *
     * @param request portlet request
     * @param response portlet response
     * @return thread description
     * @throws PortletException
     */
    @ModelAttribute(value = "posts")
    public List<ThreadPost> getPosts(PortletRequest request, PortletResponse response) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);

        return this.forumService.getThreadPosts(nuxeoController);
    }


    /**
     * Get thread post reply form.
     *
     * @param request portlet request
     * @param response portlet response
     * @return new thread post
     * @throws PortletException
     */
    @ModelAttribute(value = "replyForm")
    public ThreadPostReplyForm getReplyForm(PortletRequest request, PortletResponse response) throws PortletException {
        return new ThreadPostReplyForm();
    }


    /**
     * Get editor properties resource mapping.
     *
     * @param request resource request
     * @param response resource response
     * @param editorId editor identifier required request parameter
     */
    @ResourceMapping("editor")
    public void getEditor(ResourceRequest request, ResourceResponse response, @RequestParam(name = "editorId") String editorId) throws PortletException, IOException {
        super.serveResourceEditor(request, response, editorId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

}
