package org.osivia.services.forum.plugin.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;

/**
 * Forum list template module.
 * 
 * @author Cédric Krommenhoek
 * @see PortletModule
 */
public class ForumListTemplateModule extends PortletModule {

    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public ForumListTemplateModule(PortletContext portletContext) {
        super(portletContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Documents
        List<?> documents = (List<?>) request.getAttribute("documents");

        if (CollectionUtils.isNotEmpty(documents)) {
            // Forums
            List<DocumentDTO> forums = new ArrayList<>(documents.size());
            // Threads
            List<DocumentDTO> threads = new ArrayList<>(documents.size());

            for (Object object : documents) {
                if (object instanceof DocumentDTO) {
                    DocumentDTO document = (DocumentDTO) object;

                    // Type
                    String type;
                    if (document.getType() == null) {
                        type = null;
                    } else {
                        type = document.getType().getName();
                    }

                    if (StringUtils.equals("Forum", type)) {
                        forums.add(document);
                    } else if (StringUtils.equals("Thread", type)) {
                        threads.add(document);
                    }
                }
            }

            request.setAttribute("forums", forums);
            request.setAttribute("threads", threads);
        }
    }

}
