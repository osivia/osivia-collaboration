package org.osivia.services.calendar.edition.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.repository.CalendarRepository;
import org.osivia.services.calendar.edition.portlet.model.CalendarEditionForm;
import org.osivia.services.calendar.edition.portlet.model.CalendarSynchronizationSource;
import org.osivia.services.calendar.edition.portlet.model.Picture;

/**
 * Calendar edition portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarRepository
 */
public interface CalendarEditionRepository extends CalendarRepository {

    /** Description Nuxeo document property. */
    String DESCRIPTION_PROPERTY = "dc:description";
    /** Vignette Nuxeo document property. */
    String VIGNETTE_PROPERTY = "ttc:vignette";
    /** Synchronization sources Nuxeo document property. */
    String SYNCHRONIZATION_SOURCES_PROPERTY = "cal:sources";

    /** Synchronization source identifier. */
    String SYNCHRONIZATION_SOURCE_ID = "sourceId";
    /** Synchronization source URL. */
    String SYNCHRONIZATION_SOURCE_URL = "url";
    /** Synchronization source color. */
    String SYNCHRONIZATION_SOURCE_COLOR = "color";
    /** Synchronization source display name. */
    String SYNCHRONIZATION_SOURCE_DISPLAY_NAME = "displayName";


    /**
     * Get description.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return description
     * @throws PortletException
     */
    String getDescription(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get vignette.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return vignette
     * @throws PortletException
     */
    Picture getVignette(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get synchronization sources.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return synchornization sources
     * @throws PortletException
     */
    List<CalendarSynchronizationSource> getSynchronizationSources(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Save.
     * 
     * @param portalControllerContext portal controller context
     * @param options calendar edition options
     * @param form calendar form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, CalendarEditionOptions options, CalendarEditionForm form) throws PortletException;

}
