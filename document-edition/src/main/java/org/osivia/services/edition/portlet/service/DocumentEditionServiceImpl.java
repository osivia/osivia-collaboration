package org.osivia.services.edition.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.IOException;

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
        String name = this.getName(portalControllerContext, windowProperties);
        if (StringUtils.isEmpty(name)) {
            throw new PortletException("Unable to find portlet repository name.");
        }
        // Repository
        DocumentEditionRepository repository = this.getRepository(name);

        // Form
        AbstractDocumentEditionForm form = repository.getForm(portalControllerContext, windowProperties);
        form.setName(name);

        // Document path
        String path = windowProperties.getDocumentPath();
        form.setPath(path);

        // Creation indicator
        boolean creation = StringUtils.isEmpty(path);
        form.setCreation(creation);

        return form;
    }


    @Override
    public String getViewPath(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException {
        DocumentEditionRepository repository = this.getRepository(form.getName());

        return repository.getViewPath(portalControllerContext);
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, AbstractDocumentEditionForm form) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Window properties
        DocumentEditionWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Repository
        DocumentEditionRepository repository = this.getRepository(form.getName());


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

        // Redirection
        String redirectionPath = StringUtils.defaultIfEmpty(windowProperties.getDocumentPath(), windowProperties.getParentDocumentPath());
        String redirectionUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, redirectionPath, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
        response.sendRedirect(redirectionUrl);
    }


    /**
     * Get repository name.
     *
     * @param portalControllerContext portal controller context
     * @param windowProperties        window properties
     * @return name
     */
    private String getName(PortalControllerContext portalControllerContext, DocumentEditionWindowProperties windowProperties) throws PortletException {
        String name;

        if (StringUtils.isEmpty(windowProperties.getDocumentPath())) {
            name = windowProperties.getDocumentType();
        } else {
            String[] names = this.applicationContext.getBeanNamesForType(DocumentEditionRepository.class);
            if (ArrayUtils.isEmpty(names)) {
                name = null;
            } else {
                // Repository
                DocumentEditionRepository repository = this.getRepository(names[0]);
                // Document context
                NuxeoDocumentContext documentContext = repository.getDocumentContext(portalControllerContext, windowProperties.getDocumentPath());
                // Document type
                DocumentType documentType = documentContext.getDocumentType();

                if (documentType == null) {
                    name = null;
                } else {
                    name = documentType.getName();
                }
            }
        }

        return name;
    }


    /**
     * Get portlet repository.
     *
     * @param name repository name
     * @return repository
     */
    private DocumentEditionRepository getRepository(String name) {
        return this.applicationContext.getBean(name, DocumentEditionRepository.class);
    }

}
