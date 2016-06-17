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
package fr.toutatice.faq.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.Plugin;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * FAQ plugin.
 *
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
@Plugin("faq.plugin")
public class FaqPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "faq.plugin";


    /**
     * Constructor.
     */
    public FaqPlugin() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String customizationID, CustomizationContext context) {
        // Document types
        this.customizeDocumentTypes(context);
        // Players
        this.customizePlayers(context);
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

        // FAQ question
        DocumentType faqQuestion = new DocumentType("Question", false, false, false, false, false, true, new ArrayList<String>(0), null,
                "glyphicons glyphicons-question-sign");
        types.put(faqQuestion.getName(), faqQuestion);

        // FAQ folder
        DocumentType faqFolder = new DocumentType("FaqFolder", true, false, false, true, false, true, Arrays.asList(faqQuestion.getName()), null,
                "glyphicons glyphicons-question-sign");
        types.put(faqFolder.getName(), faqFolder);
        this.addSubType(context, "Workspace", faqFolder.getName());
        this.addSubType(context, "Room", faqFolder.getName());
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

        // FAQ player
        FaqPlayer faq = new FaqPlayer();
        players.add(faq);
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

        // FAQ
        TaskbarItem faq = factory.createCmsTaskbarItem("FAQ", "FAQ_TASK", "glyphicons glyphicons-question-sign", "FaqFolder");
        items.add(faq);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }

}
