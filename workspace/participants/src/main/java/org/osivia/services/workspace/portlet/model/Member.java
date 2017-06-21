package org.osivia.services.workspace.portlet.model;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * Workspace member java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Member {

    /** Member identifier. */
    private String id;
    /** Member URL. */
    private String url;
    /** Member avatar URL. */
    private String avatarUrl;
    /** Member display name. */
    private String displayName;
    /** Member email. */
    private String email;
    /** Member joined date. */
    private Date joinedDate;
    /** Nuxeo profile */
    private DocumentDTO nxProfile;


    /**
     * Constructor.
     */
    public Member() {
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

    /**
     * Getter for avatarUrl.
     * 
     * @return the avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Setter for avatarUrl.
     * 
     * @param avatarUrl the avatarUrl to set
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    /**
     * Getter for displayName.
     * 
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter for displayName.
     * 
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter for email.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email.
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for joinedDate.
     * 
     * @return the joinedDate
     */
    public Date getJoinedDate() {
        return joinedDate;
    }

    /**
     * Setter for joinedDate.
     * 
     * @param joinedDate the joinedDate to set
     */
    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }


	/**
	 * @return the nxProfile
	 */
	public DocumentDTO getNxProfile() {
		return nxProfile;
	}


	/**
	 * @param nxProfile the nxProfile to set
	 */
	public void setNxProfile(DocumentDTO nxProfile) {
		this.nxProfile = nxProfile;
	}

    
}
