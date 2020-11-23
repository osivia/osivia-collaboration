package org.osivia.services.calendar.integration.portlet.service;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.calendar.integration.portlet.model.CalendarIntegrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Calendar integration portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarIntegrationService
 */
@Service
public class CalendarIntegrationServiceImpl implements CalendarIntegrationService {

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
     * Constructor.
     */
    public CalendarIntegrationServiceImpl() {
        super();
    }


    @Override
    public CalendarIntegrationForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Calendar path
        String path = getCalendarPath(portalControllerContext);

        // Form
        CalendarIntegrationForm form = this.applicationContext.getBean(CalendarIntegrationForm.class);

        // Integration URL
        String url;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("format", "ics");
        try {
            url = this.portalUrlFactory.getPermaLink(portalControllerContext, "integration", parameters, path,
                    IPortalUrlFactory.PERM_LINK_TYPE_PORTLET_RESOURCE);
        } catch (PortalException e) {
            throw new PortletException(e);
        }
        url = StringUtils.replace(url, "/commands?", "/commands/events.ics?");
        form.setUrl(url);

        return form;
    }


    /**
     * Get calendar path.
     *
     * @param portalControllerContext portal controller context
     * @return path
     */
    protected String getCalendarPath(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        return window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
    }

}
