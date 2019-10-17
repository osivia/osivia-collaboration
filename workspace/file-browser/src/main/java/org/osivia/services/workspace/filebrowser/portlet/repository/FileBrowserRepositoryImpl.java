package org.osivia.services.workspace.filebrowser.portlet.repository;

import bsh.EvalError;
import bsh.Interpreter;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.user.UserPreferences;
import org.osivia.portal.core.cms.*;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserWindowProperties;
import org.osivia.services.workspace.filebrowser.portlet.repository.command.CopyDocumentCommand;
import org.osivia.services.workspace.filebrowser.portlet.repository.command.GetFileBrowserDocumentsCommand;
import org.osivia.services.workspace.filebrowser.portlet.repository.command.ImportFilesCommand;
import org.osivia.services.workspace.filebrowser.portlet.repository.command.MoveDocumentsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.*;

/**
 * File browser portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserRepository
 */
@Repository
public class FileBrowserRepositoryImpl implements FileBrowserRepository {

    /**
     * Zip file name RegEx.
     */
    private static final String ZIP_FILE_NAME_REGEX = "(.+) \\(([0-9]+)\\)";


    /**
     * Log.
     */
    private final Log log;

    /**
     * Zip file name pattern.
     */
    private final Pattern zipFileNamePattern;


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    public FileBrowserRepositoryImpl() {
        super();

        // Log
        this.log = LogFactory.getLog(this.getClass());

        // Zip file name pattern
        this.zipFileNamePattern = Pattern.compile(ZIP_FILE_NAME_REGEX);
    }


    @Override
    public String getBasePath(PortalControllerContext portalControllerContext, FileBrowserWindowProperties windowProperties) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Path
        String path;
        if (StringUtils.isEmpty(windowProperties.getBasePath())) {
            path = nuxeoController.getBasePath();
        } else {
            path = nuxeoController.getComputedPath(windowProperties.getBasePath());
        }

        return path;
    }


    @Override
    public String getContentPath(PortalControllerContext portalControllerContext, FileBrowserWindowProperties windowProperties) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Path
        String path;
        if (StringUtils.isEmpty(windowProperties.getPath())) {
            path = nuxeoController.getContentPath();
        } else {
            path = nuxeoController.getComputedPath(windowProperties.getPath());
        }

        return path;
    }


    @Override
    public NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getDocumentContext(path);
    }


    @Override
    public List<Document> getDocuments(PortalControllerContext portalControllerContext, FileBrowserWindowProperties windowProperties, String parentPath) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Parent document context
        NuxeoDocumentContext parentDocumentContext;
        if (parentPath == null) {
            parentDocumentContext = null;
        } else {
            parentDocumentContext = nuxeoController.getDocumentContext(parentPath);
        }

        // NXQL request
        String nxql;
        if (StringUtils.isEmpty(windowProperties.getNxql())) {
            nxql = null;
        } else if (BooleanUtils.isTrue(windowProperties.getBeanShell())) {
            try {
                nxql = this.beanShellInterpretation(nuxeoController, parentDocumentContext, windowProperties.getNxql());
            } catch (EvalError e) {
                throw new PortletException(e);
            }
        } else {
            nxql = windowProperties.getNxql();
        }

        // Parent document identifier
        String parentId;
        if (StringUtils.isEmpty(nxql) && (parentDocumentContext != null)) {
            parentId = parentDocumentContext.getPublicationInfos().getLiveId();
        } else {
            parentId = null;
        }

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetFileBrowserDocumentsCommand.class, nxql, parentId);
        Object result = nuxeoController.executeNuxeoCommand(command);

        // Documents
        List<Document> documents;
        if (result instanceof Documents) {
            Documents resultDocuments = (Documents) result;
            documents = resultDocuments.list();
        } else {
            documents = null;
        }

        return documents;
    }


    /**
     * BeanShell interpretation.
     *
     * @param nuxeoController       Nuxeo controller
     * @param parentDocumentContext parent document context
     * @param nxql                  NXQL request
     * @return interpreted NXQL request
     */
    private String beanShellInterpretation(NuxeoController nuxeoController, NuxeoDocumentContext parentDocumentContext, String nxql) throws PortletException, EvalError {
        // Request
        PortletRequest request = nuxeoController.getRequest();

        // CMS service
        ICMSService cmsService = NuxeoController.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // User workspace
        CMSItem userWorkspace;
        try {
            userWorkspace = cmsService.getUserWorkspace(cmsContext);
        } catch (CMSException e) {
            throw new PortletException(e);
        }

        // BeanShell interpreter
        Interpreter interpreter = new Interpreter();
        // Request parameters
        interpreter.set("request", request);
        interpreter.set("params", PageSelectors.decodeProperties(request.getParameter("selectors")));
        // Nuxeo controller parameters
        interpreter.set("basePath", nuxeoController.getBasePath());
        interpreter.set("domainPath", nuxeoController.getDomainPath());
        interpreter.set("spacePath", nuxeoController.getSpacePath());
        interpreter.set("navigationPath", nuxeoController.getNavigationPath());
        interpreter.set("contentPath", nuxeoController.getContentPath());
        // Parent document context parameters
        if (parentDocumentContext != null) {
            interpreter.set("navigationPubInfos", parentDocumentContext.getPublicationInfos());
        }
        // User workspace
        if (userWorkspace != null) {
            interpreter.set("userWorkspacePath", userWorkspace.getCmsPath());
        }

        return (String) interpreter.eval(nxql);
    }


    @Override
    public NuxeoPublicationInfos getPublicationInfos(PortalControllerContext portalControllerContext, Document document) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());

        return documentContext.getPublicationInfos();
    }


    @Override
    public NuxeoPermissions getPermissions(PortalControllerContext portalControllerContext, Document document) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());

        return documentContext.getPermissions();
    }


    @Override
    public Set<String> getUserSubscriptions(PortalControllerContext portalControllerContext) {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // User subscription documents
        List<EcmDocument> ecmDocuments;
        try {
            ecmDocuments = cmsService.getUserSubscriptions(cmsContext);
        } catch (CMSException e) {
            ecmDocuments = null;
        }

        // User subscription document identifiers
        Set<String> identifiers;
        if (CollectionUtils.isEmpty(ecmDocuments)) {
            identifiers = new HashSet<>(0);
        } else {
            identifiers = new HashSet<>(ecmDocuments.size());

            for (EcmDocument ecmDocument : ecmDocuments) {
                if (ecmDocument instanceof Document) {
                    Document nuxeoDocument = (Document) ecmDocument;

                    identifiers.add(nuxeoDocument.getId());
                }
            }
        }

        return identifiers;
    }


    @Override
    public Document getParentDocument(PortalControllerContext portalControllerContext, Document document) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Base path
        String basePath = nuxeoController.getBasePath();
        // Parent path
        CMSObjectPath objectPath = CMSObjectPath.parse(document.getPath());
        CMSObjectPath parentObjectPath = objectPath.getParent();
        String parentPath = parentObjectPath.toString();

        // Parent document
        return this.getDocumentFromNavigation(cmsService, cmsContext, basePath, parentPath);
    }


    @Override
    public UserPreferences getUserPreferences(PortalControllerContext portalControllerContext) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // User preferences
        UserPreferences userPreferences;
        try {
            userPreferences = cmsService.getUserPreferences(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return userPreferences;
    }


    @Override
    public String getDownloadUrl(PortalControllerContext portalControllerContext, Document document) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Link
        Link link;
        if ("Picture".equals(document.getType())) {
            String url = nuxeoController.createPictureLink(document.getPath(), "Original");
            link = new Link(url, false);
        } else {
            link = nuxeoController.getLink(document, "download");
        }

        // URL
        String url;
        if (link == null) {
            url = "#";
        } else {
            url = link.getUrl();
        }

        return url;
    }


    @Override
    public void duplicate(PortalControllerContext portalControllerContext, String sourcePath, String targetPath) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CopyDocumentCommand.class, sourcePath, targetPath);
        nuxeoController.executeNuxeoCommand(command);
    }


    @Override
    public void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        for (String identifier : identifiers) {
            try {
                cmsService.putDocumentInTrash(cmsContext, identifier);
            } catch (CMSException e) {
                throw new PortletException(e);
            }
        }
    }


    @Override
    public CMSBinaryContent getBinaryContent(PortalControllerContext portalControllerContext, List<String> paths) throws IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setStreamingSupport(true);

        // Binary contents
        List<CMSBinaryContent> contents = new ArrayList<>(paths.size());
        for (String path : paths) {
            CMSBinaryContent content = nuxeoController.fetchFileContent(path, "file:content");
            contents.add(content);
        }

        // Zip file
        File zipFile = File.createTempFile("file-browser-bulk-download-", ".tmp");
        zipFile.deleteOnExit();

        // Zip output stream
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        zipOutputStream.setMethod(ZipOutputStream.STORED);
        zipOutputStream.setLevel(Deflater.NO_COMPRESSION);

        // Counting output stream
        CountingOutputStream countingOutputStream = new CountingOutputStream(zipOutputStream);

        // Zip file names
        Set<String> zipFileNames = new HashSet<>();

        try {
            for (CMSBinaryContent content : contents) {
                // File name ; must be unique
                String fileName = content.getName();
                while (zipFileNames.contains(fileName)) {
                    String name = StringUtils.substringBeforeLast(fileName, ".");
                    int counter;
                    String extension = StringUtils.substringAfterLast(fileName, ".");

                    // Matcher
                    Matcher matcher = this.zipFileNamePattern.matcher(name);
                    if (matcher.matches()) {
                        name = matcher.group(1);
                        counter = NumberUtils.toInt(matcher.group(2), 0);
                    } else {
                        counter = 0;
                    }

                    fileName = name + " (" + (counter + 1) + ")." + extension;
                }
                zipFileNames.add(fileName);

                // Zip entry
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipEntry.setSize(content.getFileSize());
                zipEntry.setCompressedSize(-1);

                // Buffer
                byte[] buffer = new byte[1000000];

                if (content.getFile() != null) {
                    File file = content.getFile();
                    zipEntry.setTime(file.lastModified());

                    FileInputStream fileInputStream = new FileInputStream(file);

                    // CRC
                    CheckedInputStream checkedInputStream = new CheckedInputStream(fileInputStream, new CRC32());
                    try {
//                        while (checkedInputStream.read(buffer) >= 0) {
//                        }
                        zipEntry.setCrc(checkedInputStream.getChecksum().getValue());

                        zipOutputStream.putNextEntry(zipEntry);
                    } finally {
                        IOUtils.closeQuietly(checkedInputStream);
                    }

                    // Write
                    fileInputStream = new FileInputStream(file);
                    try {
                        int i;
                        while ((i = fileInputStream.read(buffer)) != -1) {
                            countingOutputStream.write(buffer, 0, i);
                        }
                        countingOutputStream.flush();
                    } finally {
                        IOUtils.closeQuietly(fileInputStream);
                    }
                } else if (content.getStream() != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    // CRC
                    CheckedInputStream checkedInputStream = new CheckedInputStream(content.getStream(), new CRC32());
                    try {
                        int i;
                        while ((i = checkedInputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, i);
                        }
                        zipEntry.setCrc(checkedInputStream.getChecksum().getValue());

                        zipOutputStream.putNextEntry(zipEntry);
                    } finally {
                        IOUtils.closeQuietly(checkedInputStream);
                    }

                    // Write
                    try {
                        byteArrayOutputStream.writeTo(countingOutputStream);
                        countingOutputStream.flush();
                    } finally {
                        IOUtils.closeQuietly(byteArrayOutputStream);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(countingOutputStream);
        }


        // Zip binary content
        CMSBinaryContent zipBinaryContent = new CMSBinaryContent();
        zipBinaryContent.setName("export.zip");
        zipBinaryContent.setFile(zipFile);
        zipBinaryContent.setMimeType("application/zip");
        zipBinaryContent.setFileSize(countingOutputStream.getByteCount());

        return zipBinaryContent;
    }


    @Override
    public void move(PortalControllerContext portalControllerContext, List<String> sourceIdentifiers, String targetIdentifier) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(MoveDocumentsCommand.class, sourceIdentifiers, targetIdentifier);
        nuxeoController.executeNuxeoCommand(command);
    }


    @Override
    public void importFiles(PortalControllerContext portalControllerContext, String path, List<MultipartFile> upload) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(ImportFilesCommand.class, path, upload);
        nuxeoController.executeNuxeoCommand(command);
    }


    @Override
    public void updateMenubar(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        // Document
        Document document = documentContext.getDocument();

        // Update menubar
        nuxeoController.setCurrentDoc(document);
        nuxeoController.insertContentMenuBarItems();
    }


    @Override
    public List<Document> getParentDocuments(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Base path
        String basePath = nuxeoController.getBasePath();
        // Parent path
        String parentPath = path;

        // Parent documents
        List<Document> documents = new ArrayList<>();

        while (StringUtils.startsWith(parentPath, basePath)) {
            // Parent document
            Document document = this.getDocumentFromNavigation(cmsService, cmsContext, basePath, parentPath);

            if (document != null) {
                documents.add(0, document);
            }

            // Loop on parent path
            CMSObjectPath objectPath = CMSObjectPath.parse(parentPath);
            CMSObjectPath parentObjectPath = objectPath.getParent();
            parentPath = parentObjectPath.toString();
        }

        return documents;
    }


    /**
     * Get Nuxeo document from navigation.
     *
     * @param cmsService CMS service
     * @param cmsContext CMS context
     * @param basePath   base path
     * @param path       document path
     * @return Nuxeo document, may be null if not found in navigation
     */
    private Document getDocumentFromNavigation(ICMSService cmsService, CMSServiceCtx cmsContext, String basePath, String path) {
        Document document;

        try {
            // Navigation item
            CMSItem navigationItem = cmsService.getPortalNavigationItem(cmsContext, basePath, path);

            if ((navigationItem != null) && (navigationItem.getNativeItem() instanceof Document)) {
                document = (Document) navigationItem.getNativeItem();
            } else {
                document = null;
            }
        } catch (CMSException e) {
            this.log.error(e.getMessage(), e.getCause());
            document = null;
        }

        return document;
    }

}
