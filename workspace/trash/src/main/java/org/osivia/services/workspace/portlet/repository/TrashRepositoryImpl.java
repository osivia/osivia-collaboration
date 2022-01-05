package org.osivia.services.workspace.portlet.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSObjectPath;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.workspace.portlet.model.ParentDocument;
import org.osivia.services.workspace.portlet.model.TrashedDocument;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;

/**
 * Trash repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see TrashRepository
 */
@Repository
public class TrashRepositoryImpl implements TrashRepository, ApplicationContextAware {

    /** CMS service locator. */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;

    /** Nuxeo customizer. */
    @Autowired
    private INuxeoCustomizer nuxeoCustomizer;

    /** Person service. */
    @Autowired
    private PersonService personService;


    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * {@inheritDoc}
     */
    public TrashRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrashedDocument> getTrashedDocuments(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        // Base path
        String basePath = nuxeoController.getBasePath();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetTrashedDocumentsCommand.class, basePath);
        List<Document> documents = (List<Document>) nuxeoController.executeNuxeoCommand(command);

        // Trashed documents
        List<TrashedDocument> trashedDocuments = new ArrayList<>(documents.size());
        // Previous path
        String previousPath = null;
        
        for (Document document : documents) {
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
     * @param document Nuxeo document
     * @return trashed document
     * @throws CMSException
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


        // Title
        String title = document.getTitle();

        // Type
        Map<String, DocumentType> types = this.nuxeoCustomizer.getCMSItemTypes();
        DocumentType type = types.get(document.getType());

        // Icon
        String icon;
        if (type == null) {
            icon = null;
        } else {
            icon = type.getGlyph();
        }

        // Deletion date
        Date deletionDate = document.getDate("dc:modified");

        // Last contributor
        String lastContributorId = document.getString("dc:lastContributor");
        Person lastContributorPerson;
        if (lastContributorId == null) {
            lastContributorPerson = null;
        } else {
            lastContributorPerson = this.personService.getPerson(lastContributorId);
        }
        String lastContributorDisplayName;
        if (lastContributorPerson == null) {
            lastContributorDisplayName = lastContributorId;
        } else {
            lastContributorDisplayName = StringUtils.defaultIfBlank(lastContributorPerson.getDisplayName(), lastContributorId);
        }

        // Size
        Long size = document.getLong("common:size");
        
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
            location = this.applicationContext.getBean(ParentDocument.class, parentDocument.getPath());
            location.setTitle(parentDocument.getTitle());
            if (parentNavigationItem.getType() != null) {
                location.setIcon(parentNavigationItem.getType().getGlyph());
            }
        }


        // Trashed document
        TrashedDocument trashedDocument;
        if (location == null) {
            trashedDocument = null;
        } else {
            trashedDocument = this.applicationContext.getBean(TrashedDocument.class, path);
            trashedDocument.setTitle(title);
            trashedDocument.setIcon(icon);
            trashedDocument.setSize(size);
            trashedDocument.setDeletionDate(deletionDate);
            trashedDocument.setLastContributor(lastContributorDisplayName);
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
     * @param clazz trash command class
     * @param selectedPaths selected item paths, may be null
     * @return rejected documents
     * @throws PortletException
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
    public List<ParentDocument> getLocationParents(PortalControllerContext portalControllerContext, String path) throws PortletException {
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

                // Document
                Document document = (Document) navigationItem.getNativeItem();

                // Parent document
                ParentDocument parent = this.applicationContext.getBean(ParentDocument.class, document.getPath());
                parent.setTitle(document.getTitle());
                if (navigationItem.getType() != null) {
                    parent.setIcon(navigationItem.getType().getGlyph());
                }

                parents.add(0, parent);
            } catch (CMSException e) {
                continue;
            } finally {
                // Loop on parent path
                CMSObjectPath objectPath = CMSObjectPath.parse(parentPath);
                CMSObjectPath parentObjectPath = objectPath.getParent();
                parentPath = parentObjectPath.toString();
            }
        }

        return parents;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
