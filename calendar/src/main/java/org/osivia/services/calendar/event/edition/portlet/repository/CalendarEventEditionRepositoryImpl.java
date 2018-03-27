package org.osivia.services.calendar.event.edition.portlet.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.PortletException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.Attachment;
import org.osivia.services.calendar.common.model.Attachments;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.model.CalendarEventDates;
import org.osivia.services.calendar.common.model.ICalendarColor;
import org.osivia.services.calendar.common.repository.CalendarRepositoryImpl;
import org.osivia.services.calendar.event.edition.portlet.model.CalendarEventEditionForm;
import org.osivia.services.calendar.event.edition.portlet.repository.command.CalendarEventCreationCommand;
import org.osivia.services.calendar.event.edition.portlet.repository.command.CalendarEventEditionCommand;
import org.osivia.services.calendar.event.edition.portlet.service.CalendarEventEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Calendar repository implementation.
 *
 * @author Cédric Krommenhoek
 * @author Julien Barberet
 * @see CalendarRepositoryImpl
 * @see CalendarEventEditionRepository
 */
@Repository
public class CalendarEventEditionRepositoryImpl extends CalendarRepositoryImpl implements CalendarEventEditionRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;
    
    /** Document DAO. */
    @Autowired
    private DocumentDAO dao;


    /** Date format. */
    private final DateFormat dateFormat;
    /** Time format. */
    private final DateFormat timeFormat;
    /** Half-hour. */
    private final int halfHour;


    /**
     * Constructor.
     */
    public CalendarEventEditionRepositoryImpl() {
        super();
        
        // Date format
        this.dateFormat = new SimpleDateFormat(CalendarEventEditionService.DATE_FORMAT_PATTERN);
        // Time format
        this.timeFormat = new SimpleDateFormat(CalendarEventEditionService.TIME_FORMAT_PATTERN);
        // Half-hour
        this.halfHour = Long.valueOf(TimeUnit.HOURS.toMinutes(1) / 2).intValue();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarColor getCalendarColor(PortalControllerContext portalControllerContext, CalendarEditionOptions options) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Calendar path
        String path;
        if (options.isCreation()) {
            path = options.getParentPath();
        } else {
            // Event path
            String eventPath = options.getDocument().getPath();

            path = StringUtils.substringBeforeLast(eventPath, "/");
        }

        // Calendar Nuxeo document
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        Document calendar = documentContext.getDocument();

        return this.getCalendarColor(portalControllerContext, calendar);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllDay(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        boolean allDay;

        if (document == null) {
            allDay = false;
        } else {
            allDay = BooleanUtils.toBoolean(document.getProperties().getBoolean(ALL_DAY_PROPERTY));
        }

        return allDay;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarEventDates getDates(PortalControllerContext portalControllerContext, Document document, boolean allDay) throws PortletException {
        // Dates
        CalendarEventDates dates = this.applicationContext.getBean(CalendarEventDates.class);

        // Start date
        Date startDate;
        // End date
        Date endDate;
        
        if (document == null) {
            startDate = null;
            endDate = null;
        } else {
            startDate = document.getDate(START_DATE_PROPERTY);
            endDate = document.getDate(END_DATE_PROPERTY);
        }
        
        if (startDate == null) {
            Date currentDate = new Date();
            
            // Start date
            startDate = DateUtils.truncate(currentDate, Calendar.HOUR);
            if (DateUtils.toCalendar(currentDate).get(Calendar.MINUTE) > this.halfHour) {
                startDate = DateUtils.addHours(startDate, 1);
            } else {
                startDate = DateUtils.addMinutes(startDate, this.halfHour);
            }
        }

        if (endDate == null) {
            // End date
            endDate = DateUtils.addHours(startDate, 1);
        }

        if (allDay) {
            startDate = DateUtils.truncate(startDate, Calendar.DAY_OF_MONTH);

            endDate = DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);
            endDate = DateUtils.addDays(endDate, -1);
        }

        dates.setStartDate(this.dateFormat.format(startDate));
        dates.setStartTime(this.timeFormat.format(startDate));
        dates.setEndDate(this.dateFormat.format(endDate));
        dates.setEndTime(this.timeFormat.format(endDate));

        return dates;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocation(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        String location;

        if (document == null) {
            location = null;
        } else {
            location = document.getString(LOCATION_PROPERTY);
        }

        return location;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ICalendarColor getColor(PortalControllerContext portalControllerContext, Document document, CalendarColor calendarColor) throws PortletException {
        // Color identifier
        String colorId;
        if (document == null) {
            colorId = null;
        } else {
            colorId = document.getString(COLOR_PROPERTY);
        }

        if ((colorId == null) && (calendarColor != null)) {
            colorId = calendarColor.getId();
        }

        return CalendarColor.fromId(colorId);
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
    public Attachments getAttachments(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Attachments
        Attachments attachments = this.applicationContext.getBean(Attachments.class);
        if (document != null && document.getProperties() != null)
        {
	        PropertyList propertyList = document.getProperties().getList(ATTACHMENTS_PROPERTY);
	        if ((propertyList != null) && !propertyList.isEmpty()) {
	            List<Attachment> files = attachments.getFiles();
	            if (files == null) {
	                files = new ArrayList<>();
	                attachments.setFiles(files);
	            }
	
	            for (int i = 0; i < propertyList.size(); i++) {
	                // Attachment
	                PropertyMap attachmentMap = propertyList.getMap(i);
	                PropertyMap attachmentFileMap = attachmentMap.getMap("file");
	                String fileName = attachmentFileMap.getString("name");
	                MimeType mimeType;
	                try {
	                    mimeType = new MimeType(attachmentFileMap.getString("mime-type"));
	                } catch (MimeTypeParseException e) {
	                    mimeType = null;
	                }
	
	
	                Attachment file = this.applicationContext.getBean(Attachment.class);
	                file.setIndex(i);
	                file.setTemporaryFileName(fileName);
	                file.setTemporaryMimeType(mimeType);
	                file.setIcon(this.dao.getIcon(mimeType));
	
	                files.add(file);
	            }
	        }
        }

        return attachments;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, CalendarEditionOptions options, CalendarEventEditionForm form) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command;
        if (options.isCreation()) {
            command = this.applicationContext.getBean(CalendarEventCreationCommand.class, options, form);
        } else {
            command = this.applicationContext.getBean(CalendarEventEditionCommand.class, options, form);
        }

        nuxeoController.executeNuxeoCommand(command);
    }

}
