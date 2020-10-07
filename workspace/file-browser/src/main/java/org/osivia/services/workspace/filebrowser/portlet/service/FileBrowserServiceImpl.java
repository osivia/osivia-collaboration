package org.osivia.services.workspace.filebrowser.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.liveedit.OnlyofficeLiveEditHelper;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.jboss.portal.common.invocation.Scope;
import org.jboss.portal.core.controller.ControllerContext;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.preferences.UserPreferences;
import org.osivia.directory.v2.service.preferences.UserPreferencesService;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.AccessibilityRoles;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.portal.core.constants.InternalConstants;
import org.osivia.portal.core.context.ControllerContextAdapter;
import org.osivia.services.workspace.filebrowser.portlet.configuration.FileBrowserConfiguration;
import org.osivia.services.workspace.filebrowser.portlet.model.*;
import org.osivia.services.workspace.filebrowser.portlet.model.comparator.FileBrowserItemComparator;
import org.osivia.services.workspace.filebrowser.portlet.repository.FileBrowserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.portlet.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * File browser portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserService
 */
@Service
public class FileBrowserServiceImpl implements FileBrowserService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private FileBrowserRepository repository;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Notifications service.
     */
    @Autowired
    private INotificationsService notificationsService;

    /**
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;

    /**
     * User preferences service.
     */
    @Autowired
    private UserPreferencesService userPreferencesService;


    /**
     * Constructor.
     */
    public FileBrowserServiceImpl() {
        super();
    }


    @Override
    public FileBrowserWindowProperties getWindowProperties(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);


        // Window properties
        FileBrowserWindowProperties windowProperties = this.applicationContext.getBean(FileBrowserWindowProperties.class);

        // Document base path
        String basePath = window.getProperty(BASE_PATH_WINDOW_PROPERTY);
        windowProperties.setBasePath(basePath);

        // Document path
        String path = window.getProperty(PATH_WINDOW_PROPERTY);
        windowProperties.setPath(path);

        // NXQL request
        String nxql = window.getProperty(NXQL_WINDOW_PROPERTY);
        windowProperties.setNxql(nxql);

        // BeanShell indicator
        boolean beanShell = BooleanUtils.toBoolean(window.getProperty(BEANSHELL_WINDOW_PROPERTY));
        windowProperties.setBeanShell(beanShell);

        // List mode indicator
        boolean listMode = BooleanUtils.toBoolean(window.getProperty(LIST_MODE_WINDOW_PROPERTY));
        windowProperties.setListMode(listMode);

        // Default sort field
        String defaultSortField = window.getProperty(DEFAULT_SORT_FIELD_WINDOW_PROPERTY);
        windowProperties.setDefaultSortField(defaultSortField);

        return windowProperties;
    }


    @Override
    public void setWindowProperties(PortalControllerContext portalControllerContext, FileBrowserWindowProperties windowProperties) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);


        // Document base path
        String basePath = StringUtils.trimToNull(windowProperties.getBasePath());
        window.setProperty(BASE_PATH_WINDOW_PROPERTY, basePath);

        // NXQL request
        String nxql = StringUtils.trimToNull(windowProperties.getNxql());
        window.setProperty(NXQL_WINDOW_PROPERTY, nxql);

        // BeanShell indicator
        String beanShell = BooleanUtils.toStringTrueFalse(windowProperties.getBeanShell());
        window.setProperty(BEANSHELL_WINDOW_PROPERTY, beanShell);

        // List mode indicator
        String listMode = BooleanUtils.toStringTrueFalse(windowProperties.getListMode());
        window.setProperty(LIST_MODE_WINDOW_PROPERTY, listMode);

        // Default sort field
        String defaultSortField = StringUtils.trimToNull(windowProperties.getDefaultSortField());
        window.setProperty(DEFAULT_SORT_FIELD_WINDOW_PROPERTY, defaultSortField);
    }


    @Override
    public FileBrowserView getView(PortalControllerContext portalControllerContext, String viewId) throws PortletException {
        // View
        FileBrowserView view;

        if (StringUtils.isEmpty(viewId)) {
            // Form
            FileBrowserForm form = this.getForm(portalControllerContext);

            if (StringUtils.isEmpty(form.getPath())) {
                view = FileBrowserView.DEFAULT;
            } else {
                // Current document
                NuxeoDocumentContext documentContext = this.repository.getDocumentContext(portalControllerContext, form.getPath());
                Document document = documentContext.getDocument();
                // WebId
                String webId = document.getString("ttc:webid");

                // User preferences
                UserPreferences userPreferences = null;
                try {
                    userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
                } catch (PortalException e) {
                    throw new PortletException(e);
                }

                if (userPreferences != null) {
                    // Folder displays
                    Map<String, String> folderDisplays = userPreferences.getFolderDisplays();

                    // Saved view
                    String savedView = folderDisplays.get(webId);
                    view = FileBrowserView.fromId(savedView);
                } else {
                    view = FileBrowserView.DEFAULT;
                }
            }
        } else {
            view = FileBrowserView.fromId(viewId);
        }

        return view;
    }


    @Override
    public void saveView(PortalControllerContext portalControllerContext, FileBrowserForm form, FileBrowserView view) throws PortletException {
        if (StringUtils.isNotEmpty(form.getPath())) {
            // Document
            NuxeoDocumentContext documentContext = this.repository.getDocumentContext(portalControllerContext, form.getPath());
            Document document = documentContext.getDocument();
            // WebId
            String webId = document.getString("ttc:webid");

            // User preferences
            UserPreferences userPreferences = null;
            try {
                userPreferences = this.userPreferencesService.getUserPreferences(portalControllerContext);
            } catch (PortalException e) {
                throw new PortletException(e);
            }

            if (userPreferences != null) {
                // Folder displays
                Map<String, String> folderDisplays = userPreferences.getFolderDisplays();

                // Update folder displays
                folderDisplays.put(webId, view.getId());

                // Update user preferences
                userPreferences.setFolderDisplays(folderDisplays);
                userPreferences.setUpdated(true);
            }
        }
    }


    @Override
    public FileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        FileBrowserForm form = this.applicationContext.getBean(FileBrowserForm.class);

        if (!form.isInitialized()) {
            this.initializeForm(portalControllerContext, form);

            // Initialized indicator
            form.setInitialized(true);
        }

        return form;
    }


    /**
     * Initialize form.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     */
    protected void initializeForm(PortalControllerContext portalControllerContext, FileBrowserForm form) throws PortletException {
        // Window properties
        FileBrowserWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // Document base path
        String basePath = this.repository.getBasePath(portalControllerContext, windowProperties);
        form.setBasePath(basePath);

        // Document path
        String path = this.repository.getContentPath(portalControllerContext, windowProperties);
        form.setPath(path);

        // List mode indicator
        boolean listMode = BooleanUtils.isTrue(windowProperties.getListMode());
        form.setListMode(listMode);

        // Current document type
        DocumentType type;
        if (StringUtils.isEmpty(path)) {
            type = null;
        } else {
            // Current document context
            NuxeoDocumentContext documentContext = this.repository.getDocumentContext(portalControllerContext, path);

            type = documentContext.getDocumentType();
        }

        // Documents
        List<Document> documents = this.repository.getDocuments(portalControllerContext, windowProperties, path);

        // User subscriptions
        Set<String> userSubscriptions = this.repository.getUserSubscriptions(portalControllerContext);

        // Items
        List<FileBrowserItem> items;
        if (CollectionUtils.isEmpty(documents)) {
            items = null;
        } else {
            items = new ArrayList<>(documents.size());

            for (int i = 0; i < documents.size(); i++) {
                // Document
                Document document = documents.get(i);

                FileBrowserItem item = this.createItem(portalControllerContext, document);

                // Subscription
                boolean subscription = userSubscriptions.contains(document.getId());
                item.setSubscription(subscription);

                // Native order
                item.setNativeOrder(i);

                // Parent document
                if (listMode) {
                    Document parent = this.repository.getParentDocument(portalControllerContext, document);
                    if (parent != null) {
                        DocumentDTO parentDto = this.documentDao.toDTO(parent);
                        item.setParentDocument(parentDto);
                    }
                }

                items.add(item);
            }
        }
        form.setItems(items);

        // Default field sort
        FileBrowserSortField field = this.getDefaultSortField(portalControllerContext);

        if (listMode) {
            // Sort criteria
            FileBrowserSortCriteria criteria = this.applicationContext.getBean(FileBrowserSortCriteria.class);
            criteria.setField(field);
            criteria.setAlt(false);
            form.setCriteria(criteria);
        } else {
            // Sort
            this.sortItems(portalControllerContext, form, field, false);

            // Uploadable indicator
            boolean uploadable = (type != null) && CollectionUtils.isNotEmpty(type.getSubtypes());
            form.setUploadable(uploadable);

            // Max file size
            form.setMaxFileSize(FileBrowserConfiguration.MAX_UPLOAD_SIZE_PER_FILE);
        }
    }


    /**
     * Create file browser item.
     *
     * @param portalControllerContext portal controller context
     * @param nuxeoDocument           Nuxeo document
     * @return item
     */
    protected FileBrowserItem createItem(PortalControllerContext portalControllerContext, Document nuxeoDocument) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Item
        FileBrowserItem item = this.applicationContext.getBean(FileBrowserItem.class);

        // Document DTO
        DocumentDTO documentDto = this.documentDao.toDTO(nuxeoDocument);
        item.setDocument(documentDto);
        // Document type
        DocumentType type = documentDto.getType();

        // Title
        String title = documentDto.getTitle();
        item.setTitle(title);

        // Lock icon
        String lockOwner = nuxeoDocument.getString("ttc:lockOwner");
        String lockIcon;
        if (StringUtils.isEmpty(lockOwner)) {
            lockIcon = null;
        } else if (StringUtils.equals(request.getRemoteUser(), lockOwner)) {
            lockIcon = "glyphicons glyphicons-user-lock";
        } else {
            lockIcon = "glyphicons glyphicons-lock";
        }
        item.setLock(lockIcon);

        // Last modification
        Date lastModification = nuxeoDocument.getDate("dc:modified");
        item.setLastModification(lastModification);

        // Last contributor
        String lastContributor = nuxeoDocument.getString("dc:lastContributor");
        item.setLastContributor(lastContributor);

        // File size
        Long size = nuxeoDocument.getLong("common:size");
        item.setSize(size);

        // Folderish indicator
        boolean folderish = ((type != null) && type.isFolderish());
        item.setFolderish(folderish);

        // File MIME type
        if ((type != null) && type.isFile()) {
            PropertyMap fileContent = nuxeoDocument.getProperties().getMap("file:content");
            if (fileContent != null) {
                String mimeType = fileContent.getString("mime-type");
                item.setMimeType(mimeType);
            }
        }

        // Folderish accepted types
        if (folderish) {
            List<String> acceptedTypes = type.getSubtypes();
            item.setAcceptedTypes(StringUtils.join(acceptedTypes, ","));
        }

        return item;
    }


    @Override
    public void sortItems(PortalControllerContext portalControllerContext, FileBrowserForm form, FileBrowserSortField field, boolean alt) throws PortletException {
        // Controller context
        ControllerContext controllerContext = ControllerContextAdapter.getControllerContext(portalControllerContext);

        // Window properties
        FileBrowserWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // List mode indicator
        boolean listMode = BooleanUtils.isTrue(windowProperties.getListMode());

        // Sort criteria
        FileBrowserSortCriteria criteria;

        if (form.isInitialized()) {
            criteria = this.applicationContext.getBean(FileBrowserSortCriteria.class);
            criteria.setField(field);
            criteria.setAlt(alt);

            if (!listMode) {
                controllerContext.setAttribute(Scope.PRINCIPAL_SCOPE, SORT_CRITERIA_ATTRIBUTE, criteria);
            }
        } else {
            Object criteriaAttribute = controllerContext.getAttribute(Scope.PRINCIPAL_SCOPE, SORT_CRITERIA_ATTRIBUTE);

            if (listMode || !(criteriaAttribute instanceof FileBrowserSortCriteria)) {
                criteria = this.applicationContext.getBean(FileBrowserSortCriteria.class);
                criteria.setField(field);
                criteria.setAlt(alt);
            } else {
                criteria = (FileBrowserSortCriteria) criteriaAttribute;
            }
        }

        if (CollectionUtils.isNotEmpty(form.getItems())) {
            // Comparator
            FileBrowserItemComparator comparator = this.applicationContext.getBean(FileBrowserItemComparator.class, criteria);

            form.getItems().sort(comparator);
        }

        // Update model
        form.setCriteria(criteria);
    }


    @Override
    public List<FileBrowserSortField> getSortFields(PortalControllerContext portalControllerContext) throws PortletException {
        // Window properties
        FileBrowserWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // List mode indicator
        boolean listMode = BooleanUtils.isTrue(windowProperties.getListMode());

        return this.getSortFields(portalControllerContext, listMode);
    }


    @Override
    public List<FileBrowserSortField> getSortFields(PortalControllerContext portalControllerContext, boolean listMode) throws PortletException {
        // Sort fields
        List<FileBrowserSortField> fields = this.getAllSortFields();

        // Default sort field
        FileBrowserSortField defaultSortField = this.getDefaultSortField(portalControllerContext);

        // Filtered sort fields
        List<FileBrowserSortField> filteredFields = new ArrayList<>(fields.size());
        for (FileBrowserSortField field : fields) {
            if ((listMode || !field.isListMode()) && !(StringUtils.equals(FileBrowserSortEnum.RELEVANCE.getId(), field.getId()) && ((defaultSortField == null) || !StringUtils.equals(FileBrowserSortEnum.RELEVANCE.getId(), defaultSortField.getId())))) {
                filteredFields.add(field);
            }
        }

        return filteredFields;
    }


    @Override
    public FileBrowserSortField getSortField(PortalControllerContext portalControllerContext, String fieldId) {
        return this.getSortField(portalControllerContext, fieldId, true);
    }


    /**
     * Get sort field.
     *
     * @param portalControllerContext portal controller context
     * @param fieldId                 sort field identifier
     * @param getDefaultSortField     get default sort field indicator
     * @return sort field
     */
    protected FileBrowserSortField getSortField(PortalControllerContext portalControllerContext, String fieldId, boolean getDefaultSortField) {
        // Sort fields
        List<FileBrowserSortField> fields = this.getAllSortFields();

        // Result
        FileBrowserSortField result = null;
        int i = 0;
        while ((result == null) && (i < fields.size())) {
            FileBrowserSortField value = fields.get(i);

            if (StringUtils.equals(fieldId, value.getId())) {
                result = value;
            }

            i++;
        }

        if ((result == null) && getDefaultSortField) {
            // Default result
            result = this.getDefaultSortField(portalControllerContext);
        }

        return result;
    }


    /**
     * Get all sort fields.
     *
     * @return sort fields
     */
    protected List<FileBrowserSortField> getAllSortFields() {
        // Enum values
        FileBrowserSortEnum[] values = FileBrowserSortEnum.values();

        return new ArrayList<>(Arrays.asList(values));
    }


    /**
     * Get default sort field.
     *
     * @param portalControllerContext portal controller context
     * @return field
     */
    protected FileBrowserSortField getDefaultSortField(PortalControllerContext portalControllerContext) {
        // Window properties
        FileBrowserWindowProperties windowProperties = this.getWindowProperties(portalControllerContext);

        // Default sort field
        FileBrowserSortField field;
        if (windowProperties.getDefaultSortField() != null) {
            field = this.getSortField(portalControllerContext, windowProperties.getDefaultSortField(), false);
        } else if (BooleanUtils.isTrue(windowProperties.getListMode())) {
            field = FileBrowserSortEnum.RELEVANCE;
        } else {
            field = FileBrowserSortEnum.TITLE;
        }

        return field;
    }


    @Override
    public Element getToolbar(PortalControllerContext portalControllerContext, FileBrowserForm form, List<String> indexes, String viewId)
            throws PortletException {
        // Toolbar container
        Element container = DOM4JUtils.generateDivElement(null);

        // Toolbar
        Element toolbar = DOM4JUtils.generateDivElement("btn-toolbar", AccessibilityRoles.TOOLBAR);
        container.add(toolbar);

        if (CollectionUtils.isNotEmpty(indexes)) {
            // Internationalization bundle
            Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

            // File browser items
            List<FileBrowserItem> items = form.getItems();

            // Selected file browser items
            List<FileBrowserItem> selectedItems = new ArrayList<>(indexes.size());
            for (String index : indexes) {
                int i = NumberUtils.toInt(index, -1);
                if ((i > -1) && (i < items.size())) {
                    FileBrowserItem item = items.get(i);
                    selectedItems.add(item);
                }
            }

            if (indexes.size() == selectedItems.size()) {
                // All editable indicator
                boolean allEditable = true;
                // All file indicator
                boolean allFile = true;

                // View
                FileBrowserView view = this.getView(portalControllerContext, viewId);

                // Selected documents
                List<DocumentDTO> selection = new ArrayList<>(selectedItems.size());

                Iterator<FileBrowserItem> iterator = selectedItems.iterator();
                while (iterator.hasNext() && (allEditable || allFile)) {
                    // Selected file browser item
                    FileBrowserItem item = iterator.next();
                    // Document DTO
                    DocumentDTO documentDto = item.getDocument();
                    selection.add(documentDto);
                    // Document type
                    DocumentType type = documentDto.getType();

                    if ((type != null) && type.isEditable()) {
                        // Permissions
                        NuxeoPermissions permissions = item.getPermissions();
                        if (permissions == null) {
                            permissions = this.repository.getPermissions(portalControllerContext, documentDto.getDocument());
                            item.setPermissions(permissions);
                        }

                        allEditable = allEditable && permissions.isEditable();
                    } else {
                        allEditable = false;
                    }

                    allFile = allFile && (type != null) && type.isFile();
                }


                if (indexes.size() == 1) {
                    // Selected file browser item
                    FileBrowserItem item = selectedItems.get(0);

                    // Document DTO
                    DocumentDTO documentDto = item.getDocument();

                    // Permissions
                    NuxeoPermissions permissions = item.getPermissions();
                    if (permissions == null) {
                        permissions = this.repository.getPermissions(portalControllerContext, documentDto.getDocument());
                        item.setPermissions(permissions);
                    }


                    // Live edition
                    Element liveEditionGroup = this.getToolbarLiveEditionGroup(portalControllerContext, documentDto, permissions, bundle);
                    if (liveEditionGroup != null) {
                        toolbar.add(liveEditionGroup);
                    }


                    // Single selection
                    this.addToolbarSingleSelectionItems(portalControllerContext, toolbar, form, view, documentDto, permissions, bundle);
                } else {
                    // Bulk download
                    String bulkDownloadUrl;
                    if (allFile) {
                        bulkDownloadUrl = this.getBulkDownloadUrl(portalControllerContext, selection);
                    } else {
                        bulkDownloadUrl = null;
                    }
                    this.addToolbarItem(toolbar, bulkDownloadUrl, "_blank", bundle.getString("FILE_BROWSER_TOOLBAR_DOWNLOAD"), "glyphicons glyphicons-basic-square-download");
                }

                // Multiple selection
                this.addToolbarMultipleSelectionItems(portalControllerContext, toolbar, form, view, selection, allEditable, container, bundle);
            }
        }

        return container;
    }


    /**
     * Add toolbar item.
     *
     * @param toolbar toolbar DOM element
     * @param url     toolbar item URL
     * @param target  toolbar item target
     * @param title   toolbar item title
     * @param icon    toolbar item icon
     */
    protected void addToolbarItem(Element toolbar, String url, String target, String title, String icon) {
        // Base HTML classes
        String baseHtmlClasses = "btn btn-primary btn-sm ml-1";

        // Item
        Element item;
        if (StringUtils.isEmpty(url)) {
            item = DOM4JUtils.generateLinkElement("#", null, null, baseHtmlClasses + " disabled", null, icon);
        } else {
            // Data attributes
            Map<String, String> data = new HashMap<>();

            if ("#osivia-modal".equals(target)) {
                data.put("target", "#osivia-modal");
                data.put("load-url", url);
                data.put("title", title);

                url = "javascript:";
                target = null;
            } else if ("modal".equals(target)) {
                data.put("toggle", "modal");

                target = null;
            }

            item = DOM4JUtils.generateLinkElement(url, target, null, baseHtmlClasses + " no-ajax-link", null, icon);

            // Title
            DOM4JUtils.addAttribute(item, "title", title);

            // Data attributes
            for (Map.Entry<String, String> entry : data.entrySet()) {
                DOM4JUtils.addDataAttribute(item, entry.getKey(), entry.getValue());
            }
        }

        // Text
        Element text = DOM4JUtils.generateElement("span", "d-none d-xl-inline", title);
        item.add(text);

        toolbar.add(item);
    }


    /**
     * Get toolbar live edition group DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param documentDto             document DTO
     * @param permissions             permissions
     * @param bundle                  internationalization bundle
     * @return DOM element
     */
    protected Element getToolbarLiveEditionGroup(PortalControllerContext portalControllerContext, DocumentDTO documentDto, NuxeoPermissions permissions, Bundle bundle) throws PortletException {
        // Portlet request
        PortletRequest portletRequest = portalControllerContext.getRequest();

        // Document path
        String path = documentDto.getPath();
        // Document type
        DocumentType documentType = documentDto.getType();

        // Publication infos
        NuxeoPublicationInfos publicationInfos;
        // Nuxeo drive indicator
        boolean drive;
        if (permissions.isEditable() && (documentType != null) && documentType.isFile() && documentDto.isLiveEditable()) {
            publicationInfos = this.repository.getPublicationInfos(portalControllerContext, documentDto.getDocument());
            drive = StringUtils.isNotEmpty(publicationInfos.getDriveEditionUrl()) || publicationInfos.isDriveEnabled();
        } else {
            publicationInfos = null;
            drive = false;
        }


        // Dropdown
        Element dropdownGroup;
        Element dropdownMenu;
        if (permissions.isEditable() && drive) {
            // Dropdown group
            dropdownGroup = DOM4JUtils.generateDivElement("btn-group btn-group-sm");

            // Dropdown button
            Element dropdownButton = DOM4JUtils.generateElement("button", "btn btn-primary dropdown-toggle", null);
            DOM4JUtils.addAttribute(dropdownButton, "type", "button");
            DOM4JUtils.addDataAttribute(dropdownButton, "toggle", "dropdown");
            dropdownGroup.add(dropdownButton);

            // Dropdown button caret
            Element dropdownButtonCaret = DOM4JUtils.generateElement("span", "caret", StringUtils.EMPTY);
            dropdownButton.add(dropdownButtonCaret);

            // Dropdown menu
            dropdownMenu = DOM4JUtils.generateElement("ul", "dropdown-menu dropdown-menu-right", null);
            dropdownGroup.add(dropdownMenu);
        } else {
            dropdownGroup = null;
            dropdownMenu = null;
        }

        // OnlyOffice
        Element onlyOffice;
        if (documentDto.isLiveEditable()) {
            if (permissions.isEditable()) {
                String onlyOfficeTitle = bundle.getString("FILE_BROWSER_TOOLBAR_ONLYOFFICE_EDIT_TITLE");
                String onlyOfficeAction = bundle.getString("FILE_BROWSER_TOOLBAR_ONLYOFFICE_EDIT");
                String onlyOfficeUrl = this.getOnlyOfficeUrl(portalControllerContext, path, onlyOfficeTitle);

                // With lock
                onlyOffice = DOM4JUtils.generateLinkElement(onlyOfficeUrl, null, null, "btn btn-primary no-ajax-link", onlyOfficeAction,
                        "glyphicons glyphicons-basic-pencil");
            } else if (StringUtils.isNotEmpty(portletRequest.getRemoteUser())) {
                String onlyOfficeReadOnlyTitle = bundle.getString("FILE_BROWSER_TOOLBAR_ONLYOFFICE_READ_ONLY_TITLE");
                String onlyOfficeReadOnlyAction = bundle.getString("FILE_BROWSER_TOOLBAR_ONLYOFFICE_READ_ONLY");
                String onlyOfficeReadOnlyUrl = this.getOnlyOfficeUrl(portalControllerContext, path, onlyOfficeReadOnlyTitle);

                // Read only
                onlyOffice = DOM4JUtils.generateLinkElement(onlyOfficeReadOnlyUrl, null, null, "btn btn-primary no-ajax-link",
                        onlyOfficeReadOnlyAction);
            } else {
                onlyOffice = null;
            }
        } else {
            onlyOffice = null;
        }

        if (drive && (dropdownMenu != null)) {
            if (StringUtils.isNotEmpty(publicationInfos.getDriveEditionUrl())) {
                // Drive
                Element driveDropdownItem = DOM4JUtils.generateElement("li", null, null);
                dropdownMenu.add(driveDropdownItem);
                Element driveLink = DOM4JUtils.generateLinkElement(publicationInfos.getDriveEditionUrl(), null, null, "no-ajax-link",
                        bundle.getString("FILE_BROWSER_TOOLBAR_DRIVE_EDIT"));
                driveDropdownItem.add(driveLink);
            } else {
                // Drive not started warning
                Element warning = DOM4JUtils.generateElement("li", "dropdown-header", bundle.getString("FILE_BROWSER_TOOLBAR_DRIVE_NOT_STARTED_WARNING"));
                dropdownMenu.add(warning);

                // Drive
                Element driveDropdownItem = DOM4JUtils.generateElement("li", "disabled", null);
                dropdownMenu.add(driveDropdownItem);
                Element driveLink = DOM4JUtils.generateLinkElement("#", null, null, null, bundle.getString("FILE_BROWSER_TOOLBAR_DRIVE_EDIT"));
                driveDropdownItem.add(driveLink);
            }
        }


        // Live edition group
        Element liveEditionGroup;
        if ((onlyOffice == null) && (dropdownGroup == null)) {
            liveEditionGroup = null;
        } else {
            liveEditionGroup = DOM4JUtils.generateDivElement("btn-group btn-group-sm d-none d-md-flex ml-1");
            if (onlyOffice != null) {
                liveEditionGroup.add(onlyOffice);
            }
            if (dropdownGroup != null) {
                liveEditionGroup.add(dropdownGroup);
            }
        }

        return liveEditionGroup;
    }


    /**
     * Add single selection items to toolbar.
     *
     * @param portalControllerContext portal controller context
     * @param toolbar                 toolbar DOM element
     * @param form                    form
     * @param view                    view
     * @param documentDto             document DTO
     * @param permissions             permissions
     * @param bundle                  internationalization bundle
     */
    protected void addToolbarSingleSelectionItems(PortalControllerContext portalControllerContext, Element toolbar, FileBrowserForm form, FileBrowserView view, DocumentDTO documentDto, NuxeoPermissions permissions, Bundle bundle) throws PortletException {
        // Nuxeo document
        Document nuxeoDocument = documentDto.getDocument();
        // Document path
        String path = documentDto.getPath();


        // Download
        String downloadUrl;
        if ((documentDto.getType() != null) && documentDto.getType().isFile()) {
            downloadUrl = this.repository.getDownloadUrl(portalControllerContext, nuxeoDocument);
        } else {
            downloadUrl = null;
        }
        this.addToolbarItem(toolbar, downloadUrl, "_blank", bundle.getString("FILE_BROWSER_TOOLBAR_DOWNLOAD"), "glyphicons glyphicons-basic-square-download");

        // Rename
        String renameUrl;
        if (permissions.isEditable()) {
            renameUrl = this.getRenameUrl(portalControllerContext, form, path);
        } else {
            renameUrl = null;
        }
        this.addToolbarItem(toolbar, renameUrl, "#osivia-modal", bundle.getString("FILE_BROWSER_TOOLBAR_RENAME"), "glyphicons glyphicons-basic-square-edit");

        // Duplicate
        if (!form.isListMode()) {
            String duplicateUrl;
            if (permissions.canBeCopied()) {
                duplicateUrl = this.getDuplicateUrl(portalControllerContext, path, view);
            } else {
                duplicateUrl = null;
            }
            this.addToolbarItem(toolbar, duplicateUrl, null, bundle.getString("FILE_BROWSER_TOOLBAR_DUPLICATE"), "glyphicons glyphicons-basic-copy-duplicate");
        }
    }


    /**
     * Add multiple selection items to toolbar.
     *
     * @param portalControllerContext portal controller context
     * @param toolbar                 toolbar DOM element
     * @param form                    form
     * @param view                    view
     * @param selection               selected documents
     * @param allEditable             all editable indicator
     * @param container               toolbar container DOM element
     * @param bundle                  internationalization bundle
     */
    protected void addToolbarMultipleSelectionItems(PortalControllerContext portalControllerContext, Element toolbar, FileBrowserForm form, FileBrowserView view, List<DocumentDTO> selection, boolean allEditable, Element container, Bundle bundle) throws PortletException {
        // Base path
        String basePath = form.getBasePath();
        // Selected identifiers
        List<String> identifiers;
        // Selected paths
        List<String> paths;
        // Selected accepted types
        Set<String> acceptedTypes;

        // Unknown document type indicator
        boolean unknownType = false;

        if (allEditable) {
            identifiers = new ArrayList<>(selection.size());
            paths = new ArrayList<>(selection.size());
            acceptedTypes = new HashSet<>();

            for (DocumentDTO documentDto : selection) {
                // Identifier
                String identifier = documentDto.getId();
                identifiers.add(identifier);

                // Path
                String path = documentDto.getPath();
                paths.add(path);

                // Accepted types
                String type;
                if (documentDto.getType() == null) {
                    type = null;
                } else {
                    type = documentDto.getType().getName();
                }
                if (StringUtils.isEmpty(type)) {
                    unknownType = true;
                } else {
                    acceptedTypes.add(type);
                }
            }
        } else {
            identifiers = null;
            paths = null;
            acceptedTypes = null;
        }


        // Move
        if (StringUtils.isNotEmpty(basePath)) {
            String moveUrl;
            if (allEditable && !unknownType) {
                moveUrl = this.getMoveUrl(portalControllerContext, form, basePath, identifiers, paths, acceptedTypes);
            } else {
                moveUrl = null;
            }
            this.addToolbarItem(toolbar, moveUrl, "#osivia-modal", bundle.getString("FILE_BROWSER_TOOLBAR_MOVE"), "glyphicons glyphicons-basic-block-move");
        }


        // Delete
        String deleteUrl;
        if (allEditable) {
            // Delete modal identifier
            deleteUrl = this.getDeleteUrl(portalControllerContext, identifiers, form.getPath());
        } else {
            deleteUrl = null;
        }
        this.addToolbarItem(toolbar, deleteUrl, "#osivia-modal", bundle.getString("FILE_BROWSER_TOOLBAR_DELETE"), "glyphicons glyphicons-basic-bin");
    }


    /**
     * Get OnlyOffice URL.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @param title                   title
     * @return URL
     */
    private String getOnlyOfficeUrl(PortalControllerContext portalControllerContext, String path, String title) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, path);
        properties.put("osivia.hideTitle", String.valueOf(1));
        properties.put("osivia.onlyoffice.withLock", String.valueOf(false));
        properties.put(InternalConstants.PROP_WINDOW_TITLE, title);

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, OnlyofficeLiveEditHelper.ONLYOFFICE_PORTLET_INSTANCE, properties);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    /**
     * Get rename URL.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @param path                    document path
     * @return URL
     */
    private String getRenameUrl(PortalControllerContext portalControllerContext, FileBrowserForm form, String path) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put("osivia.rename.path", path);
        properties.put("osivia.rename.redirection-path", form.getPath());

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-widgets-rename-instance", properties,
                    PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    /**
     * Get duplicate URL.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @param view                    view
     * @return URL
     */
    private String getDuplicateUrl(PortalControllerContext portalControllerContext, String path, FileBrowserView view) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();

        // URL
        String url;
        if (portletResponse instanceof MimeResponse) {
            MimeResponse mimeResponse = (MimeResponse) portletResponse;

            // Action URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, "duplicate");
            actionUrl.setParameter("path", path);
            actionUrl.setParameter("view", view.getId());

            url = actionUrl.toString();
        } else {
            url = "#";
        }

        return url;
    }


    /**
     * Get bulk download URL.
     *
     * @param portalControllerContext portal controller context
     * @param selection               selected documents
     * @return URL
     */
    private String getBulkDownloadUrl(PortalControllerContext portalControllerContext, List<DocumentDTO> selection) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();

        // URL
        String url;
        if (portletResponse instanceof MimeResponse) {
            MimeResponse mimeResponse = (MimeResponse) portletResponse;

            // Paths
            String[] paths = new String[selection.size()];
            int i = 0;
            for (DocumentDTO documentDto : selection) {
                paths[i] = documentDto.getPath();
                i++;
            }

            // Resource URL
            ResourceURL resourceUrl = mimeResponse.createResourceURL();
            resourceUrl.setResourceID("bulk-download");
            resourceUrl.setParameter("paths", paths);

            url = resourceUrl.toString();
        } else {
            url = "#";
        }

        return url;
    }


    /**
     * Get move URL.
     *
     * @param portalControllerContext portal controller context
     * @param form                    form
     * @param basePath                base path
     * @param identifiers             selected document identifiers
     * @param paths                   selected document paths
     * @param acceptedTypes           selected document types
     * @return URL
     */
    private String getMoveUrl(PortalControllerContext portalControllerContext, FileBrowserForm form, String basePath, List<String> identifiers, List<String> paths,
                              Set<String> acceptedTypes) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        if (form.isListMode()) {
            properties.put("osivia.move.redirection-url", this.portalUrlFactory.getRefreshPageUrl(portalControllerContext));
        } else {
            properties.put("osivia.move.path", form.getPath());
        }
        properties.put("osivia.move.identifiers", StringUtils.join(identifiers, ","));
        properties.put("osivia.move.ignored-paths", StringUtils.join(paths, ","));
        properties.put("osivia.move.base-path", basePath);
        properties.put("osivia.move.accepted-types", StringUtils.join(acceptedTypes, ","));

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-widgets-move-instance", properties,
                    PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    /**
     * Get delete URL.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers             selected document identifiers
     * @param redirectionPath         redirection path
     * @return URL
     */
    private String getDeleteUrl(PortalControllerContext portalControllerContext, List<String> identifiers, String redirectionPath) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put("osivia.delete.identifiers", StringUtils.join(identifiers, ","));
        properties.put("osivia.delete.redirection-path", redirectionPath);

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-widgets-delete-instance", properties,
                    PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    @Override
    public void duplicate(PortalControllerContext portalControllerContext, FileBrowserForm form, String path) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        try {
            // Duplicate
            this.repository.duplicate(portalControllerContext, path, form.getPath());

            // Notification
            String message = bundle.getString("FILE_BROWSER_DUPLICATE_SUCCESS_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } catch (NuxeoException e) {
            String message = e.getUserMessage(portalControllerContext);

            if (message == null) {
                // Notification
                message = bundle.getString("FILE_BROWSER_DUPLICATE_ERROR_MESSAGE");
            }
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);
        }

        // Refresh navigation
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);

        // Update public render parameter for associated portlets refresh
        if (response instanceof ActionResponse) {
            ActionResponse actionResponse = (ActionResponse) response;
            actionResponse.setRenderParameter("dnd-update", String.valueOf(System.currentTimeMillis()));
        }
    }


    @Override
    public FileBrowserBulkDownloadContent getBulkDownload(PortalControllerContext portalControllerContext, List<String> paths)
            throws PortletException, IOException {
        // Binary content
        CMSBinaryContent binaryContent = this.repository.getBinaryContent(portalControllerContext, paths);

        // Bulk download content
        FileBrowserBulkDownloadContent content = this.applicationContext.getBean(FileBrowserBulkDownloadContent.class);

        // Content type
        String mimeType = binaryContent.getMimeType();
        content.setType(mimeType);

        // Content disposition
        String disposition = "inline; filename=\"" + StringUtils.trimToEmpty(binaryContent.getName()) + "\"";
        content.setDisposition(disposition);

        // File
        File file = binaryContent.getFile();
        content.setFile(file);

        return content;
    }


    @Override
    public void drop(PortalControllerContext portalControllerContext, List<String> sourceIdentifiers, String targetIdentifier) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        try {
            // Move
            this.repository.move(portalControllerContext, sourceIdentifiers, targetIdentifier);

            // Notification
            String message = bundle.getString("FILE_BROWSER_MOVE_SUCCESS_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } catch (NuxeoException e) {
            // Notification
            String message = bundle.getString("FILE_BROWSER_MOVE_WARNING_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.WARNING);
        }

        // Refresh navigation
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);

        // Update public render parameter for associated portlets refresh
        if (response instanceof ActionResponse) {
            ActionResponse actionResponse = (ActionResponse) response;
            actionResponse.setRenderParameter("dnd-update", String.valueOf(System.currentTimeMillis()));
        }
    }


    @Override
    public void upload(PortalControllerContext portalControllerContext, FileBrowserForm form) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Upload multipart files
        List<MultipartFile> upload = form.getUpload();

        if (CollectionUtils.isNotEmpty(upload)) {
            try {
                // Import
                this.repository.importFiles(portalControllerContext, form.getPath(), upload);
            } catch (NuxeoException e) {
                String message = e.getUserMessage(portalControllerContext);

                if (message == null) {
                    message = bundle.getString("FILE_BROWSER_UPLOAD_ERROR_MESSAGE");
                }

                request.getPortletSession().setAttribute("uploadMsg", message);
            }

            // Refresh navigation
            request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);
        }
    }


    @Override
    public void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        FileBrowserForm form = this.getForm(portalControllerContext);

        this.repository.updateMenubar(portalControllerContext, form.getPath());
    }


    @Override
    public void endUpload(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Refresh navigation
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);

        // Update public render parameter for associated portlets refresh
        if (response instanceof ActionResponse) {
            ActionResponse actionResponse = (ActionResponse) response;
            actionResponse.setRenderParameter("dnd-update", String.valueOf(System.currentTimeMillis()));
        }


        String message = (String) request.getPortletSession().getAttribute("uploadMsg");
        NotificationsType type;

        request.getPortletSession().removeAttribute("uploadMsg");

        if (StringUtils.isEmpty(message)) {
            message = bundle.getString("FILE_BROWSER_UPLOAD_SUCCESS_MESSAGE");
            type = NotificationsType.SUCCESS;
        } else {
            type = NotificationsType.ERROR;
        }

        this.notificationsService.addSimpleNotification(portalControllerContext, message, type);
    }


    @Override
    public Element getLocationBreadcrumb(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Parent documents
        List<Document> documents = this.repository.getParentDocuments(portalControllerContext, path);

        // Breadcrumb container
        Element breadcrumb = DOM4JUtils.generateElement("ol", "breadcrumb m-0 p-0", StringUtils.EMPTY);

        for (Document document : documents) {
            // Document URL
            String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, document.getPath(), null, null, null, null, null, null, null);

            // Breadcrumb item
            Element item = DOM4JUtils.generateElement("li", "breadcrumb-item", null);
            breadcrumb.add(item);

            // Document link
            Element link = DOM4JUtils.generateLinkElement(url, null, null, "no-ajax-link", document.getTitle());
            item.add(link);
        }

        return breadcrumb;
    }

}
