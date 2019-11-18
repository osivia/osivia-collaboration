package org.osivia.services.rss.feedRss.portlet.controller;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.services.rss.common.model.ContainerRssModel;
import org.osivia.services.rss.common.validator.ContainerFormValidator;
import org.osivia.services.rss.feedRss.portlet.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Edit Container Rss controller.
 *
 * @author Frédéric Boudan
 */
@Controller
@RequestMapping(value = "VIEW", params = "edit=container")
public class EditContainerController {

	/** Portlet context. */
	@Autowired
	protected PortletContext portletContext;

	/** Application context. */
	@Autowired
	public ApplicationContext applicationContext;

	/** Container RSS service. */
	@Autowired
	protected FeedService service;

	/** Validator. */
	@Autowired
	private ContainerFormValidator formValidator;

	/** Bundle factory. */
	@Autowired
	protected IBundleFactory bundleFactory;

	/** Notifications service. */
	@Autowired
	protected INotificationsService notificationsService;

	// Add render param
	public final static String DOCID = "id";

	/**
	 * Constructor.
	 */
	public EditContainerController() {
		super();
	}

	/**
	 * View container render mapping
	 *
	 * @param request  render request
	 * @param response render response
	 * @throws PortletException
	 */
	@RenderMapping
	public String view(RenderRequest request, RenderResponse response) throws PortletException {

		return "editContainer";
	}

	/**
	 * Modification container
	 * 
	 * @param request
	 * @param response
	 * @param form
	 * @param status
	 * @throws PortletException
	 * @throws IOException
	 */
	@ActionMapping(value = "modif")
	public void add(ActionRequest request, ActionResponse response,
			@Validated @ModelAttribute("form") ContainerRssModel form, BindingResult result, SessionStatus status)
			throws PortletException, IOException {

		// Portal controller context
		PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request,
				response);

		if (result.hasErrors()) {
			response.setRenderParameter("edit", "container");
		} else {
			this.service.creatContainer(portalControllerContext, form);
			status.setComplete();
		}
	}

	/**
	 * Remove container
	 * 
	 * @param request
	 * @param response
	 * @param form
	 * @throws PortletException
	 * @throws IOException
	 */
	@ActionMapping(value = "del")
	public void del(ActionRequest request, ActionResponse response,
			@RequestParam(value = DOCID, required = false) String docId, SessionStatus status)
			throws PortletException, IOException {

		// Portal controller context
		PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request,
				response);

		this.service.removeContainer(portalControllerContext, docId);
		status.setComplete();
	}

	/**
	 * Containerform init binder.
	 *
	 * @param binder data binder
	 */
	@InitBinder("form")
	public void formInitBinder(WebDataBinder binder) {
		binder.addValidators(this.formValidator);
	}

	@ModelAttribute("form")
	public ContainerRssModel getForm(PortletRequest request, PortletResponse response) throws PortletException {
		PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request,
				response);

		return this.service.getMapContainer(portalControllerContext);
	}

}
