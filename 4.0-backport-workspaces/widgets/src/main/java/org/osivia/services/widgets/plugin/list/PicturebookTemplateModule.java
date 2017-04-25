/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *
 *
 */
package org.osivia.services.widgets.plugin.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.Notifications;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.widgets.plugin.util.UploadFilesCommand;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;


/**
 * Picturebook template module.
 *
 * @author Cédric Krommenhoek
 * @see PortletModule
 */
public class PicturebookTemplateModule extends PortletModule {

    /** File upload notifications duration. */
    private static final int FILE_UPLOAD_NOTIFICATIONS_DURATION = 1000;

    /** Notifications service. */
    private final INotificationsService notificationsService;


    /**
     * Constructor.
     *
     * @param portletContext portlet context
     */
    public PicturebookTemplateModule(PortletContext portletContext) {
        super(portletContext);

        // Notification service
        this.notificationsService = Locator.findMBean(INotificationsService.class, INotificationsService.MBEAN_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Path
        String path = window.getProperty(Constants.WINDOW_PROP_URI);

        if (StringUtils.isNotEmpty(path)) {
            // Nuxeo controller
            NuxeoDocumentContext document = NuxeoController.getDocumentContext(request, response, portletContext);

            request.setAttribute("editable", document.getPermissions(BasicPermissions.class).isEditableByUser());
            request.setAttribute("parentId", document.getPublicationInfos(BasicPublicationInfos.class).getLiveId());

        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void processAction(PortalControllerContext portalControllerContext) throws PortletException {
        // Request
        ActionRequest request = (ActionRequest) portalControllerContext.getRequest();
        // Response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Action name
        String action = request.getParameter(ActionRequest.ACTION_NAME);

        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, portalControllerContext.getPortletCtx());
        // Bundle
        Bundle bundle = this.getBundleFactory().getBundle(request.getLocale());

        if (PortletMode.VIEW.equals(request.getPortletMode())) {
            // View

            if ("fileUpload".equals(action)) {
                // File upload

                String parentId = request.getParameter("parentId");

                // Notification
                Notifications notifications;

                try {
                    DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
                    PortletFileUpload fileUpload = new PortletFileUpload(fileItemFactory);
                    List<FileItem> fileItems = fileUpload.parseRequest(request);

                    // Accepted files
                    List<FileItem> acceptedFileItems = new ArrayList<FileItem>(fileItems.size());
                    for (FileItem fileItem : fileItems) {
                        boolean accepted;
                        try {
                            MimeType mimeType = new MimeType(fileItem.getContentType());
                            String primaryType = mimeType.getPrimaryType();
                            accepted = "image".equals(primaryType);
                        } catch (MimeTypeParseException e) {
                            accepted = false;
                        }

                        if (accepted) {
                            acceptedFileItems.add(fileItem);
                        } else {
                            Notifications rejectedFileNotifications = new Notifications(NotificationsType.WARNING, FILE_UPLOAD_NOTIFICATIONS_DURATION);
                            rejectedFileNotifications.addMessage(bundle.getString("MESSAGE_FILE_UPLOAD_REJECTED_FILE", fileItem.getName()));
                            this.notificationsService.addNotifications(portalControllerContext, rejectedFileNotifications);
                        }
                    }

                    if (!acceptedFileItems.isEmpty()) {
                        // Nuxeo command
                        INuxeoCommand command = new UploadFilesCommand(parentId, acceptedFileItems);
                        nuxeoController.executeNuxeoCommand(command);

                        // Refresh navigation
                        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);

                        // Notification
                        notifications = new Notifications(NotificationsType.SUCCESS, FILE_UPLOAD_NOTIFICATIONS_DURATION);
                        notifications.addMessage(bundle.getString("MESSAGE_FILE_UPLOAD_SUCCESS"));
                    } else {
                        notifications = null;
                    }
                } catch (FileUploadException e) {
                    // Notification
                    notifications = new Notifications(NotificationsType.ERROR, FILE_UPLOAD_NOTIFICATIONS_DURATION);
                    notifications.addMessage(bundle.getString("MESSAGE_FILE_UPLOAD_ERROR"));
                }

                this.notificationsService.addNotifications(portalControllerContext, notifications);
            }
        }
    }

}
