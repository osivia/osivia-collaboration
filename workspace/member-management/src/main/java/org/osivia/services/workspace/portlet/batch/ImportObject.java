package org.osivia.services.workspace.portlet.batch;

import java.io.File;
import java.util.List;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Import form java-bean
 * 
 * @author Loïc Billon
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportObject  {
	
    /** Role. */
    private WorkspaceRole role;

    /** Local groups. */
    private List<CollabProfile> localGroups;
    
    /** Message. */
    private String message;

    /**
     * Temporary file.
     */
    private File temporaryFile;
    
    /**
     * Original file name
     */
    private String temporaryFileName;

    
    /**
     * Workspace identifier
     */
    private String workspaceId;
    
    /**
     * Batch initiator
     */
    private String initiator;
    
    private Document currentWorkspace;
    
	private int count, countreject, countinvitation, countalreadymember, countwf;

	/**
	 * Number of records to process before the end of the file (for tracking)
	 */
	private int recordNumber;


	public WorkspaceRole getRole() {
		return role;
	}

	public void setRole(WorkspaceRole role) {
		this.role = role;
	}

	public List<CollabProfile> getLocalGroups() {
		return localGroups;
	}

	public void setLocalGroups(List<CollabProfile> localGroups) {
		this.localGroups = localGroups;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public File getTemporaryFile() {
		return temporaryFile;
	}

	public void setTemporaryFile(File temporaryFile) {
		this.temporaryFile = temporaryFile;
	}
	
	

	public String getTemporaryFileName() {
		return temporaryFileName;
	}

	public void setTemporaryFileName(String temporaryFileName) {
		this.temporaryFileName = temporaryFileName;
	}

	public String getWorkspaceId() {
		return workspaceId;
	}

	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public Document getCurrentWorkspace() {
		return currentWorkspace;
	}

	public void setCurrentWorkspace(Document currentWorkspace) {
		this.currentWorkspace = currentWorkspace;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCountreject() {
		return countreject;
	}

	public void setCountreject(int countreject) {
		this.countreject = countreject;
	}

	public int getCountinvitation() {
		return countinvitation;
	}

	public void setCountinvitation(int countinvitation) {
		this.countinvitation = countinvitation;
	}

	public int getCountalreadymember() {
		return countalreadymember;
	}

	public void setCountalreadymember(int countalreadymember) {
		this.countalreadymember = countalreadymember;
	}

	public int getCountwf() {
		return countwf;
	}

	public void setCountwf(int countwf) {
		this.countwf = countwf;
	}
	

	public int getRecordNumber() {
		return recordNumber;
	}

	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;

	}


}
