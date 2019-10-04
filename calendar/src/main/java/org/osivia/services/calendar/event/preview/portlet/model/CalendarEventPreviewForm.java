package org.osivia.services.calendar.event.preview.portlet.model;

import org.osivia.services.calendar.common.model.CalendarCommonEventForm;
import org.osivia.services.calendar.event.view.portlet.model.CalendarEventViewForm;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Calendar event preview form java-bean.
 *
 * @author Cédric Krommenhoek
 * @see CalendarEventViewForm
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CalendarEventPreviewForm extends CalendarEventViewForm {

    /**
     * Detail URL.
     */
    private String detailUrl;
    /**
     * Edit URL.
     */
    private String editUrl;


    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }
}
