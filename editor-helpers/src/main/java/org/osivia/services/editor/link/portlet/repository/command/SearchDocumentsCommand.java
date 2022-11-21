package org.osivia.services.editor.link.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.lang.StringUtils;
import org.osivia.services.editor.common.model.SearchScope;
import org.osivia.services.editor.common.repository.command.SearchSourceDocumentCommand;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
        clause.append("ecm:primaryType IN ('Annonce', 'Audio', 'ContextualLink', 'DocumentUrlContainer', 'File', 'Folder', 'Forum', 'Note', 'Picture', 'PictureBook', 'Question', 'Thread', 'ToutaticePad', 'VEVENT', 'Video') ");
        if (StringUtils.isNotBlank(this.getFilter())) {
            clause.append("AND (dc:title ILIKE '%").append(this.getFilter()).append("%' OR ecm:fulltext = '").append(this.getFilter()).append("') ");
        }
        if (SearchScope.WORKSPACE.equals(this.getScope())) {
            clause.append("AND ecm:path STARTSWITH '").append(this.getBasePath()).append("' ");
        }

        return clause.toString();
    }

}
