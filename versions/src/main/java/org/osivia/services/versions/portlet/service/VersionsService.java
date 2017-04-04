/**
 * 
 */
package org.osivia.services.versions.portlet.service;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osivia.services.versions.portlet.model.Version;
import org.osivia.services.versions.portlet.model.Versions;


/**
 * @author david
 *
 */
public interface VersionsService {
    
    /**
     * @param request
     * @param response
     * @param portletContext
     * @return list of versions of current doc.
     * @throws PortletException
     */
    public Versions getListVersions(PortletRequest request, PortletResponse response, PortletContext portletContext) throws PortletException;
    
    /**
     * Restore a version.
     * 
     * @param request
     * @param response
     * @param portletContext
     * @param versionId
     */
    public void restoreVersion(PortletRequest request, PortletResponse response, PortletContext portletContext, 
            String versionId);
    
    /**
     * Create an explicit version.
     * 
     * @param request
     * @param response
     * @param portletContext
     * @param comment
     * @return version
     * @throws PortletException
     */
    public void createVersion(PortletRequest request, PortletResponse response, PortletContext portletContext, Versions versions, Version form) throws PortletException;

}
