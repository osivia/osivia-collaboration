package org.osivia.services.forum.edition.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.forum.edition.portlet.model.*;
import org.osivia.services.forum.edition.portlet.model.comparator.BlobIndexComparator;
import org.osivia.services.forum.edition.portlet.repository.ForumEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.*;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Forum edition service implementation.
 *
 * @author Cédric Krommenhoek
 * @see ForumEditionService
 */
@Service
public class ForumEditionServiceImpl implements ForumEditionService {

    /** Vignette temporary file prefix. */
    private static final String VIGNETTE_TEMPORARY_FILE_PREFIX = "vignette-";
    /** Attachment temporary file prefix. */
    private static final String ATTACHMENT_TEMPORARY_FILE_PREFIX = "attachment-";
    /** Temporary file suffix. */
    private static final String TEMPORARY_FILE_SUFFIX = ".tmp";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private ForumEditionRepository repository;

    /** Blob index comparator. */
    @Autowired
    private BlobIndexComparator blobIndexComparator;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Nuxeo service. */
    @Autowired
    private INuxeoService nuxeoService;


    /**
     * Constructor.
     */
    public ForumEditionServiceImpl() {
        super();
    }


    @Override
    public void uploadVignette(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException {
        // Vignette
        Vignette vignette = form.getVignette();
        vignette.setDeleted(false);

        // Upload
        MultipartFile upload = vignette.getUpload();

        this.setUploadedObjectProperties(upload, vignette, VIGNETTE_TEMPORARY_FILE_PREFIX);
    }


    @Override
    public void deleteVignette(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException {
        // Vignette
        Vignette vignette = form.getVignette();
        vignette.setDeleted(true);

        this.setUploadedObjectProperties(null, vignette, VIGNETTE_TEMPORARY_FILE_PREFIX);
    }


    @Override
    public void uploadAttachment(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException {
        // Attachments
        Attachments attachments = form.getAttachments();

        for (MultipartFile multipartFile : attachments.getUpload()) {
            // Attachment file
            AttachmentFile file = this.applicationContext.getBean(AttachmentFile.class);

            this.setUploadedObjectProperties(multipartFile, file, ATTACHMENT_TEMPORARY_FILE_PREFIX);

            attachments.getFiles().add(file);
        }
    }


    @Override
    public void deleteAttachment(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException {
        // Attachments
        Attachments attachments = form.getAttachments();

        // Deleted attachment index
        Integer deletedIndex = attachments.getDeletedIndex();

        if (deletedIndex != null) {
            // Attachment files
            List<AttachmentFile> files = attachments.getFiles();

            // Attachment file
            AttachmentFile file = files.get(deletedIndex);

            // Blob index
            Integer blobIndex = file.getBlobIndex();
            if (blobIndex != null) {
                attachments.getDeletedBlobIndexes().add(blobIndex);
            }

            // Temporary file
            File temporaryFile = file.getTemporaryFile();
            if (temporaryFile != null) {
                // Delete temporary file
                temporaryFile.delete();
            }

            files.remove(file);
        }

        attachments.setDeletedIndex(null);
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, ForumEditionForm form, ForumEditionOptions options) throws PortletException, IOException {
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        this.repository.save(portalControllerContext, form, options);

        // Redirection URL
        String redirectionUrl = this.getRedirectionUrl(portalControllerContext, options, true);
        response.sendRedirect(redirectionUrl);
    }


    @Override
    public void cancel(PortalControllerContext portalControllerContext, ForumEditionOptions options) throws PortletException, IOException {
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Redirection URL
        String redirectionUrl = this.getRedirectionUrl(portalControllerContext, options, false);
        response.sendRedirect(redirectionUrl);
    }


    @Override
    public void vignettePreview(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException {
        // Resource response
        ResourceResponse response = (ResourceResponse) portalControllerContext.getResponse();

        // Temporary file
        File temporaryFile = form.getVignette().getTemporaryFile();

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


    @Override
    public ForumEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Edition mode
        ForumEditionMode mode = ForumEditionMode.fromId(window.getProperty(MODE_PROPERTY));


        // Forum edition form
        ForumEditionForm form = this.applicationContext.getBean(ForumEditionForm.class);

        // Vignette
        Vignette vignette = this.applicationContext.getBean(Vignette.class);
        form.setVignette(vignette);

        // Attachments
        Attachments attachments = this.applicationContext.getBean(Attachments.class, this.blobIndexComparator);
        form.setAttachments(attachments);

        if (ForumEditionMode.EDITION.equals(mode)) {
            // Document properties
            this.repository.fillDocumentProperties(portalControllerContext, form);
        }

        // Document type
        String documentType = window.getProperty(DOCUMENT_TYPE_PROPERTY);
        form.setDocumentType(documentType);

        return form;
    }


    @Override
    public ForumEditionOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException {
        // CMS customizer
        INuxeoCustomizer cmsCustomizer = this.nuxeoService.getCMSCustomizer();

        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());


        // Forum edition options
        ForumEditionOptions options = this.applicationContext.getBean(ForumEditionOptions.class);

        // Document types
        Map<String, DocumentType> types = cmsCustomizer.getCMSItemTypes();
        // Document type
        DocumentType documentType = types.get(window.getProperty(DOCUMENT_TYPE_PROPERTY));
        options.setDocumentType(documentType);

        if (documentType == null) {
            throw new PortletException("Unknown document type.");
        }

        // Edition mode
        ForumEditionMode mode = ForumEditionMode.fromId(window.getProperty(MODE_PROPERTY));
        options.setMode(mode);

        // Current Nuxeo document
        Document document = this.repository.getDocument(portalControllerContext);

        if (ForumEditionMode.CREATION.equals(mode)) {
            // Parent path
            String parentPath = document.getPath();
            options.setParentPath(parentPath);
        } else if (ForumEditionMode.EDITION.equals(mode)) {
            options.setDocument(document);
        }

        // Fragment
        String fragmentKey = "FORUM_FRAGMENT_" + StringUtils.upperCase(documentType.getName());
        String fragment = bundle.getString(fragmentKey);
        options.setFragment(fragment);

        // Title
        String titleKey = "FORUM_TITLE_" + StringUtils.upperCase(mode.getId());
        String title = bundle.getString(titleKey, fragment);
        options.setTitle(title);

        // View path
        String viewPath = "view-" + StringUtils.lowerCase(documentType.getName());
        options.setViewPath(viewPath);

        return options;
    }


    /**
     * Set uploaded object properties.
     *
     * @param upload multipart file upload
     * @param object uploaded object
     * @param prefix temporary file prefix
     * @throws IOException
     */
    private void setUploadedObjectProperties(MultipartFile upload, UploadedObject object, String prefix) throws IOException {
        if (upload == null) {
            // Temporary file
            File temporaryFile = object.getTemporaryFile();
            if (temporaryFile != null) {
                temporaryFile.delete();
                object.setTemporaryFile(null);
            }

            // File name
            object.setFileName(null);

            // Mime type
            object.setMimeType(null);
        } else {
            // Temporary file
            File temporaryFile = object.getTemporaryFile();
            if (temporaryFile != null) {
                // Delete temporary file
                temporaryFile.delete();
            }
            temporaryFile = File.createTempFile(prefix, TEMPORARY_FILE_SUFFIX);
            temporaryFile.deleteOnExit();
            upload.transferTo(temporaryFile);
            object.setTemporaryFile(temporaryFile);

            // File name
            object.setFileName(upload.getOriginalFilename());

            // Mime type
            MimeType mimeType;
            try {
                mimeType = new MimeType(upload.getContentType());
            } catch (MimeTypeParseException e) {
                mimeType = null;
            }
            object.setMimeType(mimeType);
        }
    }


    /**
     * Get redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param options                 forum edition options
     * @param refresh                 refresh indicator
     * @return URL
     * @throws PortletException
     */
    private String getRedirectionUrl(PortalControllerContext portalControllerContext, ForumEditionOptions options, boolean refresh) throws PortletException {
        // Document
        Document document = options.getDocument();

        // Path
        String path;
        if (ForumEditionMode.CREATION.equals(options.getMode())) {
            if (document == null) {
                path = options.getParentPath();
            } else {
                path = document.getPath();
            }
        } else if (ForumEditionMode.EDITION.equals(options.getMode())) {
            path = document.getPath();
        } else {
            path = null;
        }

        // Redirection URL
        String url;
        if (path == null) {
            url = null;
        } else {
            String displayContext;
            if (refresh) {
                displayContext = IPortalUrlFactory.DISPLAYCTX_REFRESH;
            } else {
                displayContext = null;
            }

            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, displayContext, null, null, null, null);
        }

        return url;
    }

}
