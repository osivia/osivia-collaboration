package org.osivia.services.workspace.portlet.model.converter;

import java.beans.PropertyEditorSupport;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.services.workspace.portlet.model.Role;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Role property editor.
 * 
 * @author Cédric Krommenhoek
 * @see PropertyEditorSupport
 * @see ApplicationContextAware
 */
@Component
public class RolePropertyEditor extends PropertyEditorSupport implements ApplicationContextAware {

    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public RolePropertyEditor() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        WorkspaceRole workspaceRole = WorkspaceRole.fromId(text);
        Role role = this.applicationContext.getBean(Role.class, workspaceRole);
        this.setValue(role);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
