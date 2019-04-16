package org.osivia.services.workspace.filebrowser.plugin.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * File browser plugin configuration.
 * 
 * @author Cédric Krommenhoek
 */
@Configuration
@ComponentScan(basePackages = "org.osivia.services.workspace.filebrowser.plugin")
public class FileBrowserPluginConfiguration {

    /**
     * Constructor.
     */
    public FileBrowserPluginConfiguration() {
        super();
    }

}
