package org.osivia.services.workspace.plugin.portlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
import org.osivia.services.workspace.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PrivilegedPortletModule;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace member requests list template module.
 *
 * @author Cédric Krommenhoek
 * @see PrivilegedPortletModule
 */
public class RequestsListTemplateModule extends PrivilegedPortletModule {

    /** Portlet repository. */
    private MemberManagementRepository repository;


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public RequestsListTemplateModule(PortletContext portletContext) {
        super(portletContext);
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
    public String getFilter(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Current user
        String user = request.getRemoteUser();

        // Request filter
        String filter;

        if (user == null) {
            // No results
            filter = FILTER_NO_RESULTS;
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("ecm:primaryType = 'Workspace' ");
            builder.append("AND ttcs:spaceMembers/*/login <> '").append(user).append("' ");

            builder.append("AND ttcs:visibility IN (");
            WorkspaceType[] workspaceTypes = new WorkspaceType[]{WorkspaceType.PUBLIC, WorkspaceType.PUBLIC_INVITATION, WorkspaceType.PRIVATE};
            boolean first = true;
            for (WorkspaceType workspaceType : workspaceTypes) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }

                builder.append("'");
                builder.append(workspaceType.getId());
                builder.append("'");
            }
            builder.append(")");

            filter = builder.toString();
        }

        return filter;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Current user
        String user = request.getRemoteUser();
        // Pending invitations member status
        Map<String, MemberStatus> pendingStatus = this.getPendingStatus(portalControllerContext, user);

        // Documents DTO
        List<?> documents = (List<?>) request.getAttribute("documents");

        for (Object object : documents) {
            // Document DTO
            DocumentDTO documentDto = (DocumentDTO) object;
            // Document type
            DocumentType documentType = documentDto.getType();

            if ((documentType != null) && StringUtils.equals("Workspace", documentType.getName())) {
                // Workspace Nuxeo document
                Document workspace = documentDto.getDocument();
                // Workspace properties
                Map<String, Object> properties = documentDto.getProperties();

                // Workspace type
                String visibility = workspace.getString("ttcs:visibility");
                if (StringUtils.isNotEmpty(visibility)) {
                    WorkspaceType type = WorkspaceType.valueOf(visibility);
                    properties.put("workspaceType", type);
                }

                // Workspace member status
                MemberStatus status = this.getMemberStatus(workspace, user, pendingStatus);
                properties.put("memberStatus", status);
            }
        }
    }


    /**
     * Get pending invitation member status.
     *
     * @param pendingInvitations pending invitations
     * @return pending invitation member status
     * @throws PortletException
     */
    protected Map<String, MemberStatus> getPendingStatus(PortalControllerContext portalControllerContext, String user) throws PortletException {
        // Pending status
        Map<String, MemberStatus> pendingStatus;

        if (StringUtils.isEmpty(user)) {
            pendingStatus = null;
        } else {
            // Portlet repository
            MemberManagementRepository repository = this.getRepository();

            // Pending invitations
            List<Document> pendingInvitations = repository.getPendingInvitations(portalControllerContext, user);

            pendingStatus = new HashMap<>(pendingInvitations.size());

            for (Document pendingInvitation : pendingInvitations) {
                // Model webId
                String modelWebId = pendingInvitation.getString("pi:procedureModelWebId");
                // Model identifier
                String modelId = StringUtils.removeStart(modelWebId, IFormsService.FORMS_WEB_ID_PREFIX);

                // Member status
                MemberStatus status;
                if (MemberManagementRepository.INVITATION_MODEL_ID.equals(modelId)) {
                    status = MemberStatus.INVITATION_PENDING;
                } else if (MemberManagementRepository.REQUEST_MODEL_ID.equals(modelId)) {
                    status = MemberStatus.REQUEST_PENDING;
                } else {
                    status = null;
                }

                if (status != null) {
                    // Variables
                    PropertyMap variables = pendingInvitation.getProperties().getMap("pi:globalVariablesValues");

                    if (variables != null) {
                        // Workspace identifier
                        String workspaceId = variables.getString(MemberManagementRepository.WORKSPACE_IDENTIFIER_PROPERTY);

                        if (StringUtils.isNotEmpty(workspaceId)) {
                            pendingStatus.put(workspaceId, status);
                        }
                    }
                }
            }
        }

        return pendingStatus;
    }


    /**
     * Get workspace member status.
     * 
     * @param workspace workspace Nuxeo document
     * @param user user
     * @param pendingStatus invitations pending status
     * @return member status
     */
    protected MemberStatus getMemberStatus(Document workspace, String user, Map<String, MemberStatus> pendingStatus) {
        MemberStatus status;
        if (StringUtils.isEmpty(user)) {
            status = null;
        } else {
            // Workspace identifier
            String workspaceId = workspace.getString("webc:url");

            status = pendingStatus.get(workspaceId);

            if (status == null) {
                // Workspace members
                PropertyList members = workspace.getProperties().getList("ttcs:spaceMembers");

                if ((members != null) && !members.isEmpty()) {
                    boolean member = false;
                    int i = 0;
                    while (!member && (i < members.size())) {
                        PropertyMap map = members.getMap(i);
                        member = StringUtils.equals(user, map.getString("login"));
                        i++;
                    }

                    if (member) {
                        status = MemberStatus.MEMBER;
                    }
                }
            }
        }

        return status;
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

        // Current user
        String user = request.getRemoteUser();
        // Action
        String action = request.getParameter(ActionRequest.ACTION_NAME);

        if (StringUtils.isNotEmpty(user) && "createRequest".equals(action)) {
            // Workspace identifier
            String workspaceId = request.getParameter("id");
            // User message
            String userMessage = request.getParameter("userMessage");

            if (StringUtils.isNotEmpty(workspaceId)) {
                repository.createInvitationRequest(portalControllerContext, workspaceId, user, userMessage);
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
        INVITATION_PENDING("LIST_TEMPLATE_STATUS_INVITATION_PENDING", "warning", "glyphicons glyphicons-alert"),
        /** Workspace request pending. */
        REQUEST_PENDING("LIST_TEMPLATE_STATUS_REQUEST_PENDING", "info", "glyphicons glyphicons-hourglass"),
        /** Workspace member. */
        MEMBER("LIST_TEMPLATE_STATUS_MEMBER", "primary", "glyphicons glyphicons-ok");


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
            return this.id;
        }

        /**
         * Getter for key.
         *
         * @return the key
         */
        public String getKey() {
            return this.key;
        }

        /**
         * Getter for color.
         *
         * @return the color
         */
        public String getColor() {
            return this.color;
        }

        /**
         * Getter for icon.
         *
         * @return the icon
         */
        public String getIcon() {
            return this.icon;
        }

    }

}
