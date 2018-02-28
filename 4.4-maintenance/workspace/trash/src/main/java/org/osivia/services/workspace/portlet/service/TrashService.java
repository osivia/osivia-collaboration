package org.osivia.services.workspace.portlet.service;

import javax.portlet.PortletException;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.services.workspace.portlet.model.TrashForm;

/**
 * Trash portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface TrashService {

    /** Task identifier. */
    String TASK_ID = "TRASH";


    /**
     * Get trash form.
     * 
     * @param portalControllerContext portal controller context
     * @return trash form
     * @throws PortletException
     */
    TrashForm getTrashForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort trash form.
     * 
     * @param portalControllerContext portal controller context
     * @param form trash form
     * @param sort sort property
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sort(PortalControllerContext portalControllerContext, TrashForm form, String sort, boolean alt) throws PortletException;


    /**
     * Add menubar items.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void addMenubarItems(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Empty trash.
     * 
     * @param portalControllerContext portal controller context
     * @param form trash form
     * @throws PortletException
     */
    void emptyTrash(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException;


    /**
     * Restore all items.
     * 
     * @param portalControllerContext portal controller context
     * @param form trash form
     * @throws PortletException
     */
    void restoreAll(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException;


    /**
     * Delete selected items.
     * 
     * @param portalControllerContext portal controller context
     * @param form trash form
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException;


    /**
     * Restore selected items.
     * 
     * @param portalControllerContext portal controller context
     * @param form trash form
     * @throws PortletException
     */
    void restore(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException;


    /**
     * Get toolbar message.
     * 
     * @param portalControllerContext portal controller context
     * @param count selection count
     * @return message
     * @throws PortletException
     */
    String getToolbarMessage(PortalControllerContext portalControllerContext, int count) throws PortletException;


    /**
     * Get location breadcrumb DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param path location path
     * @return DOM element
     * @throws PortletException
     */
    Element getLocationBreadcrumb(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get player.
     * 
     * @param portalControllerContext portal controller context
     * @return player
     * @throws PortletException
     */
    PanelPlayer getPlayer(PortalControllerContext portalControllerContext) throws PortletException;

}
