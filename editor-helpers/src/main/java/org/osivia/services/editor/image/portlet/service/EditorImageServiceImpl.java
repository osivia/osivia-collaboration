package org.osivia.services.editor.image.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.services.editor.common.service.CommonServiceImpl;
import org.osivia.services.editor.image.portlet.model.*;
import org.osivia.services.editor.image.portlet.repository.EditorImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.*;
import java.io.*;
import java.util.*;
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
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;


    /**
     * Constructor.
     */
    public EditorImageServiceImpl() {
        super();

        // Attached image URL pattern
        this.attachedImageUrlPattern = Pattern.compile(ATTACHED_IMAGE_URL_REGEX);
    }


    @Override
    public EditorImageForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        EditorImageForm form = this.applicationContext.getBean(EditorImageForm.class);

        if (!form.isLoaded()) {
            // Window
            PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

            // Current document path
            String path = window.getProperty(PATH_WINDOW_PROPERTY);
            // Document
            Document document = this.repository.getDocument(portalControllerContext, path);

            // Attachments indicator
            boolean attachments = (document.getProperties().getList(EditorImageRepository.ATTACHED_IMAGES_PROPERTY) != null);


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
            List<ImageSourceType> availableSourceTypes = new ArrayList<>();
            if (attachments) {
                availableSourceTypes.add(ImageSourceType.ATTACHED);
            }
            availableSourceTypes.add(ImageSourceType.DOCUMENT);
            form.setAvailableSourceTypes(availableSourceTypes);

            // Loaded indicator
            form.setLoaded(true);
        }

        return form;
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, EditorImageForm form) {
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
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Current document path
        String path = window.getProperty(PATH_WINDOW_PROPERTY);

        // Attached image temporary file
        File temporaryFile = File.createTempFile("attached-image-", ".tmp");
        attachedForm.getUpload().transferTo(temporaryFile);
        // Attached image file name
        String fileName = attachedForm.getUpload().getOriginalFilename();
        // Attached image content type
        String contentType = attachedForm.getUpload().getContentType();

        this.repository.addAttachedImage(portalControllerContext, path, temporaryFile, fileName, contentType);

        // Delete temporary file
        if (!temporaryFile.delete()) {
            temporaryFile.deleteOnExit();
        }


        // Attached image index
        int index;
        if (CollectionUtils.isEmpty(attachedForm.getAttachedImages())) {
            index = 0;
        } else {
            index = attachedForm.getAttachedImages().size();
        }


        // Update attached images
        SortedSet<AttachedImage> attachedImages = this.repository.getAttachedImages(portalControllerContext, path);
        attachedForm.setAttachedImages(attachedImages);

        this.selectAttachedImage(portalControllerContext, attachedForm, index);
    }


    @Override
    public void selectAttachedImage(PortalControllerContext portalControllerContext, EditorImageSourceAttachedForm attachedForm, int index) throws PortletException {
        // Form
        EditorImageForm form = this.getForm(portalControllerContext);

        // Attached image
        AttachedImage attachedImage = this.getAttachedImage(attachedForm.getAttachedImages(), index);

        // URL
        String url;
        if (attachedImage == null) {
            url = null;
        } else {
            url = this.repository.getAttachedImageUrl(portalControllerContext, attachedImage);
        }
        form.setUrl(url);
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
    public EditorImageSourceDocumentForm getDocumentForm(PortalControllerContext portalControllerContext) {
        // Form
        EditorImageSourceDocumentForm form = this.applicationContext.getBean(EditorImageSourceDocumentForm.class);

        // Search scope
        form.setScope(SearchScope.DEFAULT);
        // Available search scopes
        form.setAvailableScopes(Arrays.asList(SearchScope.values()));

        return form;
    }


    @Override
    public void filterDocuments(PortalControllerContext portalControllerContext, EditorImageSourceDocumentForm documentForm) {
        // Do nothing
    }


    @Override
    public void serveSearchResults(PortalControllerContext portalControllerContext, String filter, String scope) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();
        // Portlet context
        PortletContext portletContext = portalControllerContext.getPortletCtx();

        // JSP path
        String jspPath = this.resolveViewPath(portalControllerContext, "search-results");

        // Search results
        List<DocumentDTO> results = this.search(portalControllerContext, filter, SearchScope.fromId(scope));
        request.setAttribute("results", results);

        // Request dispatcher
        PortletRequestDispatcher dispatcher = portletContext.getRequestDispatcher(jspPath);
        dispatcher.include(request, response);
    }


    /**
     * Search documents.
     *
     * @param portalControllerContext portal controller context
     * @param filter                  search filter
     * @param scope                   search scope
     * @return documents
     */
    private List<DocumentDTO> search(PortalControllerContext portalControllerContext, String filter, SearchScope scope) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Base path
        String basePath = window.getProperty(BASE_PATH_WINDOW_PROPERTY);

        // Nuxeo documents
        List<Document> nuxeoDocuments = this.repository.search(portalControllerContext, basePath, filter, scope);

        // Documents
        List<DocumentDTO> documents;
        if (CollectionUtils.isEmpty(nuxeoDocuments)) {
            documents = null;
        } else {
            documents = new ArrayList<>(nuxeoDocuments.size());

            for (Document nuxeoDocument : nuxeoDocuments) {
                DocumentDTO document = this.documentDao.toDTO(nuxeoDocument);
                documents.add(document);
            }
        }

        return documents;
    }


    @Override
    public void selectDocument(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Form
        EditorImageForm form = this.getForm(portalControllerContext);

        // URL
        String url = this.repository.getImageDocumentUrl(portalControllerContext, path);
        form.setUrl(url);
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
