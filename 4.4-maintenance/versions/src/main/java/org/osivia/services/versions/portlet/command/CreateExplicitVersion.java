/**
 * 
 */
package org.osivia.services.versions.portlet.command;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author david
 *
 */
public class CreateExplicitVersion implements INuxeoCommand {
    
    /** Nx id operation. */
    private static final String NX_OPERATION_ID = "Document.CreateExplicitVersion";
    
    /** Comment of version. */
    private String comment = StringUtils.EMPTY;
    
    /** Document to versioned. */
    private Document document;
    
    /**
     * Constructor.
     */
    public CreateExplicitVersion(Document document, String comment){
        this.document = document;
        this.comment = comment;
    }

   /**
    * {@inheritDoc}
    */
    @Override
    public Document execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NX_OPERATION_ID).setInput(document)
                .set("comment", comment);
        return (Document) request.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return CreateExplicitVersion.class.getName().concat(" | ");
    }

}
