package fr.toutatice.collaboratif.purgeworkspaces.portlet.model;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Purge workspace options
 * @author Julien Barberet
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class PurgeWorkspaceOptions {

	/** Workspace sort options */
	private String sort;
	
	/** Workspace alt sort options */
	private String alt;
	
	/** Page number */
	private String pageNumber;
	
	/** Page size */
	private String pageSize;
	
	/** Total number of pages */
	private String totalPageNumber;
	
	/** Way to paging (previous, next or no way) */
	private String way;
	
	/** Total number of results */
	private String totalResultNumber;

	/** Constructor */
	public PurgeWorkspaceOptions() {
		super();
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getTotalPageNumber() {
		return totalPageNumber;
	}

	public void setTotalPageNumber(String totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getTotalResultNumber() {
		return totalResultNumber;
	}

	public void setTotalResultNumber(String totalResultNumber) {
		this.totalResultNumber = totalResultNumber;
	}
}
