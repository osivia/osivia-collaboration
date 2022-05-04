package org.osivia.services.edition.portlet.repository;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.edition.portlet.model.DocumentEditionMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Metadata edition portlet repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see DocumentEditionMetadataRepository
 */
@Repository
public class DocumentEditionMetadataRepositoryImpl implements DocumentEditionMetadataRepository {

    /**
     * Description Nuxeo document property.
     */
    private static final String DESCRIPTION_PROPERTY = "dc:description";
    /**
     * Author Nuxeo document property.
     */
    private static final String AUTHOR_PROPERTY = "dc:creator";
    /**
     * Keywords Nuxeo document property.
     */
    private static final String KEYWORDS_PROPERTY = "dc:subjects";


    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public DocumentEditionMetadataRepositoryImpl() {
        super();
    }


    @Override
    public DocumentEditionMetadata getMetadata(PortalControllerContext portalControllerContext, Document document) {
        DocumentEditionMetadata metadata = this.applicationContext.getBean(DocumentEditionMetadata.class);

        // Description
        String description = document.getString(DESCRIPTION_PROPERTY);
        metadata.setDescription(description);

        // Vignette
        // TODO

        return metadata;
    }


    @Override
    public void customizeProperties(DocumentEditionMetadata metadata, PropertyMap properties) {
        // Description
        String description = metadata.getDescription();
        properties.set(DESCRIPTION_PROPERTY, description);

        // Vignette
        // TODO
    }

}
