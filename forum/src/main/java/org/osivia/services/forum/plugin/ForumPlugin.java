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

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.ICustomizationModule;
import org.osivia.portal.api.customization.Plugin;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.player.IPlayerModule;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;


/**
 * Technical portlet for attributes bundles customization.
 * 
 * @author Jean-Sébastien steux
 * @see GenericPortlet
 * @see ICustomizationModule
 */
@Plugin("forum.plugin")
public class ForumPlugin extends AbstractPluginPortlet {

    /** Customizer name. */
    private static final String PLUGIN_NAME = "forum.plugin";


    /** Forum list template. */
    public static final String STYLE_FORUM = "forum";


    /** Default schemas. */
    public static final String DEFAULT_SCHEMAS = "dublincore, common, toutatice, file";

    /** Bundle factory. */
    protected IBundleFactory bundleFactory;


    @Override
    public void init() throws PortletException {
        super.init();

        // Bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    //@PluginRegistration
    protected void customizeCMSProperties(String customizationID, CustomizationContext context) {

        // save current class loader


        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(context.getLocale());


        Map<String, DocumentType> docTypes = getDocTypes(context);

        docTypes.put("Forum", new DocumentType("Forum", true, true, false, false, true, true, Arrays.asList("Thread"), null,
                "glyphicons glyphicons-conversation"));
        // Forum thread
        docTypes.put("Thread", new DocumentType("Thread", false, false, false, false, true, true, new ArrayList<String>(0), null, "glyphicons glyphicons-chat",false,false));


        Map<String, ListTemplate> templates = getListTemplates(context);


        templates.put(STYLE_FORUM, new ListTemplate(STYLE_FORUM, bundle.getString("LIST_TEMPLATE_FORUM"), DEFAULT_SCHEMAS));
 

        List<IPlayerModule> modules = getPlayers(context);
        // ! insertion au début
        modules.add(0, new ForumPlayer(getPortletContext()));

    }

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet#getPluginName()
	 */
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}





}
