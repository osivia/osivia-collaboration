package org.osivia.services.workspace.portlet.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.portlet.model.AclEntries;
import org.osivia.services.workspace.portlet.model.AclEntry;
import org.osivia.services.workspace.portlet.model.AddForm;
import org.osivia.services.workspace.portlet.model.Role;
import org.osivia.services.workspace.portlet.model.comparator.AclEntryComparator;
import org.osivia.services.workspace.portlet.model.converter.RolePropertyEditor;
import org.osivia.services.workspace.portlet.model.validator.AddFormValidator;
import org.osivia.services.workspace.portlet.service.AclManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * Workspace ACL management controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes({"entries", "roles"})
public class AclManagementController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;

    /** Workspace ACL management service. */
    @Autowired
    private AclManagementService service;

    /** Role property editor. */
    @Autowired
    private RolePropertyEditor rolePropertyEditor;

    /** Add form validator. */
    @Autowired
    private AddFormValidator addFormValidator;


    /**
     * Constructor.
     */
    public AclManagementController() {
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
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @param entries ACL entries model attribute
     * @param sortParameter sort request parameter
     * @param altParameter alternative sort indicator request parameter
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response, @ModelAttribute("entries") AclEntries entries,
            @RequestParam(name = "sort", defaultValue = "name") String sortParameter, @RequestParam(name = "alt", required = false) String altParameter) {
        // Sort
        if (sortParameter != null) {
            request.setAttribute("sort", sortParameter);
        }
        boolean alt = BooleanUtils.toBoolean(altParameter);
        request.setAttribute("alt", alt);
        if (CollectionUtils.isNotEmpty(entries.getEntries())) {
            Comparator<AclEntry> comparator = new AclEntryComparator(sortParameter, alt);
            Collections.sort(entries.getEntries(), comparator);
        }


        return "view";
    }


    /**
     * Update action mapping.
     *
     * @param request action request
     * @param response action response
     * @param entries ACL entries model attribute
     * @param roles roles model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "update")
    public void update(ActionRequest request, ActionResponse response, @ModelAttribute("entries") AclEntries entries, @ModelAttribute("roles") List<Role> roles)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.update(portalControllerContext, entries, roles);

        this.copyRenderParameter(request, response);
    }


    /**
     * Add action mapping.
     *
     * @param request action request
     * @param response action response
     * @param entries ACL entries model attribute
     * @param roles roles model attribute
     * @param form add form model attribute
     * @param result binding result
     * @throws PortletException
     */
    @ActionMapping(name = "add", params = "save")
    public void add(ActionRequest request, ActionResponse response, @ModelAttribute("entries") AclEntries entries, @ModelAttribute("roles") List<Role> roles,
            @ModelAttribute("addForm") @Validated AddForm form, BindingResult result) throws PortletException {
        if (!result.hasErrors()) {
            // Portal controller context
            PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

            this.service.add(portalControllerContext, entries, roles, form);
        }

        this.copyRenderParameter(request, response);
    }


    /**
     * Cancel add action mapping.
     *
     * @param request action request
     * @param response action response
     */
    @ActionMapping(name = "add", params = "cancel")
    public void cancelAdd(ActionRequest request, ActionResponse response) {
        this.copyRenderParameter(request, response);
    }


    /**
     * Reset action mapping.
     *
     * @param request action request
     * @param response action response
     * @param entries ACL entries model attribute
     * @throws PortletException
     */
    @ActionMapping(name = "reset")
    public void reset(ActionRequest request, ActionResponse response, @ModelAttribute("entries") AclEntries entries) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        this.service.reset(portalControllerContext, entries);
    }


    /**
     * Close popup action mapping.
     *
     * @param request action request
     * @param response action response
     * @param entries ACL entries model attribute
     * @throws PortletException
     */
    @ActionMapping("close")
    public void close(ActionRequest request, ActionResponse response, @ModelAttribute("entries") AclEntries entries) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        // Redirection URL
        String redirectionUrl = this.service.getRedirectionUrl(portalControllerContext, entries);
        request.setAttribute(Constants.PORTLET_ATTR_REDIRECTION_URL, redirectionUrl);
    }


    /**
     * Get ACL entries model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return ACL entries
     * @throws PortletException
     */
    @ModelAttribute("entries")
    public AclEntries getAclEntries(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getAclEntries(portalControllerContext);
    }


    /**
     * ACL entries model attribute init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("entries")
    protected void aclEntriesInitBinder(WebDataBinder binder) {
        binder.setDisallowedFields("workspaceId", "document");
        binder.registerCustomEditor(Role.class, this.rolePropertyEditor);
    }


    /**
     * Get add form model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return add form
     * @throws PortletException
     */
    @ModelAttribute("addForm")
    public AddForm getAddForm(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getAddForm(portalControllerContext);
    }


    /**
     * Add form model attribute init binder.
     *
     * @param binder web data binder
     */
    @InitBinder("addForm")
    protected void addFormInitBinder(WebDataBinder binder) {
        binder.setDisallowedFields("records");
        binder.registerCustomEditor(Role.class, this.rolePropertyEditor);
        binder.addValidators(this.addFormValidator);
    }


    /**
     * Get roles model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return roles
     * @throws PortletException
     */
    @ModelAttribute("roles")
    public List<Role> getRoles(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getRoles(portalControllerContext);
    }


    /**
     * Copy render parameters.
     *
     * @param request action request
     * @param response action response
     */
    private void copyRenderParameter(ActionRequest request, ActionResponse response) {
        // Sort
        String sort = request.getParameter("sort");
        if (sort != null) {
            response.setRenderParameter("sort", sort);
        }

        // Alt
        String alt = request.getParameter("alt");
        if (alt != null) {
            response.setRenderParameter("alt", alt);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
