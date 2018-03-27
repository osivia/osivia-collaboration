package org.osivia.services.calendar.view.portlet.model;

import java.util.Date;

import org.nuxeo.ecm.automation.client.model.DocRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar view form java-bean.
 *
 * @author Julien Barberet
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarViewForm {

    /** Dod id (Nuxeo) */
    private String docid;

    /** End date */
    private Date endDate;
    
    /** Start date */
    private Date startDate;
    
    private String title;
    
    /** Calendar edition mode. */
    private CalendarEditionMode mode;
    /** Calendar document, may be null. */
    private DocRef document;
    /** Calendar parent path. */
    private String parentPath;


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getDocId() {
		return docid;
	}


	public void setDocId(String docid) {
		this.docid = docid;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public CalendarEditionMode getMode() {
		return mode;
	}


	public void setMode(CalendarEditionMode mode) {
		this.mode = mode;
	}


	public DocRef getDocument() {
		return document;
	}


	public void setDocument(DocRef document) {
		this.document = document;
	}


	public String getParentPath() {
		return parentPath;
	}


	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

}
