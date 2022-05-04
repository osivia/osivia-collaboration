package org.osivia.services.edition.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.edition.portlet.model.FileEditionForm;
import org.osivia.services.edition.portlet.repository.command.ImportZipCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Repository
public class ZipExtractionRepositoryImpl extends FileEditionRepositoryImpl {

    /**
     * File binary Nuxeo document property.
     */
    private static final String BINARY_PROPERTY = "file:content";


    /**
     * Log.
     */
    private final Log log;


    @Value("#{systemProperties['osivia.filebrowser.zip.uploadsizelimit'] ?: null}")
    private String zipSizeLimit;


    @Value("#{systemProperties['osivia.filebrowser.zip.uploadweightlimit'] ?: null}")
    private String zipWeightLimit;


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public ZipExtractionRepositoryImpl() {
        super();
        this.log = LogFactory.getLog("org.osivia.collaboration");
    }


    @Override
    public boolean matches(String documentType, boolean creation) {
        return false;
    }


    @Override
    public void customizeValidation(FileEditionForm form, Errors errors) {
        if (form.getTemporaryFile() == null) {
            // Binary file is mandatory
            errors.rejectValue("upload", "NotEmpty");
        } else {
            // Check primary type
            String originalFilename = form.getTemporaryFileName();
            if (!StringUtils.endsWithIgnoreCase(originalFilename, ".zip")) {
                errors.rejectValue("upload", "InvalidFileType", null);

            } else {
                try {
                    checkLimits(form, errors);
                } catch (IOException e) {
                    errors.rejectValue("upload", "InvalidFileType", null);
                }

            }
        }
    }


    @Override
    protected Document create(NuxeoController nuxeoController, String parentPath, String type, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException {
        // File binaries
        List<Blob> blobs = binaries.get(BINARY_PROPERTY);
        if (CollectionUtils.isEmpty(blobs)) {
            throw new PortletException("Empty file");
        }

        // Nuxeo command
        ImportZipCommand command = this.applicationContext.getBean(ImportZipCommand.class);
        command.setPath(parentPath);
        command.setBinary(blobs.get(0));

        return (Document) nuxeoController.executeNuxeoCommand(command);
    }


    private void checkLimits(FileEditionForm form, Errors errors) throws IOException {

        long startTime = new Date().getTime();

        int sizeLimit = 0;
        long weightLimit = 0;
        if (zipSizeLimit != null) {
            sizeLimit = Integer.parseInt(zipSizeLimit.trim());
        }
        if (zipWeightLimit != null) {
            weightLimit = NumberUtils.toLong(zipWeightLimit.trim()) * FileUtils.ONE_MB;
        }

        // control total entries in files
        int totalEntries = 0;


        File f = form.getTemporaryFile();
        long totalWeight = f.length();


        ZipFile zipFile = new ZipFile(f);
        totalEntries = totalEntries + zipFile.size();


        boolean nuxeoArchive = false;

        Enumeration<?> enu = zipFile.entries();
        while (enu.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enu.nextElement();

            if (zipEntry.getName().equals(".nuxeo-archive")) {
                nuxeoArchive = true;
                break;
            }
        }

        zipFile.close();

        if (nuxeoArchive) {

            logWarn("W01", form.getRemoteUser(), startTime, "Dépôt d'un fichier zip nuxeo archive");
            errors.rejectValue("upload", "InvalidZipNuxeoArchive", null);

            if (!f.delete()) {
                f.deleteOnExit();
            }
        }

        long s = totalWeight / FileUtils.ONE_MB;
        long l = weightLimit / FileUtils.ONE_MB;

        if (sizeLimit > 0 && totalEntries > sizeLimit) {

            logWarn("W02", form.getRemoteUser(), startTime, "Dépôt d'un fichier zip contenant trop d'entrées. " + totalEntries + " éléments et " + s + "Mo (limites " + sizeLimit + " et " + l + "Mo)");
            errors.rejectValue("upload", "InvalidZipTooManyEntries", new String[]{Integer.toString(totalEntries), Integer.toString(sizeLimit)}, null);

            if (!f.delete()) {
                f.deleteOnExit();
            }
        }
        if (weightLimit > 0 && totalWeight > weightLimit) {

            logWarn("W03", form.getRemoteUser(), startTime, "Dépôt d'un fichier zip trop volumineux. " + totalEntries + " éléments et " + s + "Mo (limites " + sizeLimit + " et " + l + "Mo)");
            errors.rejectValue("upload", "InvalidZipTooBig", new String[]{Long.toString(s), Long.toString(l)}, null);

            if (!f.delete()) {
                f.deleteOnExit();
            }
        }

        log("I02", form.getRemoteUser(), startTime, "Dépôt d'un fichier zip contenant " + totalEntries + " éléments et " + s + "Mo (limites " + sizeLimit + " et " + l + "Mo)");
    }


    private void log(String code, String owner, long startTime, String message) {
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        log.info(code + " " + owner + " FileBrowserService " + elapsedTime + " " + message);
    }

    private void logWarn(String code, String owner, long startTime, String message) {
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        log.warn(code + " " + owner + " FileBrowserService " + elapsedTime + " " + message);
    }

}
