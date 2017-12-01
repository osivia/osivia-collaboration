package org.osivia.services.calendar.edition.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.repository.CalendarRepositoryImpl;
import org.osivia.services.calendar.edition.portlet.model.CalendarEditionForm;
import org.osivia.services.calendar.edition.portlet.model.CalendarSynchronizationSource;
import org.osivia.services.calendar.edition.portlet.model.Picture;
import org.osivia.services.calendar.edition.portlet.repository.command.CalendarCreationCommand;
import org.osivia.services.calendar.edition.portlet.repository.command.CalendarEditionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Calendar edition portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarRepositoryImpl
 * @see CalendarEditionRepository
 */
@Repository
public class CalendarEditionRepositoryImpl extends CalendarRepositoryImpl implements CalendarEditionRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public CalendarEditionRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        String description;

        if (document == null) {
            description = null;
        } else {
            description = document.getString(DESCRIPTION_PROPERTY);
        }

        return description;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Picture getVignette(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Vignette
        Picture vignette = this.applicationContext.getBean(Picture.class);

        if (document != null) {
            // Vignette Nuxeo document properties
            PropertyMap propertyMap = document.getProperties().getMap(VIGNETTE_PROPERTY);
            if ((propertyMap != null) && !propertyMap.isEmpty()) {
                // Vignette URL
                String url = nuxeoController.createFileLink(document, VIGNETTE_PROPERTY);
                vignette.setUrl(url);
            }
        }

        return vignette;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<CalendarSynchronizationSource> getSynchronizationSources(PortalControllerContext portalControllerContext, Document document)
            throws PortletException {
        // Synchronization sources
        List<CalendarSynchronizationSource> sources;

        if (document == null) {
            sources = new ArrayList<>(0);
        } else {
            PropertyList propertyList = document.getProperties().getList(SYNCHRONIZATION_SOURCES_PROPERTY);

            if (propertyList == null) {
                sources = new ArrayList<>(0);
            } else {
                sources = new ArrayList<>(propertyList.size());

                for (int i = 0; i < propertyList.size(); i++) {
                    PropertyMap propertyMap = propertyList.getMap(i);

                    // Synchronization source
                    CalendarSynchronizationSource source = this.applicationContext.getBean(CalendarSynchronizationSource.class);

                    // Identifier
                    String id = propertyMap.getString(SYNCHRONIZATION_SOURCE_ID);
                    source.setId(id);

                    // URL
                    String url = propertyMap.getString(SYNCHRONIZATION_SOURCE_URL);
                    source.setUrl(url);

                    // Color
                    String colorId = propertyMap.getString(SYNCHRONIZATION_SOURCE_COLOR);
                    source.setColor(CalendarColor.fromId(colorId));

                    // Display name
                    String displayName = propertyMap.getString(SYNCHRONIZATION_SOURCE_DISPLAY_NAME);
                    source.setDisplayName(displayName);

                    sources.add(source);
                }
            }
        }

        return sources;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, CalendarEditionOptions options, CalendarEditionForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;
        if (options.isCreation()) {
            command = this.applicationContext.getBean(CalendarCreationCommand.class, options, form);
        } else {
            command = this.applicationContext.getBean(CalendarEditionCommand.class, options, form);
        }

        nuxeoController.executeNuxeoCommand(command);
    }

}
