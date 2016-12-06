package org.osivia.services.widgets.plugin.ew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;

import fr.toutatice.portail.cms.nuxeo.api.domain.EditableWindow;
import fr.toutatice.portail.cms.nuxeo.api.domain.EditableWindowHelper;
import fr.toutatice.portail.cms.nuxeo.api.portlet.ViewList;


public class SetEditableWindow extends EditableWindow {

    /** sch for fragment. */
    public static final String SET_SCHEMA = "setfgt:setFragment";

    public SetEditableWindow(String instancePortlet, String prefixWindow) {
        super(instancePortlet, prefixWindow);
    }

    @Override
    public Map<String, String> fillProps(Document doc, PropertyMap fragment, Boolean modeEditionPage) {

        Map<String, String> properties = super.fillGenericProps(doc, fragment, modeEditionPage);

        PropertyMap mapListe = EditableWindowHelper.findSchemaByRefURI(doc, SET_SCHEMA, fragment.getString("uri"));

        // setItems
        final PropertyList setItems = mapListe.getList("setItems");
        if ((setItems != null) && !setItems.isEmpty()) {
            properties.put(ViewList.WEBID_ORDERING_WINDOW_PROPERTY, "true");
            properties.put(ViewList.WEBID_ORDERING_SIZE_WINDOW_PROPERTY, String.valueOf(setItems.size()));
            List<String> webIds = new ArrayList<String>(setItems.size());
            String webid;
            for (int i = 0; i < setItems.list().size(); i++) {
                PropertyMap setItemMap = setItems.getMap(i);
                webid = setItemMap.getString("setWebId");
                properties.put(ViewList.WEBID_ORDERING_WINDOW_PROPERTY + "." + i, webid);
                webIds.add("'" + webid + "'");
            }

            String request = buildRequest(webIds);
            properties.put(ViewList.NUXEO_REQUEST_WINDOW_PROPERTY, request);
        } else {
            properties.put(ViewList.NUXEO_REQUEST_WINDOW_PROPERTY, "EMPTY_REQUEST");
        }


        properties.put(Constants.WINDOW_PROP_VERSION, "__inherited");


        /* Display */
        properties.put(ViewList.TEMPLATE_WINDOW_PROPERTY, mapListe.getString("style"));

        /* Technical */
        properties.put(ViewList.BEAN_SHELL_WINDOW_PROPERTY, String.valueOf(false));
        properties.put(ViewList.SCOPE_WINDOW_PROPERTY, null);
        properties.put(ViewList.METADATA_WINDOW_PROPERTY, "1");
        properties.put(ViewList.NUXEO_REQUEST_DISPLAY_WINDOW_PROPERTY, String.valueOf(false));

        properties.put(ViewList.CONTENT_FILTER_WINDOW_PROPERTY, null);

        properties.put(ViewList.PERMALINK_REFERENCE_WINDOW_PROPERTY, null);
        
        properties.put(ViewList.USE_ES_WINDOW_PROPERTY, String.valueOf(true));
        
        properties.put(ViewList.RSS_REFERENCE_WINDOW_PROPERTY, null);
        properties.put(ViewList.RSS_TITLE_WINDOW_PROPERTY, null);


        properties.put(Constants.WINDOW_PROP_URI, doc.getPath());


        return properties;
    }

    private String buildRequest(List<String> webIds) {
        final String webIdJoined = StringUtils.join(webIds, ",");
        return "ttc:webid IN (" + webIdJoined + ")";
    }

    @Override
    public List<String> prepareDelete(Document doc, String refURI) {
        List<String> propertiesToRemove = new ArrayList<String>();

        prepareDeleteGeneric(propertiesToRemove, doc, refURI);


        Integer findIndexByRefURI = EditableWindowHelper.findIndexByRefURI(doc, SET_SCHEMA, refURI);
        propertiesToRemove.add(SET_SCHEMA.concat("/").concat(findIndexByRefURI.toString()));

        // Bug automation, supprimer la liste de propriétés par son dernier élément, puis l'avant dernier, etc.
        // sinon décalage des n° d'index dans les propriétés
        Collections.reverse(propertiesToRemove);

        return propertiesToRemove;
    }

}
