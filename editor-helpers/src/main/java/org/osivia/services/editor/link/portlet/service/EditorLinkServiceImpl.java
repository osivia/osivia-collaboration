package org.osivia.services.editor.link.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Editor link portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see EditorLinkService
 * @see ApplicationContextAware
 */
@Service
public class EditorLinkServiceImpl implements EditorLinkService, ApplicationContextAware {

    /** Application context. */
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public EditorLinkServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public EditorLinkForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        return this.applicationContext.getBean(EditorLinkForm.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
