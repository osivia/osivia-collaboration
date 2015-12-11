/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.osivia.services.widgets.plugin;

import java.io.IOException;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule;

/**
 * Display zooms of a current page.
 *
 * @author Loïc Billon
 * @see FragmentModule
 */
public class ZoomFragmentModule extends FragmentModule {

    /** Zoom fragment identifier. */
    public static final String ID = "zoom_property";

    /** Nuxeo path window property name. */
    public static final String NUXEO_PATH_WINDOW_PROPERTY = Constants.WINDOW_PROP_URI;
    /** Scope window property name. */
    public static final String SCOPE_WINDOW_PROPERTY = "osivia.cms.forcePublicationScope";
    /** Reference URI window property name. */
    public static final String REF_URI_WINDOW_PROPERTY = "osivia.refURI";

    /** View JSP name. */
    private static final String VIEW_JSP_NAME = "zoom";
    /** Ref URI. */
    private static final String REF_URI = "refURI";
    /** HREF. */
    private static final String HREF = "href";
    /** Content. */
    private static final String CONTENT = "content";
    /** Picture. */
    private static final String PICTURE = "picture";
    /** Template. */
    private static final String TEMPLATE = "zoomTemplate";


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public ZoomFragmentModule(PortletContext portletContext) {
        super(portletContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        // Current window
        PortalWindow window = WindowFactory.getWindow(request);
        // Nuxeo path
        String nuxeoPath = window.getProperty(NUXEO_PATH_WINDOW_PROPERTY);
        // Empty content indicator
        boolean emptyContent = true;


        if (StringUtils.isNotEmpty(nuxeoPath)) {
            nuxeoPath = nuxeoController.getComputedPath(nuxeoPath);

            // Document
            NuxeoDocumentContext documentContext = NuxeoController.getDocumentContext(request, response, portletContext, nuxeoPath);
            Document document = documentContext.getDoc();

            // Title
            if (document.getTitle() != null) {
                response.setTitle(document.getTitle());
            }

            // Zoom schema
            String schema = ZoomEditableWindow.ZOOM_SCHEMA;
            if (StringUtils.isNotEmpty(schema)) {
                // Ref URI
                String refURI = window.getProperty(REF_URI_WINDOW_PROPERTY);
                if (StringUtils.isNotEmpty(refURI)) {
                    // Content
                    Object content = document.getProperties().get(schema);
                    if (content instanceof PropertyList) {
                        PropertyList propertyList = (PropertyList) content;
                        if ((propertyList != null) && (propertyList.size() > 0)) {
                            for (int index = 0; index < propertyList.size(); index++) {
                                PropertyMap propertyMap = propertyList.getMap(index);

                                String refURIValue = (String) propertyMap.get(REF_URI);
                                if (refURI.equalsIgnoreCase(refURIValue)) {
                                    // Template
                                    request.setAttribute("template", propertyMap.getString(TEMPLATE));

                                    // Title
                                    request.setAttribute("title", window.getProperty("osivia.title"));

                                    // URL
                                    String href = propertyMap.getString(HREF);
                                    Link link = nuxeoController.getLinkFromNuxeoURL(href);
                                    request.setAttribute("url", link.getUrl());

                                    // Image source
                                    String imageSource = null;
                                    if (StringUtils.isNotBlank(propertyMap.getString(PICTURE))) {
                                    	
                                    	 // Computed path
                                    	imageSource = nuxeoController.getComputedPath(propertyMap.getString(PICTURE));

                                        if (StringUtils.startsWith(imageSource, "/nuxeo/")) {
                                            // Portal path
                                            imageSource = nuxeoController.transformNuxeoLink(imageSource);
                                        }
                                    	
                                    }
                                    request.setAttribute("imageSource", imageSource);

                                    // Content
                                    request.setAttribute("content",
                                            nuxeoController.transformHTMLContent(StringUtils.trimToEmpty(propertyMap.getString(CONTENT))));

                                    emptyContent = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (emptyContent) {
            request.setAttribute("osivia.emptyResponse", "1");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getViewJSPName() {
        return VIEW_JSP_NAME;
    }

}
