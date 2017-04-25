package org.osivia.services.widgets.plugin.theming;

import java.util.Map;

import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.theming.TabGroup;

/**
 * Search tab group.
 *
 * @author Cédric Krommenhoek
 * @see TabGroup
 */
public class SearchTabGroup implements TabGroup {

    /** Group name. */
    private static final String NAME = "search";
    /** Group icon. */
    private static final String ICON = "halflings halflings-search";
    /** Group label internationalization key. */
    private static final String LABEL_KEY = "SEARCH";


    /**
     * Constructor.
     */
    public SearchTabGroup() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon() {
        return ICON;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelKey() {
        return LABEL_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(PortalControllerContext portalControllerContext, EcmDocument document, String type, Map<String, String> pageProperties) {
        return NAME.equals(type);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean maintains(PortalControllerContext portalControllerContext, EcmDocument document, String type, Map<String, String> pageProperties) {
        return false;
    }

}
