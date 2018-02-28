package org.osivia.services.workspace.portlet.model;

import java.util.SortedSet;
import java.util.TreeSet;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Synthesis node java-bean.
 *
 * @author Cédric Krommenhoek
 * @see Comparable
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SynthesisNode implements Comparable<SynthesisNode> {

    /** Node display name. */
    private String displayName;


    /** Node name. */
    private final String name;
    /** Node type. */
    private final SynthesisNodeType type;
    /** Node children. */
    private final SortedSet<SynthesisNode> children;


    /**
     * Constructor.
     *
     * @param name node name
     * @param type node type
     */
    public SynthesisNode(String name, SynthesisNodeType type) {
        super();
        this.name = name;
        this.type = type;
        if (SynthesisNodeType.USER.equals(type)) {
            this.children = null;
        } else {
            this.children = new TreeSet<>();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(SynthesisNode other) {
        int result;

        if ((other == null) || (other.type == null)) {
            result = -1;
        } else if (this.type == null) {
            result = 1;
        } else if (this.type.equals(other.type)) {
            if (SynthesisNodeType.ROLE.equals(this.type)) {
                // Role weight comparison
                WorkspaceRole role1 = WorkspaceRole.fromId(this.name);
                WorkspaceRole role2 = WorkspaceRole.fromId(other.name);
                if (role1 == null) {
                    result = 1;
                } else if (role2 == null) {
                    result = -1;
                } else {
                    result = -Integer.compare(role1.getWeight(), role2.getWeight());
                }
            } else {
                // Group or user display name comparison
                String displayName1 = this.displayName;
                String displayName2 = other.displayName;
                if (displayName1 == null) {
                    result = 1;
                } else if (displayName2 == null) {
                    result = -1;
                } else {
                    result = displayName1.compareTo(displayName2);
                }
            }
        } else {
            // Type sorting order comparison
            result = Integer.compare(this.type.getOrder(), other.type.getOrder());
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
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
        SynthesisNode other = (SynthesisNode) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
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
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for type.
     *
     * @return the type
     */
    public SynthesisNodeType getType() {
        return this.type;
    }

    /**
     * Getter for children.
     *
     * @return the children
     */
    public SortedSet<SynthesisNode> getChildren() {
        return this.children;
    }

}

