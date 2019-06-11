package org.osivia.services.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.collections.MapUtils;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import java.util.Map;

/**
 * Document Nuxeo command abstract super-class.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public abstract class AbstractDocumentCommand implements INuxeoCommand {

    /**
     * Document properties.
     */
    private final PropertyMap properties;
    /**
     * Document binaries.
     */
    private final Map<String, Blob> binaries;


    /**
     * Constructor.
     *
     * @param properties document properties
     * @param binaries   document binaries
     */
    public AbstractDocumentCommand(PropertyMap properties, Map<String, Blob> binaries) {
        super();
        this.properties = properties;
        this.binaries = binaries;
    }


    @Override
    public String getId() {
        return null;
    }


    /**
     * Update document binaries.
     *
     * @param documentService document service
     * @param document        document
     */
    void updateBinaries(DocumentService documentService, DocRef document) throws Exception {
        if (MapUtils.isNotEmpty(this.binaries)) {
            for (Map.Entry<String, Blob> entry : this.binaries.entrySet()) {
                String xpath = entry.getKey();
                Blob blob = entry.getValue();

                if (blob == null) {
                    documentService.removeBlob(document, xpath);
                } else {
                    documentService.setBlob(document, blob, xpath);
                }
            }
        }
    }


    public PropertyMap getProperties() {
        return properties;
    }

}
