package org.osivia.services.edition.portlet.model;

/**
 * Document edition form abstract super-class.
 *
 * @author Cédric Krommenhoek
 */
public abstract class AbstractDocumentEditionForm {

    /**
     * Window properties.
     */
    private DocumentEditionWindowProperties windowProperties;
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
     * Original title.
     */
    private String originalTitle;
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


    public DocumentEditionWindowProperties getWindowProperties() {
        return windowProperties;
    }

    public void setWindowProperties(DocumentEditionWindowProperties windowProperties) {
        this.windowProperties = windowProperties;
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

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
