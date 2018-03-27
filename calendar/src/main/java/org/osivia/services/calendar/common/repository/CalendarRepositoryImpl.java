package org.osivia.services.calendar.common.repository;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.services.calendar.common.model.CalendarColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Calendar repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see CalendarRepository
 */
@Repository("common-repository")
public class CalendarRepositoryImpl implements CalendarRepository {

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /**
     * Constructor.
     */
    public CalendarRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Document getCurrentDocument(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();

        // Nuxeo document
        Document document;
        if (documentContext == null) {
            document = null;
        } else {
            document = documentContext.getDocument();
        }

        return document;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle(PortalControllerContext portalControllerContext, Document document) {
        String title;

        if (document == null) {
            title = null;
        } else {
            title = document.getTitle();
        }

        return title;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarColor getCalendarColor(PortalControllerContext portalControllerContext, Document calendar) throws PortletException {
        // Color identifier
        String colorId;
        if (calendar == null) {
            colorId = null;
        } else {
            colorId = calendar.getString(CALENDAR_COLOR_PROPERTY);
        }

        return CalendarColor.fromId(colorId);
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void insertContentMenubarItems(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();

        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();
        // Nuxeo document
        Document document = documentContext.getDocument();
        nuxeoController.setCurrentDoc(document);

        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        if (WindowState.MAXIMIZED.equals(request.getWindowState())) {
            // Insert content menubar items
            nuxeoController.insertContentMenuBarItems();

            if ((document != null) && ("Agenda".equals(document.getType())) && (response instanceof MimeResponse)) {
                MimeResponse mimeResponse = (MimeResponse) response;

                // Action URL
                PortletURL actionUrl = mimeResponse.createActionURL();
                actionUrl.setParameters(request.getParameterMap());
                actionUrl.setParameter(ActionRequest.ACTION_NAME, "synchronize");

                // Menubar
                List<MenubarItem> menubar = (List<MenubarItem>) request.getAttribute(Constants.PORTLET_ATTR_MENU_BAR);
                MenubarItem menubarItem = new MenubarItem("SYNCHRONIZED_CALENDAR", bundle.getString("REFRESH"), "glyphicons glyphicons-repeat",
                        MenubarGroup.GENERIC, 100, actionUrl.toString(), null, null, null);
                menubar.add(menubarItem);
            }
        }
    }

}
