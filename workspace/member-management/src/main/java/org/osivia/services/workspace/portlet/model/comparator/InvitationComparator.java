package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Invitation comparator.
 * 
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Invitation
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvitationComparator implements Comparator<Invitation> {

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
    public InvitationComparator(String sort, boolean alt) {
        super();
        this.sort = sort;
        this.alt = alt;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(Invitation invitation1, Invitation invitation2) {
        int result;

        if (invitation1 == null) {
            result = -1;
        } else if (invitation2 == null) {
            result = 1;
        } else if ("date".equals(this.sort)) {
            // Date
            Date date1 = invitation1.getDate();
            Date date2 = invitation2.getDate();

            result = date1.compareTo(date2);
        } else if ("role".equals(this.sort)) {
            // Role
            Integer role1 = invitation1.getRole().getWeight();
            Integer role2 = invitation2.getRole().getWeight();

            result = role1.compareTo(role2);
        } else if ("state".equals(this.sort)) {
            // Invitation state
            InvitationState state1 = invitation1.getState();
            InvitationState state2 = invitation2.getState();

            if (state1 == null) {
                result = -1;
            } else if (state2 == null) {
                result = 1;
            } else {
                result = state1.name().compareTo(state2.name());
            }
        } else {
            // Name
            String name1 = StringUtils.defaultIfBlank(invitation1.getPerson().getDisplayName(), invitation1.getId());
            String name2 = StringUtils.defaultIfBlank(invitation2.getPerson().getDisplayName(), invitation2.getId());
            result = name1.compareToIgnoreCase(name2);
        }

        if (this.alt) {
            result = -result;
        }

        return result;
    }

}