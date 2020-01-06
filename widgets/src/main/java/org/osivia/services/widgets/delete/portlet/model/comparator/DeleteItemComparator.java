package org.osivia.services.widgets.delete.portlet.model.comparator;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.widgets.delete.portlet.model.DeleteItem;
import org.springframework.stereotype.Component;

import java.util.Comparator;

/**
 * Delete item comparator.
 *
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see DeleteItem
 */
@Component
public class DeleteItemComparator implements Comparator<DeleteItem> {

    /**
     * Constructor.
     */
    public DeleteItemComparator() {
        super();
    }


    @Override
    public int compare(DeleteItem item1, DeleteItem item2) {
        int result;

        if ((item1 == null) || (item1.getDocument() == null)) {
            result = -1;
        } else if ((item2 == null) || (item2.getDocument() == null)) {
            result = 1;
        } else {
            String title1 = StringUtils.trimToEmpty(item1.getDocument().getTitle());
            String title2 = StringUtils.trimToEmpty(item2.getDocument().getTitle());

            result = title1.compareToIgnoreCase(title2);
        }
        
        return result;
    }

}
