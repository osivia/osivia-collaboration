package org.osivia.services.workspace.plugin;

import java.util.HashMap;
import java.util.Map;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

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

    private static final String INTERNATIONALIZATION_KEY_LABEL = "JOIN_WORKSPACE_FORM_FILTER_DESCRIPTION";


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
    public void execute(FormFilterContext context, FormFilterExecutor executor) {
        // TODO Auto-generated method stub

    }


    @Override
    public String getLabelKey() {
        return INTERNATIONALIZATION_KEY;
    }


    @Override
    public String getDescriptionKey() {
        return INTERNATIONALIZATION_KEY_LABEL;
    }


    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        Map<String, FormFilterParameterType> paramsMap = new HashMap<String, FormFilterParameterType>(2);
        paramsMap.put("uid", FormFilterParameterType.TEXT);
        paramsMap.put("workspaceId", FormFilterParameterType.TEXT);
        return paramsMap;
    }

}
