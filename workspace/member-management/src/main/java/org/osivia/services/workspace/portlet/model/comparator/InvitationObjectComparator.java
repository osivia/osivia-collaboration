package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationObject;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.model.MembersSort;
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
public class InvitationObjectComparator implements Comparator<InvitationObject> {

    /** Comparator sort. */
    private final MembersSort sort;
    /** Comparator alternative sort indicator. */
    private final boolean alt;


    /**
     * Constructor.
     * 
     * @param sort comparator sort
     * @param alt comparator alternative sort indicator
     */
    public InvitationObjectComparator(MembersSort sort, boolean alt) {
        super();
        this.sort = sort;
        this.alt = alt;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(InvitationObject invitation1, InvitationObject invitation2) {
        int result;

        if (invitation1 == null) {
            result = -1;
        } else if (invitation2 == null) {
            result = 1;
        } else if (MembersSort.DATE.equals(this.sort)) {
            // Dates
            Date date1 = invitation1.getDate();
            Date date2 = invitation2.getDate();
            
            if ((invitation1 instanceof Invitation) && (invitation2 instanceof Invitation)) {
                Invitation i1 = (Invitation) invitation1;
                Invitation i2 = (Invitation) invitation2;

                if (i1.getResendingDate() != null) {
                    date1 = i1.getResendingDate();
                }
                
                if (i2.getResendingDate() != null) {
                    date2 = i2.getResendingDate();
                }
            }

            result = date1.compareTo(date2);
        } else if (MembersSort.ROLE.equals(this.sort)) {
            // Role
            Integer role1 = invitation1.getRole().getWeight();
            Integer role2 = invitation2.getRole().getWeight();

            result = role1.compareTo(role2);
        } else if (MembersSort.INVITATION_STATE.equals(this.sort)) {
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
            String name1 = StringUtils.defaultIfBlank(invitation1.getDisplayName(), invitation1.getId());
            String name2 = StringUtils.defaultIfBlank(invitation2.getDisplayName(), invitation2.getId());
            result = name1.compareToIgnoreCase(name2);
        }

        if (this.alt) {
            result = -result;
        }

        if ((result == 0) && (!MembersSort.DATE.equals(this.sort))) {
            // Date
            Date date1 = invitation1.getDate();
            Date date2 = invitation2.getDate();

            result = date2.compareTo(date1);
        }

        return result;
    }

}
