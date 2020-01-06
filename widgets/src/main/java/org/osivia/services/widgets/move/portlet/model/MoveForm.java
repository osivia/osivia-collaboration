package org.osivia.services.widgets.move.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Move portlet form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MoveForm {

    /**
     * Target path.
     */
    private String targetPath;

    /**
     * Base path.
     */
    private String basePath;
    /**
     * Navigation path.
     */
    private String navigationPath;
    /**
     * Ignored paths.
     */
    private String[] ignoredPaths;
    /**
     * Accepted types.
     */
    private String[] acceptedTypes;
    /**
     * Excluded types.
     */
    private String[] excludedTypes;


    /**
     * Constructor.
     */
    public MoveForm() {
        super();
    }


    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getNavigationPath() {
        return navigationPath;
    }

    public void setNavigationPath(String navigationPath) {
        this.navigationPath = navigationPath;
    }

    public String[] getIgnoredPaths() {
        return ignoredPaths;
    }

    public void setIgnoredPaths(String[] ignoredPaths) {
        this.ignoredPaths = ignoredPaths;
    }

    public String[] getAcceptedTypes() {
        return acceptedTypes;
    }

    public void setAcceptedTypes(String[] acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
    }

    public String[] getExcludedTypes() {
        return excludedTypes;
    }

    public void setExcludedTypes(String[] excludedTypes) {
        this.excludedTypes = excludedTypes;
    }

}
