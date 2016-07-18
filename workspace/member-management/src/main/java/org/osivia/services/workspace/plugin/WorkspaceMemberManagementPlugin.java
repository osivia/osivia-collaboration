package org.osivia.services.workspace.plugin;

import java.util.List;
import java.util.Map;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;

/**
 * Workspace member management plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class WorkspaceMemberManagementPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "workspace-member-management.plugin";


    /** Menubar module. */
    private final MenubarModule menubarModule;
    /** Join workspace form filter. */
    private final FormFilter joinWorkspaceFormFilter;


    /**
     * Constructor.
     */
    public WorkspaceMemberManagementPlugin() {
        super();
        this.menubarModule = new WorkspaceMemberManagementMenubarModule();
        this.joinWorkspaceFormFilter = new JoinWorkspaceFormFilter();
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
        // Menubar modules
        List<MenubarModule> menubarModules = this.getMenubarModules(context);
        menubarModules.add(this.menubarModule);

        // Form filters
        Map<String, FormFilter> formFilters = this.getFormFilters(context);
        formFilters.put(this.joinWorkspaceFormFilter.getId(), this.joinWorkspaceFormFilter);
    }

}
