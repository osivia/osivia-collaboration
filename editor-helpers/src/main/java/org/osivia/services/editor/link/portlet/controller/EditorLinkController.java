package org.osivia.services.editor.link.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.editor.link.portlet.model.EditorLinkForm;
import org.osivia.services.editor.link.portlet.service.EditorLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletContextAware;

/**
 * Editor link portlet controller.
 * 
 * @author Cédric Krommenhoek
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
public class EditorLinkController implements PortletContextAware {

    /** Portlet context. */
    private PortletContext portletContext;


    /** Portlet service. */
    @Autowired
    private EditorLinkService service;


    /**
     * Constructor.
     */
    public EditorLinkController() {
        super();
    }


    /**
     * View render mapping.
     * 
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }


    /**
     * Submit action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param form
     */
    @ActionMapping("submit")
    public void submit(ActionRequest request, ActionResponse response, @ModelAttribute("form") EditorLinkForm form) {
        if (StringUtils.isNotBlank(form.getLink())) {
            form.setDone(true);
        }
    }


    /**
     * Get editor link form model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute("form")
    public EditorLinkForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        return this.service.getForm(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
