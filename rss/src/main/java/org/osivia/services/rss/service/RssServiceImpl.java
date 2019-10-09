package org.osivia.services.calendar.common.service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.model.CalendarOptions;
import org.osivia.services.calendar.common.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Rss service implementation.
 * 
 * @author Frédéric Boudan
 * @see CalendarService
 */
@Service("common-service")
public class RssServiceImpl implements RssService {

    /** Temporary file suffix. */
    protected static final String TEMPORARY_FILE_SUFFIX = ".tmp";
    /** Calendar color Nuxeo document property. */
    private static final String CALENDAR_COLOR_PROPERTY = "cal:color";


    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;

    /** Repository. */
    @Autowired
    @Qualifier("common-repository")
    protected CalendarRepository repository;


    /**
     * Constructor.
     */
    public CalendarServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException {
        // Calendar options
        CalendarOptions options = this.applicationContext.getBean("options", CalendarOptions.class);

        if (!options.isLoaded()) {
            // Loaded indicator
            options.setLoaded(true);
        }

        return options;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarEditionOptions getEditionOptions(PortalControllerContext portalControllerContext) throws PortletException {
        // Calendar edition options
        CalendarEditionOptions options = this.applicationContext.getBean("edition-options", CalendarEditionOptions.class);

        if (!options.isLoaded()) {
            // Portlet request
            PortletRequest request = portalControllerContext.getRequest();
            // Window
            PortalWindow window = WindowFactory.getWindow(request);


            // Creation indicator
            boolean creation = BooleanUtils.toBoolean(window.getProperty(CREATION_PROPERTY));
            options.setCreation(creation);

            // Current Nuxeo document
            Document document = this.repository.getCurrentDocument(portalControllerContext);

            if (creation) {
                // Parent path
                String parentPath = document.getPath();
                options.setParentPath(parentPath);
            } else {
                options.setDocument(document);
            }

            // Portlet title
            String portletTitle = this.getPortletTitle(portalControllerContext, creation);
            options.setPortletTitle(portletTitle);

            // Loaded indicator
            options.setLoaded(true);
        }

        return options;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarColor getCalendarColor(PortalControllerContext portalControllerContext, Document calendar) throws PortletException {
        // Color identifier
        String colorId;
        if (calendar == null) {
            colorId = null;
        } else {
            colorId = calendar.getString(CALENDAR_COLOR_PROPERTY);
        }

        return CalendarColor.fromId(colorId);
    }
    
    
    /**
     * Get portlet title.
     * 
     * @param portalControllerContext portal controller context
     * @param creation creation indicator
     * @return portlet title
     * @throws PortletException
     */
    protected String getPortletTitle(PortalControllerContext portalControllerContext, boolean creation) throws PortletException {
        return null;
    }

}
