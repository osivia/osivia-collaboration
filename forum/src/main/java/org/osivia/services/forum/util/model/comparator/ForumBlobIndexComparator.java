package org.osivia.services.forum.util.model.comparator;

import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * Forum blob index comparator.
 *
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Integer
 */
@Component
public class ForumBlobIndexComparator implements Comparator<Integer> {

    /**
     * Constructor.
     */
    public ForumBlobIndexComparator() {
        super();
    }


    @Override
    public int compare(Integer o1, Integer o2) {
        return o2.compareTo(o1);
    }

}
