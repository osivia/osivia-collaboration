package org.osivia.services.taskbar.common.model;

import java.util.List;

/**
 * Taskbar configuration java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class TaskbarConfiguration {

    /** Tasks order. */
    private List<String> order;
    /** Taskbar view. */
    private TaskbarView view;


    /**
     * Constructor.
     */
    public TaskbarConfiguration() {
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
