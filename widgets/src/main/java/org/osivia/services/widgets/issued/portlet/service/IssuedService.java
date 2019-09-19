package org.osivia.services.widgets.issued.portlet.service;

import java.io.IOException;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.issued.portlet.model.IssuedForm;

/**
 * Issued date portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface IssuedService {

    /** Document path window property . */
    String DOCUMENT_PATH_WINDOW_PROPERTY = "osivia.issued.path";


    /**
     * Get issued date form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    IssuedForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Save.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @throws PortletException
     * @throws IOException
     */
    void save(PortalControllerContext portalControllerContext, IssuedForm form) throws PortletException, IOException;

}
