package org.osivia.services.edition.portlet.repository.command;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * @param session         Nuxeo session
     * @param documentService document service
     * @param document        document
     */
    protected void updateBinaries(Session session, DocumentService documentService, DocRef document) throws Exception {
        if (MapUtils.isNotEmpty(this.binaries)) {
            for (Map.Entry<String, List<Blob>> entry : this.binaries.entrySet()) {
                String xpath = entry.getKey();
                List<Blob> blobs = entry.getValue();

                if (CollectionUtils.isEmpty(blobs)) {
                    documentService.removeBlob(document, xpath);
                } else if (blobs.size() == 1) {
                    Blob resolvedBlob = this.resolveBlobRef(session, blobs.get(0));
                    documentService.setBlob(document, resolvedBlob, xpath);
                } else {
                    List<Blob> resolvedBlobs = blobs.stream().map(item -> this.resolveBlobRef(session, item)).collect(Collectors.toList());
                    documentService.setBlobs(document, new Blobs(resolvedBlobs), xpath);
                }
            }
        }
    }


    /**
     * Resolve blob reference.
     *
     * @param session Nuxeo session
     * @param blob    blob, maybe a blob reference
     * @return blob
     */
    private Blob resolveBlobRef(Session session, Blob blob) {
        Blob result;

        if (blob instanceof BlobRef) {
            BlobRef blobRef = (BlobRef) blob;
            try {
                result = session.getFile(blobRef.getRef());
            } catch (Exception e) {
                result = blob;
            }
        } else {
            result = blob;
        }

        return result;
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
