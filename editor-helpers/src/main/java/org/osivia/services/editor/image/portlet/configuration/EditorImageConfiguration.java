package org.osivia.services.editor.image.portlet.configuration;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
import org.osivia.services.editor.common.configuration.CommonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.portlet.multipart.CommonsPortletMultipartResolver;
import org.springframework.web.portlet.multipart.PortletMultipartResolver;

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


    /**
     * Get multipart resolver.
     *
     * @return multipart resolver
     */
    @Bean(name = "portletMultipartResolver")
    public PortletMultipartResolver getMultipartResolver() {
        CommonsPortletMultipartResolver multipartResolver = new CommonsPortletMultipartResolver();
        multipartResolver.setDefaultEncoding(CharEncoding.UTF_8);
        multipartResolver.setMaxUploadSizePerFile(100 * FileUtils.ONE_MB);
        return multipartResolver;
    }

}
