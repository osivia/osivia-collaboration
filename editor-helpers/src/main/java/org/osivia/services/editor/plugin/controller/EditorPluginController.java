package org.osivia.services.editor.plugin.controller;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.editor.EditorModule;
import org.osivia.services.editor.plugin.service.EditorPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.portlet.context.PortletConfigAware;

import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.util.List;

/**
 * Editor plugin controller.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 * @see PortletConfigAware
 */
@Controller
public class EditorPluginController extends AbstractPluginPortlet implements PortletConfigAware {

    /**
     * Plugin service.
     */
    @Autowired
    private EditorPluginService service;


    /**
     * Constructor.
     */
    public EditorPluginController() {
        super();
    }


    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        try {
            super.init(portletConfig);
        } catch (PortletException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        this.destroy();
    }


    @Override
    protected String getPluginName() {
        return EditorPluginService.PLUGIN_NAME;
    }


    @Override
    protected void customizeCMSProperties(CustomizationContext customizationContext) {
        // Editor modules
        this.customizeEditorModules(customizationContext);
    }


    /**
     * Customize editor modules.
     *
     * @param customizationContext customization context
     */
    private void customizeEditorModules(CustomizationContext customizationContext) {
        List<EditorModule> modules = this.getEditorModules(customizationContext);
        this.service.customizeEditorModules(modules);
    }

}
