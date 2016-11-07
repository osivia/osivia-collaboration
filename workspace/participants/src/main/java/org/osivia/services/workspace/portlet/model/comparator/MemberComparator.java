package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;

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

        String displayName1;
        if (member1 == null) {
            displayName1 = null;
        } else {
            displayName1 = member1.getDisplayName();
        }

        String displayName2;
        if (member2 == null) {
            displayName2 = null;
        } else {
            displayName2 = member2.getDisplayName();
        }

        if (displayName1 == null) {
            result = -1;
        } else if (displayName2 == null) {
            result = 1;
        } else {
            result = displayName1.compareToIgnoreCase(displayName2);
        }

        return result;
    }

}
