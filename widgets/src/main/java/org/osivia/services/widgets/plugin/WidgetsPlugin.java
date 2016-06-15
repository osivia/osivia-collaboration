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

import javax.portlet.PortletContext;
import javax.portlet.PortletException;

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
import org.osivia.portal.api.theming.TabGroup;
import org.osivia.services.widgets.plugin.ew.CriteriaListEditableWindow;
import org.osivia.services.widgets.plugin.ew.LinksEditableWindow;
import org.osivia.services.widgets.plugin.ew.SetEditableWindow;
import org.osivia.services.widgets.plugin.ew.SliderListEditableWindow;
import org.osivia.services.widgets.plugin.ew.ZoomEditableWindow;
import org.osivia.services.widgets.plugin.fragment.LinksFragmentModule;
import org.osivia.services.widgets.plugin.fragment.SummaryFragmentModule;
import org.osivia.services.widgets.plugin.fragment.ZoomFragmentModule;
import org.osivia.services.widgets.plugin.list.PicturebookTemplateModule;
import org.osivia.services.widgets.plugin.list.SliderTemplateModule;
import org.osivia.services.widgets.plugin.player.AnnounceFolderPlayer;
import org.osivia.services.widgets.plugin.player.DocumentUrlContainerPlayer;
import org.osivia.services.widgets.plugin.player.PictureBookPlayer;
import org.osivia.services.widgets.plugin.theming.SearchTabGroup;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.EditableWindow;
import fr.toutatice.portail.cms.nuxeo.api.domain.FragmentType;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;


/**
 * Widget plugins for spaces decoration :
 * - picture books
 * - annonce sliders
 * - zooms....
 *
 * @author Jean-Sébastien steux
 * @see AbstractPluginPortlet
 */
@Plugin("widgets.plugin")
public class WidgetsPlugin extends AbstractPluginPortlet {

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


    /** Customizer name. */
    private static final String PLUGIN_NAME = "widgets.plugin";


    /** Bundle factory. */
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public WidgetsPlugin() {
        super();
    }


    /**
     * {@inheritDoc}
     */
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
    protected String getPluginName() {
        return PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String customizationId, CustomizationContext context) {
        // Document types
        this.customizeDocumentTypes(context);
        // Players
        this.customizePlayers(context);
        // List templates
        this.customizeListTemplates(context);
        // Fragments types
        this.customizeFragmentTypes(context);
        // Editable windows
        this.customizeEditableWindows(context);
        // Tab groups
        this.customizeTabGroups(context);
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

        // Picture
        DocumentType picture = new DocumentType("Picture", false, false, false, false, false, true, new ArrayList<String>(0), null,
                "glyphicons glyphicons-picture", false, true, true);
        types.put(picture.getName(), picture);
        this.addSubType(context, "PortalSite", picture.getName());
        this.addSubType(context, "PortalPage", picture.getName());
        this.addSubType(context, "Folder", picture.getName());
        this.addSubType(context, "OrderedFolder", picture.getName());

        // Picture book
        DocumentType picturebook = new DocumentType("PictureBook", true, true, true, true, false, true, Arrays.asList(picture.getName(), "PictureBook"), null,
                "glyphicons glyphicons-pictures");
        types.put(picturebook.getName(), picturebook);

        // Audio
        DocumentType audio = new DocumentType("Audio", false, false, false, false, false, true, new ArrayList<String>(0), null, "glyphicons glyphicons-music",
                false, true, true);
        types.put(audio.getName(), audio);
        this.addSubType(context, "PortalSite", audio.getName());
        this.addSubType(context, "PortalPage", audio.getName());
        this.addSubType(context, "Folder", audio.getName());
        this.addSubType(context, "OrderedFolder", audio.getName());

        // Video
        DocumentType video = new DocumentType("Video", false, false, false, false, false, true, new ArrayList<String>(0), null, "glyphicons glyphicons-film",
                false, true, true);
        types.put(video.getName(), video);
        this.addSubType(context, "PortalSite", video.getName());
        this.addSubType(context, "PortalPage", video.getName());
        this.addSubType(context, "Folder", video.getName());
        this.addSubType(context, "OrderedFolder", video.getName());

        // Annonce
        DocumentType annonce = new DocumentType("Annonce", false, false, false, false, false, true, new ArrayList<String>(0), null,
                "glyphicons glyphicons-newspaper");
        types.put(annonce.getName(), annonce);
        this.addSubType(context, "PortalSite", annonce.getName());
        this.addSubType(context, "PortalPage", annonce.getName());

        // Annonce folder
        DocumentType annonceFolder = new DocumentType("AnnonceFolder", true, true, false, false, false, true, Arrays.asList(annonce.getName()), null,
                "glyphicons glyphicons-newspaper");
        types.put(annonceFolder.getName(), annonceFolder);

        // Document URL container
        DocumentType urlContainer = new DocumentType("DocumentUrlContainer", true, true, true, true, false, true, Arrays.asList("DocumentUrlContainer",
                "ContextualLink"), null, "glyphicons glyphicons-bookmark");
        types.put("DocumentUrlContainer", urlContainer);
    }


    /**
     * Customize players.
     *
     * @param context customize players
     */
    private void customizePlayers(CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = this.getPortletContext();

        // Players
        @SuppressWarnings("rawtypes")
        List<IPlayerModule> players = this.getPlayers(context);

        // Picture book
        PictureBookPlayer picturebook = new PictureBookPlayer(portletContext);
        players.add(0, picturebook);

        // Annonce folder
        AnnounceFolderPlayer annonceFolder = new AnnounceFolderPlayer(portletContext);
        players.add(annonceFolder);

        // Document URL container
        DocumentUrlContainerPlayer urlContainer = new DocumentUrlContainerPlayer(portletContext);
        players.add(urlContainer);
    }


    /**
     * Customize list templates.
     *
     * @param context customization context
     */
    private void customizeListTemplates(CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = this.getPortletContext();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(context.getLocale());

        // List templates
        Map<String, ListTemplate> templates = this.getListTemplates(context);

        // Picture book
        ListTemplate picturebook = new ListTemplate(STYLE_PICTUREBOOK, bundle.getString("LIST_TEMPLATE_PICTUREBOOK"), SCHEMAS_PICTUREBOOK);
        picturebook.setModule(new PicturebookTemplateModule(portletContext));
        templates.put(picturebook.getKey(), picturebook);

        // Slider
        ListTemplate slider = new ListTemplate(LIST_TEMPLATE_SLIDER, bundle.getString("LIST_TEMPLATE_SLIDER"), SLIDER_SCHEMAS);
        slider.setModule(new SliderTemplateModule(portletContext));
        templates.put(slider.getKey(), slider);

        // Slider annonces
        ListTemplate sliderAnnonces = new ListTemplate(LIST_TEMPLATE_SLIDER_ANNONCE, bundle.getString("LIST_TEMPLATE_SLIDER_ANNONCE"), SLIDER_SCHEMAS);
        sliderAnnonces.setModule(new SliderTemplateModule(portletContext));
        templates.put(sliderAnnonces.getKey(), sliderAnnonces);

        // Slider pictures
        ListTemplate sliderPictures = new ListTemplate(LIST_TEMPLATE_SLIDER_PICTURE, bundle.getString("LIST_TEMPLATE_SLIDER_PICTURE"), SLIDER_SCHEMAS);
        sliderPictures.setModule(new SliderTemplateModule(portletContext));
        templates.put(sliderPictures.getKey(), sliderPictures);
    }


    /**
     * Customize fragment types.
     *
     * @param context customization context
     */
    private void customizeFragmentTypes(CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = this.getPortletContext();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(context.getLocale());

        // Fragment types
        List<FragmentType> types = this.getFragmentTypes(context);

        // Zoom
        FragmentType zoom = new FragmentType(ZoomFragmentModule.ID, bundle.getString("FRAGMENT_TYPE_ZOOM"), new ZoomFragmentModule(portletContext));
        types.add(zoom);

        // Links
        FragmentType links = new FragmentType(LinksFragmentModule.ID, bundle.getString("FRAGMENT_TYPE_LINKS"), new LinksFragmentModule(portletContext));
        types.add(links);

        // Summary
        FragmentType summary = new FragmentType(SummaryFragmentModule.ID, bundle.getString("FRAGMENT_TYPE_SUMMARY"), new SummaryFragmentModule(portletContext));
        types.add(summary);
    }


    /**
     * Customize editable windows.
     *
     * @param context customization context
     */
    private void customizeEditableWindows(CustomizationContext context) {
        // Editable windows
        Map<String, EditableWindow> editableWindows = this.getEditableWindows(context);

        // Slider
        SliderListEditableWindow slider = new SliderListEditableWindow("toutatice-portail-cms-nuxeo-viewListPortletInstance", "slider_liste_Frag_");
        editableWindows.put("fgt.slider_list", slider);

        // Set
        final SetEditableWindow set = new SetEditableWindow("toutatice-portail-cms-nuxeo-viewListPortletInstance", "set_Frag_");
        editableWindows.put("fgt.set", set);


        // Criteria list
        CriteriaListEditableWindow criteriaList = new CriteriaListEditableWindow("toutatice-portail-cms-nuxeo-viewListPortletInstance", "criteria_liste_Frag_");
        editableWindows.put("fgt.criteria_list", criteriaList);

        // Zoom
        ZoomEditableWindow zoom = new ZoomEditableWindow("toutatice-portail-cms-nuxeo-viewFragmentPortletInstance", "zoom_frag_");
        editableWindows.put("fgt.zoom", zoom);

        // Links
        LinksEditableWindow links = new LinksEditableWindow("toutatice-portail-cms-nuxeo-viewFragmentPortletInstance", "links_frag_");
        editableWindows.put("fgt.links", links);
    }


    /**
     * Customize tab groups.
     *
     * @param context customization context
     */
    private void customizeTabGroups(CustomizationContext context) {
        // Tab groups
        Map<String, TabGroup> tabGroups = this.getTabGroups(context);

        // Search
        TabGroup search = new SearchTabGroup();
        tabGroups.put(search.getName(), search);
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

        // Gallery
        TaskbarItem gallery = factory.createCmsTaskbarItem("GALLERY", "GALLERY_TASK", "glyphicons glyphicons-pictures", "PictureBook");
        gallery.setToDefault(2);
        items.add(gallery);

        // News
        TaskbarItem news = factory.createCmsTaskbarItem("NEWS", "NEWS_TASK", "glyphicons glyphicons-newspaper", "AnnonceFolder");
        items.add(news);

        // Bookmarks
        TaskbarItem bookmarks = factory.createCmsTaskbarItem("BOOKMARKS", "BOOKMARKS_TASK", "glyphicons glyphicons-bookmark", "DocumentUrlContainer");
        items.add(bookmarks);
    }

}
