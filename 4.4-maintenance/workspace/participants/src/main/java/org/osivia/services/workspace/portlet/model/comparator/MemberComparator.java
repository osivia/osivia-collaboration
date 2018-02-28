package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.workspace.portlet.model.Member;
import org.springframework.stereotype.Component;

/**
 * Member comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Member
 */
@Component
public class MemberComparator implements Comparator<Member> {

    /**
     * Constructor.
     */
    public MemberComparator() {
        super();
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
        } else {
            String lastName1 = StringUtils.trimToEmpty(member1.getLastName());
            String lastName2 = StringUtils.trimToEmpty(member2.getLastName());

            if (StringUtils.equalsIgnoreCase(lastName1, lastName2)) {
                String displayName1 = StringUtils.trimToEmpty(member1.getDisplayName());
                String displayName2 = StringUtils.trimToEmpty(member2.getDisplayName());

                result = displayName1.compareToIgnoreCase(displayName2);
            } else {
                result = lastName1.compareToIgnoreCase(lastName2);
            }
        }

        return result;
    }

}
