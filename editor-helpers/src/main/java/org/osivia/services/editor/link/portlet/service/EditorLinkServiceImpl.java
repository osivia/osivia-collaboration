package org.osivia.services.editor.link.portlet.service;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.editor.common.repository.CommonRepository;
import org.osivia.services.editor.common.service.CommonServiceImpl;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.osivia.services.editor.link.portlet.repository.EditorLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;

/**
 * Editor link portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonServiceImpl
 * @see EditorLinkService
 */
@Service
public class EditorLinkServiceImpl extends CommonServiceImpl implements EditorLinkService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private EditorLinkRepository repository;


    /**
     * Constructor.
     */
    public EditorLinkServiceImpl() {
        super();
    }


    @Override
    protected CommonRepository getRepository() {
        return this.repository;
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, EditorLinkForm form) throws PortletException {
        if (StringUtils.isBlank(form.getText())) {
            // Try to find a document from URL
            Document document = this.repository.getDocumentFromUrl(portalControllerContext, form.getUrl());

            // Default text
            String text;
            if ((document == null) || StringUtils.isEmpty(document.getTitle())) {
                text = form.getUrl();
            } else {
                text = document.getTitle();
            }

            form.setText(text);
        }

        form.setDone(true);
    }


    @Override
    public void unlink(PortalControllerContext portalControllerContext, EditorLinkForm form) {
        form.setUrl(StringUtils.EMPTY);
        form.setDone(true);
    }


    @Override
    public EditorLinkForm getForm(PortalControllerContext portalControllerContext) {
        // Form
        EditorLinkForm form = this.applicationContext.getBean(EditorLinkForm.class);

        if (!form.isLoaded()) {
            // Window
            PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

            // URL
            String url = window.getProperty(URL_WINDOW_PROPERTY);
            form.setUrl(url);

            // Text
            String text = window.getProperty(TEXT_WINDOW_PROPERTY);
            form.setText(text);

            // Title
            String title = window.getProperty(TITLE_WINDOW_PROPERTY);
            form.setTitle(title);

            // Only text indicator
            boolean onlyText = BooleanUtils.toBoolean(window.getProperty(ONLY_TEXT_WINDOW_PROPERTY));
            form.setDisplayText(onlyText);

            // Force open in new window indicator
            boolean forceNewWindow = BooleanUtils.toBoolean(window.getProperty(FORCE_NEW_WINDOW_WINDOW_PROPERTY));
            form.setForceNewWindow(forceNewWindow);

            // Loaded indicator
            form.setLoaded(true);
        }

        return form;
    }


    @Override
    public void selectDocument(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Source document
        Document source = this.repository.getDocument(portalControllerContext, path);
        // Source webId
        String webId = source.getString(EditorLinkRepository.WEB_ID_PROPERTY);

        // URL
        String url = this.repository.getDocumentUrl(portalControllerContext, webId);

        // Form
        EditorLinkForm form = this.getForm(portalControllerContext);
        form.setUrl(url);
    }

}
