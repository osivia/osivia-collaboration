package org.osivia.services.editor.plugin.service;

import org.osivia.portal.api.editor.EditorModule;
import org.osivia.services.editor.common.service.CommonService;

import java.util.List;

/**
 * Editor plugin service interface.
 *
 * @author Cédric Krommenhoek
 * @see CommonService
 */
public interface EditorPluginService extends CommonService {

    /**
     * Plugin name.
     */
    String PLUGIN_NAME = "editor.plugin";


    /**
     * Customize editor modules.
     *
     * @param modules modules
     */
    void customizeEditorModules(List<EditorModule> modules);

}
