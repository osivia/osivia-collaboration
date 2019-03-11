package org.osivia.services.workspace.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Local groups summary item.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LocalGroupsSummaryItem extends AbstractLocalGroup {

    /** Members count. */
    private int membersCount;


    /**
     * Constructor.
     */
    public LocalGroupsSummaryItem() {
        super();
    }


    /**
     * Getter for membersCount.
     *
     * @return the membersCount
     */
    public int getMembersCount() {
        return this.membersCount;
    }

    /**
     * Setter for membersCount.
     *
     * @param membersCount the membersCount to set
     */
    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

}
