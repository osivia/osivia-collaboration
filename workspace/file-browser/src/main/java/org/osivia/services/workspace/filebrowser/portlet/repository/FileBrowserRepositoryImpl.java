package org.osivia.services.workspace.filebrowser.portlet.repository;

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
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.user.UserPreferences;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import org.osivia.services.workspace.filebrowser.portlet.repository.command.CopyDocumentCommand;
import org.osivia.services.workspace.filebrowser.portlet.repository.command.GetFileBrowserDocumentsCommand;
import org.osivia.services.workspace.filebrowser.portlet.repository.command.ImportFilesCommand;
import org.osivia.services.workspace.filebrowser.portlet.repository.command.MoveDocumentsCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPermissions;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;

/**
 * File browser portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see FileBrowserRepository
 */
@Repository
public class FileBrowserRepositoryImpl implements FileBrowserRepository {

    /** Zip file name RegEx. */
    private static final String ZIP_FILE_NAME_REGEX = "(.+) \\(([0-9]+)\\)";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** CMS service locator. */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /** Zip file name pattern. */
    private final Pattern zipFileNamePattern;


    /**
     * Constructor.
     */
    public FileBrowserRepositoryImpl() {
        super();

        // Zip file name pattern
        this.zipFileNamePattern = Pattern.compile(ZIP_FILE_NAME_REGEX);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentPath(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        return window.getProperty(Constants.WINDOW_PROP_URI);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getBasePath(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getBasePath();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public NuxeoDocumentContext getCurrentDocumentContext(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Current path
        String path = this.getCurrentPath(portalControllerContext);
        
        return nuxeoController.getDocumentContext(path);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> getDocuments(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Current path
        String path = this.getCurrentPath(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        // Document
        Document document = documentContext.getDocument();
        
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetFileBrowserDocumentsCommand.class, document.getId());
        Object result = nuxeoController.executeNuxeoCommand(command);

        // Documents
        List<Document> documents;
        if ((result != null) && (result instanceof Documents)) {
            Documents resultDocuments = (Documents) result;
            documents = resultDocuments.list();
        } else {
            documents = null;
        }

        return documents;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public NuxeoPublicationInfos getPublicationInfos(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());

        return documentContext.getPublicationInfos();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public NuxeoPermissions getPermissions(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());

        return documentContext.getPermissions();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getUserSubscriptions(PortalControllerContext portalControllerContext) throws PortletException {
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


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDownloadUrl(PortalControllerContext portalControllerContext, Document document) throws PortletException {
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void duplicate(PortalControllerContext portalControllerContext, String sourcePath) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Target path
        String targetPath = this.getCurrentPath(portalControllerContext);
        
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CopyDocumentCommand.class, sourcePath, targetPath);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
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


    /**
     * {@inheritDoc}
     */
    @Override
    public CMSBinaryContent getBinaryContent(PortalControllerContext portalControllerContext, List<String> paths) throws PortletException, IOException {
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

                    StringBuilder builder = new StringBuilder();
                    builder.append(name);
                    builder.append(" (");
                    builder.append(counter + 1);
                    builder.append(").");
                    builder.append(extension);

                    fileName = builder.toString();
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
                        while (checkedInputStream.read(buffer) >= 0) {
                        }
                        zipEntry.setCrc(checkedInputStream.getChecksum().getValue());

                        zipOutputStream.putNextEntry(zipEntry);
                    } finally {
                        IOUtils.closeQuietly(checkedInputStream);
                    }

                    // Write
                    fileInputStream = new FileInputStream(file);
                    try {
                        int i = -1;
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
                        int i = -1;
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
                } else {
                    continue;
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


    /**
     * {@inheritDoc}
     */
    @Override
    public void move(PortalControllerContext portalControllerContext, List<String> sourceIdentifiers, String targetIdentifier) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(MoveDocumentsCommand.class, sourceIdentifiers, targetIdentifier);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void importFiles(PortalControllerContext portalControllerContext, List<MultipartFile> upload) throws PortletException, IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Current path
        String path = this.getCurrentPath(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(ImportFilesCommand.class, path, upload);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Current path
        String path = this.getCurrentPath(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        // Document
        Document document = documentContext.getDocument();

        // Update menubar
        nuxeoController.setCurrentDoc(document);
        nuxeoController.insertContentMenuBarItems();
    }

}
