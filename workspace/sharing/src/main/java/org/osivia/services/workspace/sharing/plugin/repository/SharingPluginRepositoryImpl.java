package org.osivia.services.workspace.sharing.plugin.repository;

import org.osivia.services.workspace.sharing.common.repository.SharingCommonRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * Sharing plugin repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SharingCommonRepositoryImpl
 * @see SharingPluginRepository
 */
@Repository
public class SharingPluginRepositoryImpl extends SharingCommonRepositoryImpl implements SharingPluginRepository {

    /**
     * Constructor.
     */
    public SharingPluginRepositoryImpl() {
        super();
    }

}
