/**
 * 
 */
package org.osivia.services.versions.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author david
 *
 */
public class GetListVersionsCommand implements INuxeoCommand {

    /** Id of Nuxeo command. */
    private final static String NX_QUERY_OP_ID = "Document.Query";

    /** Default page size (pagination). */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /** Current document. */
    private Document document;
    /** Page size (pagination). */
    private int pageSize;
    /** Page index (pagination) */
    private int currentPageIndex;

    /**
     * Default constructor.
     * 
     * @param document
     */
    public GetListVersionsCommand(Document document) {
        this.document = document;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    /**
     * Constructor.
     * 
     * @param document
     * @param pageSize
     * @param currentPageIndex
     */
    public GetListVersionsCommand(Document document, int pageSize, int currentPageIndex) {
        this.document = document;
        this.pageSize = pageSize;
    }


    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }


    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Documents execute(Session nuxeoSession) throws Exception {
        StringBuffer query = new StringBuffer().append("select * from Document where ecm:isVersion = 1 and ecm:versionVersionableId = '")
                .append(this.document.getId()).append("' order by dc:modified desc");

        OperationRequest request = nuxeoSession.newRequest(NX_QUERY_OP_ID).setHeader(Constants.HEADER_NX_SCHEMAS, "*").setInput(this.document)
                .set("query", query.toString()).set("pageSize", this.pageSize).set("currentPageIndex", this.currentPageIndex);
        return (Documents) request.execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return GetListVersionsCommand.class.getName().concat(" | ").concat(this.document.getId());
    }

}
