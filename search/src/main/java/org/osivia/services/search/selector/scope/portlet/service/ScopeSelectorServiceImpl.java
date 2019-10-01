package org.osivia.services.search.selector.scope.portlet.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.search.portlet.service.SearchService;
import org.osivia.services.search.selector.scope.portlet.model.ScopeSelectorForm;
import org.osivia.services.search.selector.scope.portlet.model.ScopeSelectorSettings;
import org.osivia.services.search.selector.scope.portlet.model.SearchScope;
import org.osivia.services.search.selector.scope.portlet.repository.ScopeSelectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;

/**
 * Scope selector portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see ScopeSelectorService
 */
@Service
public class ScopeSelectorServiceImpl implements ScopeSelectorService {

    /** Label window property. */
    private static final String LABEL_WINDOW_PROPERTY = "foad.scope-selector.label";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private ScopeSelectorRepository repository;


    /**
     * Constructor.
     */
    public ScopeSelectorServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ScopeSelectorSettings getSettings(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        
        // Portlet settings
        ScopeSelectorSettings settings = this.applicationContext.getBean(ScopeSelectorSettings.class);
        
        // Label
        String label = window.getProperty(LABEL_WINDOW_PROPERTY);
        settings.setLabel(label);

        // Selector identifier
        settings.setSelectorId(SearchService.SCOPE_SELECTOR_ID);

        return settings;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, ScopeSelectorSettings settings) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Label
        String label = StringUtils.trimToNull(settings.getLabel());
        window.setProperty(LABEL_WINDOW_PROPERTY, label);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ScopeSelectorForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Portlet settings
        ScopeSelectorSettings settings = this.getSettings(portalControllerContext);

        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter("selectors"));
        // Scope search selector
        List<String> selectorValues = selectors.get(SearchService.SCOPE_SELECTOR_ID);

        // Empty selector indicator.
        boolean empty;
        if (CollectionUtils.isEmpty(selectorValues)) {
            empty = true;
        } else {
            // Selector value
            String selectorValue = selectorValues.get(0);

            empty = StringUtils.isEmpty(selectorValue);
        }


        // Form
        ScopeSelectorForm form = this.applicationContext.getBean(ScopeSelectorForm.class);

        // Label
        String label = settings.getLabel();
        form.setLabel(label);
        
        // Search scope
        SearchScope scope;
        if (empty) {
            scope = SearchScope.GLOBAL;
        } else {
            scope = SearchScope.LOCAL;
        }
        form.setScope(scope);
        
        // Scopes
        List<SearchScope> scopes = Arrays.asList(SearchScope.values());
        form.setScopes(scopes);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void select(PortalControllerContext portalControllerContext, ScopeSelectorForm form) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Portlet settings
        ScopeSelectorSettings settings = this.getSettings(portalControllerContext);
        // Selector identifier
        String selectorId = settings.getSelectorId();

        if (StringUtils.isNotEmpty(selectorId)) {
            // Selectors
            Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter("selectors"));

            // Search scope
            SearchScope scope = form.getScope();

            // Selector values
            if (SearchScope.LOCAL.equals(scope)) {
                // Root path
                Document root = this.repository.getRoot(portalControllerContext);
                String rootPath = root.getPath();

                if (StringUtils.isEmpty(rootPath)) {
                    selectors.remove(selectorId);
                } else {
                    List<String> selectorValues = new ArrayList<>(1);
                    selectorValues.add(rootPath);
                    selectors.put(selectorId, selectorValues);
                }
            } else {
                selectors.remove(selectorId);
            }

            response.setRenderParameter("selectors", PageSelectors.encodeProperties(selectors));
        }
    }

}
