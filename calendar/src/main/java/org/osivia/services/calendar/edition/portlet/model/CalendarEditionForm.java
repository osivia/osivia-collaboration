package org.osivia.services.calendar.edition.portlet.model;

import java.util.List;

import org.osivia.services.calendar.common.model.AbstractCalendarEditionForm;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar edition form java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractCalendarEditionForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarEditionForm extends AbstractCalendarEditionForm {

    /** Description. */
    private String description;
    /** Vignette. */
    private Picture vignette;
    /** Synchronization sources. */
    private List<CalendarSynchronizationSource> synchronizationSources;
    /** Added synchronization source. */
    private CalendarSynchronizationSource addedSynchronizationSource;


    /**
     * Constructor.
     */
    public CalendarEditionForm() {
        super();
    }

    @Override
	public CalendarColor getColor() {
		return (CalendarColor) color;
	}

	public void setColor(CalendarColor color) {
		this.color = color;
	}

    /**
     * Getter for description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for vignette.
     * 
     * @return the vignette
     */
    public Picture getVignette() {
        return vignette;
    }

    /**
     * Setter for vignette.
     * 
     * @param vignette the vignette to set
     */
    public void setVignette(Picture vignette) {
        this.vignette = vignette;
    }

    /**
     * Getter for synchronizationSources.
     * 
     * @return the synchronizationSources
     */
    public List<CalendarSynchronizationSource> getSynchronizationSources() {
        return synchronizationSources;
    }

    /**
     * Setter for synchronizationSources.
     * 
     * @param synchronizationSources the synchronizationSources to set
     */
    public void setSynchronizationSources(List<CalendarSynchronizationSource> synchronizationSources) {
        this.synchronizationSources = synchronizationSources;
    }

    /**
     * Getter for addedSynchronizationSource.
     * 
     * @return the addedSynchronizationSource
     */
    public CalendarSynchronizationSource getAddedSynchronizationSource() {
        return addedSynchronizationSource;
    }

    /**
     * Setter for addedSynchronizationSource.
     * 
     * @param addedSynchronizationSource the addedSynchronizationSource to set
     */
    public void setAddedSynchronizationSource(CalendarSynchronizationSource addedSynchronizationSource) {
        this.addedSynchronizationSource = addedSynchronizationSource;
    }

}
