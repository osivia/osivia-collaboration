package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;

import org.osivia.services.workspace.portlet.model.Task;
import org.springframework.stereotype.Component;

/**
 * Tasks comparator
 *
 * @author Cédric Krommenhoek
 */
@Component
public class TasksComparator implements Comparator<Task> {

    /**
     * Constructor.
     */
    public TasksComparator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(Task task1, Task task2) {
        int result;
        if ((task1 == null) || !task1.isActive()) {
            result = 1;
        } else if ((task2 == null) || !task2.isActive()) {
            result = -1;
        } else {
            Integer order1 = task1.getOrder();
            Integer order2 = task2.getOrder();
            result = order1.compareTo(order2);
        }
        return result;
    }

}
