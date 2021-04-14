package org.osivia.services.editor.link.portlet.repository.command;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.editor.common.model.SearchScope;
import org.osivia.services.editor.common.repository.command.SearchSourceDocumentCommand;
import org.osivia.services.editor.link.portlet.repository.EditorLinkRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Search documents Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchDocumentsCommand extends SearchSourceDocumentCommand {

    /**
     * Constructor.
     */
    public SearchDocumentsCommand() {
        super();
    }


    @Override
    protected String getClause() {
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType NOT IN ('Domain', 'TemplateRoot', 'WorkspaceRoot', 'ProceduresContainer', 'ProceduresModelsContainer', 'ProcedureModel', 'ProceduresInstancesContainer', 'ProcedureInstance') ");
        if (StringUtils.isNotBlank(this.getFilter())) {
            clause.append("AND (dc:title ILIKE '").append(this.getFilter()).append("%' OR ecm:fulltext = '").append(this.getFilter()).append("') ");
        }
        if (SearchScope.WORKSPACE.equals(this.getScope())) {
            clause.append("AND ecm:path STARTSWITH '").append(this.getBasePath()).append("' ");
        }

        return clause.toString();
    }

}
