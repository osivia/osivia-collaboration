package org.osivia.services.workspace.sharing.common.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.context.PortalControllerContext;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Sharing common repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SharingCommonRepository
 */
public class SharingCommonRepositoryImpl implements SharingCommonRepository {

    /**
     * Constructor.
     */
    public SharingCommonRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSharingEnabled(PortalControllerContext portalControllerContext, NuxeoDocumentContext documentContext) {
        // Document
        Document document;
        if (documentContext == null) {
            document = null;
        } else {
            document = documentContext.getDocument();
        }

        // Facets
        PropertyList facets;
        if (document == null) {
            facets = null;
        } else {
            facets = document.getFacets();
        }

        // Enabled indicator
        boolean enabled = false;
        if (facets != null) {
            int i = 0;
            while (!enabled && (i < facets.size())) {
                String facet = facets.getString(i);
                enabled = SHARING_FACET.equals(facet);
                i++;
            }
        }

        return enabled;
    }

}
