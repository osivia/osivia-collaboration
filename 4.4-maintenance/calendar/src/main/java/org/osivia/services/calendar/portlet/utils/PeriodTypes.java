package org.osivia.services.calendar.portlet.utils;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;


/**
 * Calendar period types enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum PeriodTypes {

    /** Day. */
    DAY("day", Calendar.DAY_OF_MONTH, "calendar/view-day", false),
    /** Week. */
    WEEK("week", Calendar.WEEK_OF_YEAR, "calendar/view-week", false),
    /** Month. */
    MONTH("month", Calendar.MONTH, "calendar/view-month", false),
    /** Planning. */
    PLANNING("planning", -1, "calendar/view-planning", true);


    /** Period type name. */
    private final String name;
    /** Internationalization resource bundle key. */
    private final String internationalizationKey;
    /** Calendar field. */
    private final int field;
    /** View path. */
    private final String viewPath;
    /** Compactable indicator. */
    private final boolean compactable;


    /**
     * Constructor.
     *
     * @param name period type name
     * @param field calendar field
     * @param viewPath view path
     * @param compactable compactable indicator
     */
    private PeriodTypes(String name, int field, String viewPath, boolean compactable) {
        this.name = name;
        this.internationalizationKey = "CALENDAR_" + StringUtils.upperCase(name);
        this.field = field;
        this.viewPath = viewPath;
        this.compactable = compactable;
    }


    /**
     * Get period from his name.
     *
     * @param name period name
     * @return period
     */
    public static PeriodTypes fromName(String name) {
        PeriodTypes period = null;
        for (PeriodTypes value : PeriodTypes.values()) {
            if (value.getName().equals(name)) {
                period = value;
                break;
            }
        }
        if (period == null) {
            // Default value
            period = PeriodTypes.PLANNING;
        }
        return period;
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
     * Getter for internationalizationKey.
     *
     * @return the internationalizationKey
     */
    public String getInternationalizationKey() {
        return this.internationalizationKey;
    }

    /**
     * Getter for field.
     *
     * @return the field
     */
    public int getField() {
        return this.field;
    }

    /**
     * Getter for viewPath.
     *
     * @return the viewPath
     */
    public String getViewPath() {
        return this.viewPath;
    }

    /**
     * Getter for compactable.
     *
     * @return the compactable
     */
    public boolean isCompactable() {
        return this.compactable;
    }

}
