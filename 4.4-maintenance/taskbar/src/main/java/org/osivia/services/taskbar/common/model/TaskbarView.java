package org.osivia.services.taskbar.common.model;

/**
 * Taskbar views enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum TaskbarView {

    /** Normal view. */
    NORMAL("normal", "view-normal", "TASKBAR_VIEW_NORMAL"),
    /** Compact view. */
    COMPACT("compact", "view-compact", "TASKBAR_VIEW_COMPACT");


    /** Default taskbar view. */
    private static final TaskbarView DEFAULT = NORMAL;


    /** View name. */
    private final String name;
    /** JSP path. */
    private final String path;
    /** Internationalization key. */
    private final String key;


    /**
     * Constructor.
     *
     * @param name view name
     * @param path JSP path
     * @param key internationalization key
     */
    private TaskbarView(String name, String path, String key) {
        this.name = name;
        this.path = path;
        this.key = key;
    }


    /**
     * Get taskbar view from name.
     *
     * @param name view name
     * @return taskbar view
     */
    public static TaskbarView fromName(String name) {
        TaskbarView result = DEFAULT;
        for (TaskbarView view : TaskbarView.values()) {
            if (view.name.equals(name)) {
                result = view;
                break;
            }
        }
        return result;
    }


    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for path.
     *
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for key.
     *
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

}
