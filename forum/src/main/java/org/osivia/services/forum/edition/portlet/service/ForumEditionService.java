package org.osivia.services.forum.edition.portlet.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
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

    /**
     * Document type window property.
     */
    String DOCUMENT_TYPE_PROPERTY = "osivia.forum.edition.documentType";
    /**
     * Forum edition mode identifier window property.
     */
    String MODE_PROPERTY = "osivia.forum.edition.mode";

    /**
     * Max upload size.
     */
    long MAX_UPLOAD_SIZE = NumberUtils.toLong(System.getProperty("osivia.forum.max.upload.size"), 10L) * FileUtils.ONE_MB;


    /**
     * Upload vignette.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     */
    void uploadVignette(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Delete vignette.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     */
    void deleteVignette(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Upload attachment.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     */
    void uploadAttachment(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Delete attachment.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     */
    void deleteAttachment(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Save.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     * @param options                 forum edition options
     */
    void save(PortalControllerContext portalControllerContext, ForumEditionForm form, ForumEditionOptions options) throws PortletException, IOException;


    /**
     * Cancel.
     *
     * @param portalControllerContext portal controller context
     * @param options                 forum edition options
     */
    void cancel(PortalControllerContext portalControllerContext, ForumEditionOptions options) throws PortletException, IOException;


    /**
     * Vignette preview.
     *
     * @param portalControllerContext portal controller context
     * @param form                    forum edition form
     */
    void vignettePreview(PortalControllerContext portalControllerContext, ForumEditionForm form) throws PortletException, IOException;


    /**
     * Get forum edition form.
     *
     * @param portalControllerContext portal controller context
     * @return forum edition form
     */
    ForumEditionForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get forum edition options.
     *
     * @param portalControllerContext portal controller context
     * @return forum edition options
     */
    ForumEditionOptions getOptions(PortalControllerContext portalControllerContext) throws PortletException;

}
