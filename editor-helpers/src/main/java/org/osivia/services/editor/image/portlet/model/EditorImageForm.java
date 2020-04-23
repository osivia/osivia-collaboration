package org.osivia.services.editor.image.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Editor image form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EditorImageForm {

    /**
     * URL.
     */
    private String url;
    /**
     * Alternate text.
     */
    private String alt;
    /**
     * Source type.
     */
    private ImageSourceType sourceType;

    /**
     * Done indicator.
     */
    private boolean done;


    /**
     * Constructor.
     */
    public EditorImageForm() {
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public ImageSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(ImageSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
