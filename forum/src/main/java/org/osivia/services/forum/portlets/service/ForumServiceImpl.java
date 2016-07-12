package org.osivia.services.forum.portlets.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.IDirectoryService;
import org.osivia.portal.api.directory.IDirectoryServiceLocator;
import org.osivia.portal.api.directory.entity.DirectoryPerson;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSPublicationInfos;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.services.forum.portlets.model.Thread;
import org.osivia.services.forum.portlets.model.ThreadObject;
import org.osivia.services.forum.portlets.model.ThreadPost;
import org.osivia.services.forum.portlets.model.ThreadPostReplyForm;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.CommentDTO;
import fr.toutatice.portail.cms.nuxeo.api.domain.ThreadPostDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCommentsService;

/**
 * Forum service implementation.
 *
 * @author Cédric Krommenhoek
 * @see IForumService
 */
@Service
public class ForumServiceImpl implements IForumService {

    /** Document request attribute name. */
    private static final String DOCUMENT_REQUEST_ATTRIBUTE = "osivia.forum.document";

    /** Portal URL factory. */
    private final IPortalUrlFactory portalURLFactory;
    /** Directory service locator. */
    private final IDirectoryServiceLocator directoryServiceLocator;


    /**
     * Default constructor.
     */
    public ForumServiceImpl() {
        super();

        // Portal URL factory
        this.portalURLFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // Directory service locator
        this.directoryServiceLocator = Locator.findMBean(IDirectoryServiceLocator.class, IDirectoryServiceLocator.MBEAN_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Document getDocument(NuxeoController nuxeoController) throws PortletException {
        try {
            Document document = (Document) nuxeoController.getRequest().getAttribute(DOCUMENT_REQUEST_ATTRIBUTE);
            if (document == null) {
                // Request
                PortletRequest request = nuxeoController.getRequest();
                // Response
                PortletResponse response = nuxeoController.getResponse();
                // Portlet context
                PortletContext portletContext = nuxeoController.getPortletCtx();
                // Current window
                PortalWindow window = WindowFactory.getWindow(request);
                // Path
                String path = window.getProperty(Constants.WINDOW_PROP_URI);

                // Nuxeo document
                NuxeoDocumentContext nuxeoDocumentContext = NuxeoController.getDocumentContext(request, response, portletContext, path);
                document = nuxeoDocumentContext.getDoc();

                // Save document in request
                nuxeoController.getRequest().setAttribute(DOCUMENT_REQUEST_ATTRIBUTE, document);
            }
            return document;
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Thread getThread(NuxeoController nuxeoController) throws PortletException {
        Document document = this.getDocument(nuxeoController);
        return this.toViewObject(nuxeoController, document);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ThreadPost> getThreadPosts(NuxeoController nuxeoController) throws PortletException {
        try {
            CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();
            Document document = this.getDocument(nuxeoController);
            INuxeoCommentsService commentsService = nuxeoController.getNuxeoCommentsService();
            List<ThreadPostDTO> postsDTO = commentsService.getForumThreadPosts(cmsContext, document);

            List<ThreadPost> posts = new ArrayList<ThreadPost>(postsDTO.size());
            for (ThreadPostDTO postDTO : postsDTO) {
                ThreadPost post = this.toViewObject(nuxeoController, postDTO);
                posts.add(post);
            }

            return posts;
        } catch (CMSException e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addThreadPost(NuxeoController nuxeoController, List<ThreadPost> posts, ThreadPostReplyForm replyForm, String parentId)
            throws PortletException {
        try {
            // Convert thread post
            ThreadPostDTO post = this.toBusinessObject(replyForm);

            CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();
            Document document = this.getDocument(nuxeoController);
            INuxeoCommentsService commentsService = nuxeoController.getNuxeoCommentsService();
            commentsService.addDocumentComment(cmsContext, document, post, parentId);

            // Update model
            posts.clear();
            posts.addAll(this.getThreadPosts(nuxeoController));
            replyForm.setContent(null);
            replyForm.setAttachment(null);
        } catch (PortletException e) {
            throw e;
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteThreadPost(NuxeoController nuxeoController, List<ThreadPost> posts, String id) throws PortletException {
        try {
            CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();
            Document document = this.getDocument(nuxeoController);
            INuxeoCommentsService commentsService = nuxeoController.getNuxeoCommentsService();
            commentsService.deleteDocumentComment(cmsContext, document, id);

            // Update model
            this.deleteThreadPostFromModel(posts, id);
            posts.clear();
            posts.addAll(this.getThreadPosts(nuxeoController));
        } catch (CMSException e) {
            throw new PortletException(e);
        }
    }


    /**
     * Delete thread post from model.
     *
     * @param posts thread posts model
     * @param id thread post identifier
     * @return true if thread post was successfully deleted
     */
    private boolean deleteThreadPostFromModel(List<ThreadPost> posts, String id) {
        boolean deleted = false;
        for (ThreadPost post : posts) {
            if (id.equals(post.getId())) {
                deleted = posts.remove(post);
                break;
            } else if (post.getChildren() != null) {
                deleted = this.deleteThreadPostFromModel(post.getChildren(), id);
                if (deleted) {
                    break;
                }
            }
        }
        return deleted;
    }


    /**
     * Convert thread Nuxeo document to view-object.
     *
     * @param nuxeoController Nuxeo controller
     * @param document thread Nuxeo document
     * @return view-object
     * @throws PortletException
     */
    private Thread toViewObject(NuxeoController nuxeoController, Document document) throws PortletException {
        try {
            Thread vo = new Thread();
            vo.setAuthor(document.getString("dc:creator"));
            vo.setMessage(document.getString("ttcth:message"));

            // Date
            Date date = document.getDate("dc:created");
            vo.setDate(date);

            // Commentable
            boolean isCommentable = this.isThreadCommentable(nuxeoController, document);
            vo.setCommentable(isCommentable);
            
            // Attachment
            String attachmentName = document.getString("file:filename");
            if (attachmentName != null) {
                vo.setAttachmentName(attachmentName);

                String attachmentURL = nuxeoController.createFileLink(document.getPath(), "file:content", attachmentName);
                vo.setAttachmentURL(attachmentURL);
            }

            // Directory person
            IDirectoryService directoryService = this.directoryServiceLocator.getDirectoryService();
            if (directoryService != null) {
                DirectoryPerson person = directoryService.getPerson(vo.getAuthor());
                vo.setPerson(person);

                // Profile URL
                if (person != null) {
                    String profileURL = this.getUserProfilePageURL(nuxeoController, vo.getAuthor(), person.getDisplayName());
                    vo.setProfileURL(profileURL);
                }
            }

            return vo;
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }


    /**
     * Convert thread post DTO to view-object.
     *
     * @param nuxeoController Nuxeo controller
     * @param dto DTO
     * @return view-object
     */
    private ThreadPost toViewObject(NuxeoController nuxeoController, ThreadPostDTO dto) throws PortletException {
        try {
            ThreadPost vo = new ThreadPost();
            BeanUtils.copyProperties(vo, dto);
            vo.setMessage(dto.getContent());
            vo.setDate(dto.getCreationDate());

            // Attachment
            if (dto.getFilename() != null) {
                vo.setAttachmentName(dto.getFilename());

                String attachmentURL = nuxeoController.createFileLink(dto.getPath(), "post:fileContent", dto.getFilename());
                vo.setAttachmentURL(attachmentURL);
            }

            // Directory person
            this.addDirectoryPerson(nuxeoController, vo);

            // Children
            for (CommentDTO child : dto.getChildren()) {
                if (child instanceof ThreadPostDTO) {
                    ThreadPostDTO childDTO = (ThreadPostDTO) child;
                    ThreadPost childVO = this.toViewObject(nuxeoController, childDTO);
                    vo.getChildren().add(childVO);
                }
            }

            return vo;
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }


    /**
     * Add directory person to view-object.
     *
     * @param nuxeoController Nuxeo controller
     * @param vo view-object
     */
    private void addDirectoryPerson(NuxeoController nuxeoController, ThreadObject vo) {
        // Directory person
        IDirectoryService directoryService = this.directoryServiceLocator.getDirectoryService();
        if (directoryService != null) {
            DirectoryPerson person = directoryService.getPerson(vo.getAuthor());
            vo.setPerson(person);

            // Profile URL
            if (person != null) {
                String profileURL = this.getUserProfilePageURL(nuxeoController, vo.getAuthor(), person.getDisplayName());
                vo.setProfileURL(profileURL);
            }
        }
    }


    /**
     * Convert thread post reply form to business object.
     *
     * @param replyForm thread post reply form
     * @return thread post business object
     * @throws PortletException
     */
    private ThreadPostDTO toBusinessObject(ThreadPostReplyForm replyForm) throws PortletException {
        try {
            ThreadPostDTO post = new ThreadPostDTO();
            BeanUtils.copyProperties(post, replyForm);
            MultipartFile attachmentMultipart = replyForm.getAttachment();
            if ((attachmentMultipart != null) && (attachmentMultipart.getSize() > 0)) {
                File attachment = File.createTempFile("attachment", null);
                attachment.deleteOnExit();
                attachmentMultipart.transferTo(attachment);
                post.setAttachment(attachment);
                post.setFilename(attachmentMultipart.getOriginalFilename());
            } else {
                post.setAttachment(null);
            }
            return post;
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }


    /**
     * Indicates if Thread can be comment.
     *
     * @param nuxeoController nuxeoController
     * @param document thread
     * @return true if Thread can be comment
     * @throws CMSException
     */
    private boolean isThreadCommentable(NuxeoController nuxeoController, Document document) throws CMSException {
        ICMSService cmsService = NuxeoController.getCMSService();
        CMSPublicationInfos publicationInfos = cmsService.getPublicationInfos(nuxeoController.getCMSCtx(), document.getPath());
        return publicationInfos.isCommentableByUser();
    }


    /**
     * Get user profile page URL.
     *
     * @param nuxeoController Nuxeo controller
     * @param name user name
     * @param displayName user display name
     * @return user profile page URL
     */
    private String getUserProfilePageURL(NuxeoController nuxeoController, String name, String displayName) {
        // Portal controller context
        PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();

        // Page properties
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("osivia.hideTitle", "1");
        properties.put("osivia.ajaxLink", "1");
        properties.put("theme.dyna.partial_refresh_enabled", "true");
        properties.put("uidFichePersonne", name);

        // Page parameters
        Map<String, String> parameters = new HashMap<String, String>(0);


        String url;
        try {
            url = this.portalURLFactory.getStartPortletInNewPage(portalControllerContext, "myprofile", displayName, "directory-person-card-instance",
                    properties, parameters);
        } catch (PortalException e) {
            url = null;
        }

        return url;
    }

}
