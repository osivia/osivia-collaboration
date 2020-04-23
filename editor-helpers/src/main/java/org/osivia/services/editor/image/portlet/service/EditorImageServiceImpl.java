package org.osivia.services.editor.image.portlet.service;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.editor.image.portlet.model.EditorImageForm;
import org.osivia.services.editor.image.portlet.model.ImageSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.portlet.PortletException;
import java.util.Arrays;
import java.util.List;

/**
 * Editor image portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see EditorImageService
 */
@Service
public class EditorImageServiceImpl implements EditorImageService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public EditorImageServiceImpl() {
        super();
    }


    @Override
    public EditorImageForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Form
        EditorImageForm form = this.applicationContext.getBean(EditorImageForm.class);

        // Source URL
        String url = window.getProperty(SRC_WINDOW_PROPERTY);
        form.setUrl(url);

        // Alternate text
        String alt = window.getProperty(ALT_WINDOW_PROPERTY);
        form.setAlt(alt);

        return form;
    }


    @Override
    public List<ImageSourceType> getSourceTypes(PortalControllerContext portalControllerContext) throws PortletException {
        return Arrays.asList(ImageSourceType.values());
    }


    @Override
    public void save(PortalControllerContext portalControllerContext, EditorImageForm form) throws PortletException {
        form.setDone(true);
    }
}
