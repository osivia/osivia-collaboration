package org.osivia.services.editor.image.portlet.controller;

import org.osivia.services.editor.common.controller.SourceDocumentController;
import org.osivia.services.editor.common.service.CommonService;
import org.osivia.services.editor.image.portlet.service.EditorImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Editor document image source portlet controller.
 *
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping(path = "VIEW", params = "view=document")
public class EditorImageSourceDocumentController extends SourceDocumentController {

    /**
     * Portlet service.
     */
    @Autowired
    private EditorImageService service;


    /**
     * Constructor.
     */
    public EditorImageSourceDocumentController() {
        super();
    }


    @Override
    protected CommonService getService() {
        return this.service;
    }

}
