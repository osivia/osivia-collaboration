package org.osivia.services.calendar.common.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Calendar options java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component("options")
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CalendarOptions {

    /** Loaded indicator. */
    private boolean loaded;


    /** Colors. */
    private final List<CalendarColor> colors;


    /**
     * Constructor.
     */
    public CalendarOptions() {
        super();
        this.colors = Arrays.asList(CalendarColor.values());
    }


    /**
     * Getter for loaded.
     * 
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Setter for loaded.
     * 
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Getter for colors.
     * 
     * @return the colors
     */
    public List<CalendarColor> getColors() {
        return colors;
    }

}
