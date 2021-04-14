package org.osivia.services.editor.link.portlet.controller;

import org.osivia.services.editor.common.controller.SourceDocumentController;
import org.osivia.services.editor.common.service.CommonService;
import org.osivia.services.editor.link.portlet.service.EditorLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Editor document link source portlet controller.
 *
 * @author Cédric Krommenhoek
 * @see SourceDocumentController
 */
@Controller
@RequestMapping(path = "VIEW", params = "view=document")
public class EditorLinkSourceDocumentController extends SourceDocumentController {

    /**
     * Portlet service.
     */
    @Autowired
    private EditorLinkService service;


    /**
     * Constructor.
     */
    public EditorLinkSourceDocumentController() {
        super();
    }


    @Override
    protected CommonService getService() {
        return this.service;
    }

}
