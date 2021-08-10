package org.osivia.services.workspace.filebrowser.portlet.model;

import java.util.HashMap;
import java.util.Map;

import org.osivia.portal.core.cms.CMSBinaryContent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser bulk download ZIP folder element
 * 
 * @author Loïc Billon
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserBulkDownloadZipFolder {
    
    /** subfiles */
    private Map<String, CMSBinaryContent> files = new HashMap<String, CMSBinaryContent>();
        
    private long fileSize = 0;

	public Map<String, CMSBinaryContent> getFiles() {
		return files;
	}

	public void setFiles(Map<String, CMSBinaryContent>files) {
		this.files = files;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
    
}
