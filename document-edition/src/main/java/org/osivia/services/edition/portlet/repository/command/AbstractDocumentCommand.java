package org.osivia.services.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import java.util.List;
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
    private PropertyMap properties;
    /**
     * Document binaries.
     */
    private Map<String, List<Blob>> binaries;


    /**
     * Constructor.
     */
    public AbstractDocumentCommand() {
        super();
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
            for (Map.Entry<String, List<Blob>> entry : this.binaries.entrySet()) {
                String xpath = entry.getKey();
                List<Blob> blobs = entry.getValue();

                if (CollectionUtils.isEmpty(blobs)) {
                    documentService.removeBlob(document, xpath);
                } else if (blobs.size() == 1) {
                    documentService.setBlob(document, blobs.get(0), xpath);
                } else {
                    documentService.setBlobs(document, new Blobs(blobs), xpath);
                }
            }
        }
    }


    public PropertyMap getProperties() {
        return properties;
    }

    public void setProperties(PropertyMap properties) {
        this.properties = properties;
    }

    public Map<String, List<Blob>> getBinaries() {
        return binaries;
    }

    public void setBinaries(Map<String, List<Blob>> binaries) {
        this.binaries = binaries;
    }
}
