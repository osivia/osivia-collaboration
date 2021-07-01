package org.osivia.services.workspace.plugin.forms;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Accept workspace invitation form filter.
 *
 * @author Loîc Billon
 * @see IFormFilterModule
 */
public class AcceptWorkspaceInvitationCurrentLoginFormFilter implements FormFilter {

    /** Form filter identifier. */
    public static final String IDENTIFIER = "ACCEPT_WORKSPACE_INVITATION_CURRENT_LOGIN";

    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "ACCEPT_WORKSPACE_INVITATION_CURRENT_LOGIN_FORM_FILTER_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = null;


    /** Member management repository. */
    private MemberManagementRepository repository;

    /** Portlet context. */
    private final PortletContext portletContext;


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public AcceptWorkspaceInvitationCurrentLoginFormFilter(PortletContext portletContext) {
        super();
        this.portletContext = portletContext;
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
     * @throws FormFilterException 
     */
    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        // Portal controller context
        PortalControllerContext portalControllerContext = context.getPortalControllerContext();
        // HTTP servlet request
        HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
        // HTTP session
        HttpSession session = servletRequest.getSession();

        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(this.portletContext);
        nuxeoController.setServletRequest(servletRequest);


        // Variables
        Map<String, String> variables = context.getVariables();
        
        // The invitation is accepted by the current logged person
        variables.put(MemberManagementRepository.PERSON_UID_PROPERTY, servletRequest.getRemoteUser());

        		
		WorkspaceService service = DirServiceFactory.getService(WorkspaceService.class);
		
		List<WorkspaceMember> allMembers = service.getAllMembers(variables.get(MemberManagementRepository.WORKSPACE_IDENTIFIER_PROPERTY));
		
		boolean alreadyMember = false;
        for(WorkspaceMember member : allMembers) {
        	if(member.getMember().getUid().equals(servletRequest.getRemoteUser())) {
        		alreadyMember = true;
        		break;
        	}
        }
	            
        
        if(alreadyMember) {

           
           String message = "Vous êtes déjà membre de cet espace.";

            /** Notifications service. */
           INotificationsService notificationsService = Locator.findMBean(INotificationsService.class, INotificationsService.MBEAN_NAME);

            notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.WARNING);
            
            variables.put("error", "1");
            
        }
        else {
            // Accept invitation
            this.getRepository().acceptInvitation(nuxeoController, variables);

            // Update invitation state
            variables.put(MemberManagementRepository.INVITATION_STATE_PROPERTY, InvitationState.ACCEPTED.toString());
            variables.put(MemberManagementRepository.ACKNOWLEDGMENT_DATE_PROPERTY, String.valueOf(System.currentTimeMillis()));
            variables.put("error", "0");
            
            // Reload nuxeo session indicator
            session.setAttribute(Constants.SESSION_RELOAD_ATTRIBUTE, true);
        }

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
