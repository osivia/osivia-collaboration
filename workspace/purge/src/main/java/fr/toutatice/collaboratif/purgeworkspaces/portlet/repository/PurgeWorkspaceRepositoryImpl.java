package fr.toutatice.collaboratif.purgeworkspaces.portlet.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.PurgeWorkspaceOptions;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.WorkspaceLine;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command.ListDeletedWorkspaceCommand;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command.ListWorkspaceCommand;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command.PutInTrashWorkspaceCommand;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.command.RestoreWorkspaceCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Purge workspace repository implementation
 * @author Julien Barberet
 *
 */
@Repository
public class PurgeWorkspaceRepositoryImpl implements PurgeWorkspaceRepository {

	private static final String EXPIRATION_DATE_PROPERTY = "dc:expired";
	
	private static final String DELETED_DATE_PROPERTY = "dc:modified";
	
    /** Person service. */
    @Autowired
    private PersonService personService;
    
    /** Workspace service. */
    @Autowired
    private WorkspaceService workspaceService;
    
    /** Document DAO. */
    @Autowired
    private DocumentDAO documentDAO;
	
	/**
     * {@inheritDoc}
     */
    @Override
	public List<WorkspaceLine> getListWorkspace(PortalControllerContext portalControllerContext, PurgeWorkspaceOptions options,
			String sortColumn, String sortOrder, int pageNumber, int pageSize)
	{
	     // Nuxeo controler
	   	 NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
	             portalControllerContext.getPortletCtx());
	
	     // Nuxeo command
	     INuxeoCommand nuxeoCommand = new ListWorkspaceCommand(sortColumn, sortOrder, pageNumber, pageSize);
	     PaginableDocuments documents = (PaginableDocuments) nuxeoController.executeNuxeoCommand(nuxeoCommand);
	     
	     //Total result number
    	 options.setTotalResultNumber(Integer.toString(documents.getTotalSize()));
    	
    	 //Total page number
    	 int totalPageNumber = (int) (pageSize >0? Math.ceil(((double) documents.getTotalSize())/pageSize) : 1);
    	 options.setTotalPageNumber(Integer.toString(totalPageNumber));
    	
    	 //Build of the return list of workspaceLine objects
    	 Calendar calendar = Calendar.getInstance();
    	 Date currentDate = calendar.getTime();
		 List<WorkspaceLine> list = new ArrayList<>();
		 Date expirationDate;
		 Date deletedDate;
    	 String inBin;
    	 boolean deleted = false;
		 for (Document document: documents)
		 {
			expirationDate = document.getDate(EXPIRATION_DATE_PROPERTY);
			inBin = document.getState();
			deleted = "deleted".equals(inBin);
			deletedDate = deleted? document.getDate(DELETED_DATE_PROPERTY) : null;
			
			// Last contributor
	        String lastContributorId = document.getString("dc:lastContributor");
	        Person lastContributorPerson;
	        if (deletedDate == null || lastContributorId == null) {
	            lastContributorPerson = null;
	        } else {
	            lastContributorPerson = this.personService.getPerson(lastContributorId);
	        }
	        String lastContributorDisplayName;
	        if (lastContributorPerson == null) {
	            lastContributorDisplayName = lastContributorId;
	        } else {
	            lastContributorDisplayName = StringUtils.defaultIfBlank(lastContributorPerson.getDisplayName(), lastContributorId);
	        }
			
	        DocumentDTO dto = documentDAO.toDTO(document);
	        String description = document.getString("dc:description");
			WorkspaceLine line = new WorkspaceLine(dto, description, new ArrayList<String>(), expirationDate, deletedDate, lastContributorDisplayName, expirationDate != null? currentDate.after(expirationDate) : false);
			list.add(line);
		 }
		 return list;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public void putInBin(PortalControllerContext portalControllerContext, List<String> listId) {
		
		// Nuxeo controler
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
	             portalControllerContext.getPortletCtx());
		for(String uid:listId)
		{
			// Nuxeo command
			INuxeoCommand nuxeoCommand = new PutInTrashWorkspaceCommand(uid);
		    nuxeoController.executeNuxeoCommand(nuxeoCommand);
		}
		
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public Documents getDeletedWorkspaces(PortalControllerContext portalControllerContext)
	{
		// Nuxeo controler
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
	             portalControllerContext.getPortletCtx());
		
		INuxeoCommand nuxeoCommand = new ListDeletedWorkspaceCommand();
	    return (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void purge(PortalControllerContext portalControllerContext, Documents documents) {
		
		for (Document document : documents)
		{
			if (document.getString("webc:url") != null) workspaceService.delete(document.getString("webc:url"), document.getId());
		}
	    
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void restore(PortalControllerContext portalControllerContext, String uid) {
		
		// Nuxeo controler
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext.getRequest(), portalControllerContext.getResponse(),
	             portalControllerContext.getPortletCtx());
		
		// Nuxeo command
		INuxeoCommand nuxeoCommand = new RestoreWorkspaceCommand(uid);
		nuxeoController.executeNuxeoCommand(nuxeoCommand);
		
	}
    
    
	
}
