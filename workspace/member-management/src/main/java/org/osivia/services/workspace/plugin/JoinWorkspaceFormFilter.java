package org.osivia.services.workspace.plugin;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;

/**
 * Join workspace form filter.
 * 
 * @author Cédric Krommenhoek
 * @see IFormFilterModule
 */
public class JoinWorkspaceFormFilter implements FormFilter {

    /** Form filter identifier. */
    private static final String IDENTIFIER = "JOIN_WORKSPACE";
    /** Form filter internationalization key. */
    private static final String INTERNATIONALIZATION_KEY = "JOIN_WORKSPACE_FORM_FILTER";


    /**
     * Constructor.
     */
    public JoinWorkspaceFormFilter() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return IDENTIFIER;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey() {
        return INTERNATIONALIZATION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) {
        // TODO Auto-generated method stub

    }

}
