package org.osivia.services.workspace.sharing.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Sharing link java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SharingLink {

    /** Link identifier. */
    private String id;
    /** Live editable indicator. */
    private boolean liveEditable;
    /** Link permission. */
    private SharingPermission permission;
    /** Link URL. */
    private String url;


    /**
     * Constructor.
     */
    public SharingLink() {
        super();
    }


    /**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id.
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for liveEditable.
     * 
     * @return the liveEditable
     */
    public boolean isLiveEditable() {
        return liveEditable;
    }

    /**
     * Setter for liveEditable.
     * 
     * @param liveEditable the liveEditable to set
     */
    public void setLiveEditable(boolean liveEditable) {
        this.liveEditable = liveEditable;
    }

    /**
     * Getter for permission.
     * 
     * @return the permission
     */
    public SharingPermission getPermission() {
        return permission;
    }

    /**
     * Setter for permission.
     * 
     * @param permission the permission to set
     */
    public void setPermission(SharingPermission permission) {
        this.permission = permission;
    }

    /**
     * Getter for url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for url.
     * 
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
