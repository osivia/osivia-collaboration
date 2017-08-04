package org.osivia.services.forum.thread.portlet.model;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import java.io.IOException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ForumThreadParserContext {

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Person service. */
    @Autowired
    private PersonService personService;


    /** Portal controller context. */
    private final PortalControllerContext portalControllerContext;


    /**
     * Constructor.
     */
    public ForumThreadParserContext() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param portalControllerContext portal controller context
     */
    public ForumThreadParserContext(PortalControllerContext portalControllerContext) {
        super();
        this.portalControllerContext = portalControllerContext;
    }


    /**
     * Get blockquote header HTML content.
     *
     * @param id     forum thread post identifier
     * @param author forum thread post author
     * @return HTML content
     */
    public String getBlockquoteHeader(String id, String author) throws PortletException, IOException {
        // Portlet request
        PortletRequest portletRequest = portalControllerContext.getRequest();

        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(portletRequest.getLocale());


        // Author display name
        String displayName;

        if (author == null) {
            displayName = StringUtils.EMPTY;
        } else {
            // Author person
            Person person = this.personService.getPerson(author);

            if (person == null) {
                displayName = author;
            } else {
                displayName = StringUtils.defaultIfBlank(person.getDisplayName(), author);
            }
        }


        // Blockquote header
        Element header = DOM4JUtils.generateElement("p", "blockquote-header", null);
        DOM4JUtils.addAttribute(header, "contenteditable", String.valueOf(false));

        // // Blockquote header link container
        Element linkContainer = DOM4JUtils.generateElement("strong", null, null);
        header.add(linkContainer);

        // Blockquote header link
        Element link = DOM4JUtils.generateLinkElement("#" + id, null, null, null, bundle.getString("FORUM_THREAD_USER_QUOTE", displayName));
        DOM4JUtils.addTooltip(link, bundle.getString("FORUM_THREAD_USER_QUOTE_TOOLTIP"));
        linkContainer.add(link);


        return DOM4JUtils.writeCompact(header);
    }

}
