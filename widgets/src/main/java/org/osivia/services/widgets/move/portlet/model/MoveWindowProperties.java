package org.osivia.services.widgets.move.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Move portlet window properties java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MoveWindowProperties {

    /**
     * Document path.
     */
    private String path;
    /**
     * Document identifiers.
     */
    private List<String> identifiers;
    /**
     * Ignored paths.
     */
    private List<String> ignoredPaths;
    /**
     * Base path.
     */
    private String basePath;
    /**
     * Accepted types.
     */
    private List<String> acceptedTypes;


    /**
     * Constructor.
     */
    public MoveWindowProperties() {
        super();
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public List<String> getIgnoredPaths() {
        return ignoredPaths;
    }

    public void setIgnoredPaths(List<String> ignoredPaths) {
        this.ignoredPaths = ignoredPaths;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<String> getAcceptedTypes() {
        return acceptedTypes;
    }

    public void setAcceptedTypes(List<String> acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
    }

}
