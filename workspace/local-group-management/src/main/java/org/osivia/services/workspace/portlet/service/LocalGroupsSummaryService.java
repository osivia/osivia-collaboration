package org.osivia.services.workspace.portlet.service;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.LocalGroupsSort;
import org.osivia.services.workspace.portlet.model.LocalGroupsSummary;

/**
 * Local groups summary portlet service interface.
 * 
 * @author Cédric Krommenhoek
 * @see LocalGroupsService
 */
public interface LocalGroupsSummaryService extends LocalGroupsService {

    /**
     * Sort local groups.
     * 
     * @param portalControllerContext portal controller context
     * @param summary summary
     * @param sort sort property
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sort(PortalControllerContext portalControllerContext, LocalGroupsSummary summary, LocalGroupsSort sort, boolean alt) throws PortletException;


    /**
     * Delete local groups.
     * 
     * @param portalControllerContext portal controller context
     * @param summary summary
     * @param identifiers selection identifiers
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, LocalGroupsSummary summary, String[] identifiers) throws PortletException;


    /**
     * Get local groups table toolbar.
     * 
     * @param portalControllerContext portal controller context
     * @param indexes selected row indexes
     * @return DOM element
     * @throws PortletException
     * @throws IOException
     */
    Element getToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException, IOException;

}
