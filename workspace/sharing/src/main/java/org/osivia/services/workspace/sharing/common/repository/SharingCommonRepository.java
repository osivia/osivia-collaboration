package org.osivia.services.workspace.sharing.common.repository;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Sharing common repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SharingCommonRepository {

    /** Sharing facet. */
    String SHARING_FACET = "Sharing";


    /**
     * Check if sharing enabled.
     * 
     * @param portalControllerContext portal controller context
     * @param documentContext Nuxeo document context
     * @return true if sharing is enabled
     */
    boolean isSharingEnabled(PortalControllerContext portalControllerContext, NuxeoDocumentContext documentContext);

}
