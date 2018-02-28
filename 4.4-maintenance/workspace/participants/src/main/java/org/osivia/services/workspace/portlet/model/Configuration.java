package org.osivia.services.workspace.portlet.model;

import java.util.Map;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Portlet configuration java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Configuration {

    /** View. */
    private View view;
    /** Display. */
    private Map<WorkspaceRole, Boolean> display;
    /** Max. */
    private int max;


    /**
     * {@inheritDoc}
     */
    public Configuration() {
        super();
    }


    /**
     * Getter for view.
     * 
     * @return the view
     */
    public View getView() {
        return view;
    }

    /**
     * Setter for view.
     * 
     * @param view the view to set
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Getter for display.
     * 
     * @return the display
     */
    public Map<WorkspaceRole, Boolean> getDisplay() {
        return display;
    }

    /**
     * Setter for display.
     * 
     * @param display the display to set
     */
    public void setDisplay(Map<WorkspaceRole, Boolean> display) {
        this.display = display;
    }

    /**
     * Getter for max.
     * 
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Setter for max.
     * 
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

}
