package org.osivia.services.workspace.portlet.model.comparator;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.workspace.portlet.model.ObjectDocument;
import org.osivia.services.workspace.portlet.model.TrashFormSort;
import org.osivia.services.workspace.portlet.model.TrashedDocument;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;

/**
 * Trashed document comparator.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrashedDocumentComparator implements Comparator<TrashedDocument> {

    /**
     * Comparator sort property.
     */
    private final TrashFormSort sort;
    /**
     * Comparator alternative sort indicator.
     */
    private final boolean alt;


    /**
     * Constructor.
     *
     * @param sort sort property
     * @param alt  alternative sort indicator
     */
    public TrashedDocumentComparator(TrashFormSort sort, boolean alt) {
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
        } else if (TrashFormSort.DATE.equals(this.sort)) {
            // Date
            result = compareDates(document1, document2);
        } else if (TrashFormSort.LOCATION.equals(this.sort)) {
            // Location
            ObjectDocument location1 = document1.getLocation();
            ObjectDocument location2 = document2.getLocation();
            result = location1.getDocument().getTitle().compareTo(location2.getDocument().getTitle());
        } else {
            // Title
            String title1 = document1.getDocument().getTitle();
            String title2 = document2.getDocument().getTitle();
            result = title1.compareToIgnoreCase(title2);
        }

        if (this.alt) {
            result = -result;
        }

        if ((result == 0) && (!TrashFormSort.DATE.equals(this.sort))) {
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
        Date date1 = this.getDeletionDate(document1);
        Date date2 = this.getDeletionDate(document2);

        if (date1 == null) {
            result = -1;
        } else if (date2 == null) {
            result = 1;
        } else {
            result = date1.compareTo(date2);
        }
        return result;
    }


    /**
     * Get deletion date.
     *
     * @param trashedDocument trashed document
     * @return date
     */
    private Date getDeletionDate(TrashedDocument trashedDocument) {
        // Document DTO
        DocumentDTO dto = trashedDocument.getDocument();
        // Nuxeo document
        Document nuxeoDocument = dto.getDocument();

        return nuxeoDocument.getDate("dc:modified");
    }

}
