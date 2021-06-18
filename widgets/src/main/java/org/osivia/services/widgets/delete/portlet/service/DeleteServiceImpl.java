package org.osivia.services.widgets.delete.portlet.service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.CMSController;
import org.osivia.portal.api.cms.UniversalID;
import org.osivia.portal.api.cms.exception.CMSException;
import org.osivia.portal.api.cms.service.CMSSession;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.widgets.delete.portlet.model.DeleteForm;
import org.osivia.services.widgets.delete.portlet.model.DeleteItem;
import org.osivia.services.widgets.delete.portlet.model.comparator.DeleteItemComparator;
import org.osivia.services.widgets.delete.portlet.repository.DeleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Delete portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see DeleteService
 */
@Service
public class DeleteServiceImpl implements DeleteService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Portlet repository.
     */
    @Autowired
    private DeleteRepository repository;

    /**
     * Delete item comparator.
     */
    @Autowired
    private DeleteItemComparator deleteItemComparator;

    /**
     * Portal URL factory.
     */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /**
     * Internationalization bundle factory.
     */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Notifications service.
     */
    @Autowired
    private INotificationsService notificationsService;

    /**
     * Document DAO.
     */
    @Autowired
    private DocumentDAO documentDao;


    /**
     * Constructor.
     */
    public DeleteServiceImpl() {
        super();
    }


    @Override
    public DeleteForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Window
        PortalWindow window = WindowFactory.getWindow(request);


        // Document path
        String path = window.getProperty(DOCUMENT_PATH_WINDOW_PROPERTY);
        // Document identifiers
        String[] identifiers = StringUtils.split(window.getProperty(DOCUMENT_IDENTIFIERS_WINDOW_PROPERTY), ",");

        // Documents
        List<Document> documents;
        if (StringUtils.isNotEmpty(path)) {
            documents = Collections.singletonList(this.repository.getDocument(portalControllerContext, path));
        } else if (ArrayUtils.isNotEmpty(identifiers)) {
            documents = this.repository.getDocuments(portalControllerContext, identifiers);
        } else {
            documents = null;
        }


        // Form
        DeleteForm form = this.applicationContext.getBean(DeleteForm.class);

        // Delete items
        SortedSet<DeleteItem> items;
        if (CollectionUtils.isEmpty(documents)) {
            items = null;
        } else {
            items = new TreeSet<>(this.deleteItemComparator);

            // Children counts
            Map<Document, Integer> childrenCounts = this.repository.getChildrenCounts(portalControllerContext, documents);
            // Remote proxies indicators
            Map<Document, Boolean> remoteProxiesMap = this.repository.haveRemoteProxies(portalControllerContext, documents);
            form.setRemoteProxiesCount(remoteProxiesMap.size());

            for (Document document : documents) {
                // Delete item
                DeleteItem item = this.applicationContext.getBean(DeleteItem.class);

                // Document DTO
                DocumentDTO dto = this.documentDao.toDTO(document);
                item.setDocument(dto);

                // Children count
                Integer childrenCount = childrenCounts.get(document);
                item.setChildrenCount(childrenCount);

                // Remote proxies indicator
                boolean remoteProxies = BooleanUtils.isTrue(remoteProxiesMap.get(document));
                item.setRemoteProxies(remoteProxies);

                items.add(item);
            }
        }
        form.setItems(items);

        // Redirection path
        String redirectionPath = window.getProperty(REDIRECTION_PATH_WINDOW_PROPERTY);
        if (StringUtils.isEmpty(redirectionPath) && StringUtils.isNotEmpty(path)) {
            redirectionPath = StringUtils.substringBeforeLast(path, "/");
        } else if (StringUtils.isEmpty(redirectionPath) && CollectionUtils.isNotEmpty(items)) {
            DeleteItem item = items.first();
            DocumentDTO document = item.getDocument();
            if (document != null) {
                redirectionPath = StringUtils.substringBeforeLast(document.getPath(), "/");
            }
        }
        form.setRedirectionPath(redirectionPath);

        return form;
    }


    @Override
    public void delete(PortalControllerContext portalControllerContext, DeleteForm form) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());


        // Delete items
        SortedSet<DeleteItem> items = form.getItems();

        if (CollectionUtils.isNotEmpty(items)) {
            
            Document firstDoc = null;
            
            // Document identifiers
            List<String> identifiers = new ArrayList<>(items.size());
            for (DeleteItem item : items) {
                DocumentDTO document = item.getDocument();
                if( firstDoc == null)
                    firstDoc = document.getDocument();
                if (document != null) {
                    identifiers.add(document.getId());
                }
            }

            try {
                

                NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
                String spacePath = nuxeoController.getSpacePath(firstDoc.getPath());
               
                // Delete
                this.repository.delete(portalControllerContext, identifiers);
                
                
                nuxeoController.notifyUpdate(firstDoc.getPath(), spacePath, false);

                // Notification
                String message = bundle.getString("DELETE_SUCCESS_MESSAGE");
                this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
            } catch (NuxeoException e) {
                // Notification
                String message = bundle.getString("DELETE_ERROR_MESSAGE");
                this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);
            }
        }

        
        String url;
        // Redirection
        
        
        String redirectionPath = form.getRedirectionPath();
        if (StringUtils.isNotEmpty(redirectionPath)) {
            // Redirection URL
            NuxeoController nuxeoController = new NuxeoController(portalControllerContext);            
            UniversalID redirectId = nuxeoController.getUniversalIDFromPath(redirectionPath);
            url = this.portalUrlFactory.getViewContentUrl(portalControllerContext, redirectId);
          }
        else    {
            url= this.portalUrlFactory.getBackURL(portalControllerContext, false, true);
        }

        response.sendRedirect(url);
    }


    
}
