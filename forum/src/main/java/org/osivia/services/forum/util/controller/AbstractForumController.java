package org.osivia.services.forum.util.controller;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

import org.osivia.portal.api.Constants;
import org.osivia.portal.api.portlet.PortletAppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import javax.annotation.PostConstruct;
import javax.portlet.*;
import java.io.IOException;

/**
 * Forum portlet controller abstract super-class.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletContextAware
 */
public abstract class AbstractForumController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;


    /** Portlet context. */
    private PortletContext portletContext;


    /**
     * Constructor.
     */
    public AbstractForumController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * Get editor properties resource mapping.
     *
     * @param request  resource request
     * @param response resource response
     * @param editorId editor identifier required request parameter
     */
    @ResourceMapping("editor")
    public void getEditor(ResourceRequest request, ResourceResponse response, @RequestParam(name = "editorId") String editorId) throws PortletException, IOException {
        super.serveResourceEditor(request, response, editorId);
    }


    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
                
     }
    
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
            PortletAppUtils.registerApplication(portletConfig, applicationContext);            

    }

}
