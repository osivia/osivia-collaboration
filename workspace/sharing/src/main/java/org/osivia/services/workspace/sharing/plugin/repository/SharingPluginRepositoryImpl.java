package org.osivia.services.workspace.sharing.plugin.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.services.workspace.sharing.common.repository.SharingCommonRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Sharing plugin repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SharingCommonRepositoryImpl
 * @see SharingPluginRepository
 */
@Repository
public class SharingPluginRepositoryImpl extends SharingCommonRepositoryImpl implements SharingPluginRepository {

    /** CMS service locator. */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    public SharingPluginRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Document getSharingRoot(PortalControllerContext portalControllerContext, NuxeoDocumentContext documentContext) {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);
        cmsContext.setDoc(documentContext.getDocument());

        // Sharing root CMS item
        CMSItem cmsItem;
        try {
            cmsItem = cmsService.getSharingRoot(cmsContext);
        } catch (CMSException e) {
            cmsItem = null;
        }

        // Sharing root Nuxeo document
        Document document;
        if ((cmsItem != null) && (cmsItem.getNativeItem() != null) && (cmsItem.getNativeItem() instanceof Document)) {
            document = (Document) cmsItem.getNativeItem();
        } else {
            document = null;
        }

        return document;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getSharingAuthor(PortalControllerContext portalControllerContext, Document sharingRoot) {
        // Sharing author
        String author;
        if (sharingRoot == null) {
            author = null;
        } else {
            author = sharingRoot.getString(SHARING_AUTHOR_PROPERTY);
        }

        return author;
    }

}
