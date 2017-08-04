package org.osivia.services.forum.edition.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
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
import org.osivia.services.forum.edition.portlet.model.ForumEditionForm;
import org.osivia.services.forum.edition.portlet.model.ForumEditionMode;
import org.osivia.services.forum.edition.portlet.model.ForumEditionOptions;
import org.osivia.services.forum.edition.portlet.model.Vignette;
import org.osivia.services.forum.edition.portlet.repository.ForumEditionRepository;
import org.osivia.services.forum.util.model.ForumFile;
import org.osivia.services.forum.util.model.ForumFiles;
import org.osivia.services.forum.util.service.AbstractForumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Forum edition service implementation.
 *
 * @author Cédric Krommenhoek
 * @see AbstractForumServiceImpl
 * @see ForumEditionService
 */
@Service
public class ForumEditionServiceImpl extends AbstractForumServiceImpl implements ForumEditionService {

    /** Vignette temporary file prefix. */
    private static final String VIGNETTE_TEMPORARY_FILE_PREFIX = "vignette-";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private ForumEditionRepository repository;

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

        this.setAttachmentFileProperties(upload, vignette, VIGNETTE_TEMPORARY_FILE_PREFIX);
    }


    @Override
    public void deleteVignette(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException {
        // Vignette
        Vignette vignette = form.getVignette();
        vignette.setDeleted(true);

        // Temporary file
        File temporaryFile = vignette.getTemporaryFile();
        if (temporaryFile != null) {
            temporaryFile.delete();
            vignette.setTemporaryFile(null);
        }

        // File name
        vignette.setFileName(null);

        // Mime type
        vignette.setMimeType(null);
    }


    @Override
    public void uploadAttachment(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException {
        // Attachments
        ForumFiles attachments = form.getAttachments();
        // Attachment files
        List<ForumFile> files = attachments.getFiles();
        if (files == null) {
            files = new ArrayList<>();
            attachments.setFiles(files);
        }

        for (MultipartFile multipartFile : attachments.getUpload()) {
            // Attachment file
            ForumFile file = this.applicationContext.getBean(ForumFile.class);

            this.setAttachmentFileProperties(multipartFile, file, ATTACHMENT_TEMPORARY_FILE_PREFIX);

            files.add(file);
        }
    }


    @Override
    public void deleteAttachment(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException {
        // Attachments
        ForumFiles attachments = form.getAttachments();

        this.deleteAttachment(attachments);
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
        ForumFiles attachments = this.applicationContext.getBean(ForumFiles.class);
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

        // Redirection URL
        return this.portalUrlFactory.getBackURL(portalControllerContext, false, refresh);
    }

}
