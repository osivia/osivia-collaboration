package org.osivia.services.workspace.quota.portlet.model;

import java.util.List;

/**
 * The Class Quota Informations.
 */
public class QuotaInformations {
	
	/** The tree size. */
	private long treeSize;
	
	
    /**
     * Gets the tree size.
     *
     * @return the tree size
     */
    public long getTreeSize() {
		return treeSize;
	}

	/**
	 * Sets the tree size.
	 *
	 * @param treeSize the new tree size
	 */
	public void setTreeSize(long treeSize) {
		this.treeSize = treeSize;
	}

	/** Quota detail. */
    private List<QuotaItem> quotaItems;

    /**
     * Getter for trashedDocuments.
     * 
     * @return the trashedDocuments
     */
    public List<QuotaItem> getQuotaItems() {
        return quotaItems;
    }

    /**
     * Setter for trashedDocuments.
     *
     * @param quotaItems the new quotas items
     */
    public void setQuotasItems(List<QuotaItem> quotaItems) {
        this.quotaItems = quotaItems;
    }

	public QuotaInformations(long treeSize, List<QuotaItem> quotaItems) {
		super();
		this.treeSize = treeSize;
		this.quotaItems = quotaItems;
	}


}
