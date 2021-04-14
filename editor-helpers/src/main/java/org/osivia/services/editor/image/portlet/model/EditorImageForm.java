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
     * Height.
     */
    private Integer height;
    /**
     * Width.
     */
    private Integer width;

    /**
     * Temporary attached image.
     */
    private TemporaryAttachedImage temporaryAttachedImage;
    /**
     * Temporary image path.
     */
    private String temporaryImagePath;
    /**
     * Temporary image URL.
     */
    private String temporaryUrl;

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
        super();
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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public TemporaryAttachedImage getTemporaryAttachedImage() {
        return temporaryAttachedImage;
    }

    public void setTemporaryAttachedImage(TemporaryAttachedImage temporaryAttachedImage) {
        this.temporaryAttachedImage = temporaryAttachedImage;
    }

    public String getTemporaryImagePath() {
        return temporaryImagePath;
    }

    public void setTemporaryImagePath(String temporaryImagePath) {
        this.temporaryImagePath = temporaryImagePath;
    }

    public String getTemporaryUrl() {
        return temporaryUrl;
    }

    public void setTemporaryUrl(String temporaryUrl) {
        this.temporaryUrl = temporaryUrl;
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
