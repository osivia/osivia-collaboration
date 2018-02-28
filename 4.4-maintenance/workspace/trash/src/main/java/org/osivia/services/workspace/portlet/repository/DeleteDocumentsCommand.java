package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Delete documents Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see TrashCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeleteDocumentsCommand extends TrashCommand {

    /**
     * Constructor.
     * 
     * @param basePath base path
     */
    public DeleteDocumentsCommand(String basePath) {
        super(basePath, null);
    }

    /**
     * Constructor.
     * 
     * @param selectedPaths selected item paths
     */
    public DeleteDocumentsCommand(List<String> selectedPaths) {
        super(null, selectedPaths);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperationName() {
        return "Services.PurgeDocuments";
    }

}
