package org.osivia.services.edition.portlet.repository;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.nuxeo.ecm.automation.client.model.*;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.Attachments;
import org.osivia.services.edition.portlet.model.ExistingFile;
import org.osivia.services.edition.portlet.model.UploadTemporaryFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.PortletException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Document edition attachments repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see DocumentEditionCommonRepositoryImpl
 * @see Attachments
 * @see DocumentEditionAttachmentsRepository
 */
@Repository
public class DocumentEditionAttachmentsRepositoryImpl extends DocumentEditionCommonRepositoryImpl<Attachments> implements DocumentEditionAttachmentsRepository {

    /**
     * Attachements Nuxeo document property.
     */
    protected static final String ATTACHMENTS_PROPERTY = "files:files";


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public DocumentEditionAttachmentsRepositoryImpl() {
        super();
    }


    @Override
    public Attachments get(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Existing files
        SortedMap<ExistingFile, Boolean> existingFiles;
        if (document == null) {
            existingFiles = null;
        } else {
            PropertyList attachmentsList = document.getProperties().getList(ATTACHMENTS_PROPERTY);
            if ((attachmentsList == null) || attachmentsList.isEmpty()) {
                existingFiles = null;
            } else {
                existingFiles = new TreeMap<>(Comparator.comparingInt(ExistingFile::getIndex));
                for (int i = 0; i < attachmentsList.size(); i++) {
                    PropertyMap attachmentMap = attachmentsList.getMap(i);
                    PropertyMap attachmentFileMap = attachmentMap.getMap("file");
                    String fileName = attachmentFileMap.getString("name");
                    MimeType mimeType;
                    try {
                        mimeType = new MimeType(attachmentFileMap.getString("mime-type"));
                    } catch (MimeTypeParseException e) {
                        mimeType = null;
                    }

                    // Existing file
                    ExistingFile file = this.applicationContext.getBean(ExistingFile.class);
                    file.setIndex(i);
                    file.setFileName(fileName);
                    file.setMimeType(mimeType);

                    existingFiles.put(file, false);
                }
            }
        }

        // Attachments
        Attachments attachments = this.applicationContext.getBean(Attachments.class);
        attachments.setExistingFiles(existingFiles);

        return attachments;
    }


    @Override
    public void uploadAttachments(PortalControllerContext portalControllerContext, Attachments attachments) throws PortletException, IOException {
        // Temporary files
        List<UploadTemporaryFile> temporaryFiles = attachments.getUploadTemporaryFiles();
        if (CollectionUtils.isEmpty(temporaryFiles)) {
            temporaryFiles = new ArrayList<>();
            attachments.setUploadTemporaryFiles(temporaryFiles);
        }

        // Upload
        for (MultipartFile multipartFile : attachments.getUpload()) {
            UploadTemporaryFile temporaryFile = this.createTemporaryFile(multipartFile);
            temporaryFiles.add(temporaryFile);
        }
    }


    @Override
    public void deleteAttachment(PortalControllerContext portalControllerContext, Attachments attachments, String value) throws PortletException, IOException {
        String[] split = StringUtils.split(value, "-");
        if (ArrayUtils.getLength(split) == 2) {
            int index = NumberUtils.toInt(split[1], -1);

            if (StringUtils.equals("existing", split[0])) {
                SortedMap<ExistingFile, Boolean> existingFiles = attachments.getExistingFiles();
                if (MapUtils.isNotEmpty(existingFiles)) {
                    existingFiles.entrySet().stream().filter(entry -> (entry.getKey().getIndex() == index)).forEach(entry -> entry.setValue(true));
                }
            } else if (StringUtils.equals("temporary", split[0])) {
                List<UploadTemporaryFile> temporaryFiles = attachments.getUploadTemporaryFiles();
                if (CollectionUtils.isNotEmpty(temporaryFiles) && (index >= 0) && (index < temporaryFiles.size())) {
                    UploadTemporaryFile temporaryFile = temporaryFiles.get(index);

                    // Delete temporary file
                    if ((temporaryFile.getFile() != null) && !temporaryFile.getFile().delete()) {
                        temporaryFile.getFile().deleteOnExit();
                    }

                    temporaryFiles.remove(temporaryFile);
                }
            }
        }
    }


    @Override
    public void restoreAttachment(PortalControllerContext portalControllerContext, Attachments attachments, String value) throws PortletException, IOException {
        int index = NumberUtils.toInt(value, -1);

        SortedMap<ExistingFile, Boolean> existingFiles = attachments.getExistingFiles();
        if (MapUtils.isNotEmpty(existingFiles)) {
            existingFiles.entrySet().stream().filter(entry -> (entry.getKey().getIndex() == index)).forEach(entry -> entry.setValue(false));
        }
    }


    @Override
    public void validate(Attachments attachments, Errors errors) {
        // Do nothing
    }


    @Override
    public void customizeProperties(PortalControllerContext portalControllerContext, Attachments attachments, boolean creation, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException {
        // Delete existing attachments
        SortedMap<ExistingFile, Boolean> existingFiles = attachments.getExistingFiles();
        if (MapUtils.isNotEmpty(existingFiles)) {
            List<Integer> indexes = existingFiles.entrySet().stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).map(ExistingFile::getIndex).sorted(Collections.reverseOrder()).collect(Collectors.toList());
            for (Integer index : indexes) {
                String xpath = ATTACHMENTS_PROPERTY + "/item[" + index + "]";
                binaries.put(xpath, null);
            }
        }

        // Add new attachments
        List<UploadTemporaryFile> temporaryFiles = attachments.getUploadTemporaryFiles();
        if (CollectionUtils.isNotEmpty(temporaryFiles)) {
            List<Blob> blobs = temporaryFiles.stream().map(this::convert).collect(Collectors.toList());
            binaries.put(ATTACHMENTS_PROPERTY, blobs);
        }
    }


    protected Blob convert(UploadTemporaryFile temporaryFile) {
        // File content type
        String contentType;
        if (temporaryFile.getMimeType() == null) {
            contentType = null;
        } else {
            contentType = temporaryFile.getMimeType().toString();
        }

        return new FileBlob(temporaryFile.getFile(), temporaryFile.getFileName(), contentType);
    }

}
