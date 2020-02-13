package org.osivia.services.search.selector.scope.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Scope selector portlet settings.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScopeSelectorSettings {

    /** Label. */
    private String label;
    /** Selector identifier. */
    private String selectorId;


    /**
     * Constructor.
     */
    public ScopeSelectorSettings() {
        super();
    }


    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter for label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for selectorId.
     * 
     * @return the selectorId
     */
    public String getSelectorId() {
        return selectorId;
    }

    /**
     * Setter for selectorId.
     * 
     * @param selectorId the selectorId to set
     */
    public void setSelectorId(String selectorId) {
        this.selectorId = selectorId;
    }

}
