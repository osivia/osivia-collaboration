package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.*;
import org.osivia.services.edition.portlet.repository.command.CheckTitleAvailabilityCommand;
import org.osivia.services.edition.portlet.repository.command.CreateDocumentCommand;
import org.osivia.services.edition.portlet.repository.command.UpdateDocumentCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.context.PortletContextAware;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Document edition portlet repository implementation abstract super-class.
 *
 * @param <T> document edition form type
 * @author Cédric Krommenhoek
 * @see PortletContextAware
 * @see DocumentEditionRepository
 */
public abstract class AbstractDocumentEditionRepositoryImpl<T extends AbstractDocumentEditionForm> implements PortletContextAware, DocumentEditionRepository<T> {

    /**
     * Title Nuxeo document property.
     */
    protected static final String TITLE_PROPERTY = "dc:title";
    /**
     * Attachements Nuxeo document property.
     */
    protected static final String ATTACHMENTS_PROPERTY = "files:files";


    /**
     * Portlet context.
     */
    private PortletContext portletContext;

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Document metadata edition repository.
     */
    @Autowired
    private DocumentEditionMetadataRepository metadataRepository;


    /**
     * Constructor.
     */
    protected AbstractDocumentEditionRepositoryImpl() {
        super();
    }


    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }


    @Override
    public boolean matches(String documentType, boolean creation) {
        return false;
    }


    @Override
    public NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getDocumentContext(path);
    }


    @Override
    public T getForm(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties) throws PortletException, IOException {
        Class<T> type = this.getParameterizedType();
        T form = this.applicationContext.getBean(type);

        if (StringUtils.isEmpty(windowProperties.getDocumentPath())) {
            // Attachments
            Attachments attachments = this.applicationContext.getBean(Attachments.class);
            form.setAttachments(attachments);
        } else {
            // Document context
            NuxeoDocumentContext documentContext = this.getDocumentContext(portalControllerContext, windowProperties.getDocumentPath());
            // Document
            Document document = documentContext.getDocument();

            // Title
            String title = document.getTitle();
            form.setTitle(title);
            form.setOriginalTitle(title);

            // Attachments
            Attachments attachments = this.loadExistingAttachments(document);
            form.setAttachments(attachments);

            // Metadata
            DocumentEditionMetadata metadata = this.metadataRepository.getMetadata(portalControllerContext, document);
            form.setMetadata(metadata);

            // Customization
            this.customizeForm(portalControllerContext, document, form);
        }

        return form;
    }


    /**
     * Load existing attachements.
     *
     * @param document document
     * @return attachments
     */
    private Attachments loadExistingAttachments(Document document) {
        // Existing files
        List<ExistingFile> existingFiles;
        PropertyList attachmentsList = document.getProperties().getList(ATTACHMENTS_PROPERTY);
        if ((attachmentsList == null) || attachmentsList.isEmpty()) {
            existingFiles = null;
        } else {
            existingFiles = new ArrayList<>(attachmentsList.size());
            for (int i = 0; i < attachmentsList.size(); i++) {
                PropertyMap attachmentMap = attachmentsList.getMap(i);
                PropertyMap attachmentFileMap = attachmentMap.getMap("file");
                String fileName = attachmentFileMap.getString("name");
                MimeType mimeType;
                try {
                    mimeType = new MimeType(attachmentFileMap.getString("mime-type"));
                } catch (MimeTypeParseException e) {
                    mimeType = null;
                }

                // Existing file
                ExistingFile file = this.applicationContext.getBean(ExistingFile.class);
                file.setIndex(i);
                file.setFileName(fileName);
                file.setMimeType(mimeType);

                existingFiles.add(file);
            }
        }

        // Attachments
        Attachments attachments = this.applicationContext.getBean(Attachments.class);
        attachments.setExistingFiles(existingFiles);

        return attachments;
    }


    /**
     * Customize document edition form.
     *
     * @param portalControllerContext portal controller context
     * @param document                current document
     * @param form                    document edition form
     */
    protected void customizeForm(PortalControllerContext portalControllerContext, Document document, T form) throws PortletException, IOException {
        // Do nothing
    }


    /**
     * Cast form.
     *
     * @param form generic form
     * @return casted form
     */
    private T castForm(AbstractDocumentEditionForm form) {
        Class<T> type = this.getParameterizedType();

        return type.cast(form);
    }


    @Override
    public void validate(AbstractDocumentEditionForm form, Errors errors) {
        this.customizeValidation(this.castForm(form), errors);
    }


    /**
     * Customize validation.
     *
     * @param form   document edition form
     * @param errors validation errors
     */
    protected void customizeValidation(T form, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty");

        if (form.isCreation() || !StringUtils.equalsIgnoreCase(form.getOriginalTitle(), form.getTitle())) {
            // Check title availability
            boolean available = this.checkTitleAvailability(form);
            if (!available) {
                errors.rejectValue("title", "Unavailable");
            }
        }
    }


    /**
     * Check title availability.
     *
     * @param form document edition form
     * @return true if title is available
     */
    private boolean checkTitleAvailability(T form) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(this.portletContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);

        // Window properties
        DocumentEditionWindowProperties windowProperties = form.getWindowProperties();

        // Parent document path
        String parentPath;
        if (form.isCreation()) {
            parentPath = windowProperties.getParentDocumentPath();
        } else {
            parentPath = StringUtils.substringBeforeLast(windowProperties.getDocumentPath(), "/");
        }

        // Parent document
        NuxeoDocumentContext parentDocumentContext = nuxeoController.getDocumentContext(parentPath);
        Document parentDocument = parentDocumentContext.getDocument();


        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CheckTitleAvailabilityCommand.class, parentDocument.getId(), form.getTitle());
        Boolean available = (Boolean) nuxeoController.executeNuxeoCommand(command);

        return BooleanUtils.isTrue(available);
    }


    @Override
    public void upload(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        this.customizeUpload(portalControllerContext, this.castForm(form));
    }


    /**
     * Customize upload.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     */
    protected void customizeUpload(PortalControllerContext portalControllerContext, T form) throws PortletException, IOException {
        // Do nothing
    }


    protected UploadTemporaryFile createTemporaryFile(MultipartFile upload) throws IOException {
        File file = File.createTempFile("uploaded-file-", ".tmp");
        file.deleteOnExit();
        upload.transferTo(file);

        // MIME type
        MimeType mimeType;
        try {
            mimeType = new MimeType(upload.getContentType());
        } catch (MimeTypeParseException e) {
            mimeType = null;
        }

        // Temporary file
        UploadTemporaryFile temporaryFile = this.applicationContext.getBean(UploadTemporaryFile.class);
        temporaryFile.setFile(file);
        temporaryFile.setFileName(upload.getOriginalFilename());
        temporaryFile.setMimeType(mimeType);
        
        return temporaryFile;
    }


    @Override
    public void restore(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        this.customizeRestore(portalControllerContext, this.castForm(form));
    }


    protected void customizeRestore(PortalControllerContext portalControllerContext, T form) throws PortletException, IOException {
        // Do nothing
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Properties
        PropertyMap properties = new PropertyMap();
        // Title
        properties.set(TITLE_PROPERTY, form.getTitle());

        // Binaries
        Map<String, List<Blob>> binaries = new HashMap<>();

        // Metadata
        this.metadataRepository.customizeProperties(form.getMetadata(), properties);

        // Customization
        this.customizeProperties(portalControllerContext, this.castForm(form), properties, binaries);

        if (form.isCreation()) {
            // Window properties
            DocumentEditionWindowProperties windowProperties = form.getWindowProperties();

            // Parent path
            String parentPath = windowProperties.getParentDocumentPath();
            // Document type
            String documentType = windowProperties.getDocumentType();

            // Parent document context
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(parentPath);
            DocumentType parentDocumentType = documentContext.getDocumentType();
            if ((parentDocumentType == null) || !parentDocumentType.getSubtypes().contains(documentType)) {
                throw new PortletException("Invalid created document type.");
            }

            // Create
            Document document = this.create(nuxeoController, parentPath, documentType, properties, binaries);
            if (document != null) {
                form.setPath(document.getPath());
            }
        } else {
            // Update
            this.update(nuxeoController, form.getPath(), properties, binaries);
        }
    }


    /**
     * Customize document properties.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     * @param properties              document properties
     * @param binaries                document updated binaries
     */
    protected void customizeProperties(PortalControllerContext portalControllerContext, T form, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException, IOException {
        // Do nothing
    }


    /**
     * Create document.
     *
     * @param nuxeoController Nuxeo controller
     * @param parentPath      parent document path
     * @param type            document type
     * @param properties      document properties
     * @param binaries        document updated binaries
     * @return created document
     */
    protected Document create(NuxeoController nuxeoController, String parentPath, String type, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException, IOException {
        CreateDocumentCommand command = this.applicationContext.getBean(CreateDocumentCommand.class);
        command.setParentPath(parentPath);
        command.setType(type);
        command.setProperties(properties);
        command.setBinaries(binaries);

        return (Document) nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * Update document.
     *
     * @param nuxeoController Nuxeo controller
     * @param path            document path
     * @param properties      document properties
     * @param binaries        document updated binaries
     */
    protected void update(NuxeoController nuxeoController, String path, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException, IOException {
        UpdateDocumentCommand command = this.applicationContext.getBean(UpdateDocumentCommand.class);
        command.setPath(path);
        command.setProperties(properties);
        command.setBinaries(binaries);

        nuxeoController.executeNuxeoCommand(command);
    }

}
