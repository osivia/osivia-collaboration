package org.osivia.services.workspace.task.creation.portlet.model.comparator;

import java.util.Comparator;

import org.springframework.stereotype.Component;

/**
 * Alpha order comparartor.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see String
 */
@Component
public class AlphaOrderComparator implements Comparator<String> {

    /**
     * Constructor.
     */
    public AlphaOrderComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
    }

}
