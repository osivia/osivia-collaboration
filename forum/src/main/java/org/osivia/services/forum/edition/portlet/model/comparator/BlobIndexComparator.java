package org.osivia.services.forum.edition.portlet.model.comparator;

import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * Blob index comparator.
 *
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Integer
 */
@Component
public class BlobIndexComparator implements Comparator<Integer> {

    /**
     * Constructor.
     */
    public BlobIndexComparator() {
        super();
    }


    @Override
    public int compare(Integer o1, Integer o2) {
        return o2.compareTo(o1);
    }

}
