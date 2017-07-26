package org.osivia.services.forum.edition.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.forum.edition.portlet.model.ForumEditionForm;
import org.osivia.services.forum.edition.portlet.model.ForumEditionOptions;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Forum edition service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface ForumEditionService {

    /** Document type window property. */
    String DOCUMENT_TYPE_PROPERTY = "osivia.forum.edition.documentType";
    /** Forum edition mode identifier window property. */
    String MODE_PROPERTY = "osivia.forum.edition.mode";


    /**
     * Upload vignette.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     * @throws PortletException
     * @throws IOException
     */
    void uploadVignette(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Delete vignette.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     * @throws PortletException
     * @throws IOException
     */
    void deleteVignette(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Upload attachment.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     * @throws PortletException
     * @throws IOException
     */
    void uploadAttachment(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Delete attachment.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     * @throws PortletException
     * @throws IOException
     */
    void deleteAttachment(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     * @param options                 forum edition options
     * @throws PortletException
     * @throws IOException
     */
    void save(PortalControllerContext portalControllerContext, ForumEditionForm form, ForumEditionOptions options) throws PortletException, IOException;


    /**
     * Cancel.
     *
     * @param portalControllerContext portal controller context
     * @param options                 forum edition options
     * @throws PortletException
     * @throws IOException
     */
    void cancel(PortalControllerContext portalControllerContext, ForumEditionOptions options) throws PortletException, IOException;


    /**
     * Vignette preview.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     * @throws PortletException
     * @throws IOException
     */
    void vignettePreview(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Get forum edition form.
     *
     * @param portalControllerContext portal controller context
     * @return forum edition form
     * @throws PortletException
     */
    ForumEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get forum edition options.
     *
     * @param portalControllerContext portal controller context
     * @return forum edition options
     * @throws PortletException
     */
    ForumEditionOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;

}
