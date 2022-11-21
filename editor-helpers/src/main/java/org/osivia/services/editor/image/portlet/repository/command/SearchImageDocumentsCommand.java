package org.osivia.services.editor.image.portlet.repository.command;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.editor.common.model.SearchScope;
import org.osivia.services.editor.common.repository.command.SearchSourceDocumentCommand;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Search image documents Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see SearchSourceDocumentCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchImageDocumentsCommand extends SearchSourceDocumentCommand {

    /**
     * Constructor.
     */
    public SearchImageDocumentsCommand() {
        super();
    }


    @Override
    protected String getClause() {
        StringBuilder clause = new StringBuilder();
        clause.append("(ecm:primaryType = 'Picture' OR (ecm:primaryType = 'File' AND file:content/mime-type LIKE 'image%')) ");
        if (StringUtils.isNotBlank(this.getFilter())) {
            clause.append("AND (dc:title ILIKE '%").append(this.getFilter()).append("%' OR ecm:fulltext = '").append(this.getFilter()).append("') ");
        }
        if (SearchScope.WORKSPACE.equals(this.getScope())) {
            clause.append("AND ecm:path STARTSWITH '").append(this.getBasePath()).append("' ");
        }

        return clause.toString();
    }

}
