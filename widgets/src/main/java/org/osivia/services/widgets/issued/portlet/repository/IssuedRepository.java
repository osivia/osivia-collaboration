package org.osivia.services.widgets.issued.portlet.repository;

import java.util.Date;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Issued date portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface IssuedRepository {

    /** Document issued date Dublin Core property. */
    String ISSUED_DATE_DUBLIN_CORE_PROPERTY = "dc:issued";
    /** Document issued date Toutatice property. */
    String ISSUED_DATE_TOUTATICE_PROPERTY = "ttc:publicationDate";


    /**
     * Get issued date.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @return date
     * @throws PortletException
     */
    Date getIssuedDate(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Set issued date.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @param date issued date
     * @return true if document has been published
     * @throws PortletException
     */
    boolean setIssuedDate(PortalControllerContext portalControllerContext, String path, Date date) throws PortletException;

}
