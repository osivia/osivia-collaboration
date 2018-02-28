package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Nuxeo document permission java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Permission {
    
    /** Public permission name. */
    public static final String PUBLIC_NAME = "Everyone";


    /** Permission name. */
    private String name;
    /** Permission values. */
    private List<String> values;
    /** Permission group indicator. */
    private boolean group;


    /**
     * Constructor.
     */
    public Permission() {
        super();
    }


    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
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
     * Getter for values.
     *
     * @return the values
     */
    public List<String> getValues() {
        return this.values;
    }

    /**
     * Setter for values.
     *
     * @param values the values to set
     */
    public void setValues(List<String> values) {
        this.values = values;
    }

    /**
     * Getter for group.
     * 
     * @return the group
     */
    public boolean isGroup() {
        return this.group;
    }

    /**
     * Setter for group.
     * 
     * @param group the group to set
     */
    public void setGroup(boolean group) {
        this.group = group;
    }

}
