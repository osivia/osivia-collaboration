package org.osivia.services.statistics.portlet.model;

/**
 * Statistics view enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum StatisticsView {

    /** Differential view. */
    DIFFERENTIAL,
    /** Aggregate view. */
    AGGREGATE;


    /** Default view. */
    public static final StatisticsView DEFAULT = DIFFERENTIAL;
    

    /** View value. */
    private final String value;
    /** Internationalization label key. */
    private final String key;


    /**
     * Constructor.
     */
    private StatisticsView() {
        this.value = this.name();
        this.key = "VIEW_" + this.name();
    }

    
    /**
     * Get view from his value.
     * 
     * @param value view value
     * @return view
     */
    public static StatisticsView fromValue(String value) {
        StatisticsView result = DEFAULT;

        for (StatisticsView view : StatisticsView.values()) {
            if (view.value.equals(value)) {
                result = view;
                break;
            }
        }
        
        return result;
    }
    

    /**
     * Getter for value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

}
