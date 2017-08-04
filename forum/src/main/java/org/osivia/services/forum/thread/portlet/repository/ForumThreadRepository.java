package org.osivia.services.forum.thread.portlet.repository;

import net.sf.json.JSONArray;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.forum.thread.portlet.model.ForumThreadForm;
import org.osivia.services.forum.thread.portlet.model.ForumThreadObject;
import org.osivia.services.forum.thread.portlet.model.ForumThreadOptions;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Forum thread portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ForumThreadRepository {

    /** Closed forum thread Nuxeo document property. */
    String CLOSED_PROPERTY = "ttc:commentsForbidden";


    /**
     * Insert menubar items.
     *
     * @param portalControllerContext portal controller context
     * @param document                forum thread Nuxeo document
     * @throws PortletException
     */
    void insertMenubarItems(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Check if forum thread is closed.
     *
     * @param portalControllerContext portal controller context
     * @param thread                  forum thread Nuxeo document
     * @return true if forum thread is closed
     * @throws PortletException
     */
    boolean isClosed(PortalControllerContext portalControllerContext, Document thread) throws PortletException;


    /**
     * Update forum thread post.
     *
     * @param portalControllerContext portal controller context
     * @param post                    forum thread post
     * @param options                 forum thread options
     * @return updated forum thread post
     * @throws PortletException
     * @throws IOException
     */
    ForumThreadObject updatePost(PortalControllerContext portalControllerContext, ForumThreadObject post, ForumThreadOptions options) throws
            PortletException, IOException;


    /**
     * Delete forum thread post.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @param options                 forum thread options
     * @throws PortletException
     */
    void deletePost(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException;


    /**
     * Create forum thread post.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @param options                 thread form options
     * @return created forum thread post
     * @throws PortletException
     * @throws IOException
     */
    ForumThreadObject createPost(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException,
            IOException;


    /**
     * Toggle forum thread closure.
     *
     * @param portalControllerContext portal controller context
     * @param thread                  forum thread Nuxeo document
     * @param close                   closure indicator
     * @return updated forum thread Nuxeo document
     * @throws PortletException
     */
    Document toggleThreadClosure(PortalControllerContext portalControllerContext, Document thread, boolean close) throws PortletException;


    /**
     * Set forum thread form properties.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @throws PortletException
     * @throws IOException
     */
    void setProperties(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException;


    /**
     * Get forum thread Nuxeo document.
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo document
     * @throws PortletException
     */
    Document getThread(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get forum thread posts JSON array.
     *
     * @param portalControllerContext portal controller context.
     * @param thread                  forum thread Nuxeo document
     * @return JSON array
     * @throws PortletException
     * @throws IOException
     */
    JSONArray getPosts(PortalControllerContext portalControllerContext, Document thread) throws PortletException, IOException;

}
