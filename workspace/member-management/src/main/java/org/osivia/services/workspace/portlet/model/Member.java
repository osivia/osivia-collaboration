package org.osivia.services.workspace.portlet.model;

import java.util.Date;

import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Member java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see MemberObject
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Member extends MemberObject {

    /** Role. */
    private WorkspaceRole role;
    /** Date. */
    private Date date;
    /** Editable. */
    private boolean editable;


    /**
     * Constructor.
     * 
     * @param workspaceMember workspace member
     */
    public Member(WorkspaceMember workspaceMember) {
        super(workspaceMember.getMember());
        this.role = workspaceMember.getRole();
    }


    /**
     * Getter for role.
     * 
     * @return the role
     */
    public WorkspaceRole getRole() {
        return role;
    }

    /**
     * Setter for role.
     * 
     * @param role the role to set
     */
    public void setRole(WorkspaceRole role) {
        this.role = role;
    }

    /**
     * Getter for date.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for date.
     * 
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }


    /**
     * Getter for editable.
     * 
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Setter for editable.
     * 
     * @param editable the editable to set
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
