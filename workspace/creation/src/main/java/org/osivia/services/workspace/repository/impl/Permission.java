package org.osivia.services.workspace.repository.impl;

import java.util.Arrays;
import java.util.List;

/**
 * Nuxeo document permission java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class Permission {

    /** Permission name. */
    private String name;
    /** Permission values. */
    private List<String> values;

    /** Granted permission indicator. */
    private final boolean granted;


    /**
     * Constructor.
     */
    public Permission() {
        this(true);
    }


    /**
     * Constructor.
     *
     * @param granted granted permission indicator.
     */
    private Permission(boolean granted) {
        super();
        this.granted = granted;
    }


    /**
     * Get inheritance blocking permission.
     * @return permission
     */
    public static Permission getInheritanceBlocking() {
        Permission permission = new Permission(false);
        permission.setName("Everyone");
        permission.setValues(Arrays.asList(new String[]{"Everything"}));
        return permission;
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
     * Getter for granted.
     *
     * @return the granted
     */
    public boolean isGranted() {
        return this.granted;
    }

}
