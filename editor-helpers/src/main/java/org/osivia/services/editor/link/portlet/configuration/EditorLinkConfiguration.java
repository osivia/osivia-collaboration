package org.osivia.services.editor.link.portlet.configuration;

import org.osivia.services.editor.common.configuration.CommonConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Editor link portlet configuration.
 *
 * @author Cédric Krommenhoek
 * @see CommonConfiguration
 */
@Configuration
@ComponentScan("org.osivia.services.editor.link.portlet")
public class EditorLinkConfiguration extends CommonConfiguration {

    /**
     * Constructor.
     */
    public EditorLinkConfiguration() {
        super();
    }


    @Override
    public String getSlug() {
        return "link";
    }
}
