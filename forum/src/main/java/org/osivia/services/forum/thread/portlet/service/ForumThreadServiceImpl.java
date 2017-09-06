package org.osivia.services.forum.thread.portlet.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.services.forum.thread.portlet.model.ForumThreadForm;
import org.osivia.services.forum.thread.portlet.model.ForumThreadObject;
import org.osivia.services.forum.thread.portlet.model.ForumThreadOptions;
import org.osivia.services.forum.thread.portlet.model.ForumThreadParser;
import org.osivia.services.forum.thread.portlet.model.ForumThreadParserAction;
import org.osivia.services.forum.thread.portlet.model.ForumThreadPosts;
import org.osivia.services.forum.thread.portlet.repository.ForumThreadRepository;
import org.osivia.services.forum.util.model.ForumFile;
import org.osivia.services.forum.util.model.ForumFiles;
import org.osivia.services.forum.util.service.AbstractForumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Forum thread portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractForumServiceImpl
 * @see ForumThreadService
 */
@Service
public class ForumThreadServiceImpl extends AbstractForumServiceImpl implements ForumThreadService {

    /** Menubar item identifier prefix. */
    private static final String MENUBAR_ITEM_PREFIX = "FORUM_THREAD_MENUBAR_";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private ForumThreadRepository repository;

    /** Forum thread parser. */
    @Autowired
    private ForumThreadParser parser;

    /** Menubar service. */
    @Autowired
    private IMenubarService menubarService;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public ForumThreadServiceImpl() {
        super();
    }


    @Override
    public String view(PortalControllerContext portalControllerContext, ForumThreadOptions options) throws PortletException {
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // Nuxeo document
        Document thread = options.getDocument();

        // Add specific menubar items
        this.addMenubarItems(portalControllerContext, thread);
        // Insert and customize menubar items
        this.repository.insertMenubarItems(portalControllerContext, thread);

        if (response instanceof RenderResponse) {
            // Render response
            RenderResponse renderResponse = (RenderResponse) response;

            // Title
            renderResponse.setTitle(thread.getTitle());
        }

        return "view";
    }


    /**
     * Add menubar items.
     *
     * @param portalControllerContext portal controller context
     * @param thread                  forum thread Nuxeo document
     * @throws PortletException
     */
    @SuppressWarnings("unchecked")
    private void addMenubarItems(PortalControllerContext portalControllerContext, Document thread) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        if (response instanceof MimeResponse) {
            // Mime response
            MimeResponse mimeResponse = (MimeResponse) response;

            // Menubar
            List<MenubarItem> menubar = (List<MenubarItem>) request.getAttribute(Constants.PORTLET_ATTR_MENU_BAR);

            // Toggle thread closed status
            String id;
            String icon;
            MenubarDropdown dropdown = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.OTHER_OPTIONS_DROPDOWN_MENU_ID);
            if (dropdown == null) {
                dropdown = new MenubarDropdown(MenubarDropdown.OTHER_OPTIONS_DROPDOWN_MENU_ID, MenubarGroup.GENERIC);
            }
            int order = 14;
            PortletURL url = mimeResponse.createActionURL();
            if (this.repository.isClosed(portalControllerContext, thread)) {
                id = MENUBAR_ITEM_PREFIX + "REOPEN";
                icon = "glyphicons glyphicons-unlock";
                url.setParameter(ActionRequest.ACTION_NAME, "reopen");

                // Closed indicator
                String indicatorId = MENUBAR_ITEM_PREFIX + "CLOSED";
                MenubarItem indicator = new MenubarItem(indicatorId, bundle.getString(indicatorId), MenubarGroup.SPECIFIC, 0, "label label-default");
                indicator.setGlyphicon("halflings halflings-lock");
                indicator.setState(true);
                menubar.add(indicator);
            } else {
                id = MENUBAR_ITEM_PREFIX + "CLOSE";
                icon = "glyphicons glyphicons-lock";
                url.setParameter(ActionRequest.ACTION_NAME, "close");
            }
            MenubarItem item = new MenubarItem(id, bundle.getString(id), icon, dropdown, order, url.toString(), null, null, null);
            menubar.add(item);
        }
    }


    @Override
    public void editPost(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException {
        // Forum thread posts
        ForumThreadPosts posts = form.getPosts();

        // Edited forum thread post
        ForumThreadObject editedPost = null;
        for (ForumThreadObject post : posts.getList()) {
            if (StringUtils.equals(posts.getEditedId(), post.getId())) {
                editedPost = post.clone();
                break;
            }
        }

        // Update model
        posts.setEditedPost(editedPost);
        posts.setEditedId(null);
    }


    @Override
    public void uploadPostAttachment(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException {
        // Posts
        ForumThreadPosts posts = form.getPosts();
        // Edited post
        ForumThreadObject editedPost = posts.getEditedPost();
        // Attachments
        ForumFiles attachments = editedPost.getAttachments();

        this.uploadAttachments(attachments);
    }


    @Override
    public void deletePostAttachment(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException {
        // Posts
        ForumThreadPosts posts = form.getPosts();
        // Edited post
        ForumThreadObject editedPost = posts.getEditedPost();
        // Attachments
        ForumFiles attachments = editedPost.getAttachments();

        this.deleteAttachment(attachments);
    }


    @Override
    public void savePostEdition(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException,
            IOException {
        // Forum thread posts
        ForumThreadPosts posts = form.getPosts();

        // Edited forum thread post
        ForumThreadObject editedPost = posts.getEditedPost();
        // Updated forum thread post
        ForumThreadObject updatedPost = this.repository.updatePost(portalControllerContext, editedPost, options);


        // Update model
        for (ForumThreadObject post : posts.getList()) {
            if (StringUtils.equals(updatedPost.getId(), post.getId())) {
                try {
                    BeanUtils.copyProperties(post, updatedPost);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new PortletException(e);
                }

                break;
            }
        }
        posts.setEditedPost(null);
    }


    @Override
    public void cancelPostEdition(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException {
        // Forum thread posts
        ForumThreadPosts posts = form.getPosts();

        // Edited forum thread post
        ForumThreadObject editedPost = posts.getEditedPost();
        if (editedPost != null) {
            // Attachments
            ForumFiles attachments = editedPost.getAttachments();
            if (CollectionUtils.isNotEmpty(attachments.getFiles())) {
                for (ForumFile file : attachments.getFiles()) {
                    // Temporary file
                    File temporaryFile = file.getTemporaryFile();
                    if (temporaryFile != null) {
                        temporaryFile.delete();
                    }
                }
            }
        }

        // Update model
        posts.setEditedPost(null);
    }


    @Override
    public void deletePost(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException {
        this.repository.deletePost(portalControllerContext, form, options);

        // Forum thread posts
        ForumThreadPosts posts = form.getPosts();

        // Deleted forum thread post
        ForumThreadObject deletedPost = null;
        for (ForumThreadObject post : posts.getList()) {
            if (StringUtils.equals(posts.getDeletedId(), post.getId())) {
                deletedPost = post;
                break;
            }
        }

        // Update model
        if (deletedPost != null) {
            posts.getList().remove(deletedPost);
        }
        posts.setDeletedId(null);
    }


    @Override
    public void uploadReplyAttachment(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException {
        // Reply
        ForumThreadObject reply = form.getReply();
        // Attachments
        ForumFiles attachments = reply.getAttachments();

        this.uploadAttachments(attachments);
    }


    @Override
    public void deleteReplyAttachment(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException {
        // Reply
        ForumThreadObject reply = form.getReply();
        // Attachments
        ForumFiles attachments = reply.getAttachments();

        this.deleteAttachment(attachments);
    }


    @Override
    public void reply(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException, IOException {
        // Added forum thread post
        ForumThreadObject addedPost = this.repository.createPost(portalControllerContext, form, options);

        // Forum thread posts list
        List<ForumThreadObject> list = form.getPosts().getList();
        if (list == null) {
            list = new ArrayList<>();
            form.getPosts().setList(list);
        }

        // Update model
        form.getReply().setMessage(null);
        form.getReply().setAttachments(null);
        if (addedPost != null) {
            list.add(addedPost);
        }
    }


    @Override
    public void closeThread(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException {
        this.toggleThreadClosure(portalControllerContext, form, options, true);
    }


    @Override
    public void reopenThread(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException {
        this.toggleThreadClosure(portalControllerContext, form, options, false);
    }


    /**
     * Toggle forum thread closure.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @param options                 forum thread options
     * @param close                   closure indicator
     * @throws PortletException
     */
    private void toggleThreadClosure(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options, boolean close) throws
            PortletException {
        // Forum thread Nuxeo document
        Document thread = options.getDocument();

        // Updated forum thread Nuxeo document
        Document updatedThread = this.repository.toggleThreadClosure(portalControllerContext, thread, close);

        // Update model
        form.setCommentable(!close);
        options.setDocument(updatedThread);
    }


    @Override
    public void quote(PortalControllerContext portalControllerContext, ForumThreadForm form, String id) throws PortletException, IOException {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();

        if (portletResponse instanceof MimeResponse) {
            // Mime response
            MimeResponse mimeResponse = (MimeResponse) portletResponse;

            // Forum thread object
            ForumThreadObject object = null;
            if (StringUtils.equals(id, form.getThread().getId())) {
                object = form.getThread();
            } else {
                for (ForumThreadObject post : form.getPosts().getList()) {
                    if (StringUtils.equals(id, post.getId())) {
                        object = post;
                        break;
                    }
                }
            }

            if (object != null) {
                // User
                String user = object.getUser();

                // Blockquote
                Element blockquote = DOM4JUtils.generateElement("blockquote", null, object.getMessage());
                DOM4JUtils.addDataAttribute(blockquote, "id", id);
                DOM4JUtils.addDataAttribute(blockquote, "author", user);

                // Empty paragraph
                Element emptyParagraph = DOM4JUtils.generateElement("p", null, StringUtils.EMPTY);


                // Content type
                mimeResponse.setContentType("text/html");

                // Content
                PrintWriter printWriter = new PrintWriter(mimeResponse.getPortletOutputStream());
                printWriter.write(this.parser.parse(portalControllerContext, DOM4JUtils.writeCompact(blockquote), ForumThreadParserAction.LOAD));
                printWriter.write(DOM4JUtils.writeCompact(emptyParagraph));
                printWriter.close();
            }
        }
    }


    @Override
    public ForumThreadForm getForm(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Forum thread form
        ForumThreadForm form = this.applicationContext.getBean(ForumThreadForm.class);
        this.repository.setProperties(portalControllerContext, form);

        return form;
    }


    @Override
    public ForumThreadOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException {
        // Forum thread options
        ForumThreadOptions options = this.applicationContext.getBean(ForumThreadOptions.class);

        // Forum thread Nuxeo document
        Document thread = this.repository.getThread(portalControllerContext);
        options.setDocument(thread);

        return options;
    }

}
