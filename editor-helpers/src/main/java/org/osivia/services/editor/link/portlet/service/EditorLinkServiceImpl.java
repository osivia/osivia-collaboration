package org.osivia.services.editor.link.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.osivia.services.editor.link.portlet.model.UrlType;
import org.osivia.services.editor.link.portlet.repository.EditorLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Editor link portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see EditorLinkService
 */
@Service
public class EditorLinkServiceImpl implements EditorLinkService {

    /** Portlet repository. */
    @Autowired
    private EditorLinkRepository repository;

    /** Internationalization bundle factory. */
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
        String basePath = window.getProperty(BASE_PATH_PROPERTY);

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
        String url = window.getProperty(URL_PROPERTY);
        // Text
        String text = window.getProperty(TEXT_PROPERTY);
        // Title
        String title = window.getProperty(TITLE_PROPERTY);
        // Only text indicator
        boolean onlyText = BooleanUtils.toBoolean(window.getProperty(ONLY_TEXT_PROPERTY));

        return this.repository.createForm(portalControllerContext, url, text, title, onlyText);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<UrlType> getUrlTypes(PortalControllerContext portalControllerContext) throws PortletException {
        return Arrays.asList(UrlType.values());
    }

}
