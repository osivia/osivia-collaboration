package org.osivia.services.workspace.portlet.repository;

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
     * Get public permission.
     * 
     * @return permission
     */
    public static Permission getPublicPermission() {
        Permission permission = new Permission();
        permission.setName("Everyone");
        permission.setValues(Arrays.asList(new String[]{"Read"}));
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
