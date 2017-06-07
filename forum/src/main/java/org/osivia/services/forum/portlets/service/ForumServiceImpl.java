package org.osivia.services.forum.portlets.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.urls.Link;
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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.CommentDTO;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentAttachmentDTO;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.domain.ThreadPostDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCommentsService;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import fr.toutatice.portail.cms.nuxeo.api.services.tag.INuxeoTagService;

/**
 * Forum service implementation.
 *
 * @author Cédric Krommenhoek
 * @see IForumService
 * @see ApplicationContextAware
 */
@Service
public class ForumServiceImpl implements IForumService, ApplicationContextAware {

    /** Document request attribute name. */
    private static final String DOCUMENT_REQUEST_ATTRIBUTE = "osivia.forum.document";


    /** Nuxeo service. */
    @Autowired
    private INuxeoService nuxeoService;

    /** Person service. */
    @Autowired
    private PersonService personService;


    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public ForumServiceImpl() {
        super();
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
                // Current window
                PortalWindow window = WindowFactory.getWindow(request);
                // Path
                String path = window.getProperty(Constants.WINDOW_PROP_URI);

                // Nuxeo document
                NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
                document = documentContext.getDocument();
                
                // Save document in request
                nuxeoController.getRequest().setAttribute(DOCUMENT_REQUEST_ATTRIBUTE, document);
            }
            return document;
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }
    
    
    /**
     * Generate document attachments.
     *
     * @param nuxeoController Nuxeo controller
     * @param document Nuxeo document
     * @param documentDTO document DTO
     */
    private void generateAttachments(NuxeoController nuxeoController, Document document, Thread documentDTO) {
        List<DocumentAttachmentDTO> attachments = documentDTO.getAttachments();
        PropertyList files = document.getProperties().getList("files:files");
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                PropertyMap map = files.getMap(i);

                DocumentAttachmentDTO attachment = new DocumentAttachmentDTO();

                // Attachment name
                String name = map.getString("filename");
                attachment.setName(name);

                // Attachement URL
                String url = nuxeoController.createAttachedFileLink(document.getPath(), String.valueOf(i));
                attachment.setUrl(url);

                attachments.add(attachment);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Thread getThread(NuxeoController nuxeoController) throws PortletException {
        Document document = this.getDocument(nuxeoController);
        Thread viewObject = toViewObject(nuxeoController, document);
        generateAttachments(nuxeoController, document, viewObject);
        return viewObject;
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
            Thread vo = this.applicationContext.getBean(Thread.class);
            
            // Get Document for raw message 
            DocumentDTO dto = DocumentDAO.getInstance().toDTO(document);
            vo.setDocument(dto);
            
            vo.setMessage(document.getString("ttcth:message"));
            vo.setAuthor(document.getString("dc:creator"));

            // Date
            Date date = document.getDate("dc:created");
            vo.setDate(date);

            // Commentable
            boolean isCommentable = this.isThreadCommentable(nuxeoController, document);
            vo.setCommentable(isCommentable);
            

            // Person
            Person person = this.personService.getPerson(vo.getAuthor());
            vo.setPerson(person);

            // Profile URL
            if (person != null) {
                // Tag service
                INuxeoTagService tagService = this.nuxeoService.getTagService();

                // Display name
                String displayName = StringUtils.defaultIfEmpty(person.getDisplayName(), vo.getAuthor());

                // Profile link
                Link link = tagService.getUserProfileLink(nuxeoController, vo.getAuthor(), displayName);

                vo.setProfileUrl(link.getUrl());
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
            ThreadPost vo = this.applicationContext.getBean(ThreadPost.class);
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
        // Person
        Person person = this.personService.getPerson(vo.getAuthor());
        vo.setPerson(person);

        // Profile URL
        if (person != null) {
            // Tag service
            INuxeoTagService tagService = this.nuxeoService.getTagService();

            // Display name
            String displayName = StringUtils.defaultIfBlank(person.getDisplayName(), vo.getAuthor());

            // Profile link
            Link link = tagService.getUserProfileLink(nuxeoController, vo.getAuthor(), displayName);

            vo.setProfileUrl(link.getUrl());
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
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
