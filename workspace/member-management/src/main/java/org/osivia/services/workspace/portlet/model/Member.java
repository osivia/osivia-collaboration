package org.osivia.services.workspace.portlet.model;

import org.osivia.directory.v2.model.ext.WorkspaceMember;
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

    /** Editable. */
    private boolean editable;


    /**
     * Constructor.
     * 
     * @param workspaceMember workspace member
     */
    public Member(WorkspaceMember workspaceMember) {
        super(workspaceMember.getMember());
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
