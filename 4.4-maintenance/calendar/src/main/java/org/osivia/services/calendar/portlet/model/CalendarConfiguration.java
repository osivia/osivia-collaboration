package org.osivia.services.calendar.portlet.model;

/**
 * Calendar configuration.
 *
 * @author Cédric Krommenhoek
 */
public class CalendarConfiguration {

    /** CMS path. */
    private String cmsPath;
    /** Compact view indicator. */
    private boolean compactView;
    /** Default period type name. */
    private String periodTypeName;


    /**
     * Default constructor.
     */
    public CalendarConfiguration() {
        super();
    }


    /**
     * Getter for cmsPath.
     *
     * @return the cmsPath
     */
    public String getCmsPath() {
        return this.cmsPath;
    }

    /**
     * Setter for cmsPath.
     *
     * @param cmsPath the cmsPath to set
     */
    public void setCmsPath(String cmsPath) {
        this.cmsPath = cmsPath;
    }

    /**
     * Getter for compactView.
     *
     * @return the compactView
     */
    public boolean isCompactView() {
        return this.compactView;
    }

    /**
     * Setter for compactView.
     *
     * @param compactView the compactView to set
     */
    public void setCompactView(boolean compactView) {
        this.compactView = compactView;
    }

    /**
     * Getter for periodTypeName.
     *
     * @return the periodTypeName
     */
    public String getPeriodTypeName() {
        return this.periodTypeName;
    }

    /**
     * Setter for periodTypeName.
     *
     * @param periodTypeName the periodTypeName to set
     */
    public void setPeriodTypeName(String periodTypeName) {
        this.periodTypeName = periodTypeName;
    }

}
