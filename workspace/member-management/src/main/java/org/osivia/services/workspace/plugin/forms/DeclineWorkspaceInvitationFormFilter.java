package org.osivia.services.workspace.plugin.forms;

import java.util.Map;

import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Decline workspace invitation form filter.
 * 
 * @author Cédric Krommenhoek
 * @see IFormFilterModule
 */
public class DeclineWorkspaceInvitationFormFilter implements FormFilter {

    /** Form filter identifier. */
    private static final String IDENTIFIER = "DECLINE_WORKSPACE_INVITATION";
    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "DECLINE_WORKSPACE_INVITATION_FORM_FILTER_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = null;


    /**
     * Constructor.
     */
    public DeclineWorkspaceInvitationFormFilter() {
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
        // Variables
        Map<String, String> variables = context.getVariables();
        variables.put(MemberManagementRepository.INVITATION_STATE_PROPERTY, InvitationState.REJECTED.toString());
        variables.put(MemberManagementRepository.ACKNOWLEDGMENT_DATE_PROPERTY, String.valueOf(System.currentTimeMillis()));
    }

}
