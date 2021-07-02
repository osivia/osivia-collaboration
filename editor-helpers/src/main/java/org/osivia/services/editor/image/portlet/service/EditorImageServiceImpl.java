package org.osivia.services.editor.image.portlet.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.services.editor.common.repository.CommonRepository;
import org.osivia.services.editor.common.service.CommonServiceImpl;
import org.osivia.services.editor.image.portlet.model.*;
import org.osivia.services.editor.image.portlet.repository.EditorImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Editor image portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonServiceImpl
 * @see EditorImageService
 */
@Service
public class EditorImageServiceImpl extends CommonServiceImpl implements EditorImageService {

    /**
     * Attached image URL RegEx.
     */
    private static final String ATTACHED_IMAGE_URL_REGEX = EditorImageRepository.ATTACHED_IMAGE_URL_PREFIX + "(?<property>[^/]+)/(?<index>[0-9]+)/file.*";


    /**
     * Attached image URL pattern.
     */
    private final Pattern attachedImageUrlPattern;


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private EditorImageRepository repository;


    /**
     * Constructor.
     */
    public EditorImageServiceImpl() {
        super();

        // Attached image URL pattern
        this.attachedImageUrlPattern = Pattern.compile(ATTACHED_IMAGE_URL_REGEX);
    }


    @Override
    protected CommonRepository getRepository() {
        return this.repository;
    }


    @Override
    public EditorImageForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        EditorImageForm form = this.applicationContext.getBean(EditorImageForm.class);

        if (!form.isLoaded()) {
            // Window
            PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

            // Creation indicator
            boolean creation = BooleanUtils.toBoolean(window.getProperty(CREATION_WINDOW_PROPERTY));
            form.setCreation(creation);

            // Source URL
            String url = window.getProperty(SRC_WINDOW_PROPERTY);
            form.setUrl(url);

            // Alternate text
            String alt = window.getProperty(ALT_WINDOW_PROPERTY);
            form.setAlt(alt);

            // Height
            Integer height = NumberUtils.toInt(window.getProperty(HEIGHT_WINDOW_PROPERTY));
            if (height < 1) {
                height = null;
            }
            form.setHeight(height);

            // Width
            Integer width = NumberUtils.toInt(window.getProperty(WIDTH_WINDOW_PROPERTY));
            if (width < 1) {
                width = null;
            }
            form.setWidth(width);

            // Available image source types
            boolean attachments;
            if (creation) {
                attachments = false;
            } else {
                Document document = this.repository.getDocument(portalControllerContext, window.getProperty(PATH_WINDOW_PROPERTY));
                attachments = (document.getProperties().getList(EditorImageRepository.ATTACHED_IMAGES_PROPERTY) != null);
            }
            List<ImageSourceType> availableSourceTypes;
            if (attachments) {
                availableSourceTypes = new ArrayList<>();
                availableSourceTypes.add(ImageSourceType.ATTACHED);
                availableSourceTypes.add(ImageSourceType.DOCUMENT);
            } else {
                availableSourceTypes = null;
            }
            form.setAvailableSourceTypes(availableSourceTypes);

            // Loaded indicator
            form.setLoaded(true);
        }

        return form;
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, EditorImageForm form) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Current document path
        String path = window.getProperty(PATH_WINDOW_PROPERTY);

        if (StringUtils.equals(form.getTemporaryUrl(), form.getUrl())) {
            // Temporary attached image
            TemporaryAttachedImage temporaryAttachedImage = form.getTemporaryAttachedImage();
            if ((temporaryAttachedImage != null) && (temporaryAttachedImage.getFile() != null)) {
                this.repository.addAttachedImage(portalControllerContext, path, temporaryAttachedImage.getFile(), temporaryAttachedImage.getFileName(), temporaryAttachedImage.getContentType());
                this.deleteTemporaryFile(temporaryAttachedImage);
            } else if (StringUtils.isNotEmpty(form.getTemporaryImagePath())) {
                this.repository.copyAttachedImage(portalControllerContext, form.getTemporaryImagePath(), path);
            }

            form.setTemporaryAttachedImage(null);
            form.setTemporaryImagePath(null);
        }


        form.setDone(true);
    }


    @Override
    public EditorImageSourceAttachedForm getAttachedForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Current document path
        String path = window.getProperty(PATH_WINDOW_PROPERTY);

        // Attached image form
        EditorImageSourceAttachedForm attachedForm = this.applicationContext.getBean(EditorImageSourceAttachedForm.class);

        // Attached images
        SortedSet<AttachedImage> attachedImages = this.repository.getAttachedImages(portalControllerContext, path);
        attachedForm.setAttachedImages(attachedImages);

        return attachedForm;
    }


    @Override
    public void addAttachedImage(PortalControllerContext portalControllerContext, EditorImageSourceAttachedForm attachedForm) throws PortletException, IOException {
        // Attached image temporary file
        File temporaryFile = File.createTempFile("attached-image-", ".tmp");
        attachedForm.getUpload().transferTo(temporaryFile);
        // Attached image file name
        String fileName = attachedForm.getUpload().getOriginalFilename();
        // Attached image content type
        String contentType = attachedForm.getUpload().getContentType();

        // Form
        EditorImageForm form = this.getForm(portalControllerContext);

        // Temporary attached image
        this.deleteTemporaryFile(form.getTemporaryAttachedImage());
        TemporaryAttachedImage temporaryAttachedImage = this.applicationContext.getBean(TemporaryAttachedImage.class);
        temporaryAttachedImage.setFile(temporaryFile);
        temporaryAttachedImage.setFileName(fileName);
        temporaryAttachedImage.setContentType(contentType);
        form.setTemporaryAttachedImage(temporaryAttachedImage);


        // Attached image index
        int index;
        if (CollectionUtils.isEmpty(attachedForm.getAttachedImages())) {
            index = 0;
        } else {
            index = attachedForm.getAttachedImages().size();
        }

        // URL
        String url = this.repository.getAttachedImageUrl(portalControllerContext, index, fileName);
        form.setUrl(url);
        form.setTemporaryUrl(url);
    }


    @Override
    public void selectAttachedImage(PortalControllerContext portalControllerContext, EditorImageSourceAttachedForm attachedForm, int index) throws PortletException, IOException {
        // Form
        EditorImageForm form = this.getForm(portalControllerContext);

        // Attached image
        AttachedImage attachedImage = this.getAttachedImage(attachedForm.getAttachedImages(), index);

        // URL
        String url;
        if (attachedImage == null) {
            url = null;
        } else {
            url = this.repository.getAttachedImageUrl(portalControllerContext, index, attachedImage.getFileName());
        }
        form.setUrl(url);
        form.setTemporaryUrl(url);

        // Delete temporary attached image
        TemporaryAttachedImage temporaryAttachedImage = form.getTemporaryAttachedImage();
        if (temporaryAttachedImage != null) {
            if (temporaryAttachedImage.getFile() != null) {
                if (!temporaryAttachedImage.getFile().delete()) {
                    temporaryAttachedImage.getFile().deleteOnExit();
                }
            }

            form.setTemporaryAttachedImage(null);
        }
    }


    @Override
    public void deleteAttachedImage(PortalControllerContext portalControllerContext, EditorImageSourceAttachedForm attachedForm, int index) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Current document path
        String path = window.getProperty(PATH_WINDOW_PROPERTY);

        // Attached images
        SortedSet<AttachedImage> attachedImages = attachedForm.getAttachedImages();
        // Attached image
        AttachedImage attachedImage = this.getAttachedImage(attachedImages, index);

        if (attachedImage != null) {
            this.repository.deleteAttachedImage(portalControllerContext, path, index);

            // Update model
            attachedImages.remove(attachedImage);
        }
    }


    /**
     * Get attached image.
     *
     * @param attachedImages attached images
     * @param index          attached image index
     * @return attached image, or null if not found
     */
    private AttachedImage getAttachedImage(SortedSet<AttachedImage> attachedImages, int index) {
        AttachedImage result = null;

        if (CollectionUtils.isNotEmpty(attachedImages)) {
            Iterator<AttachedImage> iterator = attachedImages.iterator();
            while ((result == null) && iterator.hasNext()) {
                AttachedImage item = iterator.next();
                if (index == item.getIndex()) {
                    result = item;
                }
            }
        }

        return result;
    }


    @Override
    public void selectDocument(PortalControllerContext portalControllerContext, String path) throws PortletException, IOException {
        // Source document
        Document source = this.repository.getDocument(portalControllerContext, path);

        // Attached form
        EditorImageSourceAttachedForm attachedForm = this.getAttachedForm(portalControllerContext);

        // Attached image index
        int index;
        if (CollectionUtils.isEmpty(attachedForm.getAttachedImages())) {
            index = 0;
        } else {
            index = attachedForm.getAttachedImages().size();
        }

        // Form
        EditorImageForm form = this.getForm(portalControllerContext);

        // Temporary attached image
        this.deleteTemporaryFile(form.getTemporaryAttachedImage());
        form.setTemporaryAttachedImage(null);
        form.setTemporaryImagePath(path);

        // URL
        String url = this.repository.getAttachedImageUrl(portalControllerContext, index, source.getTitle());
        form.setUrl(url);
        form.setTemporaryUrl(url);
    }


    /**
     * Delete attached image temporary file.
     *
     * @param temporaryAttachedImage temporary attached image
     */
    private void deleteTemporaryFile(TemporaryAttachedImage temporaryAttachedImage) {
        if ((temporaryAttachedImage != null) && (temporaryAttachedImage.getFile() != null)) {
            if (!temporaryAttachedImage.getFile().delete()) {
                temporaryAttachedImage.getFile().deleteOnExit();
            }
        }
    }


    @Override
    public void serveImagePreview(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        // Resource request
        ResourceRequest request = (ResourceRequest) portalControllerContext.getRequest();
        // Resource response
        ResourceResponse response = (ResourceResponse) portalControllerContext.getResponse();

        // Image source
        String source = request.getParameter("src");
        // Binary content
        CMSBinaryContent binaryContent;

        Matcher attachedImageUrlMatcher = this.attachedImageUrlPattern.matcher(source);

        if (StringUtils.startsWith(source, EditorImageRepository.DOCUMENT_URL_PREFIX)) {
            String webId = StringUtils.substringAfterLast(StringUtils.substringBefore(source, "?"), "/");
            String[] parameters = StringUtils.split(StringUtils.substringAfter(source, "?"), "&");

            // Image content
            String content = "Original";
            if (ArrayUtils.isNotEmpty(parameters)) {
                for (String parameter : parameters) {
                    String[] split = StringUtils.split(parameter, "=");
                    if (ArrayUtils.getLength(split) == 2) {
                        if (StringUtils.equals("content", split[0])) {
                            content = split[1];
                        }
                    }
                }
            }

            binaryContent = this.repository.getImageDocumentPreviewBinaryContent(portalControllerContext, webId, content);
        } else if (attachedImageUrlMatcher.matches()) {
            // Attached image index
            int index = NumberUtils.toInt(attachedImageUrlMatcher.group("index"));

            binaryContent = this.repository.getAttachedImagePreviewBinaryContent(portalControllerContext, index);
        } else {
            throw new FileNotFoundException("Unknown source: " + source);
        }

        response.setContentType(binaryContent.getMimeType());
        response.setContentLength(binaryContent.getFileSize().intValue());
        response.getCacheControl().setExpirationTime(0);

        // Input steam
        FileInputStream inputStream = new FileInputStream(binaryContent.getFile());
        // Output stream
        OutputStream outputStream = response.getPortletOutputStream();
        // Copy
        IOUtils.copy(inputStream, outputStream);

        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }

}
