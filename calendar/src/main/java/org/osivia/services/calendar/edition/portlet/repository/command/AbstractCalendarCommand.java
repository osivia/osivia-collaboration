package org.osivia.services.calendar.edition.portlet.repository.command;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.NuxeoPropertyList;
import org.nuxeo.ecm.automation.client.model.NuxeoPropertyMap;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.edition.portlet.model.CalendarEditionForm;
import org.osivia.services.calendar.edition.portlet.model.CalendarSynchronizationSource;
import org.osivia.services.calendar.edition.portlet.model.Picture;
import org.osivia.services.calendar.edition.portlet.repository.CalendarEditionRepository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Calendar edition Nuxeo command abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public abstract class AbstractCalendarCommand implements INuxeoCommand {

    /**
     * Constructor.
     */
    public AbstractCalendarCommand() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }


    /**
     * Get Nuxeo document properties.
     * 
     * @param form calendar edition form
     * @return properties
     */
    protected PropertyMap getProperties(CalendarEditionForm form) {
        // Color identifier
        String colorId;
        if (form.getColor() == null) {
            colorId = null;
        } else {
            colorId = ((CalendarColor) form.getColor()).getId();
        }
        
        // Synchronization sources property
        String synchronizationSources = this.getSynchronizationSourcesProperty(form.getSynchronizationSources());
        
        // Document properties
        PropertyMap properties = new PropertyMap();
        properties.set(CalendarEditionRepository.TITLE_PROPERTY, form.getTitle());
        properties.set(CalendarEditionRepository.DESCRIPTION_PROPERTY, form.getDescription());
        properties.set(CalendarEditionRepository.CALENDAR_COLOR_PROPERTY, colorId);
        properties.set(CalendarEditionRepository.SYNCHRONIZATION_SOURCES_PROPERTY, synchronizationSources);

        return properties;
    }


    /**
     * Get synchronization sources Nuxeo document property.
     * 
     * @param sources synchronization sources
     * @return property
     */
    private String getSynchronizationSourcesProperty(List<CalendarSynchronizationSource> sources) {
        String property;

        if (CollectionUtils.isEmpty(sources)) {
            property = null;
        } else {
            NuxeoPropertyList propertyList = new NuxeoPropertyList(sources.size());

            for (CalendarSynchronizationSource source : sources) {
                NuxeoPropertyMap propertyMap = new NuxeoPropertyMap();

                // Identifier
                String id = source.getId();
                propertyMap.put(CalendarEditionRepository.SYNCHRONIZATION_SOURCE_ID, id);

                // URL
                String url = StringUtils.trimToNull(source.getUrl());
                propertyMap.put(CalendarEditionRepository.SYNCHRONIZATION_SOURCE_URL, url);

                // Color
                String colorId;
                if (source.getColor() == null) {
                    colorId = null;
                } else {
                    colorId = source.getColor().getId();
                }
                propertyMap.put(CalendarEditionRepository.SYNCHRONIZATION_SOURCE_COLOR, colorId);

                // Display name
                String displayName = StringUtils.trimToNull(source.getDisplayName());
                propertyMap.put(CalendarEditionRepository.SYNCHRONIZATION_SOURCE_DISPLAY_NAME, displayName);

                propertyList.add(propertyMap);
            }

            property = propertyList.toString();
        }

        return property;
    }


    /**
     * Set vignette.
     * 
     * @param documentService Nuxeo document service
     * @param document Nuxeo document
     * @param form calendar form
     * @throws Exception
     */
    protected void setVignette(DocumentService documentService, Document document, CalendarEditionForm form) throws Exception {
        // Vignette
        Picture vignette = form.getVignette();
        // Temporary file
        File temporaryFile = vignette.getTemporaryFile();
        if (temporaryFile != null) {
            // File name
            String fileName = vignette.getTemporaryFileName();
            // Mime type
            String mimeType = vignette.getTemporaryMimeType().getBaseType();

            // File blob
            Blob blob = new FileBlob(temporaryFile, fileName, mimeType);

            documentService.setBlob(document, blob, CalendarEditionRepository.VIGNETTE_PROPERTY);

            // Delete temporary file
            temporaryFile.delete();
        } else if (vignette.isDeleted()) {
            documentService.removeBlob(document, CalendarEditionRepository.VIGNETTE_PROPERTY);
        }
    }

}
