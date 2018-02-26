package org.osivia.services.statistics.portlet.model;

/**
 * Statistics window settings java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class StatisticsWindowSettings {

    /** Creations months. */
    private int creationsMonths;
    /** Creations request. */
    private String creationsRequest;
    /** Creations rersion. */
    private StatisticsVersion creationsVersion;
    /** Visits days. */
    private int visitsDays;
    /** Visits months. */
    private int visitsMonths;


    /**
     * Constructor.
     */
    public StatisticsWindowSettings() {
        super();
    }


    /**
     * Getter for creationsMonths.
     * @return the creationsMonths
     */
    public int getCreationsMonths() {
        return creationsMonths;
    }

    /**
     * Setter for creationsMonths.
     * @param creationsMonths the creationsMonths to set
     */
    public void setCreationsMonths(int creationsMonths) {
        this.creationsMonths = creationsMonths;
    }

    /**
     * Getter for creationsRequest.
     * @return the creationsRequest
     */
    public String getCreationsRequest() {
        return creationsRequest;
    }

    /**
     * Setter for creationsRequest.
     * @param creationsRequest the creationsRequest to set
     */
    public void setCreationsRequest(String creationsRequest) {
        this.creationsRequest = creationsRequest;
    }

    /**
     * Getter for creationsVersion.
     * @return the creationsVersion
     */
    public StatisticsVersion getCreationsVersion() {
        return creationsVersion;
    }

    /**
     * Setter for creationsVersion.
     * @param creationsVersion the creationsVersion to set
     */
    public void setCreationsVersion(StatisticsVersion creationsVersion) {
        this.creationsVersion = creationsVersion;
    }

    /**
     * Getter for visitsDays.
     * 
     * @return the visitsDays
     */
    public int getVisitsDays() {
        return visitsDays;
    }

    /**
     * Setter for visitsDays.
     * 
     * @param visitsDays the visitsDays to set
     */
    public void setVisitsDays(int visitsDays) {
        this.visitsDays = visitsDays;
    }

    /**
     * Getter for visitsMonths.
     * 
     * @return the visitsMonths
     */
    public int getVisitsMonths() {
        return visitsMonths;
    }

    /**
     * Setter for visitsMonths.
     * 
     * @param visitsMonths the visitsMonths to set
     */
    public void setVisitsMonths(int visitsMonths) {
        this.visitsMonths = visitsMonths;
    }

}
