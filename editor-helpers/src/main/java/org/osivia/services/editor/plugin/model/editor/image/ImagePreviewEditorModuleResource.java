package org.osivia.services.editor.plugin.model.editor.image;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.editor.EditorModuleResource;
import org.osivia.services.editor.image.portlet.service.EditorImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.portlet.PortletException;
import java.io.IOException;

/**
 * Image preview editor module resource.
 *
 * @author Cédric Krommenhoek
 * @see EditorModuleResource
 */
@Component
public class ImagePreviewEditorModuleResource implements EditorModuleResource {

    /**
     * Resource identifier.
     */
    public static final String ID = "preview";


    /**
     * Image service.
     */
    @Autowired
    private EditorImageService imageService;


    /**
     * Constructor.
     */
    public ImagePreviewEditorModuleResource() {
        super();
    }


    @Override
    public void serve(PortalControllerContext portalControllerContext) throws PortletException, IOException {
        this.imageService.serveImagePreview(portalControllerContext);
    }

}
