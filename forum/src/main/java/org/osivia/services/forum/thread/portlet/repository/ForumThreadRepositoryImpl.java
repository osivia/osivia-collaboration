package org.osivia.services.forum.thread.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nuxeo.ecm.automation.client.model.*;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.forum.thread.portlet.model.*;
import org.osivia.services.forum.thread.portlet.repository.command.*;
import org.osivia.services.forum.util.model.ForumFile;
import org.osivia.services.forum.util.model.ForumFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Forum thread portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see ForumThreadRepository
 */
@Repository
public class ForumThreadRepositoryImpl implements ForumThreadRepository {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;

    /**
     * Forum thread parser.
     */
    @Autowired
    private ForumThreadParser parser;


    /**
     * Constructor.
     */
    public ForumThreadRepositoryImpl() {
        super();
    }


    @Override
    public void insertMenubarItems(PortalControllerContext portalControllerContext, Document document) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCurrentDoc(document);

        // Menubar
        nuxeoController.insertContentMenuBarItems();
    }


    @Override
    public boolean isClosed(PortalControllerContext portalControllerContext, Document thread) {
        return thread.getProperties().getBoolean(CLOSED_PROPERTY, false);
    }


    @Override
    public ForumThreadObject updatePost(PortalControllerContext portalControllerContext, ForumThreadObject post, ForumThreadOptions options) throws
            PortletException, IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Forum thread post identifier
        String id = post.getId();
        // Forum thread post message
        String message = this.parser.parse(portalControllerContext, post.getMessage(), ForumThreadParserAction.SAVE);
        // Forum thread post attachments
        ForumFiles attachments = post.getAttachments();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateForumThreadPostCommand.class, id, message, attachments);
        nuxeoController.executeNuxeoCommand(command);

        // Reload forum thread posts
        ForumThreadPosts posts = this.getThreadPosts(portalControllerContext, options.getDocument());

        // Search updated forum thread post
        ForumThreadObject updatedPost = null;
        for (ForumThreadObject p : posts.getList()) {
            if (StringUtils.equals(post.getId(), p.getId())) {
                updatedPost = p;
                break;
            }
        }

        return updatedPost;
    }


    @Override
    public void deletePost(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Forum thread Nuxeo document
        Document document = options.getDocument();
        // Deleted forum thread post identifier
        String id = form.getPosts().getDeletedId();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(DeleteForumThreadPostCommand.class, document, id);
        nuxeoController.executeNuxeoCommand(command);
    }


    @Override
    public ForumThreadObject createPost(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws
            PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        // Forum thread Nuxeo document
        Document document = options.getDocument();
        // Forum thread post author
        String author = request.getRemoteUser();
        // Forum thread post message
        String message = this.parser.parse(portalControllerContext, form.getReply().getMessage(), ForumThreadParserAction.SAVE);
        // Forum thread post attachments
        ForumFiles attachments = form.getReply().getAttachments();


        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CreateForumThreadPostCommand.class, document, author, message, attachments);
        Document postDocument = (Document) nuxeoController.executeNuxeoCommand(command);
        String postId = postDocument.getId();

        // Reload forum thread posts
        ForumThreadPosts posts = this.getThreadPosts(portalControllerContext, options.getDocument());

        // Search added forum thread post
        ForumThreadObject addedPost = null;
        for (ForumThreadObject post : posts.getList()) {
            if (StringUtils.equals(postId, post.getId())) {
                addedPost = post;
                break;
            }
        }

        return addedPost;
    }


    @Override
    public Document toggleThreadClosure(PortalControllerContext portalControllerContext, Document thread, boolean close) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(ToggleForumThreadClosure.class, thread, close);
        nuxeoController.executeNuxeoCommand(command);

        // Refresh Nuxeo document
        NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();
        documentContext.reload();

        return documentContext.getDocument();
    }


    @Override
    public void setProperties(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();
        // Nuxeo document
        Document document = documentContext.getDocument();
        // Permissions
        NuxeoPermissions permissions = documentContext.getPermissions();

        // Forum thread
        ForumThreadObject thread = this.getThread(nuxeoController, document);
        form.setThread(thread);

        // Forum thread posts
        ForumThreadPosts posts = this.getThreadPosts(portalControllerContext, document);
        form.setPosts(posts);

        // Forum thread reply
        ForumThreadObject reply = this.getThreadReply(nuxeoController);
        form.setReply(reply);

        // Document
        form.setDocument(this.documentDao.toDTO(document));

        // Commentable indicator
        boolean commentable = permissions.isCommentable();
        form.setCommentable(commentable);
    }


    @Override
    public Document getThread(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();

        // Nuxeo document
        return documentContext.getDocument();
    }


    @Override
    public JSONArray getPosts(PortalControllerContext portalControllerContext, Document thread) throws IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetForumThreadPostsCommand.class, thread);

        // Blob result
        Blob blob = (Blob) nuxeoController.executeNuxeoCommand(command);
        // JSON result
        String json = IOUtils.toString(blob.getStream(), CharEncoding.UTF_8);

        if (blob instanceof HasFile) {
            // Deleting the file
            HasFile hasFile = (HasFile) blob;
            if (!hasFile.getFile().delete()) {
                hasFile.getFile().deleteOnExit();
            }
        }


        // JSON array
        return JSONArray.fromObject(json);
    }


    /**
     * Get forum thread.
     *
     * @param nuxeoController Nuxeo controller
     * @param document        thread Nuxeo document
     * @return forum thread
     */
    private ForumThreadObject getThread(NuxeoController nuxeoController, Document document) {
        // Forum thread
        ForumThreadObject thread = this.applicationContext.getBean(ForumThreadObject.class);

        // Identifier
        String id = document.getId();
        thread.setId(id);

        // User name
        String user = document.getString("dc:creator");
        thread.setUser(user);

        // Date
        Date date = document.getDate("dc:created");
        thread.setDate(date);

        // Modified date
        Date modified = document.getDate("dc:modified");
        if (Math.abs(modified.getTime() - date.getTime()) > TimeUnit.MINUTES.toMillis(1)) {
            thread.setDate(date);
        }

        // Message
        String message = document.getString("ttcth:message");
        thread.setMessage(message);

        // Attachments
        ForumFiles attachments = this.applicationContext.getBean(ForumFiles.class);
        thread.setAttachments(attachments);

        // Attachment files
        PropertyList attachmentsList = document.getProperties().getList("files:files");
        if (attachmentsList != null) {
            List<ForumFile> files = new ArrayList<>(attachmentsList.size());

            for (int i = 0; i < attachmentsList.size(); i++) {
                PropertyMap map = attachmentsList.getMap(i);
                PropertyMap fileMap = map.getMap("file");

                // Attachment file
                ForumFile file = this.applicationContext.getBean(ForumFile.class);

                // File name
                String fileName = fileMap.getString("name");
                file.setFileName(fileName);

                // URL
                String url = nuxeoController.createAttachedFileLink(document.getPath(), String.valueOf(i));
                file.setUrl(url);

                files.add(file);
            }

            attachments.setFiles(files);
        }

        return thread;
    }


    /**
     * Get forum thread posts.
     *
     * @param portalControllerContext portal controller context
     * @param document        thread Nuxeo document
     * @return forum thread posts
     */
    private ForumThreadPosts getThreadPosts(PortalControllerContext portalControllerContext, Document document) throws PortletException, IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // JSON array
        JSONArray array = this.getPosts(portalControllerContext, document);

        // Forum thread posts
        ForumThreadPosts posts = this.applicationContext.getBean(ForumThreadPosts.class);

        // Forum thread posts list
        List<ForumThreadObject> list = this.toPosts(nuxeoController, array);
        posts.setList(list);

        return posts;
    }


    /**
     * Convert JSON array to forum thread posts.
     *
     * @param nuxeoController Nuxeo controller
     * @param array           JSON array
     * @return forum thread posts
     */
    private List<ForumThreadObject> toPosts(NuxeoController nuxeoController, JSONArray array) throws PortletException {
        List<ForumThreadObject> posts = new ArrayList<>(array.size());

        for (int i = 0; i < array.size(); i++) {
            // JSON object
            JSONObject object = array.getJSONObject(i);
            ForumThreadObject post = toPost(nuxeoController, object);


            // Add post
            posts.add(post);
        }

        return posts;
    }


    /**
     * Convert JSON object to forum thread post.
     *
     * @param nuxeoController Nuxeo controller
     * @param object          JSON object
     * @return forum thread post
     */
    private ForumThreadObject toPost(NuxeoController nuxeoController, JSONObject object) throws PortletException {
        // Forum thread post
        ForumThreadObject post = this.applicationContext.getBean(ForumThreadObject.class);

        // Identifier
        String id = object.getString("id");
        post.setId(id);

        // User
        String user = object.getString("author");
        post.setUser(user);

        // Date
        JSONObject dateObject = object.getJSONObject("creationDate");
        long dateTime = dateObject.getLong("timeInMillis");
        Date date = new Date(dateTime);
        post.setDate(date);

        // Modified date
        JSONObject modifiedObject = object.getJSONObject("modifiedDate");
        long modifiedTime = modifiedObject.getLong("timeInMillis");
        if (Math.abs(modifiedTime - dateTime) > TimeUnit.MINUTES.toMillis(1)) {
            Date modified = new Date(modifiedTime);
            post.setModified(modified);
        }

        // Message
        String message = this.parser.parse(nuxeoController.getPortalCtx(), object.getString("content"), ForumThreadParserAction.LOAD);
        post.setMessage(message);

        // Attachments
        ForumFiles attachments = this.getAttachments(nuxeoController, object);
        post.setAttachments(attachments);

        // Editable indicator
        boolean editable = object.getBoolean("canDelete");
        post.setEditable(editable);

        return post;
    }


    /**
     * Get attachments.
     *
     * @param nuxeoController Nuxeo controller
     * @param threadObject    JSON object
     * @return attachments
     */
    private ForumFiles getAttachments(NuxeoController nuxeoController, JSONObject threadObject) {
        // Attachments JSON array
        JSONArray attachmentsArray = threadObject.getJSONArray("files");

        // Attachments
        ForumFiles attachments = this.applicationContext.getBean(ForumFiles.class);

        // Attachment files
        List<ForumFile> files = new ArrayList<>(attachmentsArray.size());

        for (int i = 0; i < attachmentsArray.size(); i++) {
            // Attachment JSON object
            JSONObject attachmentObject = attachmentsArray.getJSONObject(i);

            // Attachment file
            ForumFile file = this.applicationContext.getBean(ForumFile.class);

            // Index
            String index = attachmentObject.getString("index");
            file.setBlobIndex(NumberUtils.toInt(index));

            // File name
            String fileName = attachmentObject.getString("filename");
            file.setFileName(fileName);

            // URL
            String url = nuxeoController.createAttachedFileLink(threadObject.getString("path"), index);
            file.setUrl(url);

            files.add(file);
        }

        attachments.setFiles(files);

        return attachments;
    }


    /**
     * Get forum thread reply.
     *
     * @param nuxeoController Nuxeo controller
     * @return forum thread reply
     */
    private ForumThreadObject getThreadReply(NuxeoController nuxeoController) {
        // Portlet request
        PortletRequest request = nuxeoController.getRequest();

        // Forum thread reply
        ForumThreadObject reply = this.applicationContext.getBean(ForumThreadObject.class);

        // User
        String user = request.getRemoteUser();
        reply.setUser(user);

        return reply;
    }

}
