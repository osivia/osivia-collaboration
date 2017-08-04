package org.osivia.services.forum.plugin.menubar;

import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.forum.edition.portlet.model.ForumEditionMode;
import org.osivia.services.forum.edition.portlet.repository.ForumEditionRepository;
import org.osivia.services.forum.edition.portlet.service.ForumEditionService;
import org.osivia.services.forum.plugin.ForumPlugin;

import java.util.*;

/**
 * Forum menubar module.
 *
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
public class ForumMenubarModule implements MenubarModule {

    /** Forum edition portlet instance. */
    private static final String FORUM_EDITION_PORTLET_INSTANCE = "osivia-services-forum-edition-instance";


    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     */
    public ForumMenubarModule() {
        super();

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
    }


    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext spaceDocumentContext) throws
            PortalException {
        // Do nothing
    }


    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext) throws
            PortalException {
        if (documentContext != null) {
            // Permissions
            Permissions permissions = documentContext.getPermissions();
            // Document type
            DocumentType documentType = documentContext.getDocumentType();
            // Document
            Document document = (Document) documentContext.getDocument();


            // Removed menubar items
            Set<MenubarItem> removedItems = new HashSet<>();
            Set<String> removedIds = new HashSet<>(Arrays.asList(new String[]{"LOCK_URL"}));
            for (MenubarItem menubarItem : menubar) {
                if (removedIds.contains(menubarItem.getId())) {
                    removedItems.add(menubarItem);
                }
            }
            menubar.removeAll(removedItems);


            if (permissions.isManageable()) {
                MenubarItem addForum = null;
                MenubarItem addThread = null;
                MenubarItem edit = null;

                for (MenubarItem menubarItem : menubar) {
                    if ("ADD_FORUM".equals(menubarItem.getId())) {
                        addForum = menubarItem;
                    } else if ("ADD_THREAD".equals(menubarItem.getId())) {
                        addThread = menubarItem;
                    } else if ("EDIT".equals(menubarItem.getId())) {
                        edit = menubarItem;
                    }
                }

                if (addForum != null) {
                    this.customizeAdd(portalControllerContext, document, ForumEditionRepository.DOCUMENT_TYPE_FORUM, addForum);
                }

                if (addThread != null) {
                    this.customizeAdd(portalControllerContext, document, ForumEditionRepository.DOCUMENT_TYPE_THREAD, addThread);
                }

                if ((edit != null) && (documentType != null) && (ForumEditionRepository.DOCUMENT_TYPE_FORUM.equals(documentType.getName()) ||
                        ForumEditionRepository.DOCUMENT_TYPE_THREAD.equals(documentType.getName()))) {
                    this.customizeEdit(portalControllerContext, document, documentType.getName(), edit);
                }
            }
        }
    }


    /**
     * Customize add menubar item.
     *
     * @param portalControllerContext portal controller context
     * @param document                Nuxeo document
     * @param documentType            document type
     * @param menubarItem             add menubar item
     * @throws PortalException
     */
    private void customizeAdd(PortalControllerContext portalControllerContext, Document document, String documentType, MenubarItem menubarItem) throws
            PortalException {
        // URL
        String url = this.getUrl(portalControllerContext, document.getPath(), documentType, ForumEditionMode.CREATION);

        menubarItem.setUrl(url);
        menubarItem.setOnclick(null);
        menubarItem.setHtmlClasses(null);
    }


    /**
     * Customize edit menubar item.
     *
     * @param portalControllerContext portal controller context
     * @param document                Nuxeo document
     * @param documentType            document type
     * @param menubarItem             edit menubar item
     * @throws PortalException
     */
    private void customizeEdit(PortalControllerContext portalControllerContext, Document document, String documentType, MenubarItem menubarItem) throws
            PortalException {
        // URL
        String url = this.getUrl(portalControllerContext, document.getPath(), documentType, ForumEditionMode.EDITION);

        menubarItem.setUrl(url);
        menubarItem.setOnclick(null);
        menubarItem.setHtmlClasses(null);
    }


    /**
     * Get menubar item URL.
     *
     * @param portalControllerContext portal controller context
     * @param path                    document path
     * @param documentType            document type
     * @param mode                    edition mode
     * @return URL
     * @throws PortalException
     */
    private String getUrl(PortalControllerContext portalControllerContext, String path, String documentType, ForumEditionMode mode) throws PortalException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, path);
        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
        properties.put("osivia.ajaxLink", "1");

        properties.put(ForumEditionService.DOCUMENT_TYPE_PROPERTY, documentType);
        properties.put(ForumEditionService.MODE_PROPERTY, mode.getId());

        // URL
        return this.portalUrlFactory.getStartPortletUrl(portalControllerContext, FORUM_EDITION_PORTLET_INSTANCE, properties);
    }

}
