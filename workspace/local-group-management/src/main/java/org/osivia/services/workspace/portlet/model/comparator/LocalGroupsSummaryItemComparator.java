package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.workspace.portlet.model.LocalGroupsSort;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummaryItem;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Local group comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see LocalGroupsSummaryItem
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LocalGroupsSummaryItemComparator implements Comparator<LocalGroupsSummaryItem> {

    /** Sort property. */
    private final LocalGroupsSort sort;
    /** Alternative sort indicator. */
    private final boolean alt;


    /**
     * Constructor.
     * 
     * @param sort sort property
     * @param alt alternative sort indicator
     */
    public LocalGroupsSummaryItemComparator(LocalGroupsSort sort, boolean alt) {
        super();
        this.sort = sort;
        this.alt = alt;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(LocalGroupsSummaryItem group1, LocalGroupsSummaryItem group2) {
        int result;

        if (group1 == null) {
            result = -1;
        } else if (group2 == null) {
            result = 1;
        } else if (LocalGroupsSort.MEMBERS_COUNT.equals(this.sort)) {
            // Members count
            Integer count1 = Integer.valueOf(group1.getMembersCount());
            Integer count2 = Integer.valueOf(group2.getMembersCount());
            result = count1.compareTo(count2);
        } else {
            // Display name
            String displayName1 = StringUtils.trimToEmpty(group1.getDisplayName());
            String displayName2 = StringUtils.trimToEmpty(group2.getDisplayName());
            result = displayName1.compareToIgnoreCase(displayName2);
        }

        if (this.alt) {
            result = -result;
        }

        return result;
    }

}
