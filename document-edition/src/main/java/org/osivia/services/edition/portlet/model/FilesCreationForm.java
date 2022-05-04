package org.osivia.services.edition.portlet.model;

import org.osivia.services.edition.portlet.service.DocumentEditionService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import java.io.File;
import java.util.List;

/**
 * Files creation form.
 *
 * @author Cédric Krommenhoek
 * @see AbstractDocumentEditionForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FilesCreationForm extends AbstractDocumentEditionForm {

    /**
     * Upload max size.
     */
    private final long maxSize;

    /**
     * Upload.
     */
    private List<MultipartFile> upload;
    /**
     * Temporary files.
     */
    private List<TemporaryFile> temporaryFiles;


    /**
     * Constructor.
     */
    public FilesCreationForm() {
        super();
        this.maxSize = DocumentEditionService.MAX_UPLOAD_SIZE;
    }


    public long getMaxSize() {
        return maxSize;
    }

    public List<MultipartFile> getUpload() {
        return upload;
    }

    public void setUpload(List<MultipartFile> upload) {
        this.upload = upload;
    }

    public List<TemporaryFile> getTemporaryFiles() {
        return temporaryFiles;
    }

    public void setTemporaryFiles(List<TemporaryFile> temporaryFiles) {
        this.temporaryFiles = temporaryFiles;
    }


    /**
     * Temporary file.
     *
     * @author Cédric Krommenhoek
     */
    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class TemporaryFile {

        /**
         * Temporary file.
         */
        private File file;
        /**
         * Temporary file name.
         */
        private String fileName;
        /**
         * Temporary file MIME type.
         */
        private MimeType fileMimeType;


        /**
         * Constructor.
         */
        public TemporaryFile() {
            super();
        }


        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public MimeType getFileMimeType() {
            return fileMimeType;
        }

        public void setFileMimeType(MimeType fileMimeType) {
            this.fileMimeType = fileMimeType;
        }
    }

}
