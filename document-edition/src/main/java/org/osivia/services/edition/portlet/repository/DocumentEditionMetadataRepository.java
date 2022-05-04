package org.osivia.services.edition.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.DocumentEditionMetadata;

import javax.portlet.PortletException;

/**
 * Metadata edition portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface DocumentEditionMetadataRepository {

    /**
     * Get metadata.
     *
     * @param portalControllerContext portlet controller context
     * @param document                document
     * @return metadata
     */
    DocumentEditionMetadata getMetadata(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Customize metadata properties.
     *
     * @param metadata   metadata
     * @param properties document properties
     */
    void customizeProperties(DocumentEditionMetadata metadata, PropertyMap properties) throws PortletException;

}
