package org.osivia.services.workspace.sharing.plugin.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;
import org.apache.commons.collections.CollectionUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;
import java.util.List;

/**
 * Sharing list template module.
 *
 * @author Cédric Krommenhoek
 * @see PortletModule
 */
public class SharingListTemplateModule extends PortletModule {

    @Autowired
    private IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public SharingListTemplateModule(PortletContext portletContext) {
        super(portletContext);
    }


    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) {
        List<?> documents = (List<?>) request.getAttribute("documents");

        if (CollectionUtils.isNotEmpty(documents)) {
            PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

            for (Object object : documents) {
                DocumentDTO documentDto = (DocumentDTO) object;

                String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, documentDto.getPath(), null, IPortalUrlFactory.CONTEXTUALIZATION_PORTAL, null, null, null, null, null);
                documentDto.getProperties().put("url", url);
            }
        }
    }
}
