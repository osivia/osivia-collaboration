package org.osivia.services.rss.common.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.common.command.CreateRssItemsCommand;
import org.osivia.services.rss.common.command.ItemListCommand;
import org.osivia.services.rss.common.command.ItemsDeleteCommand;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Item repository Nuxeo command.
 * 
 * @author Frédéric Boudan
 */
@Repository
public class ItemRepositoryImpl implements ItemRepository {

	/** Document DAO. */
	@Autowired
	private DocumentDAO documentDAO;
	/** Application context. */
	@Autowired
	private ApplicationContext applicationContext;

	/** Repository Item. */
	@Autowired
	public ContainerRepository repositoryContainer;

	/** FEEDS RSS */
	String FEEDS_PROPERTY = "rssc:feeds";
	/** Display Name RSS */
	String DISPLAY_NAME_PROPERTY = "displayName";
	/** url du flux RSS */
	String URL_PROPERTY = "url";
	/** Id sync flux RSS */
	String ID_PROPERTY = "syncId";
	/** WebId Nuxeo document property. */
	private static final String WEB_ID_PROPERTY = "ttc:webid";
	/** Vignette Nuxeo document property. */
	private static final String VIGNETTE_PROPERTY = "ttc:vignette";
	/** Description Nuxeo document property. */
	private static final String DESCRIPTION_PROPERTY = "dc:description";

	/**
	 * Constructor.
	 */
	public ItemRepositoryImpl() {
		super();
	}


	private ItemRssModel fillItem(Document document, NuxeoController nuxeoController) {
		String docid = document.getId();
		String id = document.getString(CONTENEUR_PROPERTY);
		String title = document.getString(TITLE_PROPERTY);
		String link = document.getString(LINK_PROPERTY);
		String description = document.getString(DESCRIPTION_PROPERTY);
		if(description != null && description.contains("<img")) {
			if(description.contains("<a")) {
				description = description.replaceAll("<a.*a>","");
			}else {
				description = description.replaceAll("<img.*>","");	
			}
		}
		String autor = document.getString(AUTHOR_PROPERTY);
		String category = document.getString(CATEGORY_PROPERTY);
		String enclosure = document.getString(ENCLOSURE_PROPERTY);
		Date pubdate = document.getDate(PUBDATE_PROPERTY);
		String guid = document.getString(GUID_PROPERTY);
		String sources = document.getString(SOURCES_PROPERTY);

		ItemRssModel item = new ItemRssModel();
		item.setDocid(docid);
		item.setIdConteneur(id);
		item.setTitle(title);
		item.setLink(link);
		item.setDescription(description);
		item.setAuthor(autor);
		item.setCategory(category);
		item.setEnclosure(enclosure);
		item.setPubDate(pubdate);
		item.setGuid(guid);
		item.setSourceRss(sources);
		return item;
	}


	@Override
	public void creatItems(PortalControllerContext portalControllerContext, List<ItemRssModel> items)  throws PortletException {
		// Nuxeo controller
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

		// Parent document path
		String parentPath = nuxeoController.getCurrentDocumentContext().getCmsPath();

		// Nuxeo command
		INuxeoCommand command = this.applicationContext.getBean(CreateRssItemsCommand.class, parentPath, items);
		nuxeoController.executeNuxeoCommand(command);
	}


	@Override
	public void removeItems(PortalControllerContext portalControllerContext, List<ItemRssModel> items) throws PortletException {
		// Nuxeo controller
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

		// Nuxeo command
		INuxeoCommand command = this.applicationContext.getBean(ItemsDeleteCommand.class, items);
		nuxeoController.executeNuxeoCommand(command);
	}


	@Override
	public Map<String, String> getDocumentProperties(PortalControllerContext portalControllerContext, Document document)
			throws PortletException {
		// Nuxeo controller
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
		// Document DTO
		DocumentDTO dto = this.documentDAO.toDTO(portalControllerContext, document);

		// Vignette property map
		PropertyMap vignettePropertyMap = document.getProperties().getMap(VIGNETTE_PROPERTY);
		// Vignette URL
		String vignetteUrl;
		if ((vignettePropertyMap == null) || vignettePropertyMap.isEmpty()) {
			vignetteUrl = null;
		} else {
			vignetteUrl = nuxeoController.createFileLink(document, VIGNETTE_PROPERTY);
		}

		// Icon
		String icon = dto.getIcon();

		// Document properties
		Map<String, String> properties = new HashMap<>();
		properties.put("id", document.getString(WEB_ID_PROPERTY));
		properties.put("title", document.getTitle());
		properties.put("vignette", vignetteUrl);
		properties.put("icon", icon);
		properties.put("description", document.getString(DESCRIPTION_PROPERTY));

		return properties;
	}

	@Override
	public List<ItemRssModel> getListItemRss(PortalControllerContext portalControllerContext, String syncid)
			throws PortletException {
		// Nuxeo controller
		NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

		List<ItemRssModel> items;

		// Nuxeo command
		INuxeoCommand nuxeoCommand = this.applicationContext.getBean(ItemListCommand.class, syncid);
		Documents documents = (Documents) nuxeoController.executeNuxeoCommand(nuxeoCommand);
		items = new ArrayList<ItemRssModel>(documents.size());

		for (Document document : documents) {
			ItemRssModel item = fillItem(document, nuxeoController);
			items.add(item);
		}

		return items;
	}

}
