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
package org.osivia.services.widgets.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
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
import fr.toutatice.portail.cms.nuxeo.api.domain.EditableWindow;
import fr.toutatice.portail.cms.nuxeo.api.domain.FragmentType;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;
import fr.toutatice.portail.cms.nuxeo.api.portlet.IPortletModule;


/**
 * Widget plugins for spaces decoration :
 *  - picture books
 *  - annonce sliders
 *  - zooms....
 *
 * @author Jean-Sébastien steux
 * @see GenericPortlet
 * @see ICustomizationModule
 */
@Plugin("widgets.plugin")
public class WidgetsPlugin extends AbstractPluginPortlet {

    /** Customizer name. */
    private static final String PLUGIN_NAME = "widgets.plugin";


    /** Picturebook list template. */
    public static final String STYLE_PICTUREBOOK = "picturebook";
    /** Picturebook schemas. */
    public static final String SCHEMAS_PICTUREBOOK = "dublincore, common, toutatice, note, files, acaren, webcontainer, file, picture";


    /** List template slider. */
    public static final String LIST_TEMPLATE_SLIDER = "slider";
    /** List template slider. */
    public static final String LIST_TEMPLATE_SLIDER_ANNONCE = "slider-annonce";
    /** List template slider. */
    public static final String LIST_TEMPLATE_SLIDER_PICTURE = "slider-picture";


    /** Slider schemas. */
    public static final String SLIDER_SCHEMAS = "dublincore, toutatice, picture, annonce";


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
    protected void customizeCMSProperties(String customizationID, CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = this.getPortletContext();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(context.getLocale());

        // ============= Picture book
        Map<String, DocumentType> docTypes = this.getDocTypes(context);
        // Picture book
        docTypes.put("PictureBook", new DocumentType("PictureBook", true, true, true, true, false, true, Arrays.asList("Picture", "PictureBook"), null,
                "glyphicons glyphicons-picture"));
        // Picture
        docTypes.put("Picture",new DocumentType("Picture", false, false, false, false, false, true, new ArrayList<String>(0), null, "glyphicons glyphicons-picture"));


        Map<String, ListTemplate> templates = this.getListTemplates(context);

        ListTemplate picturebookTemplate = new ListTemplate(STYLE_PICTUREBOOK, bundle.getString("LIST_TEMPLATE_PICTUREBOOK"), SCHEMAS_PICTUREBOOK);
        IPortletModule picturebookModule = new PicturebookTemplateModule(portletContext);
        picturebookTemplate.setModule(picturebookModule);
        templates.put(STYLE_PICTUREBOOK, picturebookTemplate);

        List<IPlayerModule> players = this.getPlayers(context);
        // ! insertion au début
        players.add(0, new PictureBookPlayer(this.getPortletContext()));

        // ============= Annonce
        // Annonce
        docTypes.put("Annonce",new DocumentType("Annonce", false, false, false, false, false, true, new ArrayList<String>(0), null, "glyphicons glyphicons-newspaper"));
        // Annonce folder
        docTypes.put("AnnonceFolder",new DocumentType("AnnonceFolder", true, true, false, false, false, true, Arrays.asList("Annonce"), null,
                "glyphicons glyphicons-newspaper"));

        players.add(new AnnounceFolderPlayer(this.getPortletContext()));

        // ============= URL Container
        // Document URL container
        docTypes.put("DocumentUrlContainer",new DocumentType("DocumentUrlContainer", true, true, true, true, false, true, Arrays.asList("DocumentUrlContainer", "ContextualLink"),
                null, "glyphicons glyphicons-bookmark"));

        players.add(new DocumentUrlContainerPlayer(this.getPortletContext()));



        // ============= Slider
        // Slider template
        ListTemplate slider = new ListTemplate(LIST_TEMPLATE_SLIDER, bundle.getString("LIST_TEMPLATE_SLIDER"), SLIDER_SCHEMAS);
        slider.setModule(new SliderTemplateModule(portletContext));
        templates.put(LIST_TEMPLATE_SLIDER,slider);

        ListTemplate sliderAnnonces = new ListTemplate(LIST_TEMPLATE_SLIDER_ANNONCE, bundle.getString("LIST_TEMPLATE_SLIDER_ANNONCE"), SLIDER_SCHEMAS);
        sliderAnnonces.setModule(new SliderTemplateModule(portletContext));
        templates.put(LIST_TEMPLATE_SLIDER_ANNONCE,sliderAnnonces);

        ListTemplate sliderPicture = new ListTemplate(LIST_TEMPLATE_SLIDER_PICTURE, bundle.getString("LIST_TEMPLATE_SLIDER_PICTURE"), SLIDER_SCHEMAS);
        sliderPicture.setModule(new SliderTemplateModule(portletContext));
        templates.put(LIST_TEMPLATE_SLIDER_PICTURE,sliderPicture);

        // Editable Window : slider
        Map<String, EditableWindow> editableWindows = this.getEditableWindows(context);
        editableWindows.put("fgt.slider_list", new SliderListEditableWindow("toutatice-portail-cms-nuxeo-viewListPortletInstance", "slider_liste_Frag_"));


        // =============== Criteria list
        editableWindows.put("fgt.criteria_list", new CriteriaListEditableWindow("toutatice-portail-cms-nuxeo-viewListPortletInstance", "criteria_liste_Frag_"));


        // =============== Zoom
        editableWindows.put("fgt.zoom", new ZoomEditableWindow("toutatice-portail-cms-nuxeo-viewFragmentPortletInstance", "zoom_frag_"));

        List<FragmentType> fragmentTypes = this.getFragmentTypes(context);
        // Zoom fragment
        fragmentTypes.add(new FragmentType(ZoomFragmentModule.ID, bundle.getString("FRAGMENT_TYPE_ZOOM"), new ZoomFragmentModule(portletContext)));

        // =============== Links
        // links editable window
        editableWindows.put("fgt.links", new LinksEditableWindow("toutatice-portail-cms-nuxeo-viewFragmentPortletInstance", "links_frag_"));

        // Links fragment
        fragmentTypes.add(new FragmentType(LinksFragmentModule.ID, bundle.getString("FRAGMENT_TYPE_LINKS"), new LinksFragmentModule(portletContext)));
    }

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet#getPluginName()
	 */
	@Override
	protected String getPluginName() {
		return PLUGIN_NAME;
	}

}
