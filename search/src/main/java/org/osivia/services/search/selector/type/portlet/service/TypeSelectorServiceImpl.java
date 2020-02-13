package org.osivia.services.search.selector.type.portlet.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.search.selector.type.portlet.model.SearchType;
import org.osivia.services.search.selector.type.portlet.model.TypeSelectorForm;
import org.osivia.services.search.selector.type.portlet.model.TypeSelectorSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;

/**
 * Type selector portlet service implementation.
 * 
 * @author Loïc Billon
 * @see TypeSelectorService
 */
@Service
public class TypeSelectorServiceImpl implements TypeSelectorService {



    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSelectorSettings getSettings(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        
        // Portlet settings
        TypeSelectorSettings settings = this.applicationContext.getBean(TypeSelectorSettings.class);
        
        // Label
        String label = window.getProperty(LABEL_WINDOW_PROPERTY);
        settings.setLabel(label);

        // Selector identifier
        settings.setSelectorId(TYPE_SELECTOR_ID);

        return settings;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, TypeSelectorSettings settings) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Label
        String label = StringUtils.trimToNull(settings.getLabel());
        window.setProperty(TypeSelectorService.LABEL_WINDOW_PROPERTY, label);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public TypeSelectorForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Portlet settings
        TypeSelectorSettings settings = this.getSettings(portalControllerContext);

        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter("selectors"));
        // Scope search selector
        List<String> selectorValues = selectors.get(TYPE_SELECTOR_ID);

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
        TypeSelectorForm form = this.applicationContext.getBean(TypeSelectorForm.class);

        // Label
        String label = settings.getLabel();
        form.setLabel(label);
        
        // Search scope
        SearchType type;
        if (empty) {
            type = SearchType.ALL;
        } else {
            type = SearchType.WORKSPACE;
        }
        form.setType(type);
        
        // Scopes
        List<SearchType> types = Arrays.asList(SearchType.values());
        form.setTypes(types);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void select(PortalControllerContext portalControllerContext, TypeSelectorForm form) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Portlet settings
        TypeSelectorSettings settings = this.getSettings(portalControllerContext);
        // Selector identifier
        String selectorId = settings.getSelectorId();

        if (StringUtils.isNotEmpty(selectorId)) {
            // Selectors
            Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter("selectors"));

            // Search scope
            SearchType type = form.getType();

            // Selector values
            if (SearchType.ALL.equals(type)) {
                selectors.remove(selectorId);
            } else {
                selectors.put(selectorId, Arrays.asList(type.getDocType()));
            }

            response.setRenderParameter("selectors", PageSelectors.encodeProperties(selectors));
        }
    }

}
