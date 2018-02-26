package fr.toutatice.collaboratif.purgeworkspaces.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Purge workspace form
 * @author jbarberet
 *
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class PurgeWorkspaceForm {

	/** List of workspace */
	private List<WorkspaceLine> list;
	
	/** Concatenation of id of workspace to put in bin */
	private String selectResult;
	
	private boolean displayPurgeButton;

	/**
	 * Constructor
	 */
	public PurgeWorkspaceForm() {
		super();
	}

	public List<WorkspaceLine> getList() {
		return list;
	}

	public void setList(List<WorkspaceLine> list) {
		this.list = list;
	}

	public String getSelectResult() {
		return selectResult;
	}

	public void setSelectResult(String selectResult) {
		this.selectResult = selectResult;
	}

	public boolean isDisplayPurgeButton() {
		return displayPurgeButton;
	}

	public void setDisplayPurgeButton(boolean displayPurgeButton) {
		this.displayPurgeButton = displayPurgeButton;
	}
	
}
