package org.osivia.services.workspace.edition.portlet.model.converter;

import java.beans.PropertyEditorSupport;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Title property editor.
 * 
 * @author Cédric Krommenhoek
 * @see PropertyEditorSupport
 */
@Component
public class TitlePropertyEditor extends PropertyEditorSupport {

    /** Whitespaces regex. */
    private static final String REGEX = "\\s\\s";


    /** Whitespaces pattern. */
    private final Pattern pattern;


    /**
     * Constructor.
     */
    public TitlePropertyEditor() {
        super();
        this.pattern = Pattern.compile(REGEX);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String value = StringUtils.trimToEmpty(text);

        // Matcher
        Matcher matcher = this.pattern.matcher(value);
        while (matcher.find()) {
            value = matcher.replaceAll(" ");
            matcher = this.pattern.matcher(value);
        }

        this.setValue(value);
    }

}
