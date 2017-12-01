package org.osivia.services.calendar.view.portlet.model.events;

import java.util.Calendar;

public class EventToSync {

	private String id;
	
	private final String title;
	
	private final boolean allDay;
	
	private final Calendar startCal;
	
	private final Calendar endCal;
	
	private final String description;
	
	private final String idAgendaSrc;
	
	private final String idEventSrc;
	
	private final Calendar createdCalSrc;
	
	private final Calendar lastModifiedSource;
	
	private final Calendar startReccuringCalSrc;
	
	public EventToSync(String id, String title, boolean allDay, Calendar start, Calendar end, String desc,
			String idAgendaSrc, String idEventSrc, Calendar createdSrc, Calendar lastModifiedSrc, Calendar startReccuring) {
		this.id = id;
		this.title = title;
		this.allDay = allDay;
		this.startCal = start;
		this.endCal = end;
		this.description = desc;
		this.idAgendaSrc = idAgendaSrc;
		this.idEventSrc = idEventSrc;
		this.createdCalSrc = createdSrc;
		this.lastModifiedSource = lastModifiedSrc;
		this.startReccuringCalSrc = startReccuring;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public boolean isAllDay() {
		return allDay;
	}

	public Calendar getStartCal() {
		return startCal;
	}

	public Calendar getEndCal() {
		return endCal;
	}

	public String getDescription() {
		return description;
	}

	public String getIdAgendaSource() {
		return idAgendaSrc;
	}

	public String getIdEventSource() {
		return idEventSrc;
	}

	public Calendar getCreateCalSource() {
		return createdCalSrc;
	}

	public Calendar getLastModifiedSource() {
		return lastModifiedSource;
	}

	public Calendar getStartReccuringCalSource() {
		return startReccuringCalSrc;
	}
}
