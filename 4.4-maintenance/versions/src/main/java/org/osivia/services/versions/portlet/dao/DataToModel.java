/**
 * 
 */
package org.osivia.services.versions.portlet.dao;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.services.versions.portlet.model.Version;
import org.osivia.services.versions.portlet.model.Versions;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;


/**
 * @author david
 *
 */
public final class DataToModel {
    
    /** Document property in which the major version is stored. */
    public final static String MAJOR_VERSION_PROP = "uid:major_version";

    /** Document property in which the minor version is stored. */
    public final static String MINOR_VERSION_PROP = "uid:minor_version";
    
    /** Instance. */
    private static DataToModel dataToModel;
    
    /** Toutatice DAO. */
    private DocumentDAO documentDao;
    
    /**
     * Constructor.
     */
    private DataToModel(){
        super();
        this.documentDao = DocumentDAO.getInstance();
    }
    
    /**
     * @return singleton.
     */
    public synchronized static DataToModel getInstance(){
        if(dataToModel == null){
            dataToModel = new DataToModel();
        }
        return dataToModel;
    }
    
    /**
     * @param document
     * @return document converted to Version.
     */
    public Version toVersion(Document document) {
        Version version = null;
        if(document != null){
            // Default DAO 
            DocumentDTO versionDto = this.documentDao.toDTO(document);
            version = new Version(versionDto);
            // FIXME: how to get cutom label?
            version.setLabel(document.getVersionLabel());
            // TODO: currentVersion
        }
        return version;
    }
    
    /**
     * @param documents
     * @return documents converted to Versions.
     */
    public Versions toVersions(Documents documents){
        Versions versions = null;
        if(documents != null){
            versions = new Versions();
            for(Document document : documents){
                versions.add(toVersion(document));
            }
        }
        return versions;
    }
    
}
