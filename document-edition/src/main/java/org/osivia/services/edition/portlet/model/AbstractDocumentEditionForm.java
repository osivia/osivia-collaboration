package org.osivia.services.edition.portlet.model;

/**
 * Document edition form abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class AbstractDocumentEditionForm {

    /**
     * Name.
     */
    private String name;

    /**
     * Document path.
     */
    private String path;

    /**
     * Document creation indicator.
     */
    private boolean creation;

    /**
     * Title.
     */
    private String title;

    /**
     * Description.
     */
    private String description;


    /**
     * Constructor.
     */
    AbstractDocumentEditionForm() {
        super();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCreation() {
        return creation;
    }

    public void setCreation(boolean creation) {
        this.creation = creation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
