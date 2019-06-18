package org.osivia.services.workspace.filebrowser.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser window properties java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserWindowProperties {

    /**
     * Document base path.
     */
    private String basePath;
    /**
     * Document path.
     */
    private String path;
    /**
     * NXQL request.
     */
    private String nxql;
    /**
     * BeanShell indicator.
     */
    private Boolean beanShell;
    /**
     * List mode indicator.
     */
    private Boolean listMode;


    /**
     * Constructor.
     */
    public FileBrowserWindowProperties() {
        super();
    }


    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNxql() {
        return nxql;
    }

    public void setNxql(String nxql) {
        this.nxql = nxql;
    }

    public Boolean getBeanShell() {
        return beanShell;
    }

    public void setBeanShell(Boolean beanShell) {
        this.beanShell = beanShell;
    }

    public Boolean getListMode() {
        return listMode;
    }

    public void setListMode(Boolean listMode) {
        this.listMode = listMode;
    }

}
