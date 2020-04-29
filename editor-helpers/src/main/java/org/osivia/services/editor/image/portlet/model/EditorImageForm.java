package org.osivia.services.editor.image.portlet.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * Editor image form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
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
     * Available image source types.
     */
    private List<ImageSourceType> availableSourceTypes;
    /**
     * Loaded indicator.
     */
    private boolean loaded;


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

    public List<ImageSourceType> getAvailableSourceTypes() {
        return availableSourceTypes;
    }

    public void setAvailableSourceTypes(List<ImageSourceType> availableSourceTypes) {
        this.availableSourceTypes = availableSourceTypes;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
