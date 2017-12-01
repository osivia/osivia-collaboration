package org.osivia.services.calendar.view.portlet.model.events;

import java.util.Calendar;

public class EventKey implements Comparable<EventKey>{

	public final String uid;
	
	public final String idParentSource;
	
	public final Calendar startDateReccuringSource;
	
	public EventKey(String uid, String idParentSrc, Calendar cal) {
		this.uid = uid;
		this.idParentSource = idParentSrc;
		this.startDateReccuringSource = cal;
	}

	public String getUid() {
		return uid;
	}

	public Calendar getStartDateReccuringSource() {
		return startDateReccuringSource;
	}
	

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(EventKey other) {
        int result;
        if (this.equals(other)) {
            result = 0;
        } else {
            result = -1;
        }
        return result;
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 0;
       	result = ((this.uid == null) ? 0 : this.uid.hashCode());
       	result = (prime * result) + ((this.idParentSource == null) ? 0 : this.idParentSource.hashCode());
        result = (prime * result) + ((this.startDateReccuringSource == null) ? 0 : this.startDateReccuringSource.hashCode());
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        EventKey other = (EventKey) obj;
        if (this.uid == null) {
            if (other.uid != null) {
                return false;
            }
            else
            {
            	if (this.idParentSource == null) {
                    if (other.idParentSource != null) {
                        return false;
                    }
                } else if (!this.idParentSource.equals(other.idParentSource)) {
                    return false;
                } else {
                	if (this.startDateReccuringSource == null) {
                        if (other.startDateReccuringSource != null) {
                            return false;
                        }
                    } else if (!this.startDateReccuringSource.equals(other.startDateReccuringSource)) {
                        return false;
                    }
                }
            }
        } else if (!this.uid.equals(other.uid)) {
            return false;
        } else
        {
        	if (this.idParentSource == null) {
                if (other.idParentSource != null) {
                    return false;
                }
            } else if (!this.idParentSource.equals(other.idParentSource)) {
                return false;
            } else
            {
	        	if (this.startDateReccuringSource == null) {
	                if (other.startDateReccuringSource != null) {
	                    return false;
	                }
	            } else if (!this.startDateReccuringSource.equals(other.startDateReccuringSource)) {
	                return false;
	            }
            }
        }
        return true;
    }
}
