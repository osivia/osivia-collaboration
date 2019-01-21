package org.osivia.services.workspace.sharing.portlet.service;

import java.util.UUID;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.workspace.sharing.portlet.model.SharingForm;
import org.osivia.services.workspace.sharing.portlet.model.SharingLink;
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

        if (enabled) {
            // Link
            SharingLink link = this.repository.getLink(portalControllerContext, path);
            form.setLink(link);
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
        SharingLink link = this.applicationContext.getBean(SharingLink.class);
        

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
            long div = Math.floorDiv(reminder, pow);
            long mod = Math.floorMod(reminder, pow);

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
    }

}
