package org.osivia.services.edition.portlet.repository;

import org.osivia.services.edition.portlet.model.UploadTemporaryFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.File;
import java.io.IOException;

/**
 * Document edition common repository implementation.
 *
 * @param <T> document edition form type
 * @author Cédric Krommenhoek
 * @see DocumentEditionCommonRepository
 */
public abstract class DocumentEditionCommonRepositoryImpl<T> implements DocumentEditionCommonRepository<T> {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public DocumentEditionCommonRepositoryImpl() {
        super();
    }


    @Override
    public void deleteTemporaryFile(UploadTemporaryFile temporaryFile) {
        if ((temporaryFile != null) && (temporaryFile.getFile() != null) && !temporaryFile.getFile().delete()) {
            temporaryFile.getFile().deleteOnExit();
        }
    }


    @Override
    public UploadTemporaryFile createTemporaryFile(MultipartFile multipartFile) throws IOException {
        // Upload
        File file = File.createTempFile("uploaded-file-", ".tmp");
        file.deleteOnExit();
        multipartFile.transferTo(file);

        // MIME type
        MimeType mimeType;
        try {
            mimeType = new MimeType(multipartFile.getContentType());
        } catch (MimeTypeParseException e) {
            mimeType = null;
        }

        // Temporary file
        UploadTemporaryFile temporaryFile = this.applicationContext.getBean(UploadTemporaryFile.class);
        temporaryFile.setFile(file);
        temporaryFile.setFileName(multipartFile.getOriginalFilename());
        temporaryFile.setMimeType(mimeType);

        return temporaryFile;
    }

}
