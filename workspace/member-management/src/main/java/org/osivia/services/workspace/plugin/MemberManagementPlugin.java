package org.osivia.services.workspace.plugin;

import java.util.Map;

import javax.portlet.PortletContext;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.services.workspace.plugin.forms.AcceptWorkspaceInvitationFormFilter;
import org.osivia.services.workspace.plugin.forms.DeclineWorkspaceInvitationFormFilter;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
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


    /**
     * Constructor.
     */
    public MemberManagementPlugin() {
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
        // Portlet context
        PortletContext portletContext = getPortletContext();

        // Form filters
        Map<String, FormFilter> formFilters = this.getFormFilters(context);
        formFilters.put(AcceptWorkspaceInvitationFormFilter.IDENTIFIER, new AcceptWorkspaceInvitationFormFilter(portletContext));
        formFilters.put(DeclineWorkspaceInvitationFormFilter.IDENTIFIER, new DeclineWorkspaceInvitationFormFilter());
    }

}
