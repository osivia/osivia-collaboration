package org.osivia.services.editor.link.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;

/**
 * Editor link portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface EditorLinkService {

    /**
     * Get editor link form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    EditorLinkForm getForm(PortalControllerContext portalControllerContext) throws PortletException;

}
