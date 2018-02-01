package org.osivia.services.pins.plugin.fragment;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.pins.plugin.service.PinsPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule;

/**
 * Pins fragment module
 * @author jbarberet
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PinsFragmentModule extends FragmentModule {

	@Autowired
	private PinsPluginService service;
	
	/**
	 * Constructor
	 * @param portletContext
	 */
	public PinsFragmentModule(PortletContext portletContext) {
		super(portletContext);
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
	public boolean isDisplayedInAdmin() {
		return true;
	}

	/**
     * {@inheritDoc}
     */
    @Override
	public String getViewJSPName() {
		return "pins";
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
	protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext)
			throws PortletException, IOException {
		
		// Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
		
		List<DocumentDTO> documents = service.getDocumentsList(portalControllerContext);
		request.setAttribute("documents", documents);
		
	}

}
