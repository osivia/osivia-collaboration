package org.osivia.services.calendar.common.repository;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.MimeResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
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
    @Override
    public void insertContentMenubarItems(PortalControllerContext portalControllerContext) throws PortletException {
		
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        NuxeoDocumentContext documentContext = nuxeoController.getCurrentDocumentContext();

        Document document = documentContext.getDocument();
        nuxeoController.setCurrentDoc(document);

        // Insert content menubar items
        nuxeoController.insertContentMenuBarItems();
        if ((document != null) && ("Agenda".equals(document.getType())))
        {
        	// Nuxeo controller
            PortletRequest request = portalControllerContext.getRequest();
        	javax.portlet.PortletResponse response = portalControllerContext.getResponse();
            if (response instanceof MimeResponse && WindowState.MAXIMIZED.equals(request.getWindowState()))
            {	
            	PortletURL createActionURL = ((MimeResponse) response).createActionURL();
            	createActionURL.setParameters(request.getParameterMap());
            	createActionURL.setParameter(ActionRequest.ACTION_NAME, "synchronize");
            	// Menubar
                List<MenubarItem> menuBar = (List<MenubarItem>) request.getAttribute(Constants.PORTLET_ATTR_MENU_BAR);
                Bundle bundle = bundleFactory.getBundle(request.getLocale());
                MenubarItem menubarItem = new MenubarItem("SYNCHRONIZED_CALENDAR", bundle.getString("REFRESH"), "glyphicons glyphicons-repeat", MenubarGroup.GENERIC, 100,
                        createActionURL.toString(), null, null, null);
                menuBar.add(menubarItem);
            }
        }
        
    }

}
