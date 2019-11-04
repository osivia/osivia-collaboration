package org.osivia.services.workspace.quota.portlet.model;

/**
 * The Class Quota Informations.
 */
public class QuotaInformations {
	
	/** The tree size. */
	private long treeSize;
	
	/**  The quota. */
	private long quota;

	/** The tree size in trash. */
	private long trashedTreeSize;
	
	
    public QuotaInformations(long treeSize, long trashedTreeSize, long quota) {
		super();
		this.treeSize = treeSize;
		this.trashedTreeSize = trashedTreeSize;
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

	/**
	 * Get the trashed tree size
	 * @return
	 */
	public long getTrashedTreeSize() {
		return trashedTreeSize;
	}

	/**
	 * Set the trashed tree size
	 * @param trashedTreeSize
	 */
	public void setTrashedTreeSize(long trashedTreeSize) {
		this.trashedTreeSize = trashedTreeSize;
	}




}
