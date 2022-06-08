package org.osivia.services.edition.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.editor.EditorService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.model.Picture;
import org.osivia.services.edition.portlet.model.UploadTemporaryFile;
import org.osivia.services.edition.portlet.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.portlet.*;
import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Document edition portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see DocumentEditionService
 */
@Service
public class DocumentEditionServiceImpl implements DocumentEditionService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Document edition default repository.
     */
    @Autowired
    private DocumentEditionDefaultRepository defaultRepository;

    /**
     * Document attachments edition repository.
     */
    @Autowired
    private DocumentEditionAttachmentsRepository attachmentsRepository;

    /**
     * Document metadata edition repository.
     */
    @Autowired
    private DocumentEditionMetadataRepository metadataRepository;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Notifications service.
     */
    @Autowired
    private INotificationsService notificationsService;

    /**
     * Editor service.
     */
    @Autowired
    private EditorService editorService;


    /**
     * Constructor.
     */
    public DocumentEditionServiceImpl() {
        super();
    }


    @Override
    public DocumentEditionWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Window properties
        DocumentEditionWindowProperties properties = this.applicationContext.getBean(DocumentEditionWindowProperties.class);

        // Document path
        String documentPath = window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
        properties.setDocumentPath(documentPath);

        // Parent document path
        String parentDocumentPath = window.getProperty(PARENT_DOCUMENT_PATH_WINDOW_PROPERTY);
        properties.setParentDocumentPath(parentDocumentPath);

        // Document type
        String documentType = window.getProperty(DOCUMENT_TYPE_WINDOW_PROPERTY);
        properties.setDocumentType(documentType);

        // Extract archives mode
        String extractArchive = window.getProperty(EXTRACT_ARCHIVE_WINDOW_PROPERTY);
        properties.setExtractArchive(BooleanUtils.toBoolean(extractArchive));

        return properties;
    }


    @Override
    public AbstractDocumentEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Window properties
        DocumentEditionWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        if (StringUtils.isEmpty(windowProperties.getDocumentPath()) && (StringUtils.isEmpty(windowProperties.getParentDocumentPath()) || StringUtils.isEmpty(windowProperties.getDocumentType()))) {
            throw new PortletException("Invalid window properties.");
        }

        // Repository name
        String name = this.getRepositoryName(portalControllerContext, windowProperties);
        if (StringUtils.isEmpty(name)) {
            throw new PortletException("Unable to find portlet repository name.");
        }
        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(name);

        // Form
        AbstractDocumentEditionForm form = repository.getForm(portalControllerContext, windowProperties);
        form.setWindowProperties(windowProperties);
        form.setName(name);

        // Document path
        String path = windowProperties.getDocumentPath();
        form.setPath(path);

        // Creation indicator
        boolean creation = StringUtils.isEmpty(path);
        form.setCreation(creation);

        // for logging in validation phase
        form.setRemoteUser(portalControllerContext.getRequest().getRemoteUser());

        return form;
    }


    @Override
    public DocumentEditionRepository<?> getRepository(String name) {
        return this.applicationContext.getBean(name, DocumentEditionRepository.class);
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Render response
        RenderResponse response = (RenderResponse) portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Form
        AbstractDocumentEditionForm form = this.getForm(portalControllerContext);

        // Title
        String title;
        if (form.getWindowProperties().isExtractArchive()) {
            title = bundle.getString("DOCUMENT_EDITION_TITLE_IMPORT");
        } else if (form.isCreation()) {
            title = bundle.getString("DOCUMENT_EDITION_TITLE_CREATE");
        } else {
            title = bundle.getString("DOCUMENT_EDITION_TITLE_EDIT");
        }
        response.setTitle(title);

        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());

        return repository.getViewPath(portalControllerContext);
    }


    @Override
    public void upload(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());

        repository.upload(portalControllerContext, form);
    }


    @Override
    public void restore(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());

        repository.restore(portalControllerContext, form);
    }


    @Override
    public void uploadAttachments(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        this.attachmentsRepository.uploadAttachments(portalControllerContext, form.getAttachments());
    }


    @Override
    public void deleteAttachment(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form, String value) throws PortletException, IOException {
        this.attachmentsRepository.deleteAttachment(portalControllerContext, form.getAttachments(), value);
    }


    @Override
    public void restoreAttachment(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form, String value) throws PortletException, IOException {
        this.attachmentsRepository.restoreAttachment(portalControllerContext, form.getAttachments(), value);
    }


    @Override
    public void uploadPicture(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form, String pictureType) throws PortletException, IOException {
        // Picture
        Picture picture = this.getPicture(portalControllerContext, form, pictureType);

        if (picture != null) {
            // Delete previous temporary file
            this.defaultRepository.deleteTemporaryFile(picture.getTemporaryFile());

            // Upload
            UploadTemporaryFile temporaryFile = defaultRepository.createTemporaryFile(picture.getUpload());
            picture.setTemporaryFile(temporaryFile);

            picture.setDeleted(false);
        }
    }


    @Override
    public void deletePicture(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form, String pictureType) throws PortletException, IOException {
        // Picture
        Picture picture = this.getPicture(portalControllerContext, form, pictureType);

        if (picture != null) {
            // Delete previous temporary file
            this.defaultRepository.deleteTemporaryFile(picture.getTemporaryFile());

            picture.setTemporaryFile(null);
            picture.setDeleted(true);
        }
    }


    @Override
    public void restorePicture(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form, String pictureType) throws PortletException, IOException {
        // Picture
        Picture picture = this.getPicture(portalControllerContext, form, pictureType);

        if (picture != null) {
            // Delete previous temporary file
            this.defaultRepository.deleteTemporaryFile(picture.getTemporaryFile());

            picture.setTemporaryFile(null);
            picture.setDeleted(false);
        }
    }


    @Override
    public void validate(AbstractDocumentEditionForm form, Errors errors) {
        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());

        // Related validation
        repository.validate(form, errors);
        // Attachments validation
        this.attachmentsRepository.validate(form.getAttachments(), errors);
        // Metadata validation
        this.metadataRepository.validate(form.getMetadata(), errors);
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());
        // Save document
        repository.save(portalControllerContext, form);


        // Notification
        String message;
        if (form.isCreation()) {
            message = bundle.getString("DOCUMENT_EDITION_MESSAGE_CREATION_SUCCESS");
        } else {
            message = bundle.getString("DOCUMENT_EDITION_MESSAGE_EDITION_SUCCESS");
        }
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);

        // Redirect
        this.redirect(portalControllerContext);
    }


    @Override
    public void cancel(PortalControllerContext portalControllerContext) throws IOException {
        // Redirect
        this.redirect(portalControllerContext);
    }


    /**
     * Redirect.
     *
     * @param portalControllerContext portal controller context
     */
    private void redirect(PortalControllerContext portalControllerContext) throws IOException {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();

        if (portletResponse instanceof ActionResponse) {
            // Action response
            ActionResponse actionResponse = (ActionResponse) portletResponse;

            // Window properties
            DocumentEditionWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

            // Redirection
            String redirectionPath = StringUtils.defaultIfEmpty(windowProperties.getDocumentPath(), windowProperties.getParentDocumentPath());
            String redirectionUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, redirectionPath, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
            actionResponse.sendRedirect(redirectionUrl);
        }
    }


    /**
     * Get repository name.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @return name
     */
    private String getRepositoryName(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties) throws PortletException {
        String repositoryName;

        if (windowProperties.isExtractArchive()) {
            String[] names = this.applicationContext.getBeanNamesForType(ZipExtractionRepositoryImpl.class);

            if (ArrayUtils.getLength(names) == 1) {
                repositoryName = names[0];
            } else {
                repositoryName = null;
            }
        } else {
            String documentType;
            boolean creation;
            if (StringUtils.isEmpty(windowProperties.getDocumentPath())) {
                documentType = windowProperties.getDocumentType();
                creation = true;
            } else {
                // Document context
                NuxeoDocumentContext documentContext = this.defaultRepository.getDocumentContext(portalControllerContext, windowProperties.getDocumentPath());

                if ((documentContext == null) || (documentContext.getDocumentType() == null)) {
                    documentType = null;
                } else {
                    documentType = documentContext.getDocumentType().getName();
                }
                creation = false;
            }


            Map<String, DocumentEditionRepository<?>> repositories = this.applicationContext.getBeansOfType(DocumentEditionRepository.class).entrySet().stream().filter(item -> item.getValue().matches(documentType, creation)).collect(Collectors.toMap(Map.Entry::getKey, item -> (DocumentEditionRepository<?>) item.getValue()));
            if (MapUtils.isEmpty(repositories)) {
                repositoryName = null;
            } else if (repositories.size() == 1) {
                repositoryName = repositories.keySet().iterator().next();
            } else {
                List<String> primaryRepositoryNames = repositories.entrySet().stream().filter(item -> item.getValue().getClass().isAnnotationPresent(Primary.class)).map(Map.Entry::getKey).collect(Collectors.toList());
                if (primaryRepositoryNames.size() == 1) {
                    repositoryName = primaryRepositoryNames.get(0);
                } else {
                    repositoryName = null;
                }
            }
        }

        return repositoryName;
    }


    @Override
    public void picturePreview(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form, String pictureType) throws PortletException, IOException {
        // Resource response
        ResourceResponse response = (ResourceResponse) portalControllerContext.getResponse();

        // Picture
        Picture picture = this.getPicture(portalControllerContext, form, pictureType);

        if (picture == null) {
            throw new NuxeoException(NuxeoException.ERROR_NOTFOUND);
        } else {
            // Temporary file
            File temporaryFile = picture.getTemporaryFile().getFile();

            // Upload size
            int size = Long.valueOf(temporaryFile.length()).intValue();
            response.setContentLength(size);

            // Content type
            String contentType = response.getContentType();
            response.setContentType(contentType);

            // Character encoding
            response.setCharacterEncoding(CharEncoding.UTF_8);

            // No cache
            response.getCacheControl().setExpirationTime(0);

            // Input steam
            InputStream inputSteam = Files.newInputStream(temporaryFile.toPath());
            // Output stream
            OutputStream outputStream = response.getPortletOutputStream();
            // Copy
            IOUtils.copy(inputSteam, outputStream);
            outputStream.close();
        }
    }


    /**
     * Get picture.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     * @param pictureType             picture type
     * @return picture
     */
    protected Picture getPicture(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form, String pictureType) {
        Picture picture;
        if ("vignette".equals(pictureType)) {
            picture = form.getMetadata().getVignette();
        } else {
            picture = null;
        }

        return picture;
    }


    @Override
    public void serveEditor(PortalControllerContext portalControllerContext, String editorId) throws PortletException, IOException {
        this.editorService.serveResource(portalControllerContext, editorId);
    }

}
