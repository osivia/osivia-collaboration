package org.osivia.services.workspace.sharing.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Sharing form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SharingForm {

    /** Enabled indicator. */
    private boolean enabled;
    /** Link. */
    private SharingLink link;
    /** Users. */
    private List<String> users;

    /** Enabled indicator initial value. */
    private boolean initialEnabled;
    /** Close modal indicator. */
    private boolean close;


    /**
     * Constructor.
     */
    public SharingForm() {
        super();
    }


    /**
     * Getter for enabled.
     * 
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Setter for enabled.
     * 
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Getter for link.
     * 
     * @return the link
     */
    public SharingLink getLink() {
        return link;
    }

    /**
     * Setter for link.
     * 
     * @param link the link to set
     */
    public void setLink(SharingLink link) {
        this.link = link;
    }

    /**
     * Getter for users.
     * 
     * @return the users
     */
    public List<String> getUsers() {
        return users;
    }

    /**
     * Setter for users.
     * 
     * @param users the users to set
     */
    public void setUsers(List<String> users) {
        this.users = users;
    }

    /**
     * Getter for initialEnabled.
     * 
     * @return the initialEnabled
     */
    public boolean isInitialEnabled() {
        return initialEnabled;
    }

    /**
     * Setter for initialEnabled.
     * 
     * @param initialEnabled the initialEnabled to set
     */
    public void setInitialEnabled(boolean initialEnabled) {
        this.initialEnabled = initialEnabled;
    }

    /**
     * Getter for close.
     * 
     * @return the close
     */
    public boolean isClose() {
        return close;
    }

    /**
     * Setter for close.
     * 
     * @param close the close to set
     */
    public void setClose(boolean close) {
        this.close = close;
    }

}
