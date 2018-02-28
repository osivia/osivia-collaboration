package org.osivia.services.workspace.portlet.repository.impl;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSPublicationInfos;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.workspace.portlet.repository.WorkspaceMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Workspace map repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see WorkspaceMapRepository
 */
@Repository
public class WorkspaceMapRepositoryImpl implements WorkspaceMapRepository {

    /** CMS service locator. */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    public WorkspaceMapRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspacePath(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS service context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();


        // Workspace Nuxeo document
        Document workspace = null;
        // Path
        String path = nuxeoController.getBasePath();
        // Last authorized document
        Document lastAuthorizedDocument = null;

        while ((workspace == null) && StringUtils.isNotEmpty(path)) {
            try {
                // Publication infos
                CMSPublicationInfos publicationInfos = cmsService.getPublicationInfos(cmsContext, path);
                // Base path
                String basePath = publicationInfos.getPublishSpacePath();
                // Space config
                CMSItem spaceConfig = cmsService.getSpaceConfig(cmsContext, basePath);
                // Document type
                DocumentType documentType = spaceConfig.getType();
                // Update last authorized document
                lastAuthorizedDocument = (Document) spaceConfig.getNativeItem();

                if ((documentType != null) && ("Workspace".equals(documentType.getName()))) {
                    workspace = lastAuthorizedDocument;
                } else {
                    // Loop on parent path
                    path = publicationInfos.getParentSpaceID();
                }
            } catch (CMSException e) {
                if (CMSException.ERROR_FORBIDDEN == e.getErrorCode()) {
                    workspace = lastAuthorizedDocument;
                } else {
                    throw new PortletException(e);
                }
            }
        }

        // Workspace path
        String workspacePath;
        if (workspace == null) {
            workspacePath = null;
        } else {
            workspacePath = workspace.getPath();
        }

        return workspacePath;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getNavigationPath(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getNavigationPath();
    }

}
