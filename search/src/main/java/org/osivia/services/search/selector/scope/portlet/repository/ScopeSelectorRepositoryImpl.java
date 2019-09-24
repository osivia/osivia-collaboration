package org.osivia.services.search.selector.scope.portlet.repository;

import org.osivia.services.search.common.repository.CommonRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Scope selector portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CommonRepositoryImpl
 * @see ScopeSelectorRepository
 */
@Repository
public class ScopeSelectorRepositoryImpl extends CommonRepositoryImpl implements ScopeSelectorRepository {

    /**
     * Constructor.
     */
    public ScopeSelectorRepositoryImpl() {
        super();
    }

}
