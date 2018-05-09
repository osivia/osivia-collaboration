package org.osivia.services.calendar.event.edition.portlet.repository.command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.calendar.common.model.Attachment;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.event.edition.portlet.model.CalendarEventEditionForm;
import org.osivia.services.calendar.event.edition.portlet.model.comparator.IndexComparator;
import org.osivia.services.calendar.event.edition.portlet.repository.CalendarEventEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Calendar event edition Nuxeo command abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public abstract class AbstractCalendarEventCommand implements INuxeoCommand {

    /** Blob index comparator. */
    @Autowired
    private IndexComparator indexComparator;
    
    
    /**
     * Constructor.
     */
    public AbstractCalendarEventCommand() {
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
     * @param form calendar event edition form
     * @return properties
     */
    protected PropertyMap getProperties(CalendarEventEditionForm form) {
        // Color identifier
        String colorId;
        if (form.getColor() == null) {
            colorId = null;
        } else {
            colorId = ((CalendarColor) form.getColor()).getId();
        }
        // Color
        CalendarColor color = CalendarColor.fromId(colorId);
        if (color.equals(form.getCalendarColor())) {
            colorId = null;
        }


        // Document properties
        PropertyMap properties = new PropertyMap();
        properties.set(CalendarEventEditionRepository.TITLE_PROPERTY, form.getTitle());
        properties.set(CalendarEventEditionRepository.START_DATE_PROPERTY, form.getStartDate());
        properties.set(CalendarEventEditionRepository.END_DATE_PROPERTY, form.getEndDate());
        properties.set(CalendarEventEditionRepository.ALL_DAY_PROPERTY, form.isAllDay());
        properties.set(CalendarEventEditionRepository.LOCATION_PROPERTY, form.getLocation());
        properties.set(CalendarEventEditionRepository.COLOR_PROPERTY, colorId);
        properties.set(CalendarEventEditionRepository.DESCRIPTION_PROPERTY, form.getDescription());

        return properties;
    }


    /**
     * Set attachments.
     *
     * @param documentService Nuxeo document service
     * @param document Nuxeo document
     * @param attachments attachments
     * @throws Exception
     */
    protected void setAttachments(DocumentService documentService, Document document, CalendarEventEditionForm form) throws Exception {
        // Attachment files
        List<Attachment> files = form.getAttachments().getFiles();

        if (CollectionUtils.isNotEmpty(files)) {
            // Remove blobs
            SortedSet<Integer> removedIndexes = new TreeSet<>(this.indexComparator);
            for (Attachment file : files) {
                if (file.isDeleted() && (file.getIndex() != null)) {
                    removedIndexes.add(file.getIndex());
                }
            }
            for (Integer index : removedIndexes) {
                StringBuilder xpath = new StringBuilder();
                xpath.append(CalendarEventEditionRepository.ATTACHMENTS_PROPERTY);
                xpath.append("/item[");
                xpath.append(index);
                xpath.append("]");

                documentService.removeBlob(document, xpath.toString());
            }


            // Blobs
            List<Blob> blobs = new ArrayList<>(files.size());

            for (Attachment file : files) {
                // Temporary file
                File temporaryFile = file.getTemporaryFile();

                if (!file.isDeleted() && (temporaryFile != null)) {
                    // File name
                    String fileName = file.getTemporaryFileName();
                    // Mime type
                    String mimeType = file.getTemporaryMimeType().getBaseType();

                    // File blob
                    Blob blob = new FileBlob(temporaryFile, fileName, mimeType);

                    blobs.add(blob);
                }
            }

            if (!blobs.isEmpty()) {
                documentService.setBlobs(document, new Blobs(blobs), CalendarEventEditionRepository.ATTACHMENTS_PROPERTY);

                // Delete temporary files
                for (Attachment file : files) {
                    if (file.getTemporaryFile() != null) {
                        file.getTemporaryFile().delete();
                    }
                }
            }
        }
    }

}
