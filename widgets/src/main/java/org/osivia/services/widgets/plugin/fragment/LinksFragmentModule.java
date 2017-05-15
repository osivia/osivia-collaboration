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
package org.osivia.services.widgets.plugin.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.widgets.plugin.ew.LinksEditableWindow;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule;

/**
 * Display links of a current page.
 *
 * @author Loïc Billon
 * @see FragmentModule
 */
public class LinksFragmentModule extends FragmentModule {

    /** Links fragment identifier. */
    public static final String ID = "links_property";

    /** View JSP name. */
    private static final String VIEW_JSP_NAME = "links";
    /** Ref URI. */
    private static final String REF_URI = "refURI";
    /** Template. */
    private static final String TEMPLATE = "linksTemplate";


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public LinksFragmentModule(PortletContext portletContext) {
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
        String nuxeoPath = window.getProperty(Constants.WINDOW_PROP_URI);
        // Empty content indicator
        boolean emptyContent = true;

        if (StringUtils.isNotEmpty(nuxeoPath)) {
            nuxeoPath = nuxeoController.getComputedPath(nuxeoPath);

            // Document
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(nuxeoPath);
            Document document = documentContext.getDocument();

            // Title
            if (document.getTitle() != null) {
                response.setTitle(document.getTitle());
            }

            // Ref URI
            String refURI = window.getProperty("osivia.refURI");
            if (StringUtils.isNotEmpty(refURI)) {
                // Links schema
                String linksSchema = LinksEditableWindow.LINKS_SCHEMA;
                if (StringUtils.isNotEmpty(linksSchema)) {
                    List<LinkFragmentBean> links = new ArrayList<LinkFragmentBean>();

                    // Content
                    Object content = document.getProperties().get(linksSchema);
                    if (content instanceof PropertyList) {
                        PropertyList dataContents = (PropertyList) content;
                        if ((dataContents != null) && (dataContents.size() > 0)) {
                            for (int index = 0; index < dataContents.size(); index++) {
                                PropertyMap propertyMap = dataContents.getMap(index);

                                String refURIValue = (String) propertyMap.get(REF_URI);
                                String href = propertyMap.getString(LinkFragmentBean.HREF_PROPERTY);
                                if (refURI.equalsIgnoreCase(refURIValue) && StringUtils.isNotBlank(href)) {
                                    // Link
                                    LinkFragmentBean link = new LinkFragmentBean(nuxeoController.getLinkFromNuxeoURL(href));

                                    // Title
                                    link.setTitle(propertyMap.getString(LinkFragmentBean.TITLE_PROPERTY));

                                    // Glyphicon
                                    link.setGlyphicon(propertyMap.getString(LinkFragmentBean.ICON_PROPERTY));

                                    links.add(link);
                                }
                            }
                        }
                    }

                    if (!links.isEmpty()) {
                        request.setAttribute("links", links);
                        emptyContent = false;
                    }
                }

                // links fragments schema
                String linksFragmentsSchema = LinksEditableWindow.LINKS_FGT_SCHEMA;
                if (StringUtils.isNotEmpty(linksFragmentsSchema)) {
                    // Content
                    Object content = document.getProperties().get(linksFragmentsSchema);
                    if (content instanceof PropertyList) {
                        PropertyList dataContents = (PropertyList) content;
                        if ((dataContents != null) && (dataContents.size() > 0)) {
                            for (int index = 0; index < dataContents.size(); index++) {
                                PropertyMap propertyMap = dataContents.getMap(index);

                                String refURIValue = (String) propertyMap.get(REF_URI);
                                if (refURI.equalsIgnoreCase(refURIValue)) {
                                    // Template
                                    request.setAttribute("template", propertyMap.getString(TEMPLATE));

                                    break;
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
