package org.osivia.services.workspace.plugin;

import java.util.Map;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.services.workspace.plugin.forms.CreateWorkspaceFormFilter;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;

/**
 * Workspace creation plugin.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class WorkspaceCreationPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "workspace-creation.plugin";


    /**
     * Constructor.
     */
    public WorkspaceCreationPlugin() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String customizationId, CustomizationContext context) {
        // Form filters
        Map<String, FormFilter> formFilters = this.getFormFilters(context);
        formFilters.put(CreateWorkspaceFormFilter.IDENTIFIER, new CreateWorkspaceFormFilter());
    }

}
