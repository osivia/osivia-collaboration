package org.osivia.services.calendar.edition.portlet.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ResourceResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.CalendarEditionOptions;
import org.osivia.services.calendar.common.service.CalendarServiceImpl;
import org.osivia.services.calendar.edition.portlet.model.CalendarEditionForm;
import org.osivia.services.calendar.edition.portlet.model.CalendarSynchronizationSource;
import org.osivia.services.calendar.edition.portlet.model.Picture;
import org.osivia.services.calendar.edition.portlet.repository.CalendarEditionRepository;
import org.osivia.services.calendar.synchronization.edition.portlet.service.CalendarSynchronizationEditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;

/**
 * Calendar edition portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarServiceImpl
 * @see CalendarEditionService
 */
@Service
public class CalendarEditionServiceImpl extends CalendarServiceImpl implements CalendarEditionService {

    /** Portlet title internationalization key prefix. */
    private static final String PORTLET_TITLE_KEY_PREFIX = "CALENDAR_PORTLET_TITLE_";
    /** Creation portlet title internationalization key. */
    private static final String CREATION_PORTLET_TITLE_KEY = PORTLET_TITLE_KEY_PREFIX + "CREATION";
    /** Edition portlet title internationalization key. */
    private static final String EDITION_PORTLET_TITLE_KEY = PORTLET_TITLE_KEY_PREFIX + "EDITION";

    /** Vignette temporary file prefix. */
    private static final String VIGNETTE_TEMPORARY_FILE_PREFIX = "vignette-";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private CalendarEditionRepository repository;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public CalendarEditionServiceImpl() {
        super();
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
    public CalendarEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Calendar edition options
        CalendarEditionOptions options = this.getEditionOptions(portalControllerContext);
        // Calendar form
        CalendarEditionForm form = this.applicationContext.getBean(CalendarEditionForm.class);

        // Nuxeo document
        Document document = options.getDocument();

        // Title
        String title = this.repository.getTitle(portalControllerContext, document);
        form.setTitle(title);

        // Description
        String description = this.repository.getDescription(portalControllerContext, document);
        form.setDescription(description);

        // Vignette
        Picture vignette = this.repository.getVignette(portalControllerContext, document);
        form.setVignette(vignette);

        // Color
        CalendarColor color = this.repository.getCalendarColor(portalControllerContext, document);
        form.setColor(color);

        // Synchronization sources
        List<CalendarSynchronizationSource> sources = this.repository.getSynchronizationSources(portalControllerContext, document);
        form.setSynchronizationSources(sources);
        
        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void uploadVignette(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException, IOException {
        // Vignette
        Picture vignette = form.getVignette();
        vignette.setDeleted(false);

        // Upload
        MultipartFile upload = vignette.getUpload();

        // Temporary file
        File temporaryFile = vignette.getTemporaryFile();
        if (temporaryFile != null) {
            // Delete temporary file
            temporaryFile.delete();
        }
        temporaryFile = File.createTempFile(VIGNETTE_TEMPORARY_FILE_PREFIX, TEMPORARY_FILE_SUFFIX);
        temporaryFile.deleteOnExit();
        upload.transferTo(temporaryFile);
        vignette.setTemporaryFile(temporaryFile);

        // Temporary file name
        vignette.setTemporaryFileName(upload.getOriginalFilename());

        // Temporary file mime type
        MimeType mimeType;
        try {
            mimeType = new MimeType(upload.getContentType());
        } catch (MimeTypeParseException e) {
            mimeType = null;
        }
        vignette.setTemporaryMimeType(mimeType);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteVignette(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException, IOException {
        // Vignette
        Picture vignette = form.getVignette();
        vignette.setDeleted(true);

        // Temporary file
        File temporaryFile = vignette.getTemporaryFile();
        if (temporaryFile != null) {
            temporaryFile.delete();
            vignette.setTemporaryFile(null);
        }

        // File name
        vignette.setTemporaryFileName(null);

        // Mime type
        vignette.setTemporaryMimeType(null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, CalendarEditionOptions options, CalendarEditionForm form)
            throws PortletException, IOException {
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

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
     * {@inheritDoc}
     */
    @Override
    public void addSynchronizationSource(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException {
        // Added synchronization source
        CalendarSynchronizationSource addedSource = form.getAddedSynchronizationSource();

        // Generate synchronization source identifier
        String sourceId = UUID.randomUUID().toString();
        addedSource.setId(sourceId);

        // Synchronization sources
        List<CalendarSynchronizationSource> sources = form.getSynchronizationSources();
        sources.add(addedSource);

        // Update model
        form.setAddedSynchronizationSource(this.applicationContext.getBean(CalendarSynchronizationSource.class));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void editSynchronizationSource(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException {
        // Do nothing
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSynchronizationSource(PortalControllerContext portalControllerContext, CalendarEditionForm form, String sourceId)
            throws PortletException {
        // Synchronization sources
        List<CalendarSynchronizationSource> sources = form.getSynchronizationSources();

        // Removed synchronization source
        CalendarSynchronizationSource source = this.applicationContext.getBean(CalendarSynchronizationSource.class);
        source.setId(sourceId);

        sources.remove(source);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void vignettePreview(PortalControllerContext portalControllerContext, CalendarEditionForm form) throws PortletException, IOException {
        // Resource response
        ResourceResponse response = (ResourceResponse) portalControllerContext.getResponse();

        // Temporary file
        File temporaryFile = form.getVignette().getTemporaryFile();

        // Upload size
        Long size = new Long(temporaryFile.length());
        response.setContentLength(size.intValue());

        // Content type
        String contentType = response.getContentType();
        response.setContentType(contentType);

        // Character encoding
        response.setCharacterEncoding(CharEncoding.UTF_8);

        // No cache
        response.getCacheControl().setExpirationTime(0);


        // Input steam
        InputStream inputSteam = new FileInputStream(temporaryFile);
        // Output stream
        OutputStream outputStream = response.getPortletOutputStream();
        // Copy
        IOUtils.copy(inputSteam, outputStream);
        outputStream.close();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void synchronizationSourceEditionUrl(PortalControllerContext portalControllerContext, CalendarEditionForm form, String sourceId)
            throws PortletException, IOException {
        // Resource response
        ResourceResponse response = (ResourceResponse) portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

        // Synchronization source creation indicator
        boolean creation = StringUtils.isEmpty(sourceId);
        
        // Modal title
        String title;
        if (creation) {
            title = bundle.getString("CALENDAR_SYNCHRONIZATION_SOURCE_ADD_TITLE");
        } else {
            title = bundle.getString("CALENDAR_SYNCHRONIZATION_SOURCE_EDIT_TITLE");
        }

        // Synchronization source
        CalendarSynchronizationSource source = null;
        if (!creation && CollectionUtils.isNotEmpty(form.getSynchronizationSources())) {
            for (CalendarSynchronizationSource synchronizationSource : form.getSynchronizationSources()) {
                if (StringUtils.equals(sourceId, synchronizationSource.getId())) {
                    source = synchronizationSource;
                    break;
                }
            }
        }

        // Window properties
        Map<String, String> properties = new HashMap<>();
        if (!creation) {
            properties.put(CalendarSynchronizationEditionService.SYNCHRONIZATION_SOURCE_ID, sourceId);
        }
        if (source != null) {
            properties.put(CalendarSynchronizationEditionService.SYNCHRONIZATION_SOURCE_URL,
                    URLEncoder.encode(URLEncoder.encode(source.getUrl(), CharEncoding.UTF_8), CharEncoding.UTF_8));
            if (source.getColor() != null) {
                properties.put(CalendarSynchronizationEditionService.SYNCHRONIZATION_SOURCE_COLOR, source.getColor().getId());
            }
            properties.put(CalendarSynchronizationEditionService.SYNCHRONIZATION_SOURCE_DISPLAY_NAME, source.getDisplayName());
        }
        
        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, CalendarSynchronizationEditionService.PORTLET_INSTANCE, properties,
                    PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // JSON
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("url", url);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(object.toString());
        printWriter.close();
    }


    /**
     * Get redirection URL.
     *
     * @param portalControllerContext portal controller context
     * @param refresh refresh indicator
     * @return URL
     * @throws PortletException
     */
    private String getRedirectionUrl(PortalControllerContext portalControllerContext, boolean refresh) throws PortletException {
        // Redirection URL
        return this.portalUrlFactory.getBackURL(portalControllerContext, false, refresh);
    }

}
