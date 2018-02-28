/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package fr.toutatice.faq.portail;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * FAQ fetch children command.
 *
 * @author Jean-Sébastien Steux
 * @see INuxeoCommand
 */
public class FaqFetchChildrenCommand implements INuxeoCommand {

    /** Live filter indicator. */
    private NuxeoQueryFilterContext queryCtx;
    /** Live identifier. */
    private String liveId;


    /**
     * Constructor.
     *
     * @param queryCtx Nuxeo query filter context
     * @param liveId live identifier
     */
    public FaqFetchChildrenCommand(NuxeoQueryFilterContext queryCtx, String liveId) {
        super();

        this.liveId = liveId;
        this.queryCtx = queryCtx;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session session) throws Exception {
        String nuxeoRequest = "ecm:parentId = '" + this.liveId + "' AND ecm:primaryType = 'Question' ORDER BY ecm:pos ";

        // Insertion du filtre sur les élements publiés
        String filteredRequest = NuxeoQueryFilter.addPublicationFilter(this.queryCtx, nuxeoRequest);

        OperationRequest request = session.newRequest("Document.Query");
        request.set("query", "SELECT * FROM Document WHERE " + filteredRequest);
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Faq/" + this.liveId;
    }

}
