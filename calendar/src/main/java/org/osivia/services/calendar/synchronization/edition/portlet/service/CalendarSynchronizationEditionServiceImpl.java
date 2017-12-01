package org.osivia.services.calendar.synchronization.edition.portlet.service;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.service.CalendarServiceImpl;
import org.osivia.services.calendar.synchronization.edition.portlet.model.CalendarSynchronizationEditionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Calendar synchronization source edition portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarServiceImpl
 * @see CalendarSynchronizationEditionService
 */
@Service
public class CalendarSynchronizationEditionServiceImpl extends CalendarServiceImpl implements CalendarSynchronizationEditionService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public CalendarSynchronizationEditionServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarSynchronizationEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Synchronization source edition form
        CalendarSynchronizationEditionForm form = this.applicationContext.getBean(CalendarSynchronizationEditionForm.class);

        // Synchronization source identifier
        String sourceId = window.getProperty(SYNCHRONIZATION_SOURCE_ID);
        form.setSourceId(sourceId);

        // URL
        String url = window.getProperty(SYNCHRONIZATION_SOURCE_URL);
        form.setUrl(url);

        // Color
        String colorId = window.getProperty(SYNCHRONIZATION_SOURCE_COLOR);
        form.setColor(CalendarColor.fromId(colorId));

        // Display name
        String displayName = window.getProperty(SYNCHRONIZATION_SOURCE_DISPLAY_NAME);
        form.setDisplayName(displayName);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void submit(PortalControllerContext portalControllerContext, CalendarSynchronizationEditionForm form) throws PortletException {
        // Update model
        form.setDone(true);
    }

}
