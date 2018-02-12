package fr.toutatice.collaboratif.purgeworkspaces.portlet.model;

import java.util.Date;
import java.util.List;

/** WorkspaceLine bean */
public class WorkspaceLine {

	private String id;
	
	private String title;
	
	private List<String> animators;
	
	private Date expirationDate;
	
	private Date deletedDate;
	
	private boolean selected;
	
	private boolean expired;
	
	private String lastContributor;

	/** Constructor */
	public WorkspaceLine(String id, String title, List<String> animators, Date expirationDate,  Date deletedDate, String lastContributor,boolean expired) {
		super();
		this.id = id;
		this.title = title;
		this.animators = animators;
		this.expirationDate = expirationDate;
		this.deletedDate = deletedDate;
		this.expired = expired;
		this.lastContributor = lastContributor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAnimators() {
		return animators;
	}

	public void setAnimators(List<String> animators) {
		this.animators = animators;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public String getLastContributor() {
		return lastContributor;
	}

	public void setLastContributor(String lastContributor) {
		this.lastContributor = lastContributor;
	}

	
}
