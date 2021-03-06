package org.osivia.services.workspace.plugin;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.services.workspace.plugin.forms.AcceptWorkspaceInvitationFormFilter;
import org.osivia.services.workspace.plugin.forms.DeclineWorkspaceInvitationFormFilter;
import org.osivia.services.workspace.plugin.menubar.MemberManagementMenubarModule;
import org.osivia.services.workspace.plugin.portlet.ManageableWorkspacesListTemplateModule;
import org.osivia.services.workspace.plugin.portlet.RequestsListTemplateModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;

/**
 * Workspace member management plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class MemberManagementPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "workspace-member-management.plugin";


    /** Menubar module. */
    private final MenubarModule menubarModule;

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public MemberManagementPlugin() {
        super();
        this.menubarModule = new MemberManagementMenubarModule();

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
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
    protected void customizeCMSProperties(CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = getPortletContext();

        // Menubar modules
        List<MenubarModule> menubarModules = this.getMenubarModules(context);
        menubarModules.add(this.menubarModule);

        // Form filters
        Map<String, FormFilter> formFilters = this.getFormFilters(context);
        formFilters.put(AcceptWorkspaceInvitationFormFilter.IDENTIFIER, new AcceptWorkspaceInvitationFormFilter(portletContext));
        formFilters.put(DeclineWorkspaceInvitationFormFilter.IDENTIFIER, new DeclineWorkspaceInvitationFormFilter());

        // List templates
        this.customizeListTemplates(context);
    }


    /**
     * Customize list templates.
     *
     * @param context customization context
     */
    private void customizeListTemplates(CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = this.getPortletContext();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(context.getLocale());

        // List templates
        Map<String, ListTemplate> templates = this.getListTemplates(context);

        // Manageable workspaces
        ListTemplate manageableWorkspaces = new ListTemplate("manageable-workspaces", bundle.getString("LIST_TEMPLATE_MANAGEABLE_WORKSPACES"),
                "dublincore, toutatice, toutatice_space");
        manageableWorkspaces.setModule(new ManageableWorkspacesListTemplateModule(portletContext));
        templates.put(manageableWorkspaces.getKey(), manageableWorkspaces);

        // Workspace member requests
        ListTemplate requests = new ListTemplate("workspace-member-requests", bundle.getString("LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS"),
                "dublincore, toutatice, toutatice_space, webcontainer");
        requests.setModule(new RequestsListTemplateModule(portletContext));
        templates.put(requests.getKey(), requests);
    }

}
