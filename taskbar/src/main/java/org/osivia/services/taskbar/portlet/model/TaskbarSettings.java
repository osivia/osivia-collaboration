package org.osivia.services.taskbar.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Taskbar settings java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskbarSettings {

    /** Tasks order. */
    private List<String> order;
    /** Taskbar view. */
    private TaskbarView view;


    /**
     * Constructor.
     */
    public TaskbarSettings() {
        super();
    }


    /**
     * Getter for order.
     *
     * @return the order
     */
    public List<String> getOrder() {
        return this.order;
    }

    /**
     * Setter for order.
     *
     * @param order the order to set
     */
    public void setOrder(List<String> order) {
        this.order = order;
    }

    /**
     * Getter for view.
     *
     * @return the view
     */
    public TaskbarView getView() {
        return this.view;
    }

    /**
     * Setter for view.
     *
     * @param view the view to set
     */
    public void setView(TaskbarView view) {
        this.view = view;
    }

}
