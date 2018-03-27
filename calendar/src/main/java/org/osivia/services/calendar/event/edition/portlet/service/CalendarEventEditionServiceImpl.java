package org.osivia.services.calendar.event.edition.portlet.service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.calendar.common.model.Attachment;
import org.osivia.services.calendar.common.model.Attachments;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.model.CalendarEventDates;
import org.osivia.services.calendar.common.model.ICalendarColor;
import org.osivia.services.calendar.common.service.CalendarServiceImpl;
import org.osivia.services.calendar.event.edition.portlet.model.CalendarEventEditionForm;
import org.osivia.services.calendar.event.edition.portlet.repository.CalendarEventEditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Calendar event edition portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarServiceImpl
 * @see CalendarEventEditionService
 */
@Service
public class CalendarEventEditionServiceImpl extends CalendarServiceImpl implements CalendarEventEditionService {

    /** Portlet title internationalization key prefix. */
    protected static final String PORTLET_TITLE_KEY_PREFIX = "CALENDAR_EVENT_PORTLET_TITLE_";
    /** Creation portlet title internationalization key. */
    protected static final String CREATION_PORTLET_TITLE_KEY = PORTLET_TITLE_KEY_PREFIX + "CREATION";
    /** Edition portlet title internationalization key. */
    protected static final String EDITION_PORTLET_TITLE_KEY = PORTLET_TITLE_KEY_PREFIX + "EDITION";

    /** Date format separator. */
    protected static final String DATE_FORMAT_SEPARATOR = " ";

    /** Attachment temporary file prefix. */
    protected static final String ATTACHMENT_TEMPORARY_FILE_PREFIX = "attachment-";


    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private CalendarEventEditionRepository repository;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Document DAO. */
    @Autowired
    private DocumentDAO dao;


    /** Log. */
    protected final Log log;

    /** Date format. */
    protected final DateFormat dateFormat;
    /** Date time format. */
    protected final DateFormat dateTimeFormat;


    /**
     * Constructor.
     */
    public CalendarEventEditionServiceImpl() {
        super();
        this.log = LogFactory.getLog(this.getClass());

        // Date format
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        // Date time format
        this.dateTimeFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN + DATE_FORMAT_SEPARATOR + TIME_FORMAT_PATTERN);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPortletTitle(PortalControllerContext portalControllerContext, boolean creation) throws PortletException {
        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);

        // Portlet title internationalization key
        String key;
        if (creation) {
            key = CREATION_PORTLET_TITLE_KEY;
        } else {
            key = EDITION_PORTLET_TITLE_KEY;
        }

        return bundle.getString(key);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarEventEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Calendar edition options
        CalendarEditionOptions options = this.getEditionOptions(portalControllerContext);
        // Calendar event edition form
        CalendarEventEditionForm form = this.applicationContext.getBean(CalendarEventEditionForm.class);
        
        // Event Nuxeo document
        Document document = options.getDocument();

        // Calendar color
        CalendarColor calendarColor = this.repository.getCalendarColor(portalControllerContext, options);
        form.setCalendarColor(calendarColor);

        // Title
        String title = this.repository.getTitle(portalControllerContext, document);
        form.setTitle(title);

        // All day indicator
        boolean allDay = this.repository.isAllDay(portalControllerContext, document);
        form.setAllDay(allDay);

        // Dates
        CalendarEventDates dates = this.repository.getDates(portalControllerContext, document, allDay);
        form.setDates(dates);
        this.updateFormDates(form);

        // Location
        String location = this.repository.getLocation(portalControllerContext, document);
        form.setLocation(location);

        // Color
        ICalendarColor color = this.repository.getColor(portalControllerContext, document, calendarColor);
        form.setColor(color);

        // Description
        String description = this.repository.getDescription(portalControllerContext, document);
        form.setDescription(description);

        // Attachments
        Attachments attachments = this.repository.getAttachments(portalControllerContext, document);
        form.setAttachments(attachments);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void uploadAttachments(PortalControllerContext portalControllerContext, CalendarEventEditionForm form) throws PortletException, IOException {
        // Attachments
        Attachments attachments = form.getAttachments();
        // Attachment files
        List<Attachment> files = attachments.getFiles();
        if (files == null) {
            files = new ArrayList<>();
            attachments.setFiles(files);
        }

        for (MultipartFile multipartFile : attachments.getUpload()) {
            // Attachment file
            Attachment file = this.applicationContext.getBean(Attachment.class);

            // Temporary file
            File temporaryFile = file.getTemporaryFile();
            if (temporaryFile != null) {
                // Delete temporary file
                temporaryFile.delete();
            }
            temporaryFile = File.createTempFile(ATTACHMENT_TEMPORARY_FILE_PREFIX, TEMPORARY_FILE_SUFFIX);
            temporaryFile.deleteOnExit();
            multipartFile.transferTo(temporaryFile);
            file.setTemporaryFile(temporaryFile);

            // Temporary file name
            file.setTemporaryFileName(multipartFile.getOriginalFilename());

            // Temporary mime type
            MimeType mimeType;
            try {
                mimeType = new MimeType(multipartFile.getContentType());
            } catch (MimeTypeParseException e) {
                mimeType = null;
            }
            file.setTemporaryMimeType(mimeType);

            // Icon
            String icon = this.dao.getIcon(mimeType);
            file.setIcon(icon);

            files.add(file);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAttachment(PortalControllerContext portalControllerContext, CalendarEventEditionForm form, int index)
            throws PortletException, IOException {
        List<Attachment> files = form.getAttachments().getFiles();
        Attachment file = files.get(index);
        file.setDeleted(true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreAttachment(PortalControllerContext portalControllerContext, CalendarEventEditionForm form, int index)
            throws PortletException, IOException {
        List<Attachment> files = form.getAttachments().getFiles();
        Attachment file = files.get(index);
        file.setDeleted(false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, CalendarEditionOptions options, CalendarEventEditionForm form)
            throws PortletException, IOException {
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Update form dates
        this.updateFormDates(form);

        this.repository.save(portalControllerContext, options, form);

        // Redirection URL
        String redirectionUrl = this.getRedirectionUrl(portalControllerContext, true);
        response.sendRedirect(redirectionUrl);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Redirection URL
        String redirectionUrl = this.getRedirectionUrl(portalControllerContext, false);
        response.sendRedirect(redirectionUrl);
    }


    /**
     * Update calendar event dates.
     * 
     * @param form calendar event edition form
     */
    protected void updateFormDates(CalendarEventEditionForm form) {
        // Dates
        CalendarEventDates dates = form.getDates();

        try {
            // Start date
            Date startDate;
            if (form.isAllDay()) {
                startDate = this.parseDate(dates.getStartDate());
            } else {
                startDate = this.parseDateTime(dates.getStartDate(), dates.getStartTime());
            }
            form.setStartDate(startDate);

            // End date
            Date endDate;
            if (form.isAllDay()) {
                endDate = this.parseDate(dates.getEndDate());
                endDate = DateUtils.addDays(endDate, 1);
            } else {
                endDate = this.parseDateTime(dates.getEndDate(), dates.getEndTime());
            }
            form.setEndDate(endDate);
        } catch (ParseException e) {
            this.log.error("Error when parsing event dates", e);
        }
    }


    /**
     * Parse formatted date.
     * 
     * @param date formatted date
     * @return date object
     * @throws ParseException
     */
    private Date parseDate(String date) throws ParseException {
        StringBuilder source = new StringBuilder();
        source.append(StringUtils.trimToEmpty(date));

        return this.dateFormat.parse(source.toString());
    }


    /**
     * Parse formatted date time.
     * 
     * @param date formatted date
     * @param time formatted time
     * @return date object
     * @throws ParseException
     */
    private Date parseDateTime(String date, String time) throws ParseException {
        StringBuilder source = new StringBuilder();
        source.append(StringUtils.trimToEmpty(date));
        source.append(DATE_FORMAT_SEPARATOR);
        source.append(StringUtils.trimToEmpty(time));

        return this.dateTimeFormat.parse(source.toString());
    }


    /**
     * Get redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param options calendar edition options
     * @param refresh refresh indicator
     * @return URL
     * @throws PortletException
     */
    private String getRedirectionUrl(PortalControllerContext portalControllerContext, boolean refresh) throws PortletException {
        // Redirection URL
        return this.portalUrlFactory.getBackURL(portalControllerContext, false, refresh);
    }

}
