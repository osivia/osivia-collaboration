package org.osivia.services.pins.edition.portlet.service;

import java.util.HashMap;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.pins.edition.portlet.model.PinsEditionForm;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import net.sf.json.JSONObject;

/**
 * Pins edition service
 * @author jbarberet
 *
 */
public interface PinsEditionService {

	/**
	 * Get edition pins form.
	 * @param portalControllerContext
	 * @param reinit
	 * @return
	 * @throws PortletException
	 */
    PinsEditionForm getForm(PortalControllerContext portalControllerContext, boolean reinit) throws PortletException;
	
    /**
     * Save pins form.
     *
     * @param portalControllerContext portal controller context
     * @param form workspace edition form
     * @throws PortletException
     */
    void save(PortalControllerContext portalControllerContext, PinsEditionForm form) throws PortletException;

    
    /**
     * Sort pins
     * @param portalControllerContext
     * @param form
     * @throws PortletException
     */
    void sort(PortalControllerContext portalControllerContext, PinsEditionForm form) throws PortletException;
    
    /**
     * Get workspace Url
     * @param portalControllerContext
     * @param form
     * @return
     * @throws PortletException
     */
    String getWorkspaceUrl(PortalControllerContext portalControllerContext, PinsEditionForm form) throws PortletException;
    
    /**
     * Set application context
     * @param applicationContext
     * @throws BeansException
     */
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
    
    /**
     * Search documents to pin
     * @param portalControllerContext
     * @param workspace
     * @param filter
     * @return
     */
    JSONObject searchDocuments(PortalControllerContext portalControllerContext, Document workspace, String filter, int page, HashMap<String, String> mapPinnedDocuments) throws PortletException;
    
    /**
     * Pin document and had it to the list of pinned documents
     * @param portalControllerContext
     * @param form
     */
    void addPin(PortalControllerContext portalControllerContext, PinsEditionForm form);
}
