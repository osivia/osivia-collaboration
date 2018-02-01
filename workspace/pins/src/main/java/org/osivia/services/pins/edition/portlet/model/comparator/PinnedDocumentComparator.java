package org.osivia.services.pins.edition.portlet.model.comparator;

import java.util.Comparator;

import org.osivia.services.pins.edition.portlet.model.PinnedDocument;
import org.springframework.stereotype.Component;

/**
 * PinnedDocument comparator
 * @author jbarberet
 *
 */
@Component
public class PinnedDocumentComparator implements Comparator<PinnedDocument> {

	
	/**
	 * Constructor
	 */
    public PinnedDocumentComparator() {
		super();
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public int compare(PinnedDocument task1, PinnedDocument task2) {
        int result;
        if (task1 == null) {
            result = 1;
        } else {
            // Compare orders
            Integer order1 = task1.getOrder();
            Integer order2 = task2.getOrder();
            result = order1.compareTo(order2);
        }
        return result;
    }
	
}
