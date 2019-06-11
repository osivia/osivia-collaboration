package org.osivia.services.widgets.issued.portlet.model.converter;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

/**
 * Date property editor.
 * 
 * @author Cédric Krommenhoek
 * @see PropertyEditorSupport
 */
@Component
public class DatePropertyEditor extends PropertyEditorSupport {

    /** Date format. */
    private final DateFormat dateFormat;


    /**
     * Constructor.
     */
    public DatePropertyEditor() {
        super();

        // Date format
        this.dateFormat = new SimpleDateFormat(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
        this.dateFormat.setTimeZone(DateUtils.UTC_TIME_ZONE);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        String result;

        Object value = this.getValue();
        if (value instanceof Date) {
            Date date = (Date) value;
            result = this.dateFormat.format(date);
        } else {
            result = super.getAsText();
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            Date date = this.dateFormat.parse(text);
            this.setValue(date);
        } catch (ParseException e) {
            this.setValue(null);
        }
    }

}
