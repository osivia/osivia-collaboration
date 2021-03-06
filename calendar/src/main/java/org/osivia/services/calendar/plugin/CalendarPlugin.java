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
package org.osivia.services.calendar.plugin;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.Plugin;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;
import org.osivia.services.calendar.plugin.menubar.CalendarMenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * Add integration in toutatice-cms: define Calendar and calendar events, enable calendar to be played with the specific player.
 *
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
@Plugin("calendar.plugin")
public class CalendarPlugin extends AbstractPluginPortlet  {

    /** Plugin name. */
	private static final String PLUGIN_NAME = "calendar.plugin";


    /**
     * Constructor.
     */
    public CalendarPlugin() {
        super();
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
        // Taskbar items
        this.customizeTaskbarItems(context);
        // Menubar modules
        this.customizeMenubarModules(context);
    }


    /**
     * Customize document types.
     *
     * @param context customization context
     */
    protected void customizeDocumentTypes(CustomizationContext context) {
        // Document types
        Map<String, DocumentType> types = this.getDocTypes(context);

        // Calendar event
        DocumentType event = DocumentType.createNode("VEVENT");
        event.setIcon("glyphicons glyphicons-important-day");
        event.setForceContextualization(true);
        event.setEditable(true);
        types.put(event.getName(), event);

        // Calendar
        DocumentType calendar = DocumentType.createNode("Agenda");
        calendar.addSubtypes(event.getName());
        calendar.setIcon("glyphicons glyphicons-calendar");
        calendar.setBrowsable(false);
        calendar.setForceContextualization(true);
        calendar.setEditable(true);
        types.put(calendar.getName(), calendar);
    }


    /**
     * Customize players.
     *
     * @param context customize players
     */
    protected void customizePlayers(CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = this.getPortletContext();

        // Players
        @SuppressWarnings("rawtypes")
        List<IPlayerModule> players = this.getPlayers(context);

        // Calendar player
        CalendarPlayer calendar = new CalendarPlayer(portletContext);
        players.add(calendar);
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

        // Calendar
        TaskbarItem calendar = factory.createCmsTaskbarItem("CALENDAR", "CALENDAR_TASK", "glyphicons glyphicons-calendar", "Agenda");
        factory.preset(calendar, true, 3);
        items.add(calendar);
    }


    /**
     * Customize menubar modules.
     *
     * @param context customization context
     */
    protected void customizeMenubarModules(CustomizationContext context) {
        // Menubar modules
        List<MenubarModule> modules = this.getMenubarModules(context);

        // Calendar menubar module
        MenubarModule calendar = new CalendarMenubarModule();
        modules.add(calendar);
    }


    /**
     * {@inheritDoc}
     */
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}

}
