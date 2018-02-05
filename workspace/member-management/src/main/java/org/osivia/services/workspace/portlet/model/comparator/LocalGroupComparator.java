package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.CollabProfile;
import org.springframework.stereotype.Component;

/**
 * Local group comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see CollabProfile
 */
@Component
public class LocalGroupComparator implements Comparator<CollabProfile> {

    /**
     * Constructor.
     */
    public LocalGroupComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(CollabProfile localGroup1, CollabProfile localGroup2) {
        int result;

        if (localGroup1 == null) {
            result = -1;
        } else if (localGroup2 == null) {
            result = 1;
        } else {
            String displayName1 = StringUtils.trimToEmpty(localGroup1.getDisplayName());
            String displayName2 = StringUtils.trimToEmpty(localGroup2.getDisplayName());

            result = displayName1.compareToIgnoreCase(displayName2);
        }

        return result;
    }

}
