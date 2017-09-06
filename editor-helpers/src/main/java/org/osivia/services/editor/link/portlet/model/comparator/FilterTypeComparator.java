package org.osivia.services.editor.link.portlet.model.comparator;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.editor.link.portlet.model.FilterType;
import org.springframework.stereotype.Component;

/**
 * Filter type comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see FilterType
 */
@Component
public class FilterTypeComparator implements Comparator<FilterType> {

    /**
     * Constructor.
     */
    public FilterTypeComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(FilterType type1, FilterType type2) {
        int result;

        if (StringUtils.isEmpty(type1.getName())) {
            result = -1;
        } else if (StringUtils.isEmpty(type2.getName())) {
            result = 1;
        } else if (type1.getLevel() == type2.getLevel()) {
            result = type1.getDisplayName().compareTo(type2.getDisplayName());
        } else {
            result = Integer.compare(type1.getLevel(), type2.getLevel());
        }

        return result;
    }

}
