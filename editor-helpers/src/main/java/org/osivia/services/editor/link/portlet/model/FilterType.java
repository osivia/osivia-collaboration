package org.osivia.services.editor.link.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Filter type java-bean.
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FilterType {

    /** Name. */
    private String name;
    /** Icon. */
    private String icon;
    /** Internationalized display name. */
    private String displayName;
    /** Level. */
    private int level;
    

    /**
     * Constructor.
     */
    public FilterType() {
        super();
    }


    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * Getter for level.
     * 
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Setter for level.
     * 
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

}
