package org.osivia.services.workspace.portlet.repository;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Restore documents Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see TrashCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RestoreDocumentsCommand extends TrashCommand {

    /**
     * Constructor.
     * 
     * @param basePath base path
     */
    public RestoreDocumentsCommand(String basePath) {
        super(basePath, null);
    }

    /**
     * Constructor.
     * 
     * @param selectedPaths selected item paths
     */
    public RestoreDocumentsCommand(List<String> selectedPaths) {
        super(null, selectedPaths);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperationName() {
        return "Services.RestoreDocuments";
    }

}
