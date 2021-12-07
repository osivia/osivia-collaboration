package org.osivia.services.calendar.edition.portlet.service;

import org.apache.commons.io.FileUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.service.CalendarService;
import org.osivia.services.calendar.edition.portlet.model.CalendarEditionForm;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Calendar edition portlet service interface.
 *
 * @author Cédric Krommenhoek
 * @see CalendarService
 */
public interface CalendarEditionService extends CalendarService {

    /**
     * File upload max size.
     */
    long FILE_UPLOAD_MAX_SIZE = 2L * FileUtils.ONE_MB;

    /**
     * Get calendar form.
     *
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    CalendarEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Upload vignette.
     *
     * @param portalControllerContext portal controller context
     * @param form                    calendar form
     * @throws PortletException
     * @throws IOException
     */
    void uploadVignette(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException, IOException;


    /**
     * Delete vignette.
     *
     * @param portalControllerContext portal controller context
     * @param form                    calendar form
     * @throws PortletException
     * @throws IOException
     */
    void deleteVignette(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException, IOException;


    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param options                 calendar edition options
     * @param form                    calendar form
     * @throws PortletException
     * @throws IOException
     */
    void save(PortalControllerContext portalControllerContext, CalendarEditionOptions options, CalendarEditionForm form) throws PortletException, IOException;


    /**
     * Cancel.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     * @throws IOException
     */
    void cancel(PortalControllerContext portalControllerContext) throws PortletException, IOException;


    /**
     * Add synchronization source.
     *
     * @param portalControllerContext portal controller context
     * @param form                    calendar form
     * @throws PortletException
     */
    void addSynchronizationSource(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException;


    /**
     * Edit synchronization source.
     *
     * @param portalControllerContext portal controller context
     * @param form                    calendar form
     * @throws PortletException
     */
    void editSynchronizationSource(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException;


    /**
     * Remove synchronization source.
     *
     * @param portalControllerContext portal controller context
     * @param form                    calendar form
     * @param sourceId                removed synchronization source identifier
     * @throws PortletException
     */
    void removeSynchronizationSource(PortalControllerContext portalControllerContext, CalendarEditionForm form, String sourceId) throws PortletException;


    /**
     * Vignette preview.
     *
     * @param portalControllerContext portal controller context
     * @param form                    calendar form
     * @throws PortletException
     * @throws IOException
     */
    void vignettePreview(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException, IOException;


    /**
     * Synchronization source edition URL.
     *
     * @param portalControllerContext portal controller context
     * @param form                    calendar form
     * @param sourceId                synchronization source identifier, null in case of creation
     * @throws PortletException
     * @throws IOException
     */
    void synchronizationSourceEditionUrl(PortalControllerContext portalControllerContext, CalendarEditionForm form, String sourceId)
            throws PortletException, IOException;

}
