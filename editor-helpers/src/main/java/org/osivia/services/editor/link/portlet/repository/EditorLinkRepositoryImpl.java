package org.osivia.services.editor.link.portlet.repository;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.FileMimeType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.web.IWebIdService;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.osivia.services.editor.link.portlet.model.UrlType;
import org.osivia.services.editor.link.portlet.repository.command.SearchDocumentsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoCustomizer;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Editor link portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see EditorLinkRepository
 */
@Repository
public class EditorLinkRepositoryImpl implements EditorLinkRepository {

    /** WebId Nuxeo document property. */
    private static final String WEB_ID_PROPERTY = "ttc:webid";
    /** Vignette Nuxeo document property. */
    private static final String VIGNETTE_PROPERTY = "ttc:vignette";
    /** Description Nuxeo document property. */
    private static final String DESCRIPTION_PROPERTY = "dc:description";

    /** Nuxeo document URL prefix. */
    private static final String DOCUMENT_URL_PREFIX = "/nuxeo/web/";
    /** Nuxeo document URL RegEx. */
    private static final String DOCUMENT_URL_REGEX = DOCUMENT_URL_PREFIX + "([a-zA-Z0-9-_/]+).*";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** WebId service. */
    @Autowired
    private IWebIdService webIdService;

    /** Nuxeo service. */
    @Autowired
    private INuxeoService nuxeoService;

    /** Document DAO. */
    @Autowired
    private DocumentDAO documentDAO;


    /** Document URL pattern. */
    private final Pattern documentUrlPattern;


    /**
     * Constructor.
     */
    public EditorLinkRepositoryImpl() {
        super();
        this.documentUrlPattern = Pattern.compile(DOCUMENT_URL_REGEX);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDocumentUrl(PortalControllerContext portalControllerContext, String webId) throws PortletException {
        return DOCUMENT_URL_PREFIX + webId;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentDTO getDocumentDto(PortalControllerContext portalControllerContext, String webId) throws PortletException {
        // Nuxeo document
        Document document = this.getDocument(portalControllerContext, webId);

        return this.documentDAO.toDTO(portalControllerContext, document);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PaginableDocuments searchDocuments(PortalControllerContext portalControllerContext, String basePath, String filter, int page) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(SearchDocumentsCommand.class, basePath, filter, page);

        return (PaginableDocuments) nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getDocumentProperties(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Document DTO
        DocumentDTO dto = this.documentDAO.toDTO(portalControllerContext, document);

        // Vignette property map
        PropertyMap vignettePropertyMap = document.getProperties().getMap(VIGNETTE_PROPERTY);
        // Vignette URL
        String vignetteUrl;
        if ((vignettePropertyMap == null) || vignettePropertyMap.isEmpty()) {
            vignetteUrl = null;
        } else {
            vignetteUrl = nuxeoController.createFileLink(document, VIGNETTE_PROPERTY);
        }

        // Icon
        String icon = dto.getIcon();

        // Document properties
        Map<String, String> properties = new HashMap<>();
        properties.put("id", document.getString(WEB_ID_PROPERTY));
        properties.put("title", document.getTitle());
        properties.put("vignette", vignetteUrl);
        properties.put("icon", icon);
        properties.put("description", document.getString(DESCRIPTION_PROPERTY));

        return properties;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public EditorLinkForm createForm(PortalControllerContext portalControllerContext, String url, String text, String title, boolean onlyText) throws
            PortletException {
        // Editor link form
        EditorLinkForm form = this.applicationContext.getBean(EditorLinkForm.class);
        form.setUrl(url);
        form.setText(text);
        form.setTitle(title);
        form.setDisplayText(onlyText);

        // Nuxeo document
        Document document;

        if (StringUtils.startsWith(url, DOCUMENT_URL_PREFIX)) {
            Matcher matcher = this.documentUrlPattern.matcher(url);
            if (matcher.matches()) {
                // WebId
                String webId = matcher.group(1);

                try {
                    document = this.getDocument(portalControllerContext, webId);
                } catch (NuxeoException e) {
                    document = null;
                }
            } else {
                document = null;
            }
        } else {
            document = null;
        }

        // URL type
        UrlType urlType;
        if (document == null) {
            urlType = UrlType.MANUAL;
            form.setManualUrl(url);
        } else {
            urlType = UrlType.DOCUMENT;
            form.setDocumentWebId(document.getString(WEB_ID_PROPERTY));

            // Document DTO
            DocumentDTO dto = this.documentDAO.toDTO(portalControllerContext, document);
            form.setDocument(dto);
        }
        form.setUrlType(urlType);

        return form;
    }


    /**
     * Get Nuxeo document from webId.
     *
     * @param portalControllerContext portal controller context
     * @param webId                   webId
     * @return Nuxeo document
     * @throws PortletException
     */
    private Document getDocument(PortalControllerContext portalControllerContext, String webId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Path
        String path = this.webIdService.webIdToFetchPath(webId);

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);

        return documentContext.getDocument();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<DocumentType> getDocumentTypes(PortalControllerContext portalControllerContext) throws PortletException {
        // CMS customizer
        INuxeoCustomizer cmsCustomizer = this.nuxeoService.getCMSCustomizer();

        return cmsCustomizer.getDocumentTypes().values();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, FileMimeType> getFileMimeTypes(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // CMS customizer
        INuxeoCustomizer cmsCustomizer = this.nuxeoService.getCMSCustomizer();

        return cmsCustomizer.getFileMimeTypes();
    }

}
