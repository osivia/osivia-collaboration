package org.osivia.services.editor.plugin.model.editor;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.editor.image.portlet.service.EditorImageService;
import org.osivia.services.editor.link.portlet.service.EditorLinkService;

/**
 * Editor helpers enumeration.
 *
 * @author Cédric Krommenhoek
 */
public enum EditorHelper {

    /**
     * Image editor helper.
     */
    IMAGE("image", EditorImageService.PORTLET_INSTANCE, EditorImageService.CREATION_PARAMETER, EditorImageService.SRC_PARAMETER, EditorImageService.ALT_PARAMETER, EditorImageService.HEIGHT_PARAMETER, EditorImageService.WIDTH_PARAMETER),
    /**
     * Link editor helper.
     */
    LINK("link", EditorLinkService.PORTLET_INSTANCE, EditorLinkService.URL_PARAMETER, EditorLinkService.TEXT_PARAMETER, EditorLinkService.TITLE_PARAMETER, EditorLinkService.ONLY_TEXT_PARAMETER);


    /**
     * Identifier.
     */
    private final String id;
    /**
     * Portlet instance.
     */
    private final String instance;
    /**
     * Internationalization key.
     */
    private final String key;
    /**
     * Parameters.
     */
    private final String[] parameters;


    /**
     * Constructor.
     *
     * @param id         identifier
     * @param instance   portlet instance
     * @param parameters parameters
     */
    EditorHelper(String id, String instance, String... parameters) {
        this.id = id;
        this.key = "EDITOR_" + StringUtils.upperCase(this.name() + "_TITLE");
        this.instance = instance;
        this.parameters = parameters;
    }


    public String getId() {
        return id;
    }

    public String getInstance() {
        return instance;
    }

    public String getKey() {
        return key;
    }

    public String[] getParameters() {
        return parameters;
    }
}
