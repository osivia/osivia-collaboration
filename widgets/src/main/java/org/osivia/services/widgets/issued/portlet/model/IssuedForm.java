package org.osivia.services.widgets.issued.portlet.model;

import java.util.Date;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Issued date form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IssuedForm {

    /** Date. */
    private Date date;


    /**
     * Constructor.
     */
    public IssuedForm() {
        super();
    }


    /**
     * Getter for date.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for date.
     * 
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

}
