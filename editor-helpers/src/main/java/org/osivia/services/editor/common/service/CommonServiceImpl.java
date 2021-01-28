package org.osivia.services.editor.common.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.portlet.PortletException;

/**
 * Portlet common service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonService
 */
public abstract class CommonServiceImpl implements CommonService {


    /**
     * View resolver.
     */
    @Autowired
    private InternalResourceViewResolver viewResolver;


    /**
     * Constructor.
     */
    public CommonServiceImpl() {
        super();
    }


    @Override
    public String resolveViewPath(PortalControllerContext portalControllerContext, String name) throws PortletException {
        // Path
        String path;

        try {
            View view = this.viewResolver.resolveViewName(name, null);
            JstlView jstlView = (JstlView) view;
            path = jstlView.getUrl();
        } catch (Exception e) {
            throw new PortletException(e);
        }

        return path;
    }

}
