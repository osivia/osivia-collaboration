package org.osivia.services.edition.portlet.repository;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.portlet.PortletException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.edition.portlet.model.FileEditionForm;
import org.osivia.services.edition.portlet.repository.command.ImportFileCommand;
import org.osivia.services.edition.portlet.repository.command.ImportZipCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.validation.Errors;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

@Repository("Zip")
public class ZipRepositoryImpl extends FileEditionRepositoryImpl {

    /**
     * File binary Nuxeo document property.
     */
    private static final String BINARY_PROPERTY = "file:content";
    /**
     * File binary name Nuxeo document property.
     */
    private static final String BINARY_NAME_PROPERTY = "file:filename";;
	
	private final static Log logger = LogFactory.getLog("org.osivia.collaboration");

	@Value("#{systemProperties['osivia.filebrowser.zip.uploadsizelimit'] ?: null}")
	private String zipSizeLimit;
	
	@Value("#{systemProperties['osivia.filebrowser.zip.uploadweightlimit'] ?: null}")
	private String zipWeightLimit;
	

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;
    
	@Override
	public void validate(FileEditionForm form, Errors errors) {

		if (form.getTemporaryFile() == null) {
			// Binary file is mandatory
			errors.rejectValue("upload", "NotEmpty");
		} else {
			// Check primary type

			String originalFilename = form.getTemporaryFileName();
			if (!StringUtils.endsWithIgnoreCase(originalFilename, ".zip")) {
				errors.rejectValue("upload", "InvalidFileType", null);

			}
			else {
				try {
					checkLimits(form, errors);
				} catch (IOException e) {
					errors.rejectValue("upload", "InvalidFileType", null);
				}
				
			}
		}

	}
	
    @Override
    protected Document create(NuxeoController nuxeoController, String parentPath, String type, PropertyMap properties, Map<String, Blob> binaries) throws PortletException {
        // File binary
        Blob binary = binaries.get(BINARY_PROPERTY);

        Document document;

        if (binary == null) {
            throw new PortletException("Empty file");
        } else {
            // Nuxeo command
            INuxeoCommand command = this.applicationContext.getBean(ImportZipCommand.class, parentPath, binary);

            document = (Document) nuxeoController.executeNuxeoCommand(command);
        }

        return document;
    }
	

	private void checkLimits(FileEditionForm form, Errors errors) throws ZipException, IOException {
		
		long startTime = new Date().getTime();
		
		int sizeLimit = 0;
		long weightLimit = 0;
		if(zipSizeLimit != null) {
			sizeLimit = Integer.parseInt(zipSizeLimit.trim());
		}
		if(zipWeightLimit != null) {
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

        if(nuxeoArchive) {
        	
        	logWarn("W01", form.getRemoteUser(), startTime, "Dépôt d'un fichier zip nuxeo archive");
        	errors.rejectValue("upload", "InvalidZipNuxeoArchive", null);
        	
        	f.delete();
        }
        
    	long s = totalWeight / FileUtils.ONE_MB;
    	long l = weightLimit / FileUtils.ONE_MB;
        
        if (sizeLimit > 0 && totalEntries > sizeLimit) {
        	
        	logWarn("W02", form.getRemoteUser(), startTime, "Dépôt d'un fichier zip contenant trop d'entrées. "+totalEntries+" éléments et "+s+"Mo (limites "+sizeLimit+" et "+l+"Mo)");
        	errors.rejectValue("upload", "InvalidZipTooManyEntries", new String[] {Integer.toString(totalEntries), Integer.toString(sizeLimit) }, null);

        	f.delete();
        }
        if (weightLimit > 0 && totalWeight > weightLimit) {
        	
        	logWarn("W03", form.getRemoteUser(), startTime, "Dépôt d'un fichier zip trop volumineux. "+totalEntries+" éléments et "+s+"Mo (limites "+sizeLimit+" et "+l+"Mo)");
        	errors.rejectValue("upload", "InvalidZipTooBig", new String[] {Long.toString(s), Long.toString(l) }, null);

        	f.delete();
        }

		log("I02", form.getRemoteUser(), startTime, "Dépôt d'un fichier zip contenant "+totalEntries+" éléments et "+s+"Mo (limites "+sizeLimit+" et "+l+"Mo)");
	}
	

    private void log(String code,  String owner, long startTime, String message)  {
        
        long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		logger.info(code+" "+owner+ " FileBrowserService "+elapsedTime+" "+message);
		
    }
    private void logWarn(String code,  String owner, long startTime, String message)  {
        
        long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		logger.warn(code+" "+owner+ " FileBrowserService "+elapsedTime+" "+message);
		
    }
}
