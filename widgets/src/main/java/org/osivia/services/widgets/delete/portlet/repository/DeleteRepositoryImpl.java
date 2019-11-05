package org.osivia.services.widgets.delete.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.widgets.delete.portlet.repository.command.GetChildrenCommand;
import org.osivia.services.widgets.delete.portlet.repository.command.GetDocumentsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.*;

/**
 * Delete portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see DeleteRepository
 */
@Repository
public class DeleteRepositoryImpl implements DeleteRepository {

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
     * Constructor.
     */
    public DeleteRepositoryImpl() {
        super();
    }


    @Override
    public Document getDocument(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);

        return documentContext.getDocument();
    }


    @Override
    public List<Document> getDocuments(PortalControllerContext portalControllerContext, String[] identifiers) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetDocumentsCommand.class, Arrays.asList(identifiers));
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        return documents.list();
    }


    @Override
    public Map<Document, Integer> getChildrenCounts(PortalControllerContext portalControllerContext, List<Document> documents) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Folderish documents
        List<Document> folderishDocuments = new ArrayList<>(documents.size());
        for (Document document : documents) {
            PropertyList facets = document.getFacets();
            if (!facets.isEmpty() && facets.list().contains("Folderish")) {
                folderishDocuments.add(document);
            }
        }

        // Children counts
        Map<Document, Integer> childrenCounts = new HashMap<>(folderishDocuments.size());

        if (CollectionUtils.isNotEmpty(folderishDocuments)) {
            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(GetChildrenCommand.class, folderishDocuments);
            Documents children = (Documents) nuxeoController.executeNuxeoCommand(command);

            Iterator<Document> childrenIterator = children.iterator();
            while (childrenIterator.hasNext()) {
                Document child = childrenIterator.next();

                // Search child parent
                Document parent = null;
                Iterator<Document> iterator = folderishDocuments.iterator();
                while ((parent == null) && iterator.hasNext()) {
                    Document document = iterator.next();

                    if (StringUtils.startsWith(child.getPath(), document.getPath() + "/")) {
                        parent = document;
                    }
                }

                if (parent != null) {
                    Integer count = childrenCounts.getOrDefault(parent, 0);
                    childrenCounts.put(parent, count + 1);
                }
            }
        }

        return childrenCounts;
    }


    @Override
    public void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        for (String identifier : identifiers) {
            try {
                cmsService.putDocumentInTrash(cmsContext, identifier);
            } catch (CMSException e) {
                throw new PortletException(e);
            }
        }
    }

}
