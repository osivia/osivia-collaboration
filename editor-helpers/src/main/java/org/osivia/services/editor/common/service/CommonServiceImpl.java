package org.osivia.services.editor.common.service;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.editor.common.model.SearchScope;
import org.osivia.services.editor.common.model.SourceDocumentForm;
import org.osivia.services.editor.common.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.portlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Portlet common service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonService
 */
public abstract class CommonServiceImpl implements CommonService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * View resolver.
     */
    @Autowired
    private InternalResourceViewResolver viewResolver;

    /**
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;


    /**
     * Constructor.
     */
    public CommonServiceImpl() {
        super();
    }


    /**
     * Get portlet repository.
     *
     * @return repository
     */
    protected abstract CommonRepository getRepository();


    @Override
    public String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException {
        // Path
        String path;

        try {
            View view = this.viewResolver.resolveViewName(name, null);
            JstlView jstlView = (JstlView) view;
            path = jstlView.getUrl();
        } catch (Exception e) {
            throw new PortletException(e);
        }

        return path;
    }


    @Override
    public SourceDocumentForm getSourceDocumentForm(PortalControllerContext portalControllerContext) {
        // Form
        SourceDocumentForm form = this.applicationContext.getBean(SourceDocumentForm.class);

        // Search scope
        form.setScope(SearchScope.DEFAULT);
        // Available search scopes
        form.setAvailableScopes(Arrays.asList(SearchScope.values()));

        return form;
    }


    @Override
    public void serveSearchResults(PortalControllerContext portalControllerContext, String filter, String scope) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();
        // Portlet context
        PortletContext portletContext = portalControllerContext.getPortletCtx();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Base path
        String basePath = window.getProperty(BASE_PATH_WINDOW_PROPERTY);

        // JSP path
        String jspPath = this.resolveViewPath(portalControllerContext, "search-results");

        if (StringUtils.isBlank(filter)) {
            // Empty filter
            request.setAttribute("emptyFilter", true);
        } else {
            // Nuxeo documents
            PaginableDocuments nuxeoDocuments = this.getRepository().searchDocuments(portalControllerContext, basePath, filter, SearchScope.fromId(scope));

            // Documents
            List<DocumentDTO> documents;
            if (nuxeoDocuments.isEmpty()) {
                documents = null;
            } else {
                documents = new ArrayList<>(nuxeoDocuments.size());

                for (Document nuxeoDocument : nuxeoDocuments) {
                    DocumentDTO document = this.documentDao.toDTO(nuxeoDocument);
                    documents.add(document);
                }
            }

            // Search results
            request.setAttribute("results", documents);
            request.setAttribute("total", nuxeoDocuments.getTotalSize());
        }



        // Request dispatcher
        PortletRequestDispatcher dispatcher = portletContext.getRequestDispatcher(jspPath);
        dispatcher.include(request, response);
    }

}
