package org.osivia.services.workspace.sharing.portlet.service;

import java.io.IOException;
import java.util.SortedMap;
import java.util.UUID;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.workspace.sharing.portlet.model.SharingForm;
import org.osivia.services.workspace.sharing.portlet.model.SharingLink;
import org.osivia.services.workspace.sharing.portlet.model.SharingPermission;
import org.osivia.services.workspace.sharing.portlet.model.SharingWindowProperties;
import org.osivia.services.workspace.sharing.portlet.repository.SharingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Sharing portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SharingService
 */
@Service
public class SharingServiceImpl implements SharingService {

    /** Base 62. */
    private static final int BASE_62 = 62;

    /** Base 62 alphabet conversion table. */
    private static final char[] TO_BASE_62 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private SharingRepository repository;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public SharingServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SharingWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Window properties
        SharingWindowProperties windowProperties = this.applicationContext.getBean(SharingWindowProperties.class);

        // Path
        String path = window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
        windowProperties.setPath(path);

        return windowProperties;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SharingForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        // Form
        SharingForm form = this.applicationContext.getBean(SharingForm.class);

        // Enabled indicator
        boolean enabled = this.repository.isSharingEnabled(portalControllerContext, path);
        form.setEnabled(enabled);
        form.setInitialEnabled(enabled);

        if (enabled) {
            // Link
            SharingLink link = this.repository.getLink(portalControllerContext, path);
            form.setLink(link);

            // Users
            SortedMap<String, Boolean> users = this.repository.getUsers(portalControllerContext, path);
            form.setUsers(users);
        }

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void enableSharing(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException {
        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        // Sharing link
        SharingLink link = this.repository.getLink(portalControllerContext, path);
        // Link identifier
        String linkId = this.generateLinkId();
        link.setId(linkId);
        // Link URL
        String url = this.portalUrlFactory.getSharingLinkUrl(portalControllerContext, linkId);
        link.setUrl(url);
        
        // Enable sharing
        this.repository.enableSharing(portalControllerContext, path, link);

        // Update model
        form.setEnabled(true);
        form.setLink(link);
    }


    /**
     * Generate link identifier.
     * 
     * @return link identifier
     */
    public String generateLinkId() {
        // UUID
        UUID uuid = UUID.randomUUID();

        // Most significant bits
        long mostSignificantBits = Math.abs(uuid.getMostSignificantBits());
        String mostSignificantBitsBase64 = this.convertLongToBase62(mostSignificantBits);

        // Least significant bits
        long leastSignificantBits = Math.abs(uuid.getLeastSignificantBits());
        String leastSignificantBitsBase64 = this.convertLongToBase62(leastSignificantBits);

        StringBuilder builder = new StringBuilder();
        builder.append(mostSignificantBitsBase64);
        builder.append("-");
        builder.append(leastSignificantBitsBase64);
        return builder.toString();
    }


    /**
     * Convert long to base 62.
     * 
     * @param value long value
     * @return base 62 representation
     */
    public String convertLongToBase62(long value) {
        // Exponent
        int exponent = 1;
        while (Math.pow(BASE_62, exponent) < value) {
            exponent++;
        }

        // Base 62 array
        int[] base62Array = new int[exponent];
        long reminder = value;
        for (int i = (exponent - 1); i >= 0; i--) {
            long pow = Double.valueOf(Math.pow(BASE_62, i)).longValue();
            long div = reminder / pow;
            long mod = reminder % pow;

            base62Array[exponent - i - 1] = Long.valueOf(div).intValue();
            reminder = mod;
        }

        StringBuilder builder = new StringBuilder();
        for (int base62 : base62Array) {
            char c = TO_BASE_62[base62];
            builder.append(c);
        }

        return builder.toString();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void disableSharing(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException {
        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        // Disable sharing
        this.repository.disableSharing(portalControllerContext, path);

        // Update model
        form.setEnabled(false);
        form.setLink(null);
        form.setUsers(null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePermissions(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        // Link
        SharingLink link = form.getLink();

        if (link.isLiveEditable()) {
            // Link permission
            SharingPermission permission = link.getPermission();

            this.repository.updatePermissions(portalControllerContext, path, permission, null, null, null);

            // Notification
            String message = bundle.getString("SAVED_SHARING_LINK_PERMISSION_SUCCESS_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUser(PortalControllerContext portalControllerContext, SharingForm form, String user) throws PortletException {
        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        this.repository.updatePermissions(portalControllerContext, path, null, user, false, true);

        // Update model
        form.getUsers().put(user, false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreUser(PortalControllerContext portalControllerContext, SharingForm form, String user) throws PortletException {
        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        this.repository.updatePermissions(portalControllerContext, path, null, user, true, false);

        // Update model
        form.getUsers().put(user, true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void close(PortalControllerContext portalControllerContext, SharingForm form) throws PortletException, IOException {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();

        // Window properties
        SharingWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);
        // Document path
        String path = windowProperties.getPath();

        if (BooleanUtils.xor(new boolean[]{form.isEnabled(), form.isInitialEnabled()}) && (portletResponse instanceof ActionResponse)) {
            // Redirection
            String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null,
                    null, null, null);
            ActionResponse actionResponse = (ActionResponse) portletResponse;
            actionResponse.sendRedirect(url);
        } else {
            // Close modal
            form.setClose(true);
        }
    }

}
