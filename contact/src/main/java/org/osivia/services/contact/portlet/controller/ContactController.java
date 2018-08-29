package org.osivia.services.contact.portlet.controller;

import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.contact.portlet.model.Form;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import com.sun.mail.smtp.SMTPTransport;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

@Controller
@RequestMapping({"VIEW"})
public class ContactController
extends CMSPortlet
implements PortletConfigAware, PortletContextAware
{
    private static final String DEFAULT_VIEW = "view";

    private static final String RETOUR_LIGNE = "<br />";

    @Autowired
    private IBundleFactory bundleFactory;

    @Autowired
    private INotificationsService notificationService;

    private PortletConfig portletConfig;

    private PortletContext portletContext;

    @PostConstruct
    public void postConstruct()
            throws PortletException
    {
        super.init(portletConfig);
    }

    @RenderMapping
    public String defaultView(RenderRequest request, RenderResponse response)
            throws PortletException
    {
        return "view";
    }

    @ModelAttribute("form")
    public Form getForm(PortletRequest request, PortletResponse response)
    {
        PortalWindow window = WindowFactory.getWindow(request);

        Bundle bundle = bundleFactory.getBundle(request.getLocale());

        Form form = new Form();
        form.setTo(window.getProperty("osivia.contact.mailto"));
        form.setFromLabel(StringUtils.defaultIfBlank(window.getProperty("osivia.contact.fromLabel"), bundle.getString("ENTER_EMAIL")));
        form.setNomLabel(StringUtils.defaultIfBlank(window.getProperty("osivia.contact.nomLabel"), bundle.getString("NAMES")));
        form.setObjectLabel(StringUtils.defaultIfBlank(window.getProperty("osivia.contact.objectLabel"), bundle.getString("SUBJECT")));
        form.setObject(window.getProperty("osivia.contact.object"));
        form.setBodyLabel(StringUtils.defaultIfBlank(window.getProperty("osivia.contact.bodyLabel"), bundle.getString("MESSAGE")));
        return form;
    }

    @ActionMapping
    public void sendMail(ActionRequest request, ActionResponse response, @ModelAttribute("form") Form form)
            throws PortletException
    {
        PortalControllerContext pcc = new PortalControllerContext(getPortletContext(), request, response);

        Bundle bundle = bundleFactory.getBundle(request.getLocale());

        Properties props = System.getProperties();

        Session mailSession = Session.getInstance(props, null);

        MimeMessage msg = new MimeMessage(mailSession);

        InternetAddress from = null;
        try
        {
            from = new InternetAddress(form.getFrom());
            form.setFromError(false);
        }
        catch (AddressException e1)
        {
            notificationService.addSimpleNotification(pcc, bundle.getString("MAILTO_ERROR_MSG"), NotificationsType.ERROR);
            form.setFromError(true);
        }
        if (StringUtils.isBlank(form.getNom()))
        {
            notificationService.addSimpleNotification(pcc, bundle.getString("NAME_ERROR_MSG"), NotificationsType.ERROR);
            form.setNomError(true);
        }
        else
        {
            form.setNomError(false);
        }
        if (StringUtils.isBlank(form.getObject()))
        {
            notificationService.addSimpleNotification(pcc, bundle.getString("SUBJECT_ERROR_MSG"), NotificationsType.ERROR);
            form.setObjectError(true);
        }
        else
        {
            form.setObjectError(false);
        }
        if (StringUtils.isBlank(form.getBody()))
        {
            notificationService.addSimpleNotification(pcc, bundle.getString("MESSAGE_ERROR_MSG"), NotificationsType.ERROR);
            form.setBodyError(true);
        }
        else
        {
            form.setBodyError(false);
        }
        try
        {
            if (!form.hasError())
            {
                msg.setFrom(from);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(form.getTo(), false));
                msg.setSubject(form.getObject(), "UTF-8");
                Multipart mp = new MimeMultipart();
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(bodyFormat(form, bundle), "text/html; charset=UTF-8");
                mp.addBodyPart(htmlPart);
                msg.setContent(mp);
                msg.setSentDate(new Date());
                InternetAddress[] replyToTab = new InternetAddress[1];
                replyToTab[0] = from;
                msg.setReplyTo(replyToTab);
                SMTPTransport t = (SMTPTransport)mailSession.getTransport();
                t.connect();
                t.sendMessage(msg, msg.getAllRecipients());
                t.close();
                form.setSent(true);
            }
        }
        catch (MessagingException e)
        {
            throw new PortletException(e);
        }
    }

    private static String bodyFormat(Form form, Bundle bundle)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(bundle.getString("NEW_MAIL") + form.getNom() + " (" + form.getFrom() + ")");
        sb.append("<br />");
        sb.append(bundle.getString("NEW_MAIL_SUBJECT") + form.getObject());
        sb.append("<br />");

        String body = form.getBody();
        body = body.replaceAll("(\r\n|\n)", "<br />");

        sb.append(body);

        return sb.toString();
    }

    @Override
    public void setPortletConfig(PortletConfig portletConfig)
    {
        this.portletConfig = portletConfig;
    }

    @Override
    public void setPortletContext(PortletContext portletContext)
    {
        this.portletContext = portletContext;
    }
}

