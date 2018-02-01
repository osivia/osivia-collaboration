package org.osivia.services.pins.edition.portlet.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.NuxeoPropertyMap;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.pins.edition.portlet.model.PinnedDocument;
import org.osivia.services.pins.edition.portlet.model.PinsEditionForm;
import org.osivia.services.pins.edition.portlet.model.comparator.PinnedDocumentComparator;
import org.osivia.services.pins.edition.portlet.repository.PinsEditionRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Pins edition service implementation
 * @author jbarberet
 *
 */
@Service
public class PinsEditionServiceImpl implements PinsEditionService, ApplicationContextAware {

    /** Select2 results page size. */
    public final static int SELECT2_RESULTS_PAGE_SIZE = 10;
	
	private static final String PIN_LIST_WEBID = "pin:listwebid";
	
	/** WebId Nuxeo document property. */
    private static final String WEB_ID_PROPERTY = "ttc:webid";
	
    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;
    
	/** Repository */
	@Autowired
	private PinsEditionRepository repository;
	
	/** Comparator */
	@Autowired
	private PinnedDocumentComparator comparator;
	
    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /** Application context. */
    private ApplicationContext applicationContext;
	
    /**
     * {@inheritDoc}
     */
    @Override
    public PinsEditionForm getForm(PortalControllerContext portalControllerContext, boolean reinit) throws PortletException {
    	// Workspace document
    	Document workspace = this.repository.getWorkspace(portalControllerContext);
    	PinsEditionForm form = null;

    	if (workspace == null)
    	{
    		form = new PinsEditionForm();
    	} else
    	{
    		// Form
    		form = this.applicationContext.getBean(PinsEditionForm.class, workspace);

    		if (form.getListPins() == null || reinit)
    		{
    			form.setWorkspace(workspace);
    			PropertyList list = (PropertyList) workspace.getProperties().get(PIN_LIST_WEBID);
    			//        PropertyList list = ((NuxeoDocument) workspace).getList(PIN_LIST_WEBID);
    			List<PinnedDocument> listPinned = new ArrayList<>();
    			if (list != null)
    			{
    				form.setCanPin(true);
    				List<Object> listObject = list.list();
    				List<PinnedDocument> documents = this.repository.getDocumentsList(portalControllerContext, workspace.getPath(), listObject);
    				PinnedDocument document;
    				HashMap<String, DocumentDTO> hashMap = new HashMap<String, DocumentDTO>();

    				if (documents != null)
    				{
    					for (int i=0; i < documents.size(); i++)
    					{
    						document = documents.get(i);
    						hashMap.put(document.getWebId(), document);
    					}
    					for (int i=0; i< listObject.size(); i++)
    					{
    						if (hashMap.get(listObject.get(i)) != null) listPinned.add((PinnedDocument) hashMap.get(listObject.get(i)));
    					}
    				}
    			} else
    			{
    				form.setCanPin(false);
    			}
    			form.setListPins(listPinned);
    		}
    	}

    	return form;
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, PinsEditionForm form) throws PortletException {

    	Document workspace = form.getWorkspace();
    	List<PinnedDocument> pinnedDocuments = form.getListPins();
    	if (pinnedDocuments != null)
    	{
    		//Creation of a string of concatenation of webid, with a comma as separator
    		StringBuffer stringLstWebid = new StringBuffer();
    		for (int i=0; i<pinnedDocuments.size(); i++)
    		{
    			if (i>0) stringLstWebid.append(",");
    			stringLstWebid.append(pinnedDocuments.get(i).getWebId());
    		}
        	
    		PropertyMap propertyMap = new NuxeoPropertyMap();
			propertyMap.set(PIN_LIST_WEBID, stringLstWebid.toString());

    		this.repository.save(portalControllerContext, workspace, propertyMap);
    	}
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkspaceUrl(PortalControllerContext portalControllerContext, PinsEditionForm form) throws PortletException {
        // Workspace path
        String path = form.getWorkspace().getPath();

        return this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void sort(PortalControllerContext portalControllerContext, PinsEditionForm form) throws PortletException {
        // Tasks
        List<PinnedDocument> pinned = form.getListPins();

        // Sort tasks
        Collections.sort(pinned, this.comparator);
    }
    
    /**
     * {@inheritDoc}
     * @throws PortletException 
     */
    @Override
    public JSONObject searchDocuments(PortalControllerContext portalControllerContext, Document workspace, String filter, int page, HashMap<String, String> mapPinnedDocuments) throws PortletException
    {
    	// Portlet request
    	PortletRequest request = portalControllerContext.getRequest();
    	// Internationalization bundle
    	Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

    	PaginableDocuments documents = this.repository.search(portalControllerContext, filter, page);

    	// Total results
    	int total = 0;
    	// JSON objects
    	List<JSONObject> objects = null;

    	if (documents != null)
    	{
    		total = documents.getTotalSize();
    		objects = new ArrayList<>(Math.min(SELECT2_RESULTS_PAGE_SIZE, total));

    		for (Document document : documents) {
    			// Search result
    			JSONObject object = new JSONObject();
    			// Document properties
    			boolean alreadyPinned = mapPinnedDocuments.containsKey(document.getString(WEB_ID_PROPERTY));
    			Map<String, String> properties = this.repository.getDocumentProperties(portalControllerContext, document, alreadyPinned);

    			for (Map.Entry<String, String> entry : properties.entrySet()) {
    				object.put(entry.getKey(), entry.getValue());
    			}

    			objects.add(object);
    		}
    	}

    	// Results JSON object
    	JSONObject results = new JSONObject();

    	// Items JSON array
    	JSONArray items = new JSONArray();

    	// Message
    	if ((page == 1) && CollectionUtils.isNotEmpty(objects)) {
    		String message;
    		if (total == 0) {
    			message = bundle.getString("SELECT2_NO_RESULT");
    		} else if (total == 1) {
    			message = bundle.getString("SELECT2_ONE_RESULT");
    		} else {
    			message = bundle.getString("SELECT2_MULTIPLE_RESULTS", total);
    		}
    		JSONObject object = new JSONObject();
    		object.put("message", message);
    		items.add(object);
    	}
	
    	// Paginated results
    	for (JSONObject object : objects) {
    		items.add(object);
    	}

    	// Pagination informations
    	results.put("page", page);
    	results.put("pageSize", SELECT2_RESULTS_PAGE_SIZE);

    	results.put("items", items);
    	results.put("total", total);

    	return results;
    }
    

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
    public void addPin(PortalControllerContext portalControllerContext, PinsEditionForm form)
    {
    	String webid = form.getDocumentWebId();
    	if (!StringUtils.isEmpty(webid) && form.getWorkspace()!=null && form.getWorkspace().getPath()!=null)
    	{
    		PinnedDocument pinnedDocument = this.repository.getDocument(portalControllerContext, form.getWorkspace().getPath(), webid);
    		if (pinnedDocument != null)
    		{
    			pinnedDocument.setOrder(getMaxOrder(form)+1);
    			form.getListPins().add(pinnedDocument);
    		}
    	}
    }
    
    /**
     * Get max value to set the order of the last pinned document
     * @param form
     * @return
     */
    private int getMaxOrder(PinsEditionForm form) {
    	int max = 0;
    	for(PinnedDocument pinned : form.getListPins())
    	{
    		if (pinned.getOrder() > max) max = pinned.getOrder();
    	}
    	return max;
    }
}
