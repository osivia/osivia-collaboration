package org.osivia.services.forum.thread.portlet.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.forum.thread.portlet.model.*;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Forum thread portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ForumThreadService {

    /**
     * Max upload size.
     */
    long MAX_UPLOAD_SIZE = NumberUtils.toLong(System.getProperty("osivia.forum.max.upload.size"), 10L) * FileUtils.ONE_MB;


    /**
     * Get view path.
     *
     * @param portalControllerContext portal controller context
     * @param options                 forum thread options
     * @return view path
     * @throws PortletException
     */
    String view(PortalControllerContext portalControllerContext, ForumThreadOptions options) throws PortletException;


    /**
     * Edit forum thread post.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @throws PortletException
     */
    void editPost(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException;


    /**
     * Upload post attachment.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @throws PortletException
     * @throws IOException
     */
    void uploadPostAttachment(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException;


    /**
     * Delete post attachment.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @throws PortletException
     * @throws IOException
     */
    void deletePostAttachment(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException;


    /**
     * Save forum thread post edition.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @param options                 forum thread options
     * @throws PortletException
     * @throws IOException
     */
    void savePostEdition(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException,
            IOException;


    /**
     * Cancel forum thread post edition.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @throws PortletException
     * @throws IOException
     */
    void cancelPostEdition(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException;


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
     * Upload reply attachment.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @throws PortletException
     * @throws IOException
     */
    void uploadReplyAttachment(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException;


    /**
     * Delete reply attachment.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @throws PortletException
     * @throws IOException
     */
    void deleteReplyAttachment(PortalControllerContext portalControllerContext, ForumThreadForm form) throws PortletException, IOException;


    /**
     * Reply.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @param options                 forum thread options
     * @throws PortletException
     * @throws IOException
     */
    void reply(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException, IOException;


    /**
     * Close forum thread.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @param options                 forum thread options
     * @throws PortletException
     */
    void closeThread(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException;


    /**
     * Reopen forum thread.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @param options                 forum thread options
     * @throws PortletException
     */
    void reopenThread(PortalControllerContext portalControllerContext, ForumThreadForm form, ForumThreadOptions options) throws PortletException;


    /**
     * Quote.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum thread form
     * @param id                      source identifier
     * @throws PortletException
     * @throws IOException
     */
    void quote(PortalControllerContext portalControllerContext, ForumThreadForm form, String id) throws PortletException, IOException;


    /**
     * Get forum thread form.
     *
     * @param portalControllerContext portal controller context
     * @return forum thread form
     * @throws PortletException
     * @throws IOException
     */
    ForumThreadForm getForm(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Get forum thread options.
     *
     * @param portalControllerContext portal controller context
     * @return forum thread options
     * @throws PortletException
     */
    ForumThreadOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;

}
