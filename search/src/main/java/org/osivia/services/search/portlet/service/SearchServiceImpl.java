package org.osivia.services.search.portlet.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.page.PageParametersEncoder;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.search.portlet.model.SearchForm;
import org.osivia.services.search.portlet.model.SearchSettings;
import org.osivia.services.search.portlet.model.TaskPath;
import org.osivia.services.search.portlet.repository.SearchRepository;
import org.osivia.services.search.selector.type.portlet.model.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Search portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SearchService
 */
@Service
public class SearchServiceImpl implements SearchService {

    
    private static final String TASKPATH_WINDOW_PROPERTY = "osivia.collaboration.search.taskpath";

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private SearchRepository repository;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     */
    public SearchServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SearchForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.applicationContext.getBean(SearchForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String search(PortalControllerContext portalControllerContext, SearchForm form) throws PortletException {
        // Root path
        Document root = this.repository.getRoot(portalControllerContext);

        // Redirection URL
        String redirectionUrl;

        if (root == null) {
            try {
                // Advanced search command
            	// Selectors
                Map<String, List<String>> selectors = PageParametersEncoder.decodeProperties(null);
                selectors.put("type", Arrays.asList(SearchType.WORKSPACE.getDocType()));
                
                redirectionUrl = this.portalUrlFactory.getAdvancedSearchUrl(portalControllerContext, form.getQuery(), false, selectors);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        } else {
            
            TaskPath path = null;
            SearchSettings settings = getSettings(portalControllerContext);
            if(settings.getTaskPath() != null) {
                path = applicationContext.getBean(TaskPath.class);
                path.setCmsPath(settings.getTaskPath());
            }
            else {
                // Search task path
                path = this.repository.getSearchTaskPath(portalControllerContext, root.getPath());            
            }
            
            // Page parameters
            Map<String, String> parameters = new HashMap<>();

            // Selectors
            Map<String, List<String>> selectors = PageParametersEncoder.decodeProperties(null);
            // Query
            String query = StringUtils.trim(form.getQuery());
            if (StringUtils.isNotEmpty(query)) {
                selectors.put("search", Arrays.asList(query));
            }
            // Scope
            selectors.put(SCOPE_SELECTOR_ID, Arrays.asList(root.getPath()));
            
            // Ldap id for search users in space
            String workspaceId = root.getProperties().getString("webc:url");
            if (StringUtils.isNotEmpty(workspaceId)) {
                selectors.put("workspaceId", Arrays.asList(workspaceId));
            }

            // Update selectors
            parameters.put("selectors", PageParametersEncoder.encodeProperties(selectors));

            // Display context
            String displayContext;
            if (path.isUpdated()) {
                displayContext = IPortalUrlFactory.DISPLAYCTX_REFRESH;
            } else {
                displayContext = null;
            }

            // CMS URL
            redirectionUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path.getCmsPath(), parameters, null, displayContext, null, null,
                    null, null);
        }

        return redirectionUrl;
    }


    @Override
    public SearchSettings getSettings(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        
        // Portlet settings
        SearchSettings settings = this.applicationContext.getBean(SearchSettings.class);
        
        // Label
        String taskPath = window.getProperty(TASKPATH_WINDOW_PROPERTY);
        settings.setTaskPath(taskPath);

        return settings;
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, SearchSettings settings) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Label
        String taskPath = StringUtils.trimToNull(settings.getTaskPath());
        window.setProperty(TASKPATH_WINDOW_PROPERTY, taskPath);
    }

}
