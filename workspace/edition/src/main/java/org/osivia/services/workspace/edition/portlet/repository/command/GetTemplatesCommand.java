package org.osivia.services.workspace.edition.portlet.repository.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get templates Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetTemplatesCommand implements INuxeoCommand {

    /** Operation request identifier. */
    private static final String OPERATION_ID = "Context.GetWebConfigurations";
    /** Schemas. */
    private static final String SCHEMAS = "dublincore, common, toutatice, webconfiguration";
    /** Configuration type. */
    private static final String CONF_TYPE = "pagetemplate";


    /** Domain path. */
    private final String domainPath;

    /**
     * Constructor.
     * 
     * @param domainPath domain path
     */
    public GetTemplatesCommand(String domainPath) {
        super();
        this.domainPath = domainPath;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest request = nuxeoSession.newRequest(OPERATION_ID);
        request.setHeader(Constants.HEADER_NX_SCHEMAS, SCHEMAS);
        request.set("domainPath", this.domainPath);
        request.set("confType", CONF_TYPE);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName());
        builder.append(this.domainPath);
        return builder.toString();
    }

}
