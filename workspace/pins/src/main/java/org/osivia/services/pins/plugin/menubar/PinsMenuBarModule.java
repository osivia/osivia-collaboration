package org.osivia.services.pins.plugin.menubar;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Pins menubar module
 * @author jbarberet
 *
 */
@Component
public class PinsMenuBarModule implements MenubarModule {

	private static final String WORKSPACE = "Workspace";
	private static final String MODEL_VERSION = "ttc:modelVersion";
	private static final Object WS_V_1 = "1.0";
	private static final String WS_ID = "acr:shortName";

	@Autowired
	private WorkspaceService wsService;
	/** Menubar service. */
	@Autowired
	private IMenubarService menubarService;
	/** Portal URL factory. */
	@Autowired
	private IPortalUrlFactory urlFactory;
	/** Bundle factory. */
	@Autowired
	private IBundleFactory bundleFactory;

	/**
	 * Constructor.
	 */
	public PinsMenuBarModule() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void customizeSpace(PortalControllerContext portalControllerContext,
			List<MenubarItem> menubar, DocumentContext spaceDocumentContext)
					throws PortalException {

		if (spaceDocumentContext != null && spaceDocumentContext.getDocument() != null && spaceDocumentContext.getDocumentType() != null) {

			if (StringUtils.equals(WORKSPACE, spaceDocumentContext.getDocumentType().getName())) {
				if (spaceDocumentContext instanceof NuxeoDocumentContext) {
					spaceDocumentContext = (NuxeoDocumentContext) spaceDocumentContext;
					Document document = ((NuxeoDocumentContext) spaceDocumentContext).getDocument();

					final String version = document.getString(MODEL_VERSION);

					if (version != null && version.equals(WS_V_1)) {
						String uid = portalControllerContext.getHttpServletRequest().getRemoteUser();

						WorkspaceMember member = wsService.getMember(document.getString(WS_ID), uid);

						if (isAdministrator(portalControllerContext) || (member != null && member.getRole() == WorkspaceRole.OWNER)) {

							Locale locale = null;
							if(portalControllerContext.getRequest() != null) {
								locale = portalControllerContext.getRequest().getLocale();
							}

							Bundle bundle = bundleFactory.getBundle(locale);

							// --------- EDIT Workspace metadata
							Map<String, String> windowProperties = new HashMap<String, String>();
							windowProperties.put("osivia.ajaxLink", "1");
							windowProperties.put("theme.dyna.partial_refresh_enabled", "true");
							windowProperties.put("osivia.title", bundle.getString("MANAGE_PINED_ACTION"));

							windowProperties.put("workspaceId", document.getId());

							final String urlEditWorkspace = this.urlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-workspace-pins-instance", windowProperties);

							MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CONFIGURATION_DROPDOWN_MENU_ID);
							final MenubarItem manageWkspItem = new MenubarItem("MANAGE_PINED", bundle.getString("MANAGE_PINED_ACTION"), "glyphicons glyphicons-wallet", parent, 12, urlEditWorkspace, null, null, null);
							manageWkspItem.setAjaxDisabled(true);

							menubar.add(manageWkspItem);

						}
					}
				}
			}
		}
	}

	/**
	 * Return true if the user has administrator role
	 * @param portalControllerContext
	 * @return true if the user has administrator role
	 */
	private boolean isAdministrator(PortalControllerContext portalControllerContext) {
		PortletRequest request = portalControllerContext.getRequest();
		if(request != null) 
			return BooleanUtils.isTrue((Boolean) request.getAttribute("osivia.isAdministrator")); 
		else return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void customizeDocument(
			PortalControllerContext portalControllerContext,
			List<MenubarItem> menubar, DocumentContext documentContext)
					throws PortalException {

	}

}
