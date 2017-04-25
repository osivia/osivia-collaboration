package org.osivia.services.statistics.portlet.model;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Statistics version enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum StatisticsVersion {

    /** Live version. */
    LIVE("live", true, "Version live"),
    /** published version. */
    PUBLISHED("published", false, "Version publiée");


    /** Default statistics version. */
    public static final StatisticsVersion DEFAULT = LIVE;


    /** Name. */
    private final String name;
    /** Nuxeo query filter context. */
    private final NuxeoQueryFilterContext filter;
    /** Label. */
    private final String label;


    /**
     * Constructor.
     *
     * @param name name
     * @param live live version indicator
     */
    private StatisticsVersion(String name, boolean live, String label) {
        // Name
        this.name = name;

        // Filter
        String version = BooleanUtils.toString(live, "1", "0");
        int state = NumberUtils.toInt(version);
        this.filter = new NuxeoQueryFilterContext(state);

        // Label
        this.label = label;
    }


    /**
     * Get statistics version from name.
     *
     * @param name version name
     * @return version
     */
    public static StatisticsVersion fromName(String name) {
        StatisticsVersion result = DEFAULT;

        for (StatisticsVersion version : StatisticsVersion.values()) {
            if (version.name.equals(name)) {
                result = version;
                break;
            }
        }

        return result;
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
     * Getter for filter.
     *
     * @return the filter
     */
    public NuxeoQueryFilterContext getFilter() {
        return this.filter;
    }

    /**
     * Getter for label.
     *
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

}
