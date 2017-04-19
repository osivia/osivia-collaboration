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
package org.osivia.services.forum.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.Plugin;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.services.forum.plugin.player.ForumPlayer;
import org.osivia.services.forum.plugin.portlet.ForumListTemplateModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;


/**
 * Technical portlet for attributes bundles customization.
 *
 * @author Jean-Sébastien steux
 * @see AbstractPluginPortlet
 */
@Plugin("forum.plugin")
public class ForumPlugin extends AbstractPluginPortlet {

    /** Forum list template. */
    public static final String FORUM_LIST_TEMPLATE = "forum";


    /** Plugin name. */
    private static final String PLUGIN_NAME = "forum.plugin";

    /** Schemas. */
    private static final String SCHEMAS = "dublincore, common, toutatice, file, thread_toutatice";


    /** Bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public ForumPlugin() {
        super();

        // Bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(CustomizationContext context) {
        // Document types
        this.customizeDocumentTypes(context);
        // Players
        this.customizePlayers(context);
        // List templates
        this.customizeListTemplates(context);
        // Taskbar items
        this.customizeTaskbarItems(context);
    }


    /**
     * Customize document types.
     *
     * @param context customization context
     */
    private void customizeDocumentTypes(CustomizationContext context) {
        // Document types
        Map<String, DocumentType> types = this.getDocTypes(context);

        // Forum thread
        DocumentType thread = new DocumentType("Thread", false, false, false, false, true, true, new ArrayList<String>(0), null, "glyphicons glyphicons-chat",
                false, false);
        types.put(thread.getName(), thread);

        // Forum
        DocumentType forum = new DocumentType("Forum", true, true, false, false, true, true, Arrays.asList("Forum", "Thread"), null,
                "glyphicons glyphicons-conversation");
        types.put(forum.getName(), forum);
    }


    /**
     * Customize players.
     *
     * @param context customize players
     */
    private void customizePlayers(CustomizationContext context) {
        // Players
        @SuppressWarnings("rawtypes")
        List<IPlayerModule> players = this.getPlayers(context);

        // Forum player
        ForumPlayer forum = new ForumPlayer(this.getPortletContext());
        players.add(forum);
    }


    /**
     * Customize list templates.
     *
     * @param context customization context
     */
    private void customizeListTemplates(CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = this.getPortletContext();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(context.getLocale());

        // List templates
        Map<String, ListTemplate> templates = this.getListTemplates(context);

        // Forum list template
        ListTemplate forum = new ListTemplate(FORUM_LIST_TEMPLATE, bundle.getString("LIST_TEMPLATE_FORUM"), SCHEMAS);
        forum.setModule(new ForumListTemplateModule(portletContext));
        templates.put(forum.getKey(), forum);
    }


    /**
     * Customize taskbar items.
     *
     * @param context customization context
     */
    private void customizeTaskbarItems(CustomizationContext context) {
        // Taskbar items
        TaskbarItems items = this.getTaskbarItems(context);
        // Factory
        TaskbarFactory factory = this.getTaskbarService().getFactory();

        // Forum
        TaskbarItem forum = factory.createCmsTaskbarItem("FORUM", "FORUM_TASK", "glyphicons glyphicons-conversation", "Forum");
        items.add(forum);
    }


    /**
     * {@inheritDoc}
     */
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}





}
