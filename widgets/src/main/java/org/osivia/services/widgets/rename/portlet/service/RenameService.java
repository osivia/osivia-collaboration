package org.osivia.services.widgets.rename.portlet.service;

import java.io.IOException;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.rename.portlet.model.RenameForm;

/**
 * Rename portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface RenameService {

    /** Document path window property . */
    String DOCUMENT_PATH_WINDOW_PROPERTY = "osivia.rename.path";


    /**
     * Get rename form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    RenameForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     * @throws IOException
     */
    void save(PortalControllerContext portalControllerContext, RenameForm form) throws PortletException, IOException;

}
