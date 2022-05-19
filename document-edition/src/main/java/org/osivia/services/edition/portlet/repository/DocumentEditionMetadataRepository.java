package org.osivia.services.edition.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.DocumentEditionMetadata;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Metadata edition portlet repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface DocumentEditionMetadataRepository extends DocumentEditionCommonRepository<DocumentEditionMetadata> {

    /**
     * Get document metadata.
     *
     * @param portalControllerContext portlet controller context
     * @param document                related Nuxeo document
     * @return document metadata
     */
    DocumentEditionMetadata get(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Upload document vignette.
     *
     * @param portalControllerContext portal controller context
     * @param metadata                document metadata
     */
    void uploadVignette(PortalControllerContext portalControllerContext, DocumentEditionMetadata metadata) throws PortletException, IOException;


    /**
     * Delete document vignette.
     *
     * @param portalControllerContext portal controller context
     * @param metadata                document metadata
     */
    void deleteVignette(PortalControllerContext portalControllerContext, DocumentEditionMetadata metadata) throws PortletException, IOException;


    /**
     * Restore document vignette.
     *
     * @param portalControllerContext portal controller context
     * @param metadata                document metadata
     */
    void restoreVignette(PortalControllerContext portalControllerContext, DocumentEditionMetadata metadata) throws PortletException, IOException;


    /**
     * Validate document metadata.
     *
     * @param metadata document metadata
     * @param errors   validation errors
     */
    void validate(DocumentEditionMetadata metadata, Errors errors);


    /**
     * Customize metadata properties.
     *
     * @param portalControllerContext portal controller context
     * @param metadata                document metadata
     * @param properties              document properties
     * @param binaries                document binaries
     */
    void customizeProperties(PortalControllerContext portalControllerContext, DocumentEditionMetadata metadata, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException;

}
