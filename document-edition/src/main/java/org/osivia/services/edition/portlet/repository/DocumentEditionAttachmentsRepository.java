package org.osivia.services.edition.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.Attachments;
import org.springframework.validation.Errors;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Document edition attachments repository interface.
 *
 * @author Cédric Krommenhoek
 * @see DocumentEditionCommonRepository
 * @see Attachments
 */
public interface DocumentEditionAttachmentsRepository extends DocumentEditionCommonRepository<Attachments> {

    /**
     * Get document attachments.
     *
     * @param portalControllerContext portal controller context
     * @param document                related Nuxeo document
     * @return document attachments
     */
    Attachments get(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Upload document attachments.
     *
     * @param portalControllerContext portal controller context
     * @param attachments             document attachments
     */
    void uploadAttachments(PortalControllerContext portalControllerContext, Attachments attachments) throws PortletException, IOException;


    /**
     * Delete document attachment.
     *
     * @param portalControllerContext portal controller context
     * @param attachments             document attachments
     * @param value                   parameter value
     */
    void deleteAttachment(PortalControllerContext portalControllerContext, Attachments attachments, String value) throws PortletException, IOException;


    /**
     * Restore document attachment.
     *
     * @param portalControllerContext portal controller context
     * @param attachments             document attachments
     * @param value                   parameter value
     */
    void restoreAttachment(PortalControllerContext portalControllerContext, Attachments attachments, String value) throws PortletException, IOException;


    /**
     * Validate document attachments.
     *
     * @param attachments document attachments
     * @param errors      validation errors
     */
    void validate(Attachments attachments, Errors errors);


    /**
     * Customize attachments properties.
     *
     * @param portalControllerContext portal controller context
     * @param attachments             document attachments
     * @param properties              document properties
     * @param binaries                document binaries
     */
    void customizeProperties(PortalControllerContext portalControllerContext, Attachments attachments, PropertyMap properties, Map<String, List<Blob>> binaries) throws PortletException;

}
