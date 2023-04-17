package org.osivia.services.editor.link.portlet.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Editor link form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public class EditorLinkForm {

    /**
     * URL.
     */
    private String url;
    /**
     * Text.
     */
    private String text;
    /**
     * Title.
     */
    private String title;
    /**
     * Display text indicator.
     */
    private boolean displayText;
    /**
     * Force open in new window indicator.
     */
    private boolean forceNewWindow;

    /**
     * Done indicator.
     */
    private boolean done;
    /**
     * Loaded indicator.
     */
    private boolean loaded;


    /**
     * Constructor.
     */
    public EditorLinkForm() {
        super();
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDisplayText() {
        return displayText;
    }

    public void setDisplayText(boolean displayText) {
        this.displayText = displayText;
    }

    public boolean isForceNewWindow() {
        return forceNewWindow;
    }

    public void setForceNewWindow(boolean forceNewWindow) {
        this.forceNewWindow = forceNewWindow;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
