package org.osivia.services.editor.image.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.editor.common.service.CommonServiceImpl;
import org.osivia.services.editor.image.portlet.model.EditorImageForm;
import org.osivia.services.editor.image.portlet.model.EditorImageSourceAttachedForm;
import org.osivia.services.editor.image.portlet.model.EditorImageSourceDocumentForm;
import org.osivia.services.editor.image.portlet.model.ImageSourceType;
import org.osivia.services.editor.image.portlet.repository.EditorImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import java.util.ArrayList;
import java.util.List;

/**
 * Editor image portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonServiceImpl
 * @see EditorImageService
 */
@Service
public class EditorImageServiceImpl extends CommonServiceImpl implements EditorImageService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private EditorImageRepository repository;

    /**
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;


    /**
     * Constructor.
     */
    public EditorImageServiceImpl() {
        super();
    }


    @Override
    public EditorImageForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        EditorImageForm form = this.applicationContext.getBean(EditorImageForm.class);

        if (!form.isLoaded()) {
            // Window
            PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

            // Current document path
            String path = window.getProperty(PATH_WINDOW_PROPERTY);
            // Document
            Document document = this.repository.getDocument(portalControllerContext, path);

            // Attachments indicator
            boolean attachments = (document.getProperties().getList("files:files") != null);


            // Source URL
            String url = window.getProperty(SRC_WINDOW_PROPERTY);
            form.setUrl(url);

            // Alternate text
            String alt = window.getProperty(ALT_WINDOW_PROPERTY);
            form.setAlt(alt);

            // Available image source types
            List<ImageSourceType> availableSourceTypes = new ArrayList<>();
            if (attachments) {
                availableSourceTypes.add(ImageSourceType.ATTACHED);
            }
            availableSourceTypes.add(ImageSourceType.DOCUMENT);
            form.setAvailableSourceTypes(availableSourceTypes);

            // Loaded indicator
            form.setLoaded(true);
        }

        return form;
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, EditorImageForm form) throws PortletException {
        form.setDone(true);
    }


    @Override
    public EditorImageSourceAttachedForm getAttachedForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.applicationContext.getBean(EditorImageSourceAttachedForm.class);
    }


    @Override
    public void selectAttached(PortalControllerContext portalControllerContext, EditorImageSourceAttachedForm attachedForm) throws PortletException {
        // Form
        EditorImageForm form = this.getForm(portalControllerContext);

        // TODO
    }


    @Override
    public EditorImageSourceDocumentForm getDocumentForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        EditorImageSourceDocumentForm form = this.applicationContext.getBean(EditorImageSourceDocumentForm.class);

        // Documents
        List<DocumentDTO> documents = this.search(portalControllerContext, null);
        form.setDocuments(documents);

        return form;
    }


    @Override
    public void filterDocuments(PortalControllerContext portalControllerContext, EditorImageSourceDocumentForm documentForm) throws PortletException {
        List<DocumentDTO> documents = this.search(portalControllerContext, documentForm.getFilter());
        documentForm.setDocuments(documents);
    }


    @Override
    public void selectDocument(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Form
        EditorImageForm form = this.getForm(portalControllerContext);

        // URL
        String url = this.repository.getImageDocumentUrl(portalControllerContext, path);
        form.setUrl(url);
    }


    /**
     * Search documents.
     *
     * @param portalControllerContext portal controller context
     * @param filter                  search filter
     * @return documents
     */
    private List<DocumentDTO> search(PortalControllerContext portalControllerContext, String filter) throws PortletException {
        // Nuxeo documents
        List<Document> nuxeoDocuments = this.repository.search(portalControllerContext, filter);

        // Documents
        List<DocumentDTO> documents;
        if (CollectionUtils.isEmpty(nuxeoDocuments)) {
            documents = null;
        } else {
            documents = new ArrayList<>(nuxeoDocuments.size());

            for (Document nuxeoDocument : nuxeoDocuments) {
                DocumentDTO document = this.documentDao.toDTO(nuxeoDocument);
                documents.add(document);
            }
        }

        return documents;
    }

}
