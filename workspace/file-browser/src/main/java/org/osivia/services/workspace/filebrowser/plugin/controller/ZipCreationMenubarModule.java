package org.osivia.services.workspace.filebrowser.plugin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
import fr.toutatice.portail.cms.nuxeo.api.services.tag.INuxeoTagService;

public class ZipCreationMenubarModule implements MenubarModule {
	
    private static final String DOCUMENT_EDITION_PORTLET_INSTANCE = "osivia-services-document-edition-instance";

    private static final String ZIP_MIMETYPE = "application/zip";


	/** menubarService */
    private final IMenubarService menubarService;

    /** bundleFactory */
    private final IBundleFactory bundleFactory;
    
    /** Nuxeo service. */
    private final INuxeoService nuxeoService;

    /** Portal url factory */
	private IPortalUrlFactory portalUrlFactory;
	
	public ZipCreationMenubarModule() {
        menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
		
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
        
        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
		
        // Nuxeo service
        this.nuxeoService = Locator.findMBean(INuxeoService.class, INuxeoService.MBEAN_NAME);
	}

	@Override
	public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
			DocumentContext spaceDocumentContext) throws PortalException {


	}

	@Override
	public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
			DocumentContext documentContext) throws PortalException {


        if (documentContext != null && documentContext.getDocument() instanceof Document) {
            if (documentContext.getDocumentType() != null) {
                String typeName = documentContext.getDocumentType().getName();
                Document doc = (Document) documentContext.getDocument();
                String docPath = doc.getPath();
                if (StringUtils.equals(typeName, "Folder")) {
                    NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
                    NuxeoPublicationInfos publicationInfos = ((NuxeoDocumentContext) documentContext).getPublicationInfos();
                    boolean acceptFiles = publicationInfos.getSubtypes().contains("File");

                    if (acceptFiles) {
                        // Tag service
                        INuxeoTagService tagService = nuxeoService.getTagService();
                        // Internationalization bundle
                        Bundle bundle = bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

                        // Add dropdown
                        MenubarDropdown addDropdown = getAddDropdown(portalControllerContext, bundle);

                        // create WORD
                        Element zipIcon;
                        try {
                            zipIcon = tagService.getMimeTypeIcon(nuxeoController, ZIP_MIMETYPE, null);
                        } catch (IOException e) {
                            throw new PortalException(e);
                        }
                        MenubarItem importZip = new MenubarItem("IMPORT_ZIP_DOCUMENT", bundle.getString("IMPORT_ZIP_DOCUMENT"),
                                null, addDropdown, 10, "javascript:;", null, null, null);
                        //createWordDocument.setDivider(true);
                        importZip.setCustomizedIcon(zipIcon);
                        importZip.setDivider(true);
                        importZip.setCategoryTitle(bundle.getString("IMPORT_CATEGORY"));
                        importZip.setOrder(100);
                        // new word doc modal

                        
                        importZip.getData().put("target", "#osivia-modal");
                        importZip.getData().put("load-url", getZipPortletUrl(portalControllerContext, docPath));
                        importZip.getData().put("title", bundle.getString("IMPORT_ZIP_DOCUMENT_TITLE"));
                        menubar.add(importZip);
                    }
                }
            }
        }
                    
	}


    /**
     * Retrieve or build the add menu dropdown
     *
     * @param portalControllerContext
     * @param bundle
     * @return
     */
    private MenubarDropdown getAddDropdown(PortalControllerContext portalControllerContext, Bundle bundle) {
        MenubarDropdown addDropdown = menubarService.getDropdown(portalControllerContext, "ADD");
        if (addDropdown == null) {
            addDropdown = new MenubarDropdown("ADD", bundle.getString("ADD"), "halflings halflings-plus", MenubarGroup.CMS, 2);
            this.menubarService.addDropdown(portalControllerContext, addDropdown);
        }
        return addDropdown;
    }
    

    /**
     * @param nuxeoController
     * @param docTypeParam
     * @param currentDocPath
     * @return
     * @throws PortalException
     */
    private String getZipPortletUrl(PortalControllerContext portalControllerContext , String currentDocPath) throws PortalException {
        Map<String, String> windowProperties = new HashMap<>(2);
        //windowProperties.put(DOC_TYPE_WINDOW_PARAM, docTypeParam);
        windowProperties.put(Constants.WINDOW_PROP_URI, currentDocPath);

        windowProperties.put("osivia.document.edition.parent-path", currentDocPath);
        windowProperties.put("osivia.document.edition.document-type", "File");
        windowProperties.put("osivia.document.edition.extract-archive", "true");

        windowProperties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
        windowProperties.put("osivia.ajaxLink", "1");

        return portalUrlFactory.getStartPortletUrl(portalControllerContext, DOCUMENT_EDITION_PORTLET_INSTANCE, windowProperties, PortalUrlType.MODAL);
    }
}
