package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.repository.command.CreateDocumentCommand;
import org.osivia.services.edition.portlet.repository.command.UpdateDocumentCommand;
import org.osivia.services.edition.portlet.service.DocumentEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Document edition portlet repository implementation abstract super-class.
 *
 * @param <T> document edition form type
 * @author Cédric Krommenhoek
 * @see DocumentEditionRepository
 */
abstract class AbstractDocumentEditionRepositoryImpl<T extends AbstractDocumentEditionForm> implements DocumentEditionRepository<T> {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet service.
     */
    @Autowired
    private DocumentEditionService service;


    /**
     * Constructor.
     */
    AbstractDocumentEditionRepositoryImpl() {
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
    T getForm(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties, Class<? extends T> type) throws PortletException, IOException {
        T form = this.applicationContext.getBean(type);

        if (StringUtils.isNotEmpty(windowProperties.getDocumentPath())) {
            // Document context
            NuxeoDocumentContext documentContext = this.getDocumentContext(portalControllerContext, windowProperties.getDocumentPath());
            // Document
            Document document = documentContext.getDocument();

            // Title
            String title = document.getTitle();
            form.setTitle(title);

            // Description
            String description = document.getString("dc:description");
            form.setDescription(description);

            // Customization
            this.customizeForm(portalControllerContext, document, form);
        }

        return form;
    }


    /**
     * Customize document edition form.
     *
     * @param portalControllerContext portal controller context
     * @param document                current document
     * @param form                    document edition form
     */
    abstract void customizeForm(PortalControllerContext portalControllerContext, Document document, T form) throws PortletException, IOException;


    @Override
    public void save(PortalControllerContext portalControllerContext, T form) throws PortletException, IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Properties
        PropertyMap properties = new PropertyMap();
        // Title
        properties.set("dc:title", form.getTitle());
        // Description
        properties.set("dc:description", StringUtils.trimToNull(form.getDescription()));

        // Customization
        this.customizeProperties(portalControllerContext, form, properties);

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
            Document document = this.create(nuxeoController, parentPath, documentType, properties);
            form.setPath(document.getPath());
        } else {
            // Update
            this.update(nuxeoController, form.getPath(), properties);
        }
    }


    /**
     * Customize document properties.
     *
     * @param portalControllerContext portal controller context
     * @param form                    document edition form
     * @param properties              document properties
     */
    abstract void customizeProperties(PortalControllerContext portalControllerContext, T form, PropertyMap properties) throws PortletException, IOException;


    /**
     * Create document.
     *
     * @param nuxeoController Nuxeo controller
     * @param parentPath      parent document path
     * @param type            document type
     * @param properties      document properties
     * @return created document
     */
    Document create(NuxeoController nuxeoController, String parentPath, String type, PropertyMap properties) {
        INuxeoCommand command = this.applicationContext.getBean(CreateDocumentCommand.class, parentPath, type, properties);

        return (Document) nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * Update document.
     *
     * @param nuxeoController Nuxeo controller
     * @param path            document path
     * @param properties      document properties
     * @return updated document
     */
    Document update(NuxeoController nuxeoController, String path, PropertyMap properties) {
        INuxeoCommand command = this.applicationContext.getBean(UpdateDocumentCommand.class, path, properties);

        return (Document) nuxeoController.executeNuxeoCommand(command);
    }

}
