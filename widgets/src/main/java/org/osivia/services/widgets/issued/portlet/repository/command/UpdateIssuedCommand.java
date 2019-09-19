package org.osivia.services.widgets.issued.portlet.repository.command;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.widgets.issued.portlet.repository.IssuedRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Update issued date Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateIssuedCommand implements INuxeoCommand {

    /** Operation request identifier. */
    private static final String ID = "Document.TTCPPublish";


    /** Document. */
    private final Document document;
    /** Updated issued date. */
    private final Date date;
    /** Publish indicator. */
    private final boolean publish;


    /**
     * Constructor.
     * 
     * @param document document
     * @param date updated issued date
     * @param publish publish indicator
     */
    public UpdateIssuedCommand(Document document, Date date, boolean publish) {
        super();
        this.document = document;
        this.date = date;
        this.publish = publish;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Update issued date
        this.updateIssuedDate(nuxeoSession);

        // Publish
        if (this.publish) {
            this.publish(nuxeoSession);
        }

        return null;
    }


    /**
     * Update document issued date.
     * 
     * @param nuxeoSession Nuxeo session
     * @throws Exception
     */
    private void updateIssuedDate(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Properties
        PropertyMap properties = new PropertyMap(1);
        properties.set(IssuedRepository.ISSUED_DATE_TOUTATICE_PROPERTY, this.date);

        documentService.update(this.document, properties);
    }


    /**
     * Publish document.
     * 
     * @param nuxeoSession Nuxeo session
     * @throws Exception
     */
    private void publish(Session nuxeoSession) throws Exception {
        // Parent
        String parentPath = StringUtils.substringBeforeLast(this.document.getPath(), "/");
        DocRef parent = new PathRef(parentPath);

        // Operation request
        OperationRequest request = nuxeoSession.newRequest(ID);
        request.setInput(this.document);
        request.set("target", parent);
        
        request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
