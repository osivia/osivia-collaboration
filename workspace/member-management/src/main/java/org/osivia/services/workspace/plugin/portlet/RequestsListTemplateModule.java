package org.osivia.services.workspace.plugin.portlet;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PrivilegedPortletModule;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Workspace member requests list template module.
 * 
 * @author Cédric Krommenhoek
 * @see PrivilegedPortletModule
 */
public class RequestsListTemplateModule extends PrivilegedPortletModule {

    /** Portlet repository. */
    private MemberManagementRepository repository;

    /** Workspace service. */
    private final WorkspaceService workspaceService;


    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public RequestsListTemplateModule(PortletContext portletContext) {
        super(portletContext);
        this.workspaceService = DirServiceFactory.getService(WorkspaceService.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getAuthType() {
        return NuxeoCommandContext.AUTH_TYPE_SUPERUSER;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilter() {
        return "ecm:primaryType = 'Workspace'";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        // Portlet repository
        MemberManagementRepository repository = this.getRepository();

        // Principal
        Principal principal = request.getUserPrincipal();
        
        if (principal != null) {
            // User
            String user = principal.getName();

            // Workspaces
            List<?> workspaces = (List<?>) request.getAttribute("documents");

            for (Object object : workspaces) {
                // Workspace
                DocumentDTO workspace = (DocumentDTO) object;
                // Workspace identifier
                String workspaceId = (String) workspace.getProperties().get("webc:url");

                // Search user in workspace members
                WorkspaceMember member = this.workspaceService.getMember(workspaceId, user);

                // Workspace member status
                MemberStatus status;
                
                if (member != null) {
                    // Member
                    status = MemberStatus.MEMBER;
                } else if (repository.isPendingInvitation(portalControllerContext, workspaceId, user, false)) {
                    // Invitation pending
                    status = MemberStatus.INVITATION_PENDING;
                } else if (repository.isPendingInvitation(portalControllerContext, workspaceId, user, true)) {
                    // Request pending
                    status = MemberStatus.REQUEST_PENDING;
                } else {
                    // No status
                    status = null;
                }

                workspace.getProperties().put("memberStatus", status);
            }
        }
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processAction(ActionRequest request, ActionResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        // Portlet repository
        MemberManagementRepository repository = this.getRepository();

        // Principal
        Principal principal = request.getUserPrincipal();
        // Action
        String action = request.getParameter(ActionRequest.ACTION_NAME);

        if ((principal != null) && "createRequest".equals(action)) {
            // User
            String user = principal.getName();
            // Workspace identifier
            String workspaceId = request.getParameter("id");

            if (StringUtils.isNotEmpty(workspaceId)) {
                repository.createInvitationRequest(portalControllerContext, workspaceId, user);
            }
        }
    }


    /**
     * Get portlet repository.
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


    /**
     * Workspace member status enumeration.
     * 
     * @author Cédric Krommenhoek
     */
    public enum MemberStatus {
        
        /** Workspace invitation pending. */
        INVITATION_PENDING("LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_STATUS_INVITATION_PENDING", "warning", "glyphicons glyphicons-alert"),
        /** Workspace request pending. */
        REQUEST_PENDING("LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_STATUS_REQUEST_PENDING", "info", "glyphicons glyphicons-hourglass"),
        /** Workspace member. */
        MEMBER("LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_STATUS_MEMBER", "success", "glyphicons glyphicons-tick");


        /** Identifier. */
        private final String id;
        /** Internationalization key. */
        private final String key;
        /** Color. */
        private final String color;
        /** Icon. */
        private final String icon;


        /**
         * Constructor.
         * 
         * @param key internationalization key
         * @param color color
         * @param icon icon
         */
        private MemberStatus(String key, String color, String icon) {
            this.id = StringUtils.lowerCase(this.name());
            this.key = key;
            this.color = color;
            this.icon = icon;
        }


        /**
         * Getter for id.
         * 
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * Getter for key.
         * 
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Getter for color.
         * 
         * @return the color
         */
        public String getColor() {
            return color;
        }

        /**
         * Getter for icon.
         * 
         * @return the icon
         */
        public String getIcon() {
            return icon;
        }
        
    }

}
