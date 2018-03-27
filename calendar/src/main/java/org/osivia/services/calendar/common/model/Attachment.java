package org.osivia.services.calendar.common.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Attachment java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Attachment extends AbstractTemporaryFile {

    /** Original BLOB index. */
    private Integer index;
    /** Icon. */
    private String icon;
    /** URL */
    private String url;

    /**
     * Constructor.
     */
    public Attachment() {
        super();
    }


    /**
     * Getter for index.
     * 
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Setter for index.
     * 
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Setter for icon.
     * 
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }


	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
