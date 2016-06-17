package org.osivia.services.workspace.portlet.model.comparator;

import java.util.Comparator;

import org.osivia.services.workspace.portlet.model.Task;
import org.springframework.stereotype.Component;

/**
 * Tasks comparator
 *
 * @author Cédric Krommenhoek
 * @see Comparator
 * @see Task
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
        if (task1 == null) {
            result = 1;
        } else if (task2 == null) {
            result = -1;
        } else if (task1.isActive() && task2.isActive()) {
            // Compare orders
            Integer order1 = task1.getOrder();
            Integer order2 = task2.getOrder();
            result = order1.compareTo(order2);
        } else if (task1.isActive()) {
            result = -1;
        } else if (task2.isActive()) {
            result = 1;
        } else {
            // Compare display names
            String displayName1 = task1.getDisplayName();
            String displayName2 = task2.getDisplayName();
            result = displayName1.compareToIgnoreCase(displayName2);
        }
        return result;
    }

}
