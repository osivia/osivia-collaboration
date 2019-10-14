package org.osivia.services.calendar.event.view.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.osivia.portal.api.portlet.Refreshable;
import org.osivia.services.calendar.common.model.CalendarCommonEventForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Calendar event view form java-bean.
 *
 * @author Cédric Krommenhoek
 * @see CalendarCommonEventForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Refreshable
public class CalendarEventViewForm extends CalendarCommonEventForm {

    /**
     * Same day indicator.
     */
    private boolean sameDay;
    /**
     * End date for all day.
     */
    private Date endDateAllDay;
    /**
     * Document DTO.
     */
    private DocumentDTO document;
    /**
     * Workspace indicator.
     */
    private boolean workspace;


    /**
     * Constructor.
     */
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

    public void setEndDateAllDay(Date endDateAllDay) {
        this.endDateAllDay = endDateAllDay;
    }

    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    public boolean isWorkspace() {
        return workspace;
    }

    public void setWorkspace(boolean workspace) {
        this.workspace = workspace;
    }
}
