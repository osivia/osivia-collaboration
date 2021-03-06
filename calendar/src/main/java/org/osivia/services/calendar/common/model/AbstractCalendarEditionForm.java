package org.osivia.services.calendar.common.model;

/**
 * Calendar edition form abstract super-class.
 * 
 * @author Cédric Krommenhoek
 */
public abstract class AbstractCalendarEditionForm {

    /** Title. */
    private String title;
    /** Color. */
    protected Enum color;


    /**
     * Constructor.
     */
    public AbstractCalendarEditionForm() {
        super();
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
     * Getter for color.
     * 
     * @return the color
     */
    public Enum getColor() {
        return color;
    }

    /**
     * Setter for color.
     * 
     * @param color the color to set
     */
    public void setColor(Enum color) {
        this.color = color;
    }

}
