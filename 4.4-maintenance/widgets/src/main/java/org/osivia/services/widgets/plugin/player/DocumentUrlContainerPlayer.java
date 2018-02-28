/**
 * 
 */
package org.osivia.services.widgets.plugin.player;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentState;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.player.Player;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.constants.InternalConstants;

import fr.toutatice.portail.cms.nuxeo.api.FileBrowserView;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
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

	/* (non-Javadoc)
	 * @see org.osivia.portal.api.player.IPlayerModule#getCMSPlayer(org.osivia.portal.api.cms.DocumentContext)
	 */
	@Override
	public Player getCMSPlayer(DocumentContext<Document> docCtx) {
		
		BasicPublicationInfos navigationInfos = docCtx.getPublicationInfos(BasicPublicationInfos.class);
		Document doc = docCtx.getDoc();

        // Workspace indicator
        boolean workspace = (navigationInfos.isContextualized()  && navigationInfos.isLiveSpace());
		
        if (("DocumentUrlContainer".equals(doc.getType()))) {
            if (workspace) {
                // File browser
                //cmsContext.setDisplayLiveVersion("1");
                navigationInfos.setState(DocumentState.LIVE);
                Player properties = getNuxeoCustomizer().getCMSFileBrowser(docCtx);
                Map<String, String> windowProperties = properties.getWindowProperties();
                windowProperties.put(InternalConstants.PROP_WINDOW_TITLE, doc.getTitle());
                windowProperties.put(InternalConstants.DEFAULT_VIEW_WINDOW_PROPERTY, FileBrowserView.THUMBNAILS.getName());
                return properties;
            } else {
                return this.getCMSUrlContainerPlayer(docCtx);
            }
        }
        else return null;
	}


    /**
     * Get CMS URL container player.
     *
     * @param ctx CMS context
     * @return CMS URL container player
     * @throws CMSException
     */
    public Player getCMSUrlContainerPlayer(DocumentContext<Document> docCtx) {
    	
    	BasicPublicationInfos navigationInfos = docCtx.getPublicationInfos(BasicPublicationInfos.class);
    	
        Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.nuxeoRequest", NuxeoController.createFolderRequest(docCtx, true));
        windowProperties.put("osivia.cms.style", ViewList.LIST_TEMPLATE_CONTEXTUAL_LINKS);
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("theme.dyna.partial_refresh_enabled", "false");
        windowProperties.put(Constants.WINDOW_PROP_SCOPE, navigationInfos.getScope());
        windowProperties.put(Constants.WINDOW_PROP_VERSION, navigationInfos.getState().toString());
        // TODO
        //windowProperties.put(InternalConstants.METADATA_WINDOW_PROPERTY, ctx.getHideMetaDatas());
        windowProperties.put("osivia.cms.pageSizeMax", "10");
        // JSS V3.1 : incompatible avec refresh CMS de type portlets
        // windowProperties.put("osivia.title", "Liste de liens");

        Player linkProps = new Player();
        linkProps.setWindowProperties(windowProperties);
        linkProps.setPortletInstance("toutatice-portail-cms-nuxeo-viewListPortletInstance");

        return linkProps;
    }

}
