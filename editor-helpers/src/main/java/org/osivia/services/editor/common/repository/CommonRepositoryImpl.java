package org.osivia.services.editor.common.repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.common.model.SearchScope;
import org.osivia.services.editor.common.repository.command.SearchSourceDocumentCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Portlet common repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonRepository
 */
public abstract class CommonRepositoryImpl implements CommonRepository {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public CommonRepositoryImpl() {
        super();
    }


    @Override
    public Document getDocument(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);

        // Disable cache
        documentContext.reload();

        return documentContext.getDocument();
    }


    @Override
    public List<Document> searchDocuments(PortalControllerContext portalControllerContext, String basePath, String filter, SearchScope scope) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo command
        SearchSourceDocumentCommand command = this.applicationContext.getBean(SearchSourceDocumentCommand.class);
        command.setBasePath(basePath);
        command.setFilter(filter);
        command.setScope(scope);

        Documents result = (Documents) nuxeoController.executeNuxeoCommand(command);

        return result.list();
    }

}
