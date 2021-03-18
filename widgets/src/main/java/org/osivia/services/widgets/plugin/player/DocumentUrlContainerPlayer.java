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
import org.osivia.portal.core.cms.CMSException;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.plugin.PluginModule;
import fr.toutatice.portail.cms.nuxeo.api.portlet.ViewList;

/**
 * @author Loïc Billon
 *
 */
public class DocumentUrlContainerPlayer extends PluginModule implements INuxeoPlayerModule {

	/**
	 * @param portletContext
	 */
	public DocumentUrlContainerPlayer(PortletContext portletContext) {
		super(portletContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Player getCMSPlayer(NuxeoDocumentContext documentContext) {
		NuxeoPublicationInfos navigationInfos = documentContext.getPublicationInfos();
		Document doc = documentContext.getDocument();

		// Workspace indicator
		boolean workspace = (documentContext.isContextualized() && navigationInfos.isLiveSpace());

		if (("DocumentUrlContainer".equals(doc.getType())) && !workspace) {

			return this.getCMSUrlContainerPlayer(documentContext);

		} else
			return null;
	}

	/**
	 * Get CMS URL container player.
	 *
	 * @param documentContext document context
	 * @return CMS URL container player
	 * @throws CMSException
	 */
	public Player getCMSUrlContainerPlayer(NuxeoDocumentContext documentContext) {
		Map<String, String> windowProperties = new HashMap<String, String>();
		windowProperties.put("osivia.nuxeoRequest", NuxeoController.createFolderRequest(documentContext, true));
		windowProperties.put("osivia.cms.style", ViewList.LIST_TEMPLATE_CONTEXTUAL_LINKS);
		windowProperties.put("osivia.hideDecorators", "1");
		windowProperties.put("theme.dyna.partial_refresh_enabled", "false");
		windowProperties.put(Constants.WINDOW_PROP_SCOPE, documentContext.getScope());
		windowProperties.put(Constants.WINDOW_PROP_VERSION, documentContext.getDocumentState().toString());
		windowProperties.put("osivia.cms.pageSizeMax", "10");

		Player linkProps = new Player();
		linkProps.setWindowProperties(windowProperties);
		linkProps.setPortletInstance("toutatice-portail-cms-nuxeo-viewListPortletInstance");

		return linkProps;
	}

}
