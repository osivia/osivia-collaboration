package org.osivia.services.workspace.task.creation.portlet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Workspace task creation repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see WorkspaceTaskCreationRepository
 */
@Repository
public class WorkspaceTaskCreationRepositoryImpl implements WorkspaceTaskCreationRepository {

    /**
     * Constructor.
     */
    public WorkspaceTaskCreationRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<DocumentType> getTaskTypes(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document types
        Map<String, DocumentType> documentTypes = nuxeoController.getCMSItemTypes();

        // Workspace document type
        String workspaceType = this.getWorkspaceType(portalControllerContext);
        DocumentType workspaceDocumentType = documentTypes.get(workspaceType);

        // Workspace sub-types
        List<String> workspaceSubTypes;
        if (workspaceDocumentType == null) {
            workspaceSubTypes = new ArrayList<>(0);
        } else {
            workspaceSubTypes = workspaceDocumentType.getPortalFormSubTypes();
        }

        // Types
        List<DocumentType> types = new ArrayList<>(workspaceSubTypes.size());
        for (String subType : workspaceSubTypes) {
            DocumentType type = documentTypes.get(subType);
            if (type != null) {
                types.add(type);
            }
        }

        return types;
    }


    /**
     * Get workspace type.
     * 
     * @param portalControllerContext portal controller context
     * @return type
     */
    private String getWorkspaceType(PortalControllerContext portalControllerContext) {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Window property
        String property = window.getProperty(WORKSPACE_TYPE_WINDOW_PROPERTY);

        return StringUtils.defaultIfEmpty(property, "Workspace");
    }

}
