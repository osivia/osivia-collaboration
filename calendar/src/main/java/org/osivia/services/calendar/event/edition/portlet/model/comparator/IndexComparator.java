package org.osivia.services.calendar.event.edition.portlet.model.comparator;

import java.util.Comparator;

import org.springframework.stereotype.Component;

/**
 * Blob index comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Integer
 */
@Component
public class IndexComparator implements Comparator<Integer> {

    /**
     * Constructor.
     */
    public IndexComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(Integer o1, Integer o2) {
        return o2.compareTo(o1);
    }

}
