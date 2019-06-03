package org.osivia.services.workspace.quota.portlet.model;


public class QuotaItem {

    /** The source. */
    private String source;
    /** The value. */
    private long value;
    
    /**
     * Getter for source.
     * @return the source
     */
    public String getSource() {
        return source;
    }
    
    /**
     * Setter for source.
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }
    
    /**
     * Getter for value.
     * @return the value
     */
    public long getValue() {
        return value;
    }
    
    /**
     * Setter for value.
     * @param value the value to set
     */
    public void setValue(long value) {
        this.value = value;
    }


}
