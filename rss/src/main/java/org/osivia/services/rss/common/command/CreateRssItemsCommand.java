package org.osivia.services.rss.common.command;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.rss.common.repository.ItemRepository;
import org.osivia.services.rss.feedRss.portlet.model.ItemRssModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Create RSS items Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateRssItemsCommand implements INuxeoCommand {

    /**
     * Parent document path.
     */
    private final String parentPath;
    /**
     * RSS items.
     */
    private final List<ItemRssModel> items;

    /**
     * Log.
     */
    private final Log log;


    /**
     * Constructor.
     *
     * @param parentPath parent document path
     * @param items      RSS items
     */
    public CreateRssItemsCommand(String parentPath, List<ItemRssModel> items) {
        super();
        this.parentPath = parentPath;
        this.items = items;

        // Log
        this.log = LogFactory.getLog(this.getClass());
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Parent document reference
        DocRef parent = new PathRef(this.parentPath);

        for (ItemRssModel item : this.items) {
            try {
                this.createItem(documentService, parent, item);
            } catch (Exception e) {
                this.log.error(e.getLocalizedMessage());
            }
        }

        return null;
    }


    /**
     * Create RSS item.
     *
     * @param documentService document service
     * @param parent          parent document reference
     * @param item            RSS item
     */
    private void createItem(DocumentService documentService, DocRef parent, ItemRssModel item) throws Exception {
        // Properties
        PropertyMap properties = new PropertyMap();
        properties.set(ItemRepository.AUTHOR_PROPERTY, item.getAuthor());
        properties.set(ItemRepository.CATEGORY_PROPERTY, item.getCategory());
        properties.set(ItemRepository.DESCRIPTION_PROPERTY, item.getDescription());
        properties.set(ItemRepository.DESC_PROPERTY, item.getDescription());
        properties.set(ItemRepository.ENCLOSURE_PROPERTY, item.getEnclosure());
        properties.set(ItemRepository.GUID_PROPERTY, item.getGuid());
        properties.set(ItemRepository.LINK_PROPERTY, item.getLink());
        properties.set(ItemRepository.PUBDATE_PROPERTY, item.getPubDate());
        properties.set(ItemRepository.SOURCES_PROPERTY, item.getSourceRss());
        properties.set(ItemRepository.TITLE_PROPERTY, item.getTitle());
        properties.set(ItemRepository.NAME_PROPERTY, item.getTitle());
        properties.set(ItemRepository.CONTENEUR_PROPERTY, item.getIdConteneur());

        Document document = documentService.createDocument(parent, ItemRepository.DOCUMENT_TYPE_EVENEMENT, null, properties);

        // Enclosure URL
        URL url;
        if (StringUtils.isBlank(item.getEnclosure())) {
            url = null;
        } else {
            try {
                url = new URL(item.getEnclosure());
            } catch (MalformedURLException e) {
                url = null;
                this.log.error(e.getLocalizedMessage());
            }
        }

        if (url != null) {
            File file = File.createTempFile("rss-enclosure-", "tmp");
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(url.openStream());
                out = new BufferedOutputStream(new FileOutputStream(file));
                IOUtils.copy(in, out);
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }

            // File blob
            Blob blob = new FileBlob(file);
            // blob.setFileName(visual.getName());
            documentService.setBlob(document, blob, ItemRepository.PICTURE_PROPERTY);

            // Delete temporary file
            file.delete();
        }
    }


    @Override
    public String getId() {
        return null;
    }

}
