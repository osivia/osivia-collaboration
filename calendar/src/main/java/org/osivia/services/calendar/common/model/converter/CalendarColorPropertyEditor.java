package org.osivia.services.calendar.common.model.converter;

import java.beans.PropertyEditorSupport;

import org.osivia.services.calendar.common.model.CalendarColor;
import org.osivia.services.calendar.common.model.ICalendarColor;
import org.springframework.stereotype.Component;

/**
 * Calendar color property editor.
 * 
 * @author Cédric Krommenhoek
 * @see PropertyEditorSupport
 */
@Component
public class CalendarColorPropertyEditor extends PropertyEditorSupport {

    /**
     * Constructor.
     */
    public CalendarColorPropertyEditor() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        ICalendarColor color = CalendarColor.fromId(text);

        this.setValue(color);
    }

}
