package org.osivia.services.sets.edition.portlet.service;

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
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.sets.edition.portlet.model.AddedDocument;
import org.osivia.services.sets.edition.portlet.model.SetsEditionForm;
import org.osivia.services.sets.edition.portlet.model.comparator.AddedDocumentComparator;
import org.osivia.services.sets.edition.portlet.repository.SetsEditionRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Sets edition service implementation
 * @author Julien Barberet
 *
 */
@Service
public class SetsEditionServiceImpl implements SetsEditionService, ApplicationContextAware {

	/** Select2 results page size. */
	public final static int SELECT2_RESULTS_PAGE_SIZE = 10;

	/** WebId Nuxeo document property. */
	private static final String WEB_ID_PROPERTY = "ttc:webid";
	/** Sets property */
	private static final String SETS_PROPERTY = "sets:sets";
	/** Webids property */
	private static final String LIST_WEBID_PROPERTY= "webids";
	/** Name property */
	private static final String NAME_PROPERTY = "name";
	/** Window sets id property */
    private static final String SETS_ID = "osivia.sets.id";

	/** Portal URL factory. */
	@Autowired
	private IPortalUrlFactory portalUrlFactory;

	/** Repository */
	@Autowired
	private SetsEditionRepository repository;

	/** Comparator */
	@Autowired
	private AddedDocumentComparator comparator;

	/** Bundle factory. */
	@Autowired
	private IBundleFactory bundleFactory;


	/** Application context. */
	private ApplicationContext applicationContext;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SetsEditionForm getForm(PortalControllerContext portalControllerContext, boolean reinit) throws PortletException {
		// Workspace document
		Document workspace = this.repository.getWorkspace(portalControllerContext);
		
		// Current window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        String setsId = window.getProperty(SETS_ID);
		
		SetsEditionForm form = null;

		if (workspace == null)
		{
			form = new SetsEditionForm();
		} else
		{
			// Form
			form = this.applicationContext.getBean(SetsEditionForm.class, workspace);

			if (form.getList() == null || reinit)
			{
				form.setWorkspace(workspace);

				List<AddedDocument> listPinned = new ArrayList<>();
				
				PropertyList list = (PropertyList) workspace.getProperties().get(SETS_PROPERTY);
				
				form.setEnableSets(list != null);
				if (list != null && list.list().size() >0)
				{
					for (Object map : list.list())
					{
						if (StringUtils.equals(setsId, ((PropertyMap) map).getString(NAME_PROPERTY)))
						{
							PropertyList listWebIds = ((PropertyMap) map).getList(LIST_WEBID_PROPERTY);
							List<Object> listObject = listWebIds.list();

							List<AddedDocument> documents = this.repository.getDocumentsList(portalControllerContext, workspace.getPath(), listObject);
							AddedDocument document;
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
									if (hashMap.get(listObject.get(i)) != null) listPinned.add((AddedDocument) hashMap.get(listObject.get(i)));
								}
							}
						}
					}
				}
				form.setList(listPinned);
			}
		}

		return form;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(PortalControllerContext portalControllerContext, SetsEditionForm form) throws PortletException {

		Document workspace = form.getWorkspace();
		
		List<AddedDocument> pinnedDocuments = form.getList();
		if (pinnedDocuments != null)
		{
			//Creation of the webids list to save
			List<String> listWebIds = new ArrayList<>();
			for (int i=0; i<pinnedDocuments.size(); i++)
			{
				listWebIds.add(pinnedDocuments.get(i).getWebId());
			}
			
			this.repository.save(portalControllerContext, workspace, listWebIds);

		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getWorkspaceUrl(PortalControllerContext portalControllerContext, SetsEditionForm form) throws PortletException {
		// Workspace path
		String path = form.getWorkspace().getPath();

		return this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sort(PortalControllerContext portalControllerContext, SetsEditionForm form) throws PortletException {
		// Tasks
		List<AddedDocument> pinned = form.getList();

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
	public void addDocumentToSet(PortalControllerContext portalControllerContext, SetsEditionForm form)
	{
		String webid = form.getDocumentWebId();
		if (!StringUtils.isEmpty(webid) && form.getWorkspace()!=null && form.getWorkspace().getPath()!=null)
		{
			AddedDocument pinnedDocument = this.repository.getDocument(portalControllerContext, form.getWorkspace().getPath(), webid);
			if (pinnedDocument != null)
			{
				pinnedDocument.setOrder(getMaxOrder(form)+1);
				form.getList().add(pinnedDocument);
			}
		}
	}

	/**
	 * Get max value to set the order of the last pinned document
	 * @param form
	 * @return
	 */
	private int getMaxOrder(SetsEditionForm form) {
		int max = 0;
		for(AddedDocument pinned : form.getList())
		{
			if (pinned.getOrder() > max) max = pinned.getOrder();
		}
		return max;
	}
}
