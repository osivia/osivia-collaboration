package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.workspace.portlet.model.Member;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Member comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Member
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MemberComparator implements Comparator<Member> {

    /** Comparator sort field. */
    private final String sort;
    /** Comparator alternative sort indicator. */
    private final boolean alt;


    /**
     * Constructor.
     * 
     * @param sort comparator sort field
     * @param alt comparator alternative sort indicator
     */
    public MemberComparator(String sort, boolean alt) {
        super();
        this.sort = sort;
        this.alt = alt;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(Member member1, Member member2) {
        int result;

        if (member1 == null) {
            result = -1;
        } else if (member2 == null) {
            result = 1;
        } else if ("date".equals(this.sort)) {
            // Date
            result = compareDates(member1, member2);
        } else if ("role".equals(this.sort)) {
            // Role
            Integer role1 = member1.getRole().getWeight();
            Integer role2 = member2.getRole().getWeight();

            result = role1.compareTo(role2);
        } else {
            // Name
            String name1 = StringUtils.defaultIfBlank(member1.getPerson().getDisplayName(), member1.getId());
            String name2 = StringUtils.defaultIfBlank(member2.getPerson().getDisplayName(), member2.getId());
            result = name1.compareToIgnoreCase(name2);
        }

        if (this.alt) {
            result = -result;
        }

        if ((result == 0) && (!"date".equals(this.sort))) {
            // Date
            result = compareDates(member1, member2);
        }

        return result;
    }


    /**
     * Compare member dates.
     * 
     * @param member1 member #1
     * @param member2 member #2
     * @return date comparison result
     */
    private int compareDates(Member member1, Member member2) {
        int result;
        Date date1 = member1.getDate();
        Date date2 = member2.getDate();

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
