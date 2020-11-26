package org.osivia.services.editor.image.portlet.configuration;

import org.osivia.services.editor.common.configuration.CommonConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Editor image portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see CommonConfiguration
 */
@Configuration
@ComponentScan("org.osivia.services.editor.image.portlet")
public class EditorImageConfiguration extends CommonConfiguration {

    /**
     * Constructor.
     */
    public EditorImageConfiguration() {
        super();
    }


    @Override
    public String getSlug() {
        return "image";
    }

}
