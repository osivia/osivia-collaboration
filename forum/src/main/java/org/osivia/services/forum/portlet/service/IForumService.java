package org.osivia.services.forum.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.forum.portlet.model.Thread;
import org.osivia.services.forum.portlet.model.ThreadPost;
import org.osivia.services.forum.portlet.model.ThreadPostReplyForm;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Forum service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface IForumService {

    /**
     * Get current Nuxeo document.
     *
     * @param nuxeoController Nuxeo controller
     * @return current Nuxeo document
     * @throws PortletException
     */
    Document getDocument(NuxeoController nuxeoController) throws PortletException;


    /**
     * Get current thread view object.
     *
     * @param nuxeoController Nuxeo controller
     * @return current thread view object
     * @throws PortletException
     */
    Thread getThread(NuxeoController nuxeoController) throws PortletException;


    /**
     * Get thread posts.
     *
     * @param nuxeoController Nuxeo controller
     * @return thread posts
     * @throws PortletException
     */
    List<ThreadPost> getThreadPosts(NuxeoController nuxeoController) throws PortletException;

    /**
     * Add thread post.
     *
     * @param nuxeoController Nuxeo controller
     * @param posts thread posts
     * @param replyForm thread post reply form
     * @param parentId parent thread post identifier, may be null
     * @throws PortletException
     */
    void addThreadPost(NuxeoController nuxeoController, List<ThreadPost> posts, ThreadPostReplyForm replyForm, String parentId) throws PortletException;


    /**
     * Delete thread post.
     *
     * @param nuxeoController Nuxeo controller
     * @param posts thread posts
     * @param id thread post identifier
     * @throws PortletException
     */
    void deleteThreadPost(NuxeoController nuxeoController, List<ThreadPost> posts, String id) throws PortletException;

}
