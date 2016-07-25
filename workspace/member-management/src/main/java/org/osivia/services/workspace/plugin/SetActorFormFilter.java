package org.osivia.services.workspace.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormActors;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

public class SetActorFormFilter implements FormFilter {

    @Override
    public String getId() {
        return "SET_ACTOR";
    }

    @Override
    public String getLabelKey() {
        return "SET_ACTOR_LABEL";
    }

    @Override
    public String getDescriptionKey() {
        return null;
    }

    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        Map<String, FormFilterParameterType> parameters = new HashMap<>();
        parameters.put("actor", FormFilterParameterType.TEXT);
        return parameters;
    }

    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) {
        // Actors
        FormActors actors = context.getActors();

        // Variables
        Map<String, String> variables = context.getVariables();

        // Actor
        String actor = context.getParamValue(executor, "actor");

        if (StringUtils.isNotBlank(actor)) {
            Pattern pattern = Pattern.compile("^\\$\\{([^\\$]+)\\}$");
            Matcher matcher = pattern.matcher(actor);

            if (matcher.matches()) {
                actor = variables.get(matcher.group(1));
            }

            if (actor != null) {
                actors.getUsers().add(actor);
            }
        }
    }

}
