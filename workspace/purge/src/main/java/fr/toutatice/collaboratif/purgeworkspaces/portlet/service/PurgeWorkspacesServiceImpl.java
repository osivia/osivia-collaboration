package fr.toutatice.collaboratif.purgeworkspaces.portlet.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.PurgeWorkspaceForm;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.PurgeWorkspaceOptions;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.model.WorkspaceLine;
import fr.toutatice.collaboratif.purgeworkspaces.portlet.repository.PurgeWorkspaceRepository;

/**
 * Purge workspace service implementation
 * @author Julien Barberet
 *
 */
@Service
public class PurgeWorkspacesServiceImpl implements PurgeWorkspacesService, ApplicationContextAware {

	private static final String EXPIRATION_DATE_PROPERTY = "dc:expired";
	
	private static final String IN_BIN_PROPERTY = "ecm:currentLifeCycleState";
	
	private static final String DELETED_DATE_PROPERTY = "dc:modified";
	
	public static final String OTHER_WAY = "other";
	
	private static final String PREVIOUS_WAY = "previous";
	
	private static final String NEXT_WAY = "next";
	
	/** Repository */
	@Autowired
	private PurgeWorkspaceRepository repository;

    /** Application context. */
    private ApplicationContext applicationContext;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
	/**
     * {@inheritDoc}
     */
    @Override
    public PurgeWorkspaceForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
    	
    	// Form
		PurgeWorkspaceForm form = this.applicationContext.getBean(PurgeWorkspaceForm.class);

		return form;
    	
    }
    
	/**
     * {@inheritDoc}
     */
    @Override
    public PurgeWorkspaceOptions getOptions(PortalControllerContext portalControllerContext, 
    		String sort, String alt, String pageNumber, String pageSize) throws PortletException {
    	
    	// Form
		PurgeWorkspaceOptions options = this.applicationContext.getBean(PurgeWorkspaceOptions.class);
		options.setAlt(alt);
		options.setSort(sort);
		options.setPageNumber(pageNumber);
		options.setPageSize(pageSize);

		return options;
    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<WorkspaceLine> loadList(PortalControllerContext portalControllerContext, PurgeWorkspaceOptions options)
    {
    	//Sort column
    	String sortColumn = "dc:title";
    	if ("deletedDate".equals(options.getSort())) sortColumn = IN_BIN_PROPERTY+","+DELETED_DATE_PROPERTY;
    	if ("expirationDate".equals(options.getSort())) sortColumn = "dc:expired";
    	
    	//Sort order
    	String sortOrder = "true".equals(options.getAlt()	)? "desc" : "asc";
    	
    	//Page number
    	int pageNumber = Integer.parseInt(options.getPageNumber());
    	//The page number begin with 1 on screen but with 0 on database
    	pageNumber--;
    	int pageSize = Integer.parseInt(options.getPageSize());
    	if (PREVIOUS_WAY.equals(options.getWay()))
    	{
    		pageNumber--;
    	} else if (NEXT_WAY.equals(options.getWay()))
    	{
    		pageNumber++;
    	}
    	options.setPageNumber(Integer.toString(pageNumber+1));
    	
    	//Load workspace list with order criteria and paging
    	PaginableDocuments documents = this.repository.getListWorkspace(portalControllerContext, sortColumn, sortOrder, pageNumber, pageSize);
    	
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
			WorkspaceLine line = new WorkspaceLine(document.getId(),document.getTitle(), new ArrayList<String>(), expirationDate, deletedDate, expirationDate != null? currentDate.after(expirationDate) : false);
			list.add(line);
		}
		return list;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void putInBin(PortalControllerContext portalControllerContext, PurgeWorkspaceForm form)
    {
    	String[] list = StringUtils.split(form.getSelectResult(), "#");
    	ArrayList<String> listWorkspaceToDel = new ArrayList<String>();
    	String uid;
    	for (String str: list)
    	{
    		int index = Integer.parseInt(str);
    		if (index>=0)
    		{
    			uid = ((WorkspaceLine) form.getList().get(index)).getId();
    			listWorkspaceToDel.add(uid);
    		}
    	}
    	this.repository.putInBin(portalControllerContext, listWorkspaceToDel);
    	System.out.println(listWorkspaceToDel);
    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void purge(PortalControllerContext portalControllerContext, List<String> listIdToPurge)
    {
    	this.repository.purge(portalControllerContext, listIdToPurge);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void restore(PortalControllerContext portalControllerContext, String uid)
    {
    	this.repository.restore(portalControllerContext, uid);
    }
}
