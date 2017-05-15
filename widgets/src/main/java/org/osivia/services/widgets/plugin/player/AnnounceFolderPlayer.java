/**
 *
 */
package org.osivia.services.widgets.plugin.player;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.player.Player;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.plugin.PluginModule;
import fr.toutatice.portail.cms.nuxeo.api.portlet.ViewList;

/**
 * Announce folder player.
 *
 * @author Loïc Billon
 * @see PluginModule
 * @see INuxeoPlayerModule
 */
public class AnnounceFolderPlayer extends PluginModule implements INuxeoPlayerModule {

    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public AnnounceFolderPlayer(PortletContext portletContext) {
        super(portletContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
        // Document
        Document document = documentContext.getDocument();

        if ("AnnonceFolder".equals(document.getType())) {
            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put("osivia.nuxeoRequest", NuxeoController.createFolderRequest(documentContext, false));
            windowProperties.put("osivia.cms.style", ViewList.LIST_TEMPLATE_EDITORIAL);
            windowProperties.put("osivia.hideDecorators", "1");
            windowProperties.put("theme.dyna.partial_refresh_enabled", "false");
            windowProperties.put(Constants.WINDOW_PROP_SCOPE, documentContext.getScope());
            windowProperties.put(Constants.WINDOW_PROP_VERSION, documentContext.getDocumentState().toString());
            windowProperties.put("osivia.document.metadata", String.valueOf(false));
            windowProperties.put("osivia.title", document.getTitle());
            windowProperties.put(ViewList.CREATION_PARENT_PATH_WINDOW_PROPERTY, document.getPath());

            Player player = new Player();
            player.setWindowProperties(windowProperties);
            player.setPortletInstance("toutatice-portail-cms-nuxeo-viewListPortletInstance");

            return player;
        } else {
            return null;
        }
    }

}
