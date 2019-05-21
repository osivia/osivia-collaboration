package org.osivia.services.widgets.move.portlet.repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.*;
import org.osivia.services.widgets.move.portlet.model.MoveWindowProperties;
import org.osivia.services.widgets.move.portlet.repository.command.MoveDocumentCommand;
import org.osivia.services.widgets.move.portlet.repository.command.RemoveAllPermissionsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import javax.portlet.PortletException;
import java.util.List;

/**
 * Move portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see MoveRepository
 */
@Repository
public class MoveRepositoryImpl implements MoveRepository {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * CMS service locator.
     */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    public MoveRepositoryImpl() {
        super();
    }


    @Override
    public String getBasePath(PortalControllerContext portalControllerContext, MoveWindowProperties windowProperties) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getComputedPath(windowProperties.getBasePath());
    }


    @Override
    public String getNavigationPath(PortalControllerContext portalControllerContext, MoveWindowProperties windowProperties, String basePath) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);


        // Document path
        String documentPath = windowProperties.getPath();

        // Navigation item
        CMSItem navigationItem;
        if (documentPath == null) {
            navigationItem = null;
        } else {
            try {
                navigationItem = cmsService.getPortalNavigationItem(cmsContext, basePath, documentPath);
                if (navigationItem == null) {
                    CMSObjectPath objectPath = CMSObjectPath.parse(documentPath);
                    CMSObjectPath parentObjectPath = objectPath.getParent();
                    navigationItem = cmsService.getPortalNavigationItem(cmsContext, basePath, parentObjectPath.toString());
                }
            } catch (CMSException e) {
                throw new PortletException(e);
            }
        }

        // Navigation path
        String navigationPath;
        if (navigationItem == null) {
            navigationPath = null;
        } else {
            navigationPath = navigationItem.getNavigationPath();
        }

        return navigationPath;
    }


    @Override
    public Document getDocument(PortalControllerContext portalControllerContext, String path) {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);

        return documentContext.getDocument();
    }


    @Override
    public void move(PortalControllerContext portalControllerContext, String basePath, List<String> identifiers, String targetPath) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // Target identifier
        NuxeoDocumentContext targetDocumentContext = nuxeoController.getDocumentContext(targetPath);
        NuxeoPublicationInfos targetPublicationInfos = targetDocumentContext.getPublicationInfos();
        String targetId = targetPublicationInfos.getLiveId();

        // Move command
        INuxeoCommand command = this.applicationContext.getBean(MoveDocumentCommand.class, identifiers, targetId);
        nuxeoController.executeNuxeoCommand(command);

        // Source root document
        Document sourceRoot = this.getRootDocument(cmsContext, basePath);
        // Target root document
        Document targetRoot = this.getRootDocument(cmsContext, targetPath);

        if (!StringUtils.equals(sourceRoot.getPath(), targetRoot.getPath())) {
            // Update ACLs
            command = this.applicationContext.getBean(RemoveAllPermissionsCommand.class, identifiers);
            nuxeoController.executeNuxeoCommand(command);
        }
    }


    /**
     * Get root document.
     *
     * @param cmsContext CMS context
     * @param basePath   base path
     * @return document
     */
    private Document getRootDocument(CMSServiceCtx cmsContext, String basePath) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();

        // Current space document
        Document currentSpace = null;
        // Root document
        Document root = null;

        String path = basePath;
        while ((root == null) && StringUtils.isNotEmpty(path)) {
            // Space config
            CMSItem spaceConfig;
            try {
                spaceConfig = cmsService.getSpaceConfig(cmsContext, path);
            } catch (CMSException e) {
                throw new PortletException(e);
            }

            if (currentSpace == null) {
                currentSpace = (Document) spaceConfig.getNativeItem();
            }

            // Document type
            DocumentType spaceType = spaceConfig.getType();

            if ((spaceType != null) && spaceType.isRoot()) {
                root = (Document) spaceConfig.getNativeItem();
            }

            // Loop on parent path
            CMSObjectPath objectPath = CMSObjectPath.parse(path);
            CMSObjectPath parentObjectPath = objectPath.getParent();
            path = parentObjectPath.toString();
        }

        if (root == null) {
            root = currentSpace;
        }

        return root;
    }

}
