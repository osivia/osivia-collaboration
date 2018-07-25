package org.osivia.services.calendar.view.portlet.model;

/**
 * Calendar configuration.
 *
 * @author Cédric Krommenhoek
 */
public class CalendarOptions {

    /** CMS path. */
    private String cmsPath;
    /** Compact view indicator. */
    private boolean compactView;
    /** Default period type name. */
    private String periodTypeName;
    /** Read only indicator. */
    private boolean readOnly;


    /**
     * Default constructor.
     */
    public CalendarOptions() {
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

    /**
     * Getter for readOnly.
     * 
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Setter for readOnly.
     * 
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
