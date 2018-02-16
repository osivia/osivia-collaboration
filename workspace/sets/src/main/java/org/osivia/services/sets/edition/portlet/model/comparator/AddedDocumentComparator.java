package org.osivia.services.sets.edition.portlet.model.comparator;

import java.util.Comparator;

import org.osivia.services.sets.edition.portlet.model.AddedDocument;
import org.springframework.stereotype.Component;

/**
 * AddedDocument comparator
 * @author Julien Barberet
 *
 */
@Component
public class AddedDocumentComparator implements Comparator<AddedDocument> {

	
	/**
	 * Constructor
	 */
    public AddedDocumentComparator() {
		super();
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public int compare(AddedDocument task1, AddedDocument task2) {
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
