package org.osivia.services.calendar.plugin.menubar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletException;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.PublicationInfos;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.calendar.edition.portlet.service.CalendarEditionService;
import org.osivia.services.calendar.event.edition.portlet.service.CalendarEventEditionService;

/**
 * Calendar menubar module.
 *
 * @author Julien Barberet
 * @see MenubarModule
 */
public class CalendarMenubarModule implements MenubarModule {

    /** Calendar Nuxeo document type name. */
	protected static final String CALENDAR_TYPE = "Agenda";
    /** Calendar event Nuxeo document type name. */
    protected static final String EVENT_TYPE = "VEVENT";
    /** Id event source */
    protected static final String ID_EVENT_SOURCE_PROPERTY = "sync:idSource";

    /** Add menubar item identifier. */
    protected static final String ADD_MENUBAR_ITEM_ID = "ADD";
    /** Add calendar menubar item identifier. */
    protected static final String ADD_CALENDAR_MENUBAR_ITEM_ID = ADD_MENUBAR_ITEM_ID + "_" + CALENDAR_TYPE;
    /** Add calendar event menubar item identifier. */
    protected static final String ADD_EVENT_MENUBAR_ITEM_ID = ADD_MENUBAR_ITEM_ID + "_" + EVENT_TYPE;
    /** Edit menubar item identifier. */
    protected static final String EDIT_MENUBAR_ITEM_ID = "EDIT";
    /** Edit menubar item identifier. */
    protected static final String DELETE_MENUBAR_ITEM_ID = "DELETE";
    /** Refresh menubar item identifier. */
    private static final String REFRESH_MENUBAR_ITEM_ID = "REFRESH";

    protected static final String SYNCHRO_MENUBAR_ITEM_ID = "SYNCHRONIZED_CALENDAR";

    /** Calendar edition portlet instance. */
    protected static final String CALENDAR_EDITION_PORTLET_INSTANCE = "osivia-services-calendar-edition-instance";
    /** Calendar event edition portlet instance. */
    protected static final String EVENT_EDITION_PORTLET_INSTANCE = CalendarEventEditionService.PORTLET_INSTANCE;

    /** Portal URL factory. */
    protected final IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     */
    public CalendarMenubarModule() {
        super();

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext spaceDocumentContext)
            throws PortalException {
        // Delete the Refresh button if Synchronized button is present.
        boolean synchroPresent = false;
        MenubarItem refresh = null;
        for (MenubarItem menubarItem : menubar) {
            if (SYNCHRO_MENUBAR_ITEM_ID.equals(menubarItem.getId())) {
                synchroPresent = true;
            }
            if (REFRESH_MENUBAR_ITEM_ID.equals(menubarItem.getId())) {
                refresh = menubarItem;
            }
            if (synchroPresent && (refresh != null)) {
                break;
            }
        }
        if (synchroPresent && (refresh != null)) {
            menubar.remove(refresh);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        if (documentContext != null) {
            // Document type
            DocumentType documentType = documentContext.getDocumentType();
            // Publication infos
            PublicationInfos publicationInfos = documentContext.getPublicationInfos();

            if ((documentType != null) && publicationInfos.isLiveSpace()) {
                // Document
                Document document = (Document) documentContext.getDocument();

                MenubarItem addCalendar = null;
                MenubarItem addEvent = null;
                MenubarItem editCalendar = null;
                MenubarItem editEvent = null;

                for (MenubarItem menubarItem : menubar) {
                    if (ADD_CALENDAR_MENUBAR_ITEM_ID.equals(menubarItem.getId())) {
                        addCalendar = menubarItem;
                    } else if (ADD_EVENT_MENUBAR_ITEM_ID.equals(menubarItem.getId())) {
                        addEvent = menubarItem;
                    } else if (ADD_MENUBAR_ITEM_ID.equals(menubarItem.getId())) {
                        if (CALENDAR_TYPE.equals(documentType.getName())) {
                            addEvent = menubarItem;
                        }
                    } else if (EDIT_MENUBAR_ITEM_ID.equals(menubarItem.getId())) {
                        if (CALENDAR_TYPE.equals(documentType.getName())) {
                            editCalendar = menubarItem;
                        } else if (EVENT_TYPE.equals(documentType.getName())) {
                            editEvent = menubarItem;
                        }
                    }
                }

                if (addCalendar != null) {
                    this.customizeMenubarItem(portalControllerContext, addCalendar, document, CALENDAR_TYPE, true);
                }
                if (addEvent != null) {
                    this.customizeMenubarItem(portalControllerContext, addEvent, document, EVENT_TYPE, true);
                }
                if (editCalendar != null) {
                    this.customizeMenubarItem(portalControllerContext, editCalendar, document, CALENDAR_TYPE, false);
                }
                if (editEvent != null) { 
                    this.customizeMenubarItem(portalControllerContext, editEvent, document, EVENT_TYPE, false);
                }

                // If event came from synchronization, disable edit and delete options
                if (document.getString(ID_EVENT_SOURCE_PROPERTY) != null && !document.getString(ID_EVENT_SOURCE_PROPERTY).isEmpty()) {
                    try {
						this.removeEditAndDelete(menubar, documentType, document, portalControllerContext);
					} catch (PortletException e) {
						throw new PortalException(e);
					}
                }
            }
        }
    }


    protected void customizeMenubarItem(PortalControllerContext portalControllerContext, MenubarItem item, Document document, String documentType,
            boolean creation) throws PortalException {
        // Portlet instance
        String instance;

        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, document.getPath());


        // Creation indicator
        properties.put(CalendarEditionService.CREATION_PROPERTY, String.valueOf(creation));

        if (CALENDAR_TYPE.equals(documentType)) {
            // Calendar edition
            instance = CALENDAR_EDITION_PORTLET_INSTANCE;
        } else if (EVENT_TYPE.equals(documentType)) {
            // Calendar event edition
            instance = EVENT_EDITION_PORTLET_INSTANCE;
        } else {
            // Unknown case
            instance = null;
        }

        // URL
        String url;
        if (instance == null) {
            url = null;
        } else {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, instance, properties);
        }


        // Update menubar item
        item.setUrl(url);
        item.setOnclick(null);
        item.setHtmlClasses(null);
    }

    /**
     * Remove edit and delete menubar item.
     * 
     * @param menubar menubar
     * @param documentType document type
     */
    public void removeEditAndDelete(List<MenubarItem> menubar, DocumentType documentType, Document document, PortalControllerContext portalControllerContext) throws PortletException{
        if ((documentType != null) && EVENT_TYPE.equals(documentType.getName())) {
            Set<MenubarItem> removedItems = new HashSet<>();
            for (MenubarItem menubarItem : menubar) {
                if (EDIT_MENUBAR_ITEM_ID.equals(menubarItem.getId()) || DELETE_MENUBAR_ITEM_ID.equals(menubarItem.getId())) {
                    removedItems.add(menubarItem);
                }
            }
            menubar.removeAll(removedItems);
        }
    }

}
