package org.osivia.services.calendar.event.view.portlet.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.PortletException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.common.model.Attachment;
import org.osivia.services.calendar.common.model.Attachments;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.repository.CalendarRepository;
import org.osivia.services.calendar.common.service.CalendarServiceImpl;
import org.osivia.services.calendar.event.view.portlet.model.CalendarEventViewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Calendar event view portlet service implementation.
 *
 * @author Julien Barberet
 */
@Service
public class CalendarEventViewServiceImpl extends CalendarServiceImpl implements CalendarEventViewService {


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    @Qualifier("common-repository")
    private CalendarRepository repository;

    /** Document DAO. */
    @Autowired
    private DocumentDAO dao;


    /** Date format. */
    private final DateFormat dateFormat;


    /**
     * Constructor.
     */
    public CalendarEventViewServiceImpl() {
        super();

        // Date format
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void insertContentMenubarItems(PortalControllerContext portalControllerContext) throws PortletException {
        this.repository.insertContentMenubarItems(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarEventViewForm getForm(PortalControllerContext portalControllerContext) throws PortletException {

        // Calendar event edition form
        CalendarEventViewForm form = this.applicationContext.getBean(CalendarEventViewForm.class);

        Document document = this.repository.getCurrentDocument(portalControllerContext);

        form.setDocument(this.dao.toDTO(document));
        form.setTitle(document.getString(TITLE_PROPERTY));

        // All day indicator
        boolean allDay = this.isAllDay(portalControllerContext, document);
        form.setAllDay(allDay);

        form.setStartDate(document.getDate(START_DATE_PROPERTY));
        form.setEndDate(document.getDate(END_DATE_PROPERTY));

        form.setSameDay(this.isSameDay(form));
        form.setEndDateAllDay(this.getEndDateAllDay(form));

        // Location
        String location = this.getLocation(portalControllerContext, document);
        form.setLocation(location);

        // Calendar color
        CalendarColor calendarColor = this.getCalendarColor(portalControllerContext, document);
        // Color
        CalendarColor color = this.getColor(portalControllerContext, document, calendarColor);
        form.setColor(color);
        
        // Description
        String description = this.getDescription(portalControllerContext, document);
        form.setDescription(description);

        // Attachments
        this.setAttachments(portalControllerContext, document, form);

        return form;
    }


    private CalendarColor getCalendarColor(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Calendar path
        String path;

        // Event path
        String eventPath = document.getPath();

        path = StringUtils.substringBeforeLast(eventPath, "/");


        // Calendar Nuxeo document
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        Document calendar = documentContext.getDocument();

        // Color identifier
        String colorId = null;
        if (calendar != null) {
            String sourceId = document.getString(ID_PARENT_SOURCE_PROPERTY);
            if ((sourceId == null) || sourceId.isEmpty()) {
                colorId = calendar.getString(CALENDAR_COLOR_PROPERTY);
            } else {
                PropertyList propertyList = (PropertyList) calendar.getProperties().get(LIST_SOURCE_SYNCHRO);
                if (propertyList != null) {
                    for (int i = 0; i < propertyList.size(); i++) {
                        PropertyMap map = propertyList.getMap(i);
                        if (sourceId.equals(map.get(SOURCEID_SYNCHRONIZATION))) {
                            colorId = map.getString(COLOR_SYNCHRONIZATION);
                            break;
                        }
                    }
                }
            }
        }

        return CalendarColor.fromId(colorId);
    }


    private void setAttachments(PortalControllerContext portalControllerContext, Document document, CalendarEventViewForm form) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Attachments
        Attachments attachments = this.applicationContext.getBean(Attachments.class);
        form.setAttachments(attachments);

        // Attachment files
        PropertyList attachmentsList = document.getProperties().getList("files:files");
        if (attachmentsList != null) {
            List<Attachment> files = new ArrayList<>(attachmentsList.size());

            for (int i = 0; i < attachmentsList.size(); i++) {
                PropertyMap map = attachmentsList.getMap(i);
                PropertyMap fileMap = map.getMap("file");

                // Attachment file
                Attachment file = this.applicationContext.getBean(Attachment.class);

                // File name
                String fileName = fileMap.getString("name");
                file.setTemporaryFileName(fileName);

                // Icon
                MimeType mimeType;
                try {
                    mimeType = new MimeType(fileMap.getString("mime-type"));
                } catch (MimeTypeParseException e) {
                    mimeType = null;
                }
                file.setIcon(this.dao.getIcon(mimeType));

                // URL
                String url = nuxeoController.createAttachedFileLink(document.getPath(), String.valueOf(i));
                file.setUrl(url);

                files.add(file);
            }

            attachments.setFiles(files);
        }
    }


    private Date getEndDateAllDay(CalendarEventViewForm form) {
        if (form.getEndDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(form.getEndDate());
            cal.add(Calendar.DAY_OF_MONTH, -1);
            return cal.getTime();
        }

        return null;
    }


    private boolean isSameDay(CalendarEventViewForm form) {
        boolean sameDay = false;
        Calendar calStart = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();
        
        calStart.setTime(form.getStartDate());
        calEnd.setTime(form.getEndDate());
        
        if (form.isAllDay())
        {
            long diff = 0;
            diff = Math.round((calEnd.getTime().getTime() - calStart.getTime().getTime())/86400000);
            sameDay = diff <= 1;
        } else
        {
            calStart.set(Calendar.HOUR_OF_DAY, 0);
            calStart.set(Calendar.MINUTE, 0);
            calStart.set(Calendar.SECOND,0);
            calStart.set(Calendar.MILLISECOND, 0);
            
            calEnd.set(Calendar.HOUR_OF_DAY, 0);
            calEnd.set(Calendar.MINUTE, 0);
            calEnd.set(Calendar.SECOND,0);
            calEnd.set(Calendar.MILLISECOND, 0);
            
            sameDay = dateFormat.format(calStart.getTime()).equals(dateFormat.format(calEnd.getTime()));
        }
        return sameDay;
    }


    private boolean isAllDay(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        boolean allDay;

        if (document == null) {
            allDay = false;
        } else {
            allDay = BooleanUtils.toBoolean(document.getProperties().getBoolean(ALL_DAY_PROPERTY));
        }

        return allDay;
    }


    private String getLocation(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        String location;

        if (document == null) {
            location = null;
        } else {
            location = document.getString(LOCATION_PROPERTY);
        }

        return location;
    }


    private CalendarColor getColor(PortalControllerContext portalControllerContext, Document document, CalendarColor calendarColor) throws PortletException {
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


    private String getDescription(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        String description;

        if (document == null) {
            description = null;
        } else {
            description = document.getString(DESCRIPTION_PROPERTY);
        }

        return description;
    }

}
