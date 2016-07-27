package org.osivia.services.workspace.portlet.model;

import org.osivia.directory.v2.model.ext.WorkspaceRole;

/**
 * Role java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class Role {

    /** Display name. */
    private String displayName;

    /** Identifier. */
    private final String id;
    /** Weight. */
    private final int weight;


    /**
     * Constructor.
     *
     * @param role workspace role
     */
    public Role(WorkspaceRole role) {
        super();
        this.id = role.getId();
        this.weight = role.getWeight();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Role other = (Role) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }


    /**
     * Getter for displayName.
     *
     * @return the displayName
     */
    public String getDisplayName() {
        return this.displayName;
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
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for weight.
     *
     * @return the weight
     */
    public int getWeight() {
        return this.weight;
    }

}
