package org.osivia.services.workspace.plugin;

import java.util.HashMap;
import java.util.Map;

import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepositoryImpl;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.osivia.services.workspace.portlet.service.MemberManagementServiceImpl;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Join workspace form filter.
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


    /** Member management service. */
    private final MemberManagementService service;
    /** Member management repository. */
    private final MemberManagementRepository repository;


    /**
     * Constructor.
     */
    public AcceptWorkspaceInvitationFormFilter() {
        super();
        this.service = new MemberManagementServiceImpl();
        this.repository = new MemberManagementRepositoryImpl();
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
        Map<String, FormFilterParameterType> parameters = new HashMap<>(2);
        parameters.put("uid", FormFilterParameterType.TEXT);
        parameters.put("workspaceId", FormFilterParameterType.TEXT);
        parameters.put("role", FormFilterParameterType.TEXT);
        return parameters;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) {
        // Variables
        Map<String, String> variables = context.getVariables();

        String workspaceId = variables.get(MemberManagementRepository.WORKSPACE_IDENTIFIER_PROPERTY);
        String uid = variables.get(MemberManagementRepository.PERSON_UID_PROPERTY);
        WorkspaceRole role = WorkspaceRole.fromId(MemberManagementRepository.ROLE_PROPERTY);

        // TODO Auto-generated method stub
        System.out.println("Invitation à l'espace acceptée.");
    }

}
