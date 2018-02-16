package org.osivia.services.sets.edition.portlet.service;

import java.util.HashMap;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.sets.edition.portlet.model.SetsEditionForm;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import net.sf.json.JSONObject;

/**
 * Sets edition service
 * @author Julien Barberet
 *
 */
public interface SetsEditionService {

	/**
	 * Get edition sets form.
	 * @param portalControllerContext
	 * @param reinit
	 * @return
	 * @throws PortletException
	 */
    SetsEditionForm getForm(PortalControllerContext portalControllerContext, boolean reinit) throws PortletException;
	
    /**
     * Save sets form.
     *
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, SetsEditionForm form) throws PortletException;

    
    /**
     * Sort sets
     * @param portalControllerContext
     * @param form
     * @throws PortletException
     */
    void sort(PortalControllerContext portalControllerContext, SetsEditionForm form) throws PortletException;
    
    /**
     * Get workspace Url
     * @param portalControllerContext
     * @param form
     * @return
     * @throws PortletException
     */
    String getWorkspaceUrl(PortalControllerContext portalControllerContext, SetsEditionForm form) throws PortletException;
    
    /**
     * Set application context
     * @param applicationContext
     * @throws BeansException
     */
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
    
    /**
     * Search documents to add (for select2)
     * @param portalControllerContext
     * @param workspace
     * @param filter
     * @return
     */
    JSONObject searchDocuments(PortalControllerContext portalControllerContext, Document workspace, String filter, int page, HashMap<String, String> mapPinnedDocuments) throws PortletException;
    
    /**
     * Add document to the set of documents
     * @param portalControllerContext
     * @param form
     */
    void addDocumentToSet(PortalControllerContext portalControllerContext, SetsEditionForm form);
}
