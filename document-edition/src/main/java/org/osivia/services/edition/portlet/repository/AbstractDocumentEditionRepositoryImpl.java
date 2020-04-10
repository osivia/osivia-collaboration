package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.*;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.repository.command.CheckTitleAvailabilityCommand;
import org.osivia.services.edition.portlet.repository.command.CreateDocumentCommand;
import org.osivia.services.edition.portlet.repository.command.UpdateDocumentCommand;
import org.osivia.services.edition.portlet.service.DocumentEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
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
 * @see DocumentEditionRepository
 */
public abstract class AbstractDocumentEditionRepositoryImpl<T extends AbstractDocumentEditionForm> implements DocumentEditionRepository<T> {

    /**
     * Title Nuxeo document property.
     */
    protected static final String TITLE_PROPERTY = "dc:title";
    /**
     * Description Nuxeo document property.
     */
    protected static final String DESCRIPTION_PROPERTY = "dc:description";


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet context.
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PortletContext portletContext;

    /**
     * Portlet service.
     */
    @Autowired
    private DocumentEditionService service;

    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    protected AbstractDocumentEditionRepositoryImpl() {
        super();
    }


    @Override
    public NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getDocumentContext(path);
    }


    /**
     * Get document edition form.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @param type                    edition form type
     * @return form
     */
    protected T getForm(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties, Class<? extends T> type) throws PortletException, IOException {
        T form = this.applicationContext.getBean(type);

        if (StringUtils.isNotEmpty(windowProperties.getDocumentPath())) {
            // Document context
            NuxeoDocumentContext documentContext = this.getDocumentContext(portalControllerContext, windowProperties.getDocumentPath());
            // Document
            Document document = documentContext.getDocument();

            // Title
            String title = document.getTitle();
            form.setTitle(title);
            form.setOriginalTitle(title);

            // Description
            String description = document.getString(DESCRIPTION_PROPERTY);
            form.setDescription(description);

            // Customization
            this.customizeForm(portalControllerContext, document, form);
        } else if (StringUtils.isNotEmpty(windowProperties.getBasePath()) && StringUtils.isNotEmpty(windowProperties.getParentDocumentPath())) {
            // Breadcrumb
            List<String> breadcrumb = this.getBreadcrumb(portalControllerContext, windowProperties.getBasePath(), windowProperties.getParentDocumentPath());
            form.setBreadcrumb(breadcrumb);
        }

        return form;
    }


    /**
     * Get breadcrumb.
     *
     * @param portalControllerContext portal controller context
     * @param basePath                base path
     * @param parentPath              parent path
     * @return breadcrumb
     */
    private List<String> getBreadcrumb(PortalControllerContext portalControllerContext, String basePath, String parentPath) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Breadcrumb
        List<String> breadcrumb = new ArrayList<>();

        while (StringUtils.startsWith(parentPath, basePath)) {
            try {
                // Navigation item
                CMSItem navigationItem = cmsService.getPortalNavigationItem(cmsContext, basePath, parentPath);

                if (navigationItem == null) {
                    continue;
                }

                // Nuxeo document
                Document document = (Document) navigationItem.getNativeItem();

                breadcrumb.add(0, document.getTitle());
            } catch (CMSException e) {
                // Do nothing
            } finally {
                // Loop on parent path
                CMSObjectPath objectPath = CMSObjectPath.parse(parentPath);
                CMSObjectPath parentObjectPath = objectPath.getParent();
                parentPath = parentObjectPath.toString();
            }
        }

        return breadcrumb;
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


    @Override
    public void validate(T form, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "NotEmpty");

        if (form.isCreation() || !StringUtils.equalsIgnoreCase(form.getOriginalTitle(), form.getTitle())) {
            // Check title availability
            boolean available = this.checkTitleAvailability(form);
            if (!available) {
                errors.rejectValue("upload", "Unavailable");
            }
        }
    }


    /**
     * Check title availability.
     *
     * @param form document edition form
     * @return true is titre is available
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
    public void upload(PortalControllerContext portalControllerContext, T form) throws PortletException, IOException {
        // Do nothing
    }


    @Override
    public void restore(PortalControllerContext portalControllerContext, T form) throws PortletException, IOException {
        // Do nothing
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, T form) throws PortletException, IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Properties
        PropertyMap properties = new PropertyMap();
        // Title
        properties.set(TITLE_PROPERTY, form.getTitle());
        // Description
        properties.set(DESCRIPTION_PROPERTY, StringUtils.trimToNull(form.getDescription()));

        // Binaries
        Map<String, Blob> binaries = new HashMap<>();

        // Customization
        this.customizeProperties(portalControllerContext, form, properties, binaries);

        if (form.isCreation()) {
            // Window properties
            DocumentEditionWindowProperties windowProperties = this.service.getWindowProperties(portalControllerContext);

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
            form.setPath(document.getPath());
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
    protected void customizeProperties(PortalControllerContext portalControllerContext, T form, PropertyMap properties, Map<String, Blob> binaries) throws PortletException, IOException {
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
    protected Document create(NuxeoController nuxeoController, String parentPath, String type, PropertyMap properties, Map<String, Blob> binaries) throws PortletException, IOException {
        INuxeoCommand command = this.applicationContext.getBean(CreateDocumentCommand.class, parentPath, type, properties, binaries);

        return (Document) nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * Update document.
     *
     * @param nuxeoController Nuxeo controller
     * @param path            document path
     * @param properties      document properties
     * @param binaries        document updated binaries
     * @return updated document
     */
    protected Document update(NuxeoController nuxeoController, String path, PropertyMap properties, Map<String, Blob> binaries) throws PortletException, IOException {
        INuxeoCommand command = this.applicationContext.getBean(UpdateDocumentCommand.class, path, properties, binaries);

        return (Document) nuxeoController.executeNuxeoCommand(command);
    }

}
