package org.osivia.services.workspace.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.core.cms.*;
import org.osivia.services.workspace.portlet.model.ParentDocument;
import org.osivia.services.workspace.portlet.model.TrashedDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.List;

/**
 * Trash repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see TrashRepository
 */
@Repository
public class TrashRepositoryImpl implements TrashRepository {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;

    /**
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;


    /**
     * Constructor.
     */
    public TrashRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrashedDocument> getTrashedDocuments(PortalControllerContext portalControllerContext) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Base path
        String basePath = nuxeoController.getBasePath();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetTrashedDocumentsCommand.class, basePath);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Trashed documents
        List<TrashedDocument> trashedDocuments = new ArrayList<>(documents.size());
        // Previous path
        String previousPath = null;

        for (Document document : documents.list()) {
            // Path
            String path = document.getPath();

            if (!StringUtils.startsWith(path, previousPath)) {
                try {
                    // Trashed document
                    TrashedDocument trashedDocument = this.getTrashedDocument(nuxeoController, document);
                    if (trashedDocument != null) {
                        trashedDocuments.add(trashedDocument);
                    }
                } catch (CMSException e) {
                    // Do nothing
                }

                previousPath = path;
            }
        }

        return trashedDocuments;
    }


    /**
     * Get trashed document.
     *
     * @param nuxeoController Nuxeo controller
     * @param document        Nuxeo document
     * @return trashed document
     */
    private TrashedDocument getTrashedDocument(NuxeoController nuxeoController, Document document) throws CMSException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Base path
        String basePath = nuxeoController.getBasePath();
        // Document path
        String path = document.getPath();

        // Location
        CMSObjectPath objectPath = CMSObjectPath.parse(path);
        CMSObjectPath parentObjectPath = objectPath.getParent();
        String parentPath = parentObjectPath.toString();
        CMSItem parentNavigationItem = cmsService.getPortalNavigationItem(cmsContext, basePath, parentPath);
        ParentDocument location;
        if (parentNavigationItem == null) {
            location = null;
        } else {
            Document parentDocument = (Document) parentNavigationItem.getNativeItem();
            DocumentDTO parentDocumentDto = this.documentDao.toDTO(parentDocument);
            location = this.applicationContext.getBean(ParentDocument.class, parentDocumentDto);
        }

        // Trashed document
        TrashedDocument trashedDocument;
        if (location == null) {
            trashedDocument = null;
        } else {
            DocumentDTO documentDto = this.documentDao.toDTO(document);
            trashedDocument = this.applicationContext.getBean(TrashedDocument.class, documentDto);
            trashedDocument.setLocation(location);
        }

        return trashedDocument;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrashedDocument> deleteAll(PortalControllerContext portalControllerContext) throws PortletException {
        return this.delete(portalControllerContext, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrashedDocument> restoreAll(PortalControllerContext portalControllerContext) throws PortletException {
        return this.restore(portalControllerContext, null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrashedDocument> delete(PortalControllerContext portalControllerContext, List<String> selectedPaths) throws PortletException {
        return this.executeTrashCommand(portalControllerContext, DeleteDocumentsCommand.class, selectedPaths);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrashedDocument> restore(PortalControllerContext portalControllerContext, List<String> selectedPaths) throws PortletException {
        return this.executeTrashCommand(portalControllerContext, RestoreDocumentsCommand.class, selectedPaths);
    }


    /**
     * Execute trash Nuxeo command.
     *
     * @param portalControllerContext portal controller context
     * @param clazz                   trash command class
     * @param selectedPaths           selected item paths, may be null
     * @return rejected documents
     */
    private List<TrashedDocument> executeTrashCommand(PortalControllerContext portalControllerContext, Class<? extends TrashCommand> clazz,
                                                      List<String> selectedPaths) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;

        if (selectedPaths == null) {
            // Base path
            String basePath = nuxeoController.getBasePath();

            command = this.applicationContext.getBean(clazz, basePath);
        } else if (selectedPaths.isEmpty()) {
            command = null;
        } else {
            command = this.applicationContext.getBean(clazz, selectedPaths);
        }


        // Rejected documents
        List<TrashedDocument> rejected;

        if (command == null) {
            rejected = null;
        } else {
            Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);
            rejected = new ArrayList<>(documents.size());
            for (Document document : documents.list()) {
                TrashedDocument trashedDocument;
                try {
                    trashedDocument = this.getTrashedDocument(nuxeoController, document);
                } catch (CMSException e) {
                    throw new PortletException(e);
                }

                if (trashedDocument != null) {
                    rejected.add(trashedDocument);
                }
            }
        }

        return rejected;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParentDocument> getLocationParents(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Base path
        String basePath = nuxeoController.getBasePath();
        // Parent path
        String parentPath = path;

        // Parent documents
        List<ParentDocument> parents = new ArrayList<>();

        while (StringUtils.startsWith(parentPath, basePath)) {
            try {
                // Navigation item
                CMSItem navigationItem = cmsService.getPortalNavigationItem(cmsContext, basePath, parentPath);

                if (navigationItem == null) {
                    continue;
                }

                // Nuxeo document
                Document document = (Document) navigationItem.getNativeItem();
                // Document DTO
                DocumentDTO documentDto = this.documentDao.toDTO(document);

                // Parent document
                ParentDocument parent = this.applicationContext.getBean(ParentDocument.class, documentDto);

                parents.add(0, parent);
            } catch (CMSException e) {
                // Do nothing
            } finally {
                // Loop on parent path
                CMSObjectPath objectPath = CMSObjectPath.parse(parentPath);
                CMSObjectPath parentObjectPath = objectPath.getParent();
                parentPath = parentObjectPath.toString();
            }
        }

        return parents;
    }

}
