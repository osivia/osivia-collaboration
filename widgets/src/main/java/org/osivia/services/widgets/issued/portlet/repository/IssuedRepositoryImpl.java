package org.osivia.services.widgets.issued.portlet.repository;

import java.util.Date;

import javax.portlet.PortletException;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.widgets.issued.portlet.repository.command.UpdateIssuedCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;

/**
 * Issued date portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see IssuedRepository
 */
@Repository
public class IssuedRepositoryImpl implements IssuedRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public IssuedRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Date getIssuedDate(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Document context
        NuxeoDocumentContext documentContext = this.getDocumentContext(portalControllerContext, path, true);
        documentContext.reload();

        // Document
        Document document = documentContext.getDocument();

        // Issued date
        Date date = document.getDate(ISSUED_DATE_TOUTATICE_PROPERTY);
        if (date == null) {
            try {
                NuxeoDocumentContext proxyDocumentContext = this.getDocumentContext(portalControllerContext, path, false);
                Document proxy = proxyDocumentContext.getDocument();
                date = proxy.getDate(ISSUED_DATE_DUBLIN_CORE_PROPERTY);
            } catch (NuxeoException e) {
                // Do nothing: proxy does not exists
            }
        }

        return date;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setIssuedDate(PortalControllerContext portalControllerContext, String path, Date date) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = this.getDocumentContext(portalControllerContext, path, true);
        // Publication infos
        NuxeoPublicationInfos publicationInfos = documentContext.getPublicationInfos();
        // Document
        Document document = documentContext.getDocument();
        // Publish indicator
        boolean publish = publicationInfos.isPublished() && !publicationInfos.isBeingModified();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateIssuedCommand.class, document, date, publish);
        nuxeoController.executeNuxeoCommand(command);

        return publish;
    }


    /**
     * Get document context.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @param live live indicator
     * @return document context
     * @throws PortletException
     */
    private NuxeoDocumentContext getDocumentContext(PortalControllerContext portalControllerContext, String path, boolean live) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        if (live) {
            nuxeoController.setDisplayLiveVersion("1");
        }

        return nuxeoController.getDocumentContext(path);
    }

}
