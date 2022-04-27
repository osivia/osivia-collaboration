package org.osivia.services.edition.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.editor.EditorService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.edition.portlet.model.AbstractDocumentEditionForm;
import org.osivia.services.edition.portlet.model.DocumentEditionWindowProperties;
import org.osivia.services.edition.portlet.repository.DocumentEditionRepository;
import org.osivia.services.edition.portlet.repository.ZipExtractionRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.portlet.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Document edition portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see DocumentEditionService
 */
@Service
public class DocumentEditionServiceImpl implements DocumentEditionService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Notifications service.
     */
    @Autowired
    private INotificationsService notificationsService;

    /**
     * Editor service.
     */
    @Autowired
    private EditorService editorService;


    /**
     * Constructor.
     */
    public DocumentEditionServiceImpl() {
        super();
    }


    @Override
    public DocumentEditionWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Window properties
        DocumentEditionWindowProperties properties = this.applicationContext.getBean(DocumentEditionWindowProperties.class);

        // Document path
        String documentPath = window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
        properties.setDocumentPath(documentPath);

        // Parent document path
        String parentDocumentPath = window.getProperty(PARENT_DOCUMENT_PATH_WINDOW_PROPERTY);
        properties.setParentDocumentPath(parentDocumentPath);

        // Document type
        String documentType = window.getProperty(DOCUMENT_TYPE_WINDOW_PROPERTY);
        properties.setDocumentType(documentType);

        // Extract archives mode
        String extractArchive = window.getProperty(EXTRACT_ARCHIVE_WINDOW_PROPERTY);
        properties.setExtractArchive(BooleanUtils.toBoolean(extractArchive));

        return properties;
    }


    @Override
    public AbstractDocumentEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Window properties
        DocumentEditionWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        if (StringUtils.isEmpty(windowProperties.getDocumentPath()) && (StringUtils.isEmpty(windowProperties.getParentDocumentPath()) || StringUtils.isEmpty(windowProperties.getDocumentType()))) {
            throw new PortletException("Invalid window properties.");
        }

        // Repository name
        String name = this.getRepositoryName(portalControllerContext, windowProperties);
        if (StringUtils.isEmpty(name)) {
            throw new PortletException("Unable to find portlet repository name.");
        }
        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(name);

        // Form
        AbstractDocumentEditionForm form = repository.getForm(portalControllerContext, windowProperties);
        form.setWindowProperties(windowProperties);
        form.setName(name);

        // Document path
        String path = windowProperties.getDocumentPath();
        form.setPath(path);

        // Creation indicator
        boolean creation = StringUtils.isEmpty(path);
        form.setCreation(creation);

        form.setExtractArchive(windowProperties.isExtractArchive());

        // for logging in validation phase
        form.setRemoteUser(portalControllerContext.getRequest().getRemoteUser());

        return form;
    }


    @Override
    public DocumentEditionRepository<?> getRepository(String name) {
        return this.applicationContext.getBean(name, DocumentEditionRepository.class);
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Render response
        RenderResponse response = (RenderResponse) portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Form
        AbstractDocumentEditionForm form = this.getForm(portalControllerContext);

        // Title
        String title;
        if (form.getWindowProperties().isExtractArchive()) {
            title = bundle.getString("DOCUMENT_EDITION_TITLE_IMPORT");
        } else if (form.isCreation()) {
            title = bundle.getString("DOCUMENT_EDITION_TITLE_CREATE");
        } else {
            title = bundle.getString("DOCUMENT_EDITION_TITLE_EDIT");
        }
        response.setTitle(title);

        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());

        return repository.getViewPath(portalControllerContext);
    }


    @Override
    public void upload(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());

        repository.upload(portalControllerContext, form);
    }


    @Override
    public void restore(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());

        repository.restore(portalControllerContext, form);
    }


    @Override
    public void validate(AbstractDocumentEditionForm form, Errors errors) {
        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());

        repository.validate(form, errors);
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Repository
        DocumentEditionRepository<?> repository = this.getRepository(form.getName());
        // Save document
        repository.save(portalControllerContext, form);


        // Notification
        String message;
        if (form.isCreation()) {
            message = bundle.getString("DOCUMENT_EDITION_MESSAGE_CREATION_SUCCESS");
        } else {
            message = bundle.getString("DOCUMENT_EDITION_MESSAGE_EDITION_SUCCESS");
        }
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);

        // Redirect
        this.redirect(portalControllerContext);
    }


    @Override
    public void cancel(PortalControllerContext portalControllerContext) throws IOException {
        // Redirect
        this.redirect(portalControllerContext);
    }


    /**
     * Redirect.
     *
     * @param portalControllerContext portal controller context
     */
    private void redirect(PortalControllerContext portalControllerContext) throws IOException {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();

        if (portletResponse instanceof ActionResponse) {
            // Action response
            ActionResponse actionResponse = (ActionResponse) portletResponse;

            // Window properties
            DocumentEditionWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

            // Redirection
            String redirectionPath = StringUtils.defaultIfEmpty(windowProperties.getDocumentPath(), windowProperties.getParentDocumentPath());
            String redirectionUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, redirectionPath, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
            actionResponse.sendRedirect(redirectionUrl);
        }
    }


    /**
     * Get repository name.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @return name
     */
    private String getRepositoryName(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties) throws PortletException {
        String repositoryName;

        if (windowProperties.isExtractArchive()) {
            String[] names = this.applicationContext.getBeanNamesForType(ZipExtractionRepositoryImpl.class);

            if (ArrayUtils.getLength(names) == 1) {
                repositoryName = names[0];
            } else {
                repositoryName = null;
            }
        } else {
            String documentType;
            boolean creation;
            if (StringUtils.isEmpty(windowProperties.getDocumentPath())) {
                documentType = windowProperties.getDocumentType();
                creation = true;
            } else {
                // Default repository
                DocumentEditionRepository<?> defaultRepository = this.applicationContext.getBeansOfType(DocumentEditionRepository.class).values().stream().findFirst().orElse(null);

                // Document context
                NuxeoDocumentContext documentContext;
                if (defaultRepository == null) {
                    documentContext = null;
                } else {
                    documentContext = defaultRepository.getDocumentContext(portalControllerContext, windowProperties.getDocumentPath());
                }

                if ((documentContext == null) || (documentContext.getDocumentType() == null)) {
                    documentType = null;
                } else {
                    documentType = documentContext.getDocumentType().getName();
                }
                creation = false;
            }


            Map<String, DocumentEditionRepository<?>> repositories = this.applicationContext.getBeansOfType(DocumentEditionRepository.class).entrySet().stream().filter(item -> item.getValue().matches(documentType, creation)).collect(Collectors.toMap(Map.Entry::getKey, item -> (DocumentEditionRepository<?>) item.getValue()));
            if (MapUtils.isEmpty(repositories)) {
                repositoryName = null;
            } else if (repositories.size() == 1) {
                repositoryName = repositories.keySet().iterator().next();
            } else {
                List<String> primaryRepositoryNames = repositories.entrySet().stream().filter(item -> item.getValue().getClass().isAnnotationPresent(Primary.class)).map(Map.Entry::getKey).collect(Collectors.toList());
                if (primaryRepositoryNames.size() == 1) {
                    repositoryName = primaryRepositoryNames.get(0);
                } else {
                    repositoryName = null;
                }
            }
        }

        return repositoryName;
    }


    @Override
    public void serveEditor(PortalControllerContext portalControllerContext, String editorId) throws PortletException, IOException {
        this.editorService.serveResource(portalControllerContext, editorId);
    }

}
