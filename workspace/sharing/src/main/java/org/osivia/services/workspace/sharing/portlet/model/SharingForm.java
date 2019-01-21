package org.osivia.services.workspace.sharing.portlet.model;

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

}
