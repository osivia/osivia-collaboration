package org.osivia.services.editor.link.portlet.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.FileMimeType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.editor.common.service.CommonServiceImpl;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.osivia.services.editor.link.portlet.model.FilterType;
import org.osivia.services.editor.link.portlet.model.UrlType;
import org.osivia.services.editor.link.portlet.model.comparator.FilterTypeComparator;
import org.osivia.services.editor.link.portlet.repository.EditorLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Editor link portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonServiceImpl
 * @see EditorLinkService
 */
@Service
public class EditorLinkServiceImpl extends CommonServiceImpl implements EditorLinkService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private EditorLinkRepository repository;

    /**
     * Filter type comparator.
     */
    @Autowired
    private FilterTypeComparator filterTypeComparator;

    /**
     * View resolver.
     */
    @Autowired
    private InternalResourceViewResolver viewResolver;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public EditorLinkServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, EditorLinkForm form) throws PortletException {
        if (UrlType.MANUAL.equals(form.getUrlType())) {
            form.setUrl(form.getManualUrl());

            if (StringUtils.isBlank(form.getText())) {
                form.setText(form.getManualUrl());
            }
        } else if (UrlType.DOCUMENT.equals(form.getUrlType())) {
            String webId = form.getDocumentWebId();

            String url = this.repository.getDocumentUrl(portalControllerContext, webId);
            form.setUrl(url);

            DocumentDTO document = this.repository.getDocumentDto(portalControllerContext, webId);
            form.setDocument(document);

            if (StringUtils.isBlank(form.getText())) {
                form.setText(document.getTitle());
            }
        }

        form.setDone(true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void unlink(PortalControllerContext portalControllerContext, EditorLinkForm form) throws PortletException {
        form.setUrl(StringUtils.EMPTY);
        form.setDone(true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject searchDocuments(PortalControllerContext portalControllerContext, String filter, int page) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Base path
        String basePath = window.getProperty(BASE_PATH_WINDOW_PROPERTY);

        // Documents
        PaginableDocuments documents = this.repository.searchDocuments(portalControllerContext, basePath, filter, page - 1);


        // Total results
        int total = documents.getTotalSize();
        // JSON objects
        List<JSONObject> objects = new ArrayList<>(Math.min(EditorLinkRepository.SELECT2_RESULTS_PAGE_SIZE, total));

        for (Document document : documents) {
            // Search result
            JSONObject object = new JSONObject();
            // Document properties
            Map<String, String> properties = this.repository.getDocumentProperties(portalControllerContext, document);

            for (Map.Entry<String, String> entry : properties.entrySet()) {
                object.put(entry.getKey(), entry.getValue());
            }

            objects.add(object);
        }


        // Results JSON object
        JSONObject results = new JSONObject();

        // Items JSON array
        JSONArray items = new JSONArray();

        // Message
        if ((page == 1) && CollectionUtils.isNotEmpty(objects)) {
            String message;
            if (total == 0) {
                message = bundle.getString("SELECT2_NO_RESULT");
            } else if (total == 1) {
                message = bundle.getString("SELECT2_ONE_RESULT");
            } else {
                message = bundle.getString("SELECT2_MULTIPLE_RESULTS", total);
            }
            JSONObject object = new JSONObject();
            object.put("message", message);
            items.add(object);
        }

        // Paginated results
        for (JSONObject object : objects) {
            items.add(object);
        }

        // Pagination informations
        results.put("page", page);
        results.put("pageSize", EditorLinkRepository.SELECT2_RESULTS_PAGE_SIZE);

        results.put("items", items);
        results.put("total", total);


        return results;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public EditorLinkForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        // URL
        String url = window.getProperty(URL_WINDOW_PROPERTY);
        // Text
        String text = window.getProperty(TEXT_WINDOW_PROPERTY);
        // Title
        String title = window.getProperty(TITLE_WINDOW_PROPERTY);
        // Only text indicator
        boolean onlyText = BooleanUtils.toBoolean(window.getProperty(ONLY_TEXT_WINDOW_PROPERTY));

        return this.repository.createForm(portalControllerContext, url, text, title, onlyText);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<UrlType> getUrlTypes(PortalControllerContext portalControllerContext) throws PortletException {
        return Arrays.asList(UrlType.values());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<FilterType> getFilterTypes(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Document types
        Collection<DocumentType> documentTypes = this.repository.getDocumentTypes(portalControllerContext);
        // File MIME types
        Map<String, FileMimeType> fileMimeTypes = this.repository.getFileMimeTypes(portalControllerContext);

        // Filter types
        List<FilterType> filterTypes = new ArrayList<>(documentTypes.size() + fileMimeTypes.size());

        // All types
        FilterType all = this.applicationContext.getBean(FilterType.class);
        all.setName(StringUtils.EMPTY);
        all.setDisplayName(bundle.getString("FILTER_TYPE_ALL"));
        all.setLevel(1);
        filterTypes.add(all);

        for (DocumentType documentType : documentTypes) {
            if (!documentType.isFile() || StringUtils.equals("File", documentType.getName())) {
                FilterType filterType = this.applicationContext.getBean(FilterType.class);
                all.setName(documentType.getName());
                all.setIcon(documentType.getIcon());
                all.setDisplayName(bundle.getString("FILTER_TYPE_" + StringUtils.upperCase(documentType.getName())));
                all.setLevel(1);
                filterTypes.add(filterType);
            }
        }

        for (FileMimeType fileDocumentType : fileMimeTypes.values()) {
            FilterType filterType = this.applicationContext.getBean(FilterType.class);
            all.setName(fileDocumentType.getMimeType().getBaseType());
            all.setIcon(fileDocumentType.getIcon());
            all.setDisplayName(fileDocumentType.getDescription());
            all.setLevel(2);
            filterTypes.add(filterType);
        }


        // Sort filter types
        Collections.sort(filterTypes, filterTypeComparator);

        return filterTypes;
    }


    /**
     * {@inheritDoc}
     */
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

}
