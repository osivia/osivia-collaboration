package org.osivia.services.contact.portlet.controller;

import com.github.cage.GCage;
import com.sun.mail.smtp.SMTPTransport;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.contact.portlet.model.Form;
import org.osivia.services.contact.portlet.model.validation.ContactFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.portlet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Controller
@RequestMapping({"VIEW"})
@SessionAttributes("form")
public class ContactController
        extends CMSPortlet
        implements PortletConfigAware, PortletContextAware {
    private static final String DEFAULT_VIEW = "view";

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IBundleFactory bundleFactory;

    @Autowired
    private INotificationsService notificationService;

    /**
     * Contact form validator.
     */
    @Autowired
    private ContactFormValidator formValidator;

    private PortletConfig portletConfig;

    private PortletContext portletContext;

    private static String bodyFormat(Form form, Bundle bundle) {
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

    @PostConstruct
    public void postConstruct()
            throws PortletException {
        super.init(portletConfig);
    }

    @RenderMapping
    public String defaultView(RenderRequest request, RenderResponse response)
            throws PortletException {
        return "view";
    }

    @ModelAttribute("form")
    public Form getForm(PortletRequest request, PortletResponse response) {
        PortalWindow window = WindowFactory.getWindow(request);

        Bundle bundle = bundleFactory.getBundle(request.getLocale());
        GCage gcage = new GCage();

        Form form = this.applicationContext.getBean(Form.class);
        form.setTo(window.getProperty("osivia.contact.mailto"));
        form.setObject(window.getProperty("osivia.contact.object"));
        form.setToken(gcage.getTokenGenerator().next());
        form.setFromLabel(StringUtils.defaultIfBlank(window.getProperty("osivia.contact.fromLabel"), bundle.getString("ENTER_EMAIL")));
        form.setNomLabel(StringUtils.defaultIfBlank(window.getProperty("osivia.contact.nomLabel"), bundle.getString("NAMES")));
        form.setObjectLabel(StringUtils.defaultIfBlank(window.getProperty("osivia.contact.objectLabel"), bundle.getString("SUBJECT")));
        form.setBodyLabel(StringUtils.defaultIfBlank(window.getProperty("osivia.contact.bodyLabel"), bundle.getString("MESSAGE")));
        return form;
    }

    @ActionMapping(name = "submit", params = "send")
    public void sendMail(ActionRequest request, ActionResponse response, @ModelAttribute("form") @Validated Form form, BindingResult result, SessionStatus status)
            throws PortletException, IOException {
        PortalControllerContext pcc = new PortalControllerContext(getPortletContext(), request, response);

        Bundle bundle = bundleFactory.getBundle(request.getLocale());

        Properties props = System.getProperties();

        Session mailSession = Session.getInstance(props, null);

        MimeMessage msg = new MimeMessage(mailSession);
        try {
            if (!result.hasErrors()) {
                status.setComplete();
                InternetAddress from = new InternetAddress(form.getFrom());
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
                SMTPTransport t = (SMTPTransport) mailSession.getTransport();
                t.connect();
                t.sendMessage(msg, msg.getAllRecipients());
                t.close();
                form.setSent(true);
            }
        } catch (MessagingException e) {
            throw new PortletException(e);
        }
    }

    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    /**
     * Contactform init binder.
     *
     * @param binder data binder
     */
    @InitBinder("form")
    public void formInitBinder(WebDataBinder binder) {
        binder.addValidators(this.formValidator);
        binder.setDisallowedFields("token", "captchaValidate");
    }

    /**
     * @throws IOException
     */
    @ResourceMapping("captcha")
    public void captchaImage(ResourceRequest request, ResourceResponse response, @ModelAttribute("form") Form form) throws IOException {
        GCage gcage = new GCage();

        response.setContentType("image/" + gcage.getFormat());
        response.setProperty("Cache-Control", "no-cache");
        response.setProperty("Progma", "no-cache");

        gcage.draw(form.getToken(), response.getPortletOutputStream());
    }

    @ActionMapping(name = "submit", params = "reload")
    public void reloadImage(ActionRequest request, ActionResponse response, @ModelAttribute("form") Form form) {
        GCage gcage = new GCage();
        form.setToken(gcage.getTokenGenerator().next());
    }
}

