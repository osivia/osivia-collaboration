package org.osivia.services.workspace.portlet.service;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.services.workspace.portlet.model.TrashForm;
import org.osivia.services.workspace.portlet.model.TrashFormSort;

import javax.portlet.PortletException;
import java.io.IOException;
import java.util.List;

/**
 * Trash portlet service interface.
 *
 * @author Cédric Krommenhoek
 */
public interface TrashService {

    /**
     * Task identifier.
     */
    String TASK_ID = "TRASH";


    /**
     * Get trash form.
     *
     * @param portalControllerContext portal controller context
     * @return trash form
     */
    TrashForm getTrashForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort trash form.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     * @param sort                    sort property
     * @param alt                     alternative sort indicator
     */
    void sort(PortalControllerContext portalControllerContext, TrashForm form, TrashFormSort sort, boolean alt) throws PortletException;


    /**
     * Add menubar items.
     *
     * @param portalControllerContext portal controller context
     */
    void addMenubarItems(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Empty trash.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     */
    void emptyTrash(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException;


    /**
     * Restore all items.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     */
    void restoreAll(PortalControllerContext portalControllerContext, TrashForm form) throws PortletException;


    /**
     * Delete selected items.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     * @param identifiers             selection identifiers
     */
    void delete(PortalControllerContext portalControllerContext, TrashForm form, String[] identifiers) throws PortletException;


    /**
     * Restore selected items.
     *
     * @param portalControllerContext portal controller context
     * @param form                    trash form
     * @param identifiers             selection identifiers
     */
    void restore(PortalControllerContext portalControllerContext, TrashForm form, String[] identifiers) throws PortletException;


    /**
     * Get toolbar DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param indexes                 selected row indexes
     * @return DOM element
     */
    Element getToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException, IOException;


    /**
     * Get location breadcrumb DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param path                    location path
     * @return DOM element
     */
    Element getLocationBreadcrumb(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Get player.
     *
     * @param portalControllerContext portal controller context
     * @return player
     */
    PanelPlayer getPlayer(PortalControllerContext portalControllerContext) throws PortletException;

}
