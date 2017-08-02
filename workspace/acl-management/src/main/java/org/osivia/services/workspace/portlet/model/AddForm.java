package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Add form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddForm {

    /** Records. */
    private List<Record> records;
    /** Record identifiers. */
    private List<String> identifiers;
    /** Role. */
    private Role role;


    /**
     * Constructor.
     */
    public AddForm() {
        super();
    }


    /**
     * Getter for records.
     *
     * @return the records
     */
    public List<Record> getRecords() {
        return this.records;
    }

    /**
     * Setter for records.
     *
     * @param records the records to set
     */
    public void setRecords(List<Record> records) {
        this.records = records;
    }

    /**
     * Getter for identifiers.
     *
     * @return the identifiers
     */
    public List<String> getIdentifiers() {
        return this.identifiers;
    }

    /**
     * Setter for identifiers.
     *
     * @param identifiers the identifiers to set
     */
    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * Getter for role.
     *
     * @return the role
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * Setter for role.
     *
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

}
