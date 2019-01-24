package org.osivia.services.workspace.sharing.portlet.repository.command;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.osivia.services.workspace.sharing.portlet.repository.SharingRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONArray;

/**
 * Get sharing permissions Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetSharingPermissionsCommand implements INuxeoCommand {

    /** Operation identifier. */
    private static final String OPERATION_ID = "Document.GetSharingPermissions";


    /** Document path. */
    private final String path;


    /**
     * Constructor.
     * 
     * @param path document path
     */
    public GetSharingPermissionsCommand(String path) {
        super();
        this.path = path;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document reference
        DocRef ref = new PathRef(this.path);

        // Operation request
        OperationRequest request = nuxeoSession.newRequest(OPERATION_ID);
        request.setHeader(SharingRepository.ES_SYNC_FLAG, String.valueOf(true));
        request.setInput(ref);

        // Execution
        Blob blob = (Blob) request.execute();

        // Result content
        String content = IOUtils.toString(blob.getStream(), CharEncoding.UTF_8);

        // JSON array
        return JSONArray.fromObject(content);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append("|");
        builder.append(this.path);
        return builder.toString();
    }

}
