package org.osivia.services.rss.common.repository;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;

/**
 * RSS repository interface.
 *
 * @author Frédéric Boudan
 */
public interface ItemRepository {

    /**
     * RSS document type name.
     */
    String DOCUMENT_TYPE_EVENEMENT = "RssItem";
    /**
     * Id Conteneur RSS
     */
    String CONTENEUR_PROPERTY = "rssi:syncId";
    /**
     * title Item Nuxeo property.
     */
    String TITLE_PROPERTY = "rssi:title";
    String NAME_PROPERTY = "dc:title";
    /**
     * link Nuxeo property.
     */
    String LINK_PROPERTY = "rssi:link";
    /**
     * Description Nuxeo property.
     */
    String DESCRIPTION_PROPERTY = "rssi:description";
    String DESC_PROPERTY = "dc:description";
    /**
     * title Item Nuxeo property.
     */
    String AUTHOR_PROPERTY = "rssi:author";
    /**
     * Category Nuxeo property.
     */
    String CATEGORY_PROPERTY = "rssi:category";
    /**
     * enclosure Nuxeo property.
     */
    String ENCLOSURE_PROPERTY = "rssi:enclosure";
    /**
     * GUID Nuxeo property.
     */
    String GUID_PROPERTY = "rssi:guid";
    /**
     * pubDate Nuxeo property.
     */
    String PUBDATE_PROPERTY = "rssi:pubDate";
    /**
     * source Nuxeo property.
     */
    String SOURCES_PROPERTY = "rssi:source";
    /**
     * source Nuxeo property.
     */
    String PICTURE_PROPERTY = "rssi:picture";

    /**
     * Select2 results page size.
     */
    int SELECT2_RESULTS_PAGE_SIZE = 10;


    /**
     * Create Item RSS.
     *
     * @param portalControllerContext portal controller context
     * @param parentPath              parent document path
     * @param items
     * @throws PortletException
     */
    void creatItems(PortalControllerContext portalControllerContext, String parentPath, List<ItemRssModel> items) throws PortletException;

    /**
     * remove Items.
     *
     * @param portalControllerContext portal controller context
     * @param items
     * @throws PortletException
     */
    void removeItems(PortalControllerContext portalControllerContext, List<ItemRssModel> items) throws PortletException;

    /**
     * get feed list RSS.
     *
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    List<ItemRssModel> getListItemRss(PortalControllerContext portalControllerContext, String syncid) throws PortletException;

    /**
     * Get document properties.
     *
     * @param document Nuxeo document
     * @return properties
     * @throws PortletException
     */
    Map<String, String> getDocumentProperties(PortalControllerContext portalControllerContext, Document document) throws PortletException;

}