package org.osivia.services.workspace.portlet.model;

import java.util.Date;
import java.util.List;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitation java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see InvitationObject
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Invitation extends InvitationObject {

    /** Acknowledgment date. */
    private Date acknowledgmentDate;
    /** Resending date. */
    private Date resendingDate;
    /** Local groups. */
    private List<CollabProfile> localGroups;
    /** Message. */
    private String message;

    /** Unknown user indicator. */
    private final boolean unknownUser;


    /**
     * Constructor.
     * 
     * @param person person
     */
    public Invitation(Person person) {
        super(person);
        this.unknownUser = false;
    }

    /**
     * Constructor used when no person was found with this UID.
     * 
     * @param uid person UID
     */
    public Invitation(String uid) {
        super(uid);
        this.unknownUser = true;
    }


    /**
     * Getter for acknowledgmentDate.
     * 
     * @return the acknowledgmentDate
     */
    public Date getAcknowledgmentDate() {
        return acknowledgmentDate;
    }

    /**
     * Setter for acknowledgmentDate.
     * 
     * @param acknowledgmentDate the acknowledgmentDate to set
     */
    public void setAcknowledgmentDate(Date acknowledgmentDate) {
        this.acknowledgmentDate = acknowledgmentDate;
    }

    /**
     * Getter for resendingDate.
     * 
     * @return the resendingDate
     */
    public Date getResendingDate() {
        return resendingDate;
    }

    /**
     * Setter for resendingDate.
     * 
     * @param resendingDate the resendingDate to set
     */
    public void setResendingDate(Date resendingDate) {
        this.resendingDate = resendingDate;
    }

    /**
     * Getter for localGroups.
     * 
     * @return the localGroups
     */
    public List<CollabProfile> getLocalGroups() {
        return localGroups;
    }

    /**
     * Setter for localGroups.
     * 
     * @param localGroups the localGroups to set
     */
    public void setLocalGroups(List<CollabProfile> localGroups) {
        this.localGroups = localGroups;
    }

    /**
     * Getter for message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message.
     * 
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for unknownUser.
     * 
     * @return the unknownUser
     */
    public boolean isUnknownUser() {
        return unknownUser;
    }

}
