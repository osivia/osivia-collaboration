package org.osivia.services.forum.edition.portlet.repository;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.forum.edition.portlet.model.ForumEditionForm;
import org.osivia.services.forum.edition.portlet.model.ForumEditionOptions;

import javax.portlet.PortletException;

/**
 * Forum edition repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ForumEditionRepository {

    /** Forum document type name. */
    String DOCUMENT_TYPE_FORUM = "Forum";
    /** Thread document type name. */
    String DOCUMENT_TYPE_THREAD = "Thread";

    /** Title Nuxeo document property. */
    String TITLE_PROPERTY = "dc:title";
    /** Description Nuxeo document property. */
    String DESCRIPTION_PROPERTY = "dc:description";
    /** Vignette Nuxeo document property. */
    String VIGNETTE_PROPERTY = "ttc:vignette";
    /** Message Nuxeo document property. */
    String MESSAGE_PROPERTY = "ttcth:message";
    /** Attachments Nuxeo document property. */
    String ATTACHMENTS_PROPERTY = "files:files";


    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     * @param options                 forum edition options
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, ForumEditionForm form, ForumEditionOptions options) throws PortletException;


    /**
     * Get Nuxeo document.
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo document
     * @throws PortletException
     */
    Document getDocument(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Fill Nuxeo document properties.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     * @throws PortletException
     */
    void fillDocumentProperties(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException;

}
