package org.osivia.services.widgets.move.portlet.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.path.IBrowserService;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSObjectPath;
import org.osivia.services.widgets.move.portlet.model.MoveForm;
import org.osivia.services.widgets.move.portlet.model.MoveWindowProperties;
import org.osivia.services.widgets.move.portlet.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Move portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see MoveService
 */
@Service
public class MoveServiceImpl implements MoveService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private MoveRepository repository;

    /**
     * Portal URL factory
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Browser service.
     */
    @Autowired
    private IBrowserService browserService;

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
    
    /** The Constant HIDE_FIRST_LEVEL system property. */
    protected static final String HIDE_FIRST_LEVEL_SYSTEM_PROPERTY = "osivia.services.userWorkSpace.hideFirstLevel";


    /**
     * Constructor.
     */
    public MoveServiceImpl() {
        super();
    }


    @Override
    public MoveForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        MoveForm form = this.applicationContext.getBean(MoveForm.class);

        // Window properties
        MoveWindowProperties windowProperties = getWindowProperties(portalControllerContext);

        // Base path
        String basePath = this.repository.getBasePath(portalControllerContext, windowProperties);
        if (BooleanUtils.isTrue(BooleanUtils.toBooleanObject(System.getProperty(HIDE_FIRST_LEVEL_SYSTEM_PROPERTY)))) {
            // User workspace
            Document userWorkspace = this.repository.getUserWorkspace(portalControllerContext);
            if (userWorkspace != null) {
                if (StringUtils.startsWith(basePath, userWorkspace.getPath())) {
                    basePath = basePath + "/documents";
                    }
                }
            }
        form.setBasePath(basePath);

        
        

        // Navigation path
        String navigationPath = this.repository.getNavigationPath(portalControllerContext, windowProperties, basePath);
        form.setNavigationPath(navigationPath);

        // Ignored paths
        String[] ignoredPaths;
        if (CollectionUtils.isEmpty(windowProperties.getIgnoredPaths())) {
            ignoredPaths = null;
        } else {
            ignoredPaths = windowProperties.getIgnoredPaths().toArray(new String[0]);
        }
        form.setIgnoredPaths(ignoredPaths);

        // Accepted types
        String[] acceptedTypes;
        if (CollectionUtils.isEmpty(windowProperties.getAcceptedTypes())) {
            acceptedTypes = null;
        } else {
            acceptedTypes = windowProperties.getAcceptedTypes().toArray(new String[0]);
        }
        form.setAcceptedTypes(acceptedTypes);

        // Excluded types
        String[] excludedTypes = new String[]{"Workspace"};
        form.setExcludedTypes(excludedTypes);

        return form;
    }


    /**
     * Get window properties.
     *
     * @param portalControllerContext portal controller context
     * @return window properties
     */
    private MoveWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Window properties
        MoveWindowProperties properties = this.applicationContext.getBean(MoveWindowProperties.class);

        // Document path
        String path = window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
        properties.setPath(path);

        // Document identifiers
        List<String> identifiers = this.getWindowPropertyList(window, DOCUMENT_IDENTIFIERS_WINDOW_PROPERTY);
        properties.setIdentifiers(identifiers);

        // Ignored paths
        List<String> ignoredPaths = this.getWindowPropertyList(window, IGNORED_PATHS_WINDOW_PROPERTY);
        properties.setIgnoredPaths(ignoredPaths);

        // Base path
        String basePath = window.getProperty(BASE_PATH_WINDOW_PROPERTY);
        properties.setBasePath(basePath);

        // Accepted types
        List<String> acceptedTypes = this.getWindowPropertyList(window, ACCEPTED_TYPES_WINDOW_PROPERTY);
        properties.setAcceptedTypes(acceptedTypes);

        // Redirection URL
        String redirectionUrl = window.getProperty(REDIRECTION_URL_WINDOW_PROPERTY);
        properties.setRedirectionUrl(redirectionUrl);

        return properties;
    }


    /**
     * Get window property list.
     *
     * @param window window
     * @param name   property name
     * @return list
     */
    private List<String> getWindowPropertyList(PortalWindow window, String name) {
        String value = window.getProperty(name);
        List<String> result;
        if (StringUtils.isEmpty(value)) {
            result = null;
        } else {
            result = Arrays.asList(StringUtils.split(value, ","));
        }
        return result;
    }


    @Override
    public void move(PortalControllerContext portalControllerContext, MoveForm form) throws PortletException, IOException {
        // Window properties
        MoveWindowProperties windowProperties = getWindowProperties(portalControllerContext);
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());


        // Document path
        String path = windowProperties.getPath();

        // Source identifiers
        List<String> identifiers;
        // Redirection path
        String redirectionPath;

        if (CollectionUtils.isEmpty(windowProperties.getIdentifiers())) {
            Document document = this.repository.getDocument(portalControllerContext, path);

            identifiers = Collections.singletonList(document.getId());

            CMSObjectPath parentPath = CMSObjectPath.parse(path).getParent();
            redirectionPath = parentPath.toString();
        } else {
            identifiers = windowProperties.getIdentifiers();

            redirectionPath = path;
        }

        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        String spacePath = nuxeoController.getSpacePath(windowProperties.getBasePath());

        // Move
        this.repository.move(portalControllerContext, windowProperties.getBasePath(), identifiers, form.getTargetPath());
        

        nuxeoController.notifyUpdate( spacePath,windowProperties.getBasePath(), true);

        // Notification
        String message = bundle.getString("MOVE_MESSAGE_SUCCESS");
        this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);


        // Redirection
        String url;
        if (StringUtils.isEmpty(redirectionPath)) {
            url = windowProperties.getRedirectionUrl();
        } else {
            url = this.portalUrlFactory.getBackURL(portalControllerContext, false, true);

        }
        response.sendRedirect(url);
    }


    @Override
    public String browse(PortalControllerContext portalControllerContext) throws PortletException {
        // Data
        String data;
        try {
            data = this.browserService.browse(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return data;
    }

}
