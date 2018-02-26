package org.osivia.services.statistics.portlet.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Statistics form java-bean
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StatisticsForm {

    /** Creations view. */
    private CreationsView creationsView;
    /** Visits view. */
    private VisitsView visitsView;

    /** Creations views. */
    private final List<CreationsView> creationsViews;
    /** Visits views. */
    private final List<VisitsView> visitsViews;


    /**
     * Constructor.
     */
    public StatisticsForm() {
        super();
        this.creationsViews = Arrays.asList(CreationsView.values());
        this.visitsViews = Arrays.asList(VisitsView.values());
    }


    /**
     * Getter for creationsView.
     * 
     * @return the creationsView
     */
    public CreationsView getCreationsView() {
        return creationsView;
    }

    /**
     * Setter for creationsView.
     * 
     * @param creationsView the creationsView to set
     */
    public void setCreationsView(CreationsView creationsView) {
        this.creationsView = creationsView;
    }

    /**
     * Getter for visitsView.
     * 
     * @return the visitsView
     */
    public VisitsView getVisitsView() {
        return visitsView;
    }

    /**
     * Setter for visitsView.
     * 
     * @param visitsView the visitsView to set
     */
    public void setVisitsView(VisitsView visitsView) {
        this.visitsView = visitsView;
    }

    /**
     * Getter for creationsViews.
     * 
     * @return the creationsViews
     */
    public List<CreationsView> getCreationsViews() {
        return creationsViews;
    }

    /**
     * Getter for visitsViews.
     * 
     * @return the visitsViews
     */
    public List<VisitsView> getVisitsViews() {
        return visitsViews;
    }

}
