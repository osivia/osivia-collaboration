package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;
import java.util.Date;

import org.osivia.services.workspace.portlet.model.ObjectDocument;
import org.osivia.services.workspace.portlet.model.TrashedDocument;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Trashed document comparator.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrashedDocumentComparator implements Comparator<TrashedDocument> {

    /** Comparator sort property. */
    private final String sort;
    /** Comparator alternative sort indicator. */
    private final boolean alt;


    /**
     * Constructor.
     * 
     * @param sort sort property
     * @param alt alternative sort indicator
     */
    public TrashedDocumentComparator(String sort, boolean alt) {
        super();
        this.sort = sort;
        this.alt = alt;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(TrashedDocument document1, TrashedDocument document2) {
        int result;

        if (document1 == null) {
            result = -1;
        } else if (document2 == null) {
            result = 1;
        } else if ("date".equals(this.sort)) {
            // Date
            result = compareDates(document1, document2);
        } else if ("location".equals(this.sort)) {
            // Location
            ObjectDocument location1 = document1.getLocation();
            ObjectDocument location2 = document2.getLocation();
            result = location1.getTitle().compareTo(location2.getTitle());
        } else {
            // Title
            String title1 = document1.getTitle();
            String title2 = document2.getTitle();
            result = title1.compareToIgnoreCase(title2);
        }

        if (this.alt) {
            result = -result;
        }

        if ((result == 0) && (!"date".equals(this.sort))) {
            // Date
            result = compareDates(document1, document2);
        }

        return result;
    }


    /**
     * Compare trashed document deletion dates.
     * 
     * @param document1 trashed document #1
     * @param document2 trashed document #2
     * @return date comparison result
     */
    private int compareDates(TrashedDocument document1, TrashedDocument document2) {
        int result;
        Date date1 = document1.getDeletionDate();
        Date date2 = document2.getDeletionDate();

        if (date1 == null) {
            result = -1;
        } else if (date2 == null) {
            result = 1;
        } else {
            result = date1.compareTo(date2);
        }
        return result;
    }

}
