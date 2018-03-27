package org.osivia.services.calendar.event.view.portlet.model;

import java.util.Date;

import org.osivia.portal.api.portlet.Refreshable;
import org.osivia.services.calendar.common.model.CalendarCommonEventForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class CalendarEventViewForm extends CalendarCommonEventForm {

	private boolean sameDay;
	
	private Date endDateAllDay;
	
	private DocumentDTO document;
	
	public CalendarEventViewForm() {
		super();
	}

	public boolean isSameDay() {
		return sameDay;
	}

	public void setSameDay(boolean sameDay) {
		this.sameDay = sameDay;
	}

	public Date getEndDateAllDay() {
		return endDateAllDay;
	}

	public void setEndDateAllDay(Date endDayAllDay) {
		this.endDateAllDay = endDayAllDay;
	}

	/**
	 * @return the document
	 */
	public DocumentDTO getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(DocumentDTO document) {
		this.document = document;
	}

}
