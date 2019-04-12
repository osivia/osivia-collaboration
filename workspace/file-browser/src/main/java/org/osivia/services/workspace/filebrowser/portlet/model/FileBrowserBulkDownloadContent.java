package org.osivia.services.workspace.filebrowser.portlet.model;

import java.io.File;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser bulk download content java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserBulkDownloadContent {

    /** Content type. */
    private String type;
    /** Content disposition. */
    private String disposition;
    /** File. */
    private File file;


    /**
     * Constructor.
     */
    public FileBrowserBulkDownloadContent() {
        super();
    }


    /**
     * Getter for type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for disposition.
     * 
     * @return the disposition
     */
    public String getDisposition() {
        return disposition;
    }

    /**
     * Setter for disposition.
     * 
     * @param disposition the disposition to set
     */
    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    /**
     * Getter for file.
     * 
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Setter for file.
     * 
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

}
