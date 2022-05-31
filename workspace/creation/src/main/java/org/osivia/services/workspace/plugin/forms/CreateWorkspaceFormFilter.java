package org.osivia.services.workspace.plugin.forms;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Name;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.service.RoleService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;
import org.osivia.services.workspace.portlet.service.WorkspaceCreationService;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Create workspace form filter.
 * 
 * @author Cédric Krommenhoek
 * @see FormFilter
 */
public class CreateWorkspaceFormFilter implements FormFilter {

    /** Form filter identifier. */
    public static final String IDENTIFIER = "CREATE_WORKSPACE";

    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "CREATE_WORKSPACE_FORM_FILTER_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = "CREATE_WORKSPACE_FORM_FILTER_DESCRIPTION";

    /** Title variable name parameter. */
    private static final String TITLE_VARIABLE_NAME = "titleVariableName";
    /** Description variable name parameter. */
    private static final String DESCRIPTION_VARIABLE_NAME = "descriptionVariableName";
    /** Workspace type variable name parameter. */
    private static final String WORKSPACE_TYPE_VARIABLE_NAME = "workspaceTypeVariableName";
    /** Stop workflow if granted indicator parameter. */
    private static final String STOP_WORKFLOW_IF_GRANTED = "stopWorkflowIfGranted";

    /** Whitespaces regex. */
    private static final String REGEX = "\\s\\s";


    /** Workspace creation service. */
    private WorkspaceCreationService service;
    

    /** Whitespaces pattern. */
    private final Pattern pattern;

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;
    /** Person service. */
    private final PersonService personService;
    /** Role service. */
    private final RoleService roleService;


    /**
     * Constructor.
     */
    public CreateWorkspaceFormFilter() {
        super();
        this.pattern = Pattern.compile(REGEX);

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
        // Person service
        this.personService = DirServiceFactory.getService(PersonService.class);
        // Role service
        this.roleService = DirServiceFactory.getService(RoleService.class);
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
        Map<String, FormFilterParameterType> parameters = new HashMap<>();
        parameters.put(TITLE_VARIABLE_NAME, FormFilterParameterType.TEXT);
        parameters.put(DESCRIPTION_VARIABLE_NAME, FormFilterParameterType.TEXT);
        parameters.put(WORKSPACE_TYPE_VARIABLE_NAME, FormFilterParameterType.TEXT);
        parameters.put(STOP_WORKFLOW_IF_GRANTED, FormFilterParameterType.BOOLEAN);
        return parameters;
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
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        // Portal controller context
        PortalControllerContext portalControllerContext = context.getPortalControllerContext();
        // HTTP servlet request
        HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
        
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

        // User
        String user = servletRequest.getRemoteUser();
        // User DN
        Name dn = this.personService.getEmptyPerson().buildDn(user);

        // Model webId
        String modelWebId = context.getModelWebId();
        // Procedure instance UUID
        String procedureInstanceUuid = context.getProcedureInstanceUuid();
        
        // Variables
        Map<String, String> variables = context.getVariables();

        // Workspace title
        String titleVariableName = context.getParamValue(executor, TITLE_VARIABLE_NAME);
        String title = StringUtils.trimToEmpty(variables.get(titleVariableName));
        Matcher matcher = this.pattern.matcher(title);
        while (matcher.find()) {
            title = matcher.replaceAll(" ");
            matcher = this.pattern.matcher(title);
        }

        // Check workspace title availability
        if (!this.checkTitleAvailability(portalControllerContext, modelWebId, procedureInstanceUuid, title, titleVariableName)) {
            String message = bundle.getString("MESSAGE_WORKSPACE_TITLE_NOT_AVAILABLE_ERROR");
            throw new FormFilterException(message);
        }

        // Granted user indicator
        //boolean granted = this.roleService.hasRole(dn, "role_workspace-management");
        boolean granted = true; // Always validate space
        
        if (granted) {
            // Description
            String descriptionVariableName = context.getParamValue(executor, DESCRIPTION_VARIABLE_NAME);
            String description = variables.get(descriptionVariableName);

            // Workspace type
            String workspaceTypeVariableName = context.getParamValue(executor, WORKSPACE_TYPE_VARIABLE_NAME);
            WorkspaceType type;
            if (StringUtils.isEmpty(workspaceTypeVariableName)) {
                type = null;
            } else {
                type = WorkspaceType.valueOf(variables.get(workspaceTypeVariableName));
            }


            // Owner identifier
            String owner = context.getProcedureInitiator();


            // Workspace creation form
            WorkspaceCreationForm form = new WorkspaceCreationForm();
            form.setTitle(title);
            form.setDescription(description);
            form.setType(type);
            form.setOwner(owner);

            try {
                // Creation
                this.getService().create(portalControllerContext, form);
            } catch (PortletException e) {
                throw new FormFilterException(e);
            }


            // Stop workflow indicator
            boolean stopWorkflow = BooleanUtils.toBoolean(context.getParamValue(executor, STOP_WORKFLOW_IF_GRANTED));
            if (stopWorkflow) {
                context.setNextStep(IFormsService.ENDSTEP);
            }
        }
    }


    /**
     * Check workspace title availability.
     * 
     * @param portalControllerContext portal controller context
     * @param modelWebId model webId
     * @param procedureInstanceUuid procedure instance UUID
     * @param title workspace title
     * @param titleVariableName workspace title variable name
     * @return true if workspace title is available
     * @throws FormFilterException
     */
    private boolean checkTitleAvailability(PortalControllerContext portalControllerContext, String modelWebId, String procedureInstanceUuid, String title,
            String titleVariableName) throws FormFilterException {
        boolean available;
        try {
            available = this.getService().checkTitleAvailability(portalControllerContext, modelWebId, procedureInstanceUuid, title, titleVariableName);
        } catch (PortletException e) {
            throw new FormFilterException(e);
        }
        return available;
    }


    /**
     * Get workspace creation service.
     * 
     * @return service
     */
    private WorkspaceCreationService getService() {
        if (this.service == null) {
            ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
            this.service = applicationContext.getBean(WorkspaceCreationService.class);
        }
        return this.service;
    }

}
