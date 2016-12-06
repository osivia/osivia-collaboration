package org.osivia.services.workspace.portlet.model;

/**
 * Object document java-bean.
 * 
 * @author Cédric Krommenhoek
 */
public abstract class ObjectDocument {

    /** Document title. */
    private String title;
    /** Document type icon. */
    private String icon;


    /** Document path. */
    private final String path;


    /**
     * Constructor.
     * 
     * @param path document path
     */
    public ObjectDocument(String path) {
        super();
        this.path = path;
    }


    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Setter for icon.
     * 
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

}
