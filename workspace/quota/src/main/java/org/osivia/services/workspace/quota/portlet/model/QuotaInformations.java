package org.osivia.services.workspace.quota.portlet.model;

import java.util.List;
/**
 * The Class Quota Informations.
 */
public class QuotaInformations {
	
	/** The tree size. */
	private long treeSize;
	
	/**  The quota. */
	private long quota;
	
	
    public QuotaInformations(long treeSize, long quota) {
		super();
		this.treeSize = treeSize;
		this.quota = quota;
	}

	/**
     * Gets the quota.
     *
     * @return the quota
     */
    public long getQuota() {
		return quota;
	}

	/**
	 * Sets the quota.
	 *
	 * @param quota the new quota
	 */
	public void setQuota(long quota) {
		this.quota = quota;
	}

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




}
