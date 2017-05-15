/**
 * 
 */
package org.osivia.services.versions.portlet.service.impl;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.services.versions.portlet.command.CreateExplicitVersion;
import org.osivia.services.versions.portlet.command.GetListVersionsCommand;
import org.osivia.services.versions.portlet.command.RestoreVersionCommand;
import org.osivia.services.versions.portlet.dao.DataToModel;
import org.osivia.services.versions.portlet.model.Version;
import org.osivia.services.versions.portlet.model.Versions;
import org.osivia.services.versions.portlet.service.VersionsService;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;


/**
 * @author david
 *
 */
@Service
public class VersionsServiceImpl implements VersionsService {
    
    /** DAO. */
    private DataToModel dataToModel;
    
    /**
     * Constructor.
     */
    public VersionsServiceImpl(){
        this.dataToModel = DataToModel.getInstance();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Versions getListVersions(PortletRequest request, PortletResponse response, PortletContext portletContext) throws PortletException {
        NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        Document currentDoc = getCurrentDocument(request, response, portletContext);
        Documents versionsAsDocs = (Documents) nuxeoController.executeNuxeoCommand(new GetListVersionsCommand(currentDoc));
        return dataToModel.toVersions(versionsAsDocs);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreVersion(PortletRequest request, PortletResponse response, PortletContext portletContext, 
            String versionId) {
        NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        nuxeoController.executeNuxeoCommand(new RestoreVersionCommand(versionId));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void createVersion(PortletRequest request, PortletResponse response, PortletContext portletContext, Versions versions, Version form) throws PortletException{
        NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        Document currentDocument = getCurrentDocument(request, response, portletContext);
        Document document = (Document) nuxeoController.executeNuxeoCommand(new CreateExplicitVersion(currentDocument, form.getComment()));
        // Update of model attribute
        versions.add(dataToModel.toVersion(document));
    }
    
    /**
     * @param request
     * @param response
     * @param portletContext
     * @return the current document.
     * @throws PortletException
     */
    public Document getCurrentDocument(PortletRequest request, PortletResponse response, PortletContext portletContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();
        return documentContext.getDocument();
    }

}
