package org.osivia.services.workspace.plugin.forms;

import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.PortletResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Accept workspace invitation form filter.
 * 
 * @author Cédric Krommenhoek
 * @see IFormFilterModule
 */
public class AcceptWorkspaceInvitationFormFilter implements FormFilter {

    /** Form filter identifier. */
    private static final String IDENTIFIER = "ACCEPT_WORKSPACE_INVITATION";
    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "ACCEPT_WORKSPACE_INVITATION_FORM_FILTER_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = null;


    /** Member management repository. */
    private MemberManagementRepository repository;
    
    
    /**
     * Constructor.
     */
    public AcceptWorkspaceInvitationFormFilter() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return IDENTIFIER;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelKey() {
        return LABEL_INTERNATIONALIZATION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_INTERNATIONALIZATION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChildren() {
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) {
        // Portal controller context
        PortalControllerContext portalControllerContext = context.getPortalControllerContext();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // Reload indicator render parameter
        if (response instanceof ActionResponse) {
            ActionResponse actionResponse = (ActionResponse) response;
            actionResponse.setRenderParameter("reload", String.valueOf(true));
        }

        // Variables
        Map<String, String> variables = context.getVariables();

        // Accept invitation
        this.getRepository().acceptInvitation(portalControllerContext, variables);

        // Update invitation state
        variables.put(MemberManagementRepository.INVITATION_STATE_PROPERTY, InvitationState.ACCEPTED.toString());
        variables.put(MemberManagementRepository.ACKNOWLEDGMENT_DATE_PROPERTY, String.valueOf(System.currentTimeMillis()));
    }
    
    
    /**
     * Get repository.
     * 
     * @return repository
     */
    private MemberManagementRepository getRepository() {
        if (this.repository == null) {
            ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
            this.repository = applicationContext.getBean(MemberManagementRepository.class);
        }
        return this.repository;
    }

}
