package org.osivia.services.search.selector.type.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Type selector portlet settings.
 * 
 * @author Loïc Billon
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TypeSelectorSettings {

    /** Label. */
    private String label;
    /** Selector identifier. */
    private String selectorId;


    /**
     * Constructor.
     */
    public TypeSelectorSettings() {
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
