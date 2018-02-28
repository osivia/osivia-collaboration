package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.services.workspace.portlet.model.Group;
import org.springframework.stereotype.Component;

/**
 * Group comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Group
 */
@Component
public class GroupComparator implements Comparator<Group> {

    /**
     * Constructor.
     */
    public GroupComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(Group group1, Group group2) {
        int result;
        
        WorkspaceRole role1;
        if (group1 == null) {
            role1 = null;
        } else {
            role1 = group1.getRole();
        }

        WorkspaceRole role2;
        if (group2 == null) {
            role2 = null;
        } else {
            role2 = group2.getRole();
        }

        if (role1 == null) {
            result = -1;
        } else if (role2 == null) {
            result = 1;
        } else {
            Integer weight1 = role1.getWeight();
            Integer weight2 = role2.getWeight();

            result = weight2.compareTo(weight1);
        }
        
        return result;
    }

}

