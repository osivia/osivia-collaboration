package org.osivia.services.statistics.model;

/**
 * Statistics configuration java-bean.
 *
 * @author Cédric Krommenhoek
 */
public class StatisticsConfiguration {

    /** Periods number. */
    private int number;
    /** Request. */
    private String request;
    /** Version. */
    private StatisticsVersion version;


    /**
     * Constructor.
     */
    public StatisticsConfiguration() {
        super();
    }


    /**
     * Getter for number.
     *
     * @return the number
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Setter for number.
     *
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Getter for request.
     *
     * @return the request
     */
    public String getRequest() {
        return this.request;
    }

    /**
     * Setter for request.
     *
     * @param request the request to set
     */
    public void setRequest(String request) {
        this.request = request;
    }

    /**
     * Getter for version.
     * 
     * @return the version
     */
    public StatisticsVersion getVersion() {
        return this.version;
    }

    /**
     * Setter for version.
     * 
     * @param version the version to set
     */
    public void setVersion(StatisticsVersion version) {
        this.version = version;
    }

}
