package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.services.workspace.portlet.model.MemberObject;
import org.osivia.services.workspace.portlet.model.MembersSort;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Member object comparator.
 *
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see MemberObject
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MemberObjectComparator implements Comparator<MemberObject> {

    /** Comparator sort. */
    private final MembersSort sort;
    /** Comparator alternative sort indicator. */
    private final boolean alt;

    /** Log. */
    private final Log log;

    /**
     * Constructor.
     *
     * @param sort comparator sort
     * @param alt comparator alternative sort indicator
     */
    public MemberObjectComparator(MembersSort sort, boolean alt) {
        super();
        this.sort = sort;
        this.alt = alt;

        // Log
        this.log = LogFactory.getLog(this.getClass());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(MemberObject memberObject1, MemberObject memberObject2) {
        int result;

        if (memberObject1 == null) {
            result = -1;
        } else if (memberObject2 == null) {
            result = 1;
        } else if (MembersSort.DATE.equals(this.sort)) {
            // Date
            result = this.compareDates(memberObject1, memberObject2);
        } else if (MembersSort.ROLE.equals(this.sort)) {
            // Role weight
            // #1718 - catch errors during sort
            Integer roleWeight1;
            if (memberObject1.getRole() == null) {
                roleWeight1 = 0;
                this.log.error(memberObject1.getId() + " has no role !");
            } else {
                roleWeight1 = memberObject1.getRole().getWeight();
            }
            Integer roleWeight2 = 0;
            if (memberObject2.getRole() == null) {
                roleWeight2 = 0;
                this.log.error(memberObject2.getId() + " has no role !");
            } else {
                roleWeight2 = memberObject2.getRole().getWeight();
            }
            result = roleWeight1.compareTo(roleWeight2);
        } else {
            // Name
            String name1 = StringUtils.defaultIfBlank(memberObject1.getDisplayName(), memberObject1.getId());
            String name2 = StringUtils.defaultIfBlank(memberObject2.getDisplayName(), memberObject2.getId());
            result = name1.compareToIgnoreCase(name2);
        }

        if (this.alt) {
            result = -result;
        }

        if ((result == 0) && (!MembersSort.DATE.equals(this.sort))) {
            // Date
            result = this.compareDates(memberObject1, memberObject2);
        }

        return result;
    }


    /**
     * Compare member object dates.
     *
     * @param memberObject1 member object #1
     * @param memberObject2 member object #2
     * @return date comparison result
     */
    private int compareDates(MemberObject memberObject1, MemberObject memberObject2) {
        int result;
        Date date1 = memberObject1.getDate();
        Date date2 = memberObject2.getDate();

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
