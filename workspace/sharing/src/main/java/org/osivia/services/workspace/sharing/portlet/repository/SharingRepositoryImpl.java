package org.osivia.services.workspace.sharing.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.workspace.sharing.portlet.model.SharingLink;
import org.osivia.services.workspace.sharing.portlet.model.SharingPermission;
import org.osivia.services.workspace.sharing.portlet.repository.command.DisableSharingCommand;
import org.osivia.services.workspace.sharing.portlet.repository.command.EnableSharingCommand;
import org.osivia.services.workspace.sharing.portlet.repository.command.GetSharingPermissionsCommand;
import org.osivia.services.workspace.sharing.portlet.repository.command.UpdateSharingPermissionsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Sharing portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see SharingRepository
 */
@Repository
public class SharingRepositoryImpl implements SharingRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     */
    public SharingRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSharingEnabled(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        // Document
        Document document = documentContext.getDocument();

        // Facets
        PropertyList facets = document.getFacets();

        boolean enabled = false;
        if (facets != null) {
            int i = 0;
            while (!enabled && (i < facets.size())) {
                String facet = facets.getString(i);
                enabled = SHARING_FACET.equals(facet);
                i++;
            }
        }

        return enabled;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SharingLink getLink(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        documentContext.reload();
        // Document
        Document document = documentContext.getDocument();


        // Sharing link
        SharingLink link = this.applicationContext.getBean(SharingLink.class);

        // Link identifier
        String id = document.getString(SHARING_LINK_ID_PROPERTY);
        link.setId(id);

        // Link URL
        if (StringUtils.isNotEmpty(id)) {
            String url = this.portalUrlFactory.getSharingLinkUrl(portalControllerContext, id);
            link.setUrl(url);
        }

        // Link permission
        SharingPermission permission = SharingPermission.fromId(document.getString(SHARING_LINK_PERMISSION_PROPERTY));
        link.setPermission(permission);

        return link;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getUsers(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetSharingPermissionsCommand.class, path);
        Object result = nuxeoController.executeNuxeoCommand(command);

        // Users
        List<String> users;

        if ((result != null) && (result instanceof JSONArray)) {
            JSONArray array = (JSONArray) result;
            users = new ArrayList<>(array.size());

            for (int i = 0; i < array.size(); i++) {
                JSONObject object = array.getJSONObject(i);
                String user = object.getString("user");
                users.add(user);
            }
        } else {
            users = null;
        }

        return users;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void enableSharing(PortalControllerContext portalControllerContext, String path, SharingLink link) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(EnableSharingCommand.class, path, link);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void disableSharing(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(DisableSharingCommand.class, path);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePermissions(PortalControllerContext portalControllerContext, String path, SharingPermission permission, String user, Boolean add)
            throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Permission identifier
        String permissionId;
        if (permission == null) {
            permissionId = null;
        } else {
            permissionId = permission.getId();
        }

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(UpdateSharingPermissionsCommand.class, path, permissionId, user, add);
        nuxeoController.executeNuxeoCommand(command);
    }

}
