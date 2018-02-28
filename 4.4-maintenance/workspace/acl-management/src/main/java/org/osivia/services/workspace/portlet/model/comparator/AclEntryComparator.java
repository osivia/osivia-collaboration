package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.workspace.portlet.model.AclEntry;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Workspace ACL entry comparator.
 *
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see AclEntry
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AclEntryComparator implements Comparator<AclEntry> {

    /** Comparator sort field. */
    private final String sort;
    /** Comparator alternative sort indicator. */
    private final boolean alt;


    /**
     * Constructor.
     *
     * @param sort comparator sort field
     * @param alt comparator alternative sort indicator
     */
    public AclEntryComparator(String sort, boolean alt) {
        super();
        this.sort = sort;
        this.alt = alt;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(AclEntry entry1, AclEntry entry2) {
        int result;

        if (entry1 == null) {
            result = -1;
        } else if (entry2 == null) {
            result = 1;
        } else if ("role".equals(this.sort)) {
            // Role
            Integer role1 = entry1.getRole().getWeight();
            Integer role2 = entry2.getRole().getWeight();

            result = role1.compareTo(role2);
        } else {
            // Name
            String name1 = StringUtils.defaultIfBlank(entry1.getDisplayName(), entry1.getId());
            String name2 = StringUtils.defaultIfBlank(entry2.getDisplayName(), entry2.getId());
            result = name1.compareToIgnoreCase(name2);
        }

        if (this.alt) {
            result = -result;
        }

        return result;
    }

}
