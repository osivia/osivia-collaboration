package org.osivia.services.search.common.repository;

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
import org.springframework.beans.factory.annotation.Autowired;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Common repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CommonRepository
 */
public abstract class CommonRepositoryImpl implements CommonRepository {

    /** CMS service locator. */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    public CommonRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Document getRoot(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setForcePublicationInfosScope("superuser_context");

        // Base path
        String basePath = nuxeoController.getBasePath();

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS service context
        CMSServiceCtx cmsContext = nuxeoController.getCMSCtx();

        // Workspace Nuxeo document
        Document workspace = null;
        try {
            while ((workspace == null) && StringUtils.isNotEmpty(basePath)) {
                // Publication infos
                CMSPublicationInfos publicationInfos = cmsService.getPublicationInfos(cmsContext, basePath);
                // Space config
                CMSItem spaceConfig = cmsService.getSpaceConfig(cmsContext, basePath);
                // Document type
                DocumentType documentType = spaceConfig.getType();

                if ((documentType != null) && (("Workspace".equals(documentType.getName())) || ("PortalSite".equals(documentType.getName()))  )) {
                    workspace = (Document) spaceConfig.getNativeItem();
                } else {
                    // Loop on parent path
                    basePath = publicationInfos.getParentSpaceID();
                }
            }
        } catch (CMSException e) {
            throw new PortletException(e);
        }

        return workspace;
    }

}
