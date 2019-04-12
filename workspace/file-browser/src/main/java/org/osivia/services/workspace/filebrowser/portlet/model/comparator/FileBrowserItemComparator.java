package org.osivia.services.workspace.filebrowser.portlet.model.comparator;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserItem;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSort;
import org.osivia.services.workspace.filebrowser.portlet.model.FileBrowserSortCriteria;

/**
 * File browser item comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see FileBrowserItem
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserItemComparator implements Comparator<FileBrowserItem> {

    /** Sort criteria. */
    private final FileBrowserSortCriteria criteria;


    /**
     * Constructor.
     * 
     * @param criteria sort criteria
     * @param alt alternative sort indicator
     */
    public FileBrowserItemComparator(FileBrowserSortCriteria criteria) {
        super();
        this.criteria = criteria;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(FileBrowserItem item1, FileBrowserItem item2) {
        int result;

        // Folderish comparison
        boolean folderish1 = item1.isFolderish();
        boolean folderish2 = item2.isFolderish();
        boolean folderishComparison = BooleanUtils.xor(new boolean[]{folderish1, folderish2});

        if (folderishComparison) {
            if (folderish1) {
                result = -1;
            } else {
                result = 1;
            }
        } else if (FileBrowserSort.LAST_MODIFICATION.equals(this.criteria.getSort())) {
            // Last modification comparison
            Date date1 = item1.getLastModification();
            Date date2 = item2.getLastModification();

            if (date1 == null) {
                result = -1;
            } else if (date2 == null) {
                result = 1;
            } else {
                result = date1.compareTo(date2);
            }
        } else if (FileBrowserSort.FILE_SIZE.equals(this.criteria.getSort())) {
            // File size comparison
            Long size1 = item1.getSize();
            Long size2 = item2.getSize();

            if (size1 == null) {
                result = -1;
            } else if (size2 == null) {
                result = 1;
            } else {
                result = size1.compareTo(size2);
            }
        } else {
            // Title comparison
            String title1 = StringUtils.trimToEmpty(item1.getTitle());
            String title2 = StringUtils.trimToEmpty(item2.getTitle());

            result = title1.compareToIgnoreCase(title2);
        }


        // Alternative sort
        if (this.criteria.isAlt() && !folderishComparison) {
            result = -result;
        }

        return result;
    }

}
