package org.osivia.services.workspace.plugin.portlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Name;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PrivilegedPortletModule;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Manageable workspaces list template module.
 * 
 * @author Cédric Krommenhoek
 * @see PrivilegedPortletModule
 */
public class ManageableWorkspacesListTemplateModule extends PrivilegedPortletModule {

    /** Person service. */
    private final PersonService personService;
    /** Workspace service. */
    private final WorkspaceService workspaceService;


    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public ManageableWorkspacesListTemplateModule(PortletContext portletContext) {
        super(portletContext);

        // Person service
        this.personService = DirServiceFactory.getService(PersonService.class);
        // Workspace service
        this.workspaceService = DirServiceFactory.getService(WorkspaceService.class);
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
            // Current user DN
            Name dn = this.personService.getEmptyPerson().buildDn(user);

            // Search workspace profiles criteria
            CollabProfile criteria = workspaceService.getEmptyProfile();
            criteria.setType(WorkspaceGroupType.security_group);
            criteria.setUniqueMember(Arrays.asList(new Name[]{dn}));

            // Search workspace profiles results
            List<CollabProfile> results = workspaceService.findByCriteria(criteria);

            // Workspace identifier;
            Set<String> identifiers;

            if (CollectionUtils.isEmpty(results)) {
                identifiers = null;
            } else {
                identifiers = new HashSet<>(results.size());

                for (CollabProfile result : results) {
                    // Workspace role
                    WorkspaceRole role = result.getRole();

                    if ((role != null) && (role.getWeight() >= WorkspaceRole.ADMIN.getWeight())) {
                        identifiers.add(result.getWorkspaceId());
                    }
                }
            }

            if (CollectionUtils.isEmpty(identifiers)) {
                // No results
                filter = FILTER_NO_RESULTS;
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("ecm:primaryType = 'Workspace' ");
                builder.append("AND webc:url IN (");
                boolean first = true;
                for (String identifier : identifiers) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(", ");
                    }

                    builder.append("'").append(identifier).append("'");
                }
                builder.append(")");

                filter = builder.toString();
            }
        }

        return filter;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Workspaces
        List<?> workspaces = (List<?>) request.getAttribute("documents");

        for (Object object : workspaces) {
            // Workspace
            DocumentDTO workspace = (DocumentDTO) object;
            // Workspace properties
            Map<String, Object> properties = workspace.getProperties();

            // Workspace type
            String visibility = (String) properties.get("ttcs:visibility");
            if (StringUtils.isNotEmpty(visibility)) {
                WorkspaceType type = WorkspaceType.valueOf(visibility);
                properties.put("workspaceType", type);
            }
        }
    }

}
