package org.osivia.services.workspace.plugin.menubar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.service.RoleService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspace member management menubar module.
 *
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
public class MemberManagementMenubarModule implements MenubarModule {

    /** Menubar item identifier. */
    public static final String MENUBAR_ITEM_ID = "WORKSPACE_MEMBER_MANAGEMENT";


    /** Menubar service. */
    private final IMenubarService menubarService;
    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** Bundle factory. */
    private final IBundleFactory bundleFactory;
    /** Person service. */
    private final PersonService personService;
    /** Role service. */
    private final RoleService roleService;


    /**
     * Constructor.
     */
    public MemberManagementMenubarModule() {
        super();

        // Menubar service
        this.menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // Bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
        // Person service
        this.personService = DirServiceFactory.getService(PersonService.class);
        // Role service
        this.roleService = DirServiceFactory.getService(RoleService.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext spaceDocumentContext)
            throws PortalException {
        if (spaceDocumentContext != null) {
            // Space document
            Document document = (Document) spaceDocumentContext.getDocument();
            if (document != null) {
                // Check document type
                String documentType = document.getType();
                if ("Workspace".equals(documentType)) {
                    // Workspace type
                    String visibility = document.getString("ttcs:visibility");
                    WorkspaceType workspaceType;
                    if (StringUtils.isEmpty(visibility)) {
                        workspaceType = null;
                    } else {
                        workspaceType = WorkspaceType.valueOf(visibility);
                    }

                    // Check permissions
                    boolean granted;
                    if ((workspaceType != null) && workspaceType.isPortalAdministratorRestriction()) {
                        // User
                        String user = portalControllerContext.getRequest().getRemoteUser();
                        // User DN
                        Name dn = this.personService.getEmptyPerson().buildDn(user);

                        // Administrator indicator
                        granted = this.roleService.hasRole(dn, "role_workspace-management");
                    } else {
                        Permissions permissions = spaceDocumentContext.getPermissions();
                        granted = permissions.isManageable();
                    }
                    
                    if (granted) {
                        // HTTP servlet request
                        HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
                        // Bundle
                        Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

                        // Window properties
                        Map<String, String> properties = new HashMap<String, String>();
                        properties.put("osivia.title", bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_TITLE"));
                        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
                        properties.put("osivia.ajaxLink", "1");
                        properties.put("osivia.back.reset", String.valueOf(true));
                        properties.put("osivia.navigation.reset", String.valueOf(true));

                        // Menubar item
                        String title = bundle.getString("WORKSPACE_MEMBER_MANAGEMENT_MENUBAR_ITEM");
                        String icon = "glyphicons glyphicons-parents";
                        MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CONFIGURATION_DROPDOWN_MENU_ID);
                        int order = 2;
                        String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-workspace-member-management-instance",
                                properties);
                        String target = null;
                        String onclick = null;
                        String htmlClasses = null;

                        MenubarItem menubarItem = new MenubarItem(MENUBAR_ITEM_ID, title, icon, parent, order, url, target, onclick, htmlClasses);
                        menubar.add(menubarItem);
                    }
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        // Do nothing
    }

}

