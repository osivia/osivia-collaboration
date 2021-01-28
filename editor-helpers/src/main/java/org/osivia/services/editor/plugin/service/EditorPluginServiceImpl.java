package org.osivia.services.editor.plugin.service;

import org.apache.commons.lang.ArrayUtils;
import org.osivia.portal.api.editor.EditorModule;
import org.osivia.portal.api.editor.EditorModuleResource;
import org.osivia.services.editor.common.service.CommonServiceImpl;
import org.osivia.services.editor.plugin.model.editor.EditorHelper;
import org.osivia.services.editor.plugin.model.editor.image.ImagePreviewEditorModuleResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Editor plugin service implementation.
 *
 * @author Cédric Krommenhoek
 * @see CommonServiceImpl
 * @see EditorPluginService
 */
@Service
public class EditorPluginServiceImpl extends CommonServiceImpl implements EditorPluginService {

    /**
     * Application context.
     */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public EditorPluginServiceImpl() {
        super();
    }


    @Override
    public void customizeEditorModules(List<EditorModule> modules) {
        // Editor helpers
        EditorHelper[] helpers = EditorHelper.values();

        if (ArrayUtils.isNotEmpty(helpers)) {
            for (EditorHelper helper : helpers) {
                EditorModule module = new EditorModule();
                module.setId(helper.getId());
                module.setInstance(helper.getInstance());
                module.setKey(helper.getKey());
                module.setClassLoader(this.getClass().getClassLoader());
                module.setApplicationContext(this.applicationContext);
                if (ArrayUtils.isNotEmpty(helper.getParameters())) {
                    Set<String> parameters = new HashSet<>(Arrays.asList(helper.getParameters()));
                    module.setParameters(parameters);
                }

                if (EditorHelper.IMAGE.equals(helper)) {
                    Map<String, EditorModuleResource> resources = new HashMap<>(1);
                    resources.put(ImagePreviewEditorModuleResource.ID, this.applicationContext.getBean(ImagePreviewEditorModuleResource.class));
                    module.setResources(resources);
                }

                modules.add(module);
            }
        }
    }
}
