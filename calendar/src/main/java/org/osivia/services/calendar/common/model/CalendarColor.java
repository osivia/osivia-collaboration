package org.osivia.services.calendar.common.model;

import org.apache.commons.lang.StringUtils;

/**
 * Calendar colors enumeration.
 * 
 * @author Cédric Krommenhoek
 */
public enum CalendarColor {

    /** Red tomato. */
    RED_TOMATO,
    /** Light pink. */
    LIGHT_PINK,
    /** Mandarin. */
    MANDARIN,
    /** Yellow banana. */
    YELLOW_BANANA,
    /** Sage green. */
    SAGE_GREEN,
    /** Green basil. */
    GREEN_BASIL,
    /** Peacock blue. */
    PEACOCK_BLUE,
    /** Blueberry. */
    BLUEBERRY,
    /** Lavender. */
    LAVENDER,
    /** Purple grape. */
    PURPLE_GRAPE,
    /** Anthracite. */
    ANTHRACITE,
    /** Default color (primary). */
    PRIMARY;


    /** Default calendar color. */
    public static final CalendarColor DEFAULT = PRIMARY;


    /** Internationalization key prefix. */
    private static final String KEY_PREFIX = "CALENDAR_COLOR_";
    /** Text color HTML class prefix. */
    private static final String TEXT_CLASS_PREFIX = "text-";
    /** Background color HTML class prefix. */
    private static final String BACKGROUND_CLASS_PREFIX = "bg-";


    /** Color identifier. */
    private final String id;
    /** Color internationalization key. */
    private final String key;
    /** Text color HTML class. */
    private final String textClass;
    /** Background color HTML class. */
    private final String backgroundClass;


    /**
     * Constructor.
     */
    private CalendarColor() {
        this.id = StringUtils.upperCase(this.name());
        this.key = KEY_PREFIX + this.id;
        this.textClass = this.getHtmlClass(TEXT_CLASS_PREFIX, this.id);
        this.backgroundClass = this.getHtmlClass(BACKGROUND_CLASS_PREFIX, this.id);
    }


    /**
     * Get color from identifier.
     * 
     * @param id color identifier
     * @return color
     */
    public static CalendarColor fromId(String id) {
        CalendarColor result = DEFAULT;

        for (CalendarColor value : CalendarColor.values()) {
            if (StringUtils.equalsIgnoreCase(id, value.id)) {
                result = value;
                break;
            }
        }
        
        return result;
    }


    /**
     * Get color HTML class.
     * 
     * @param prefix HTML class prefix
     * @param id color identifier
     * @return HTML class
     */
    private String getHtmlClass(String prefix, String id) {
        return prefix + StringUtils.replace(StringUtils.lowerCase(id), "_", "-");
    }


    /**
     * Getter for id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Getter for textClass.
     * 
     * @return the textClass
     */
    public String getTextClass() {
        return textClass;
    }

    /**
     * Getter for backgroundClass.
     * 
     * @return the backgroundClass
     */
    public String getBackgroundClass() {
        return backgroundClass;
    }

}
