package org.osivia.services.search.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SearchSettings {

    private String taskPath;

    
    public String getTaskPath() {
        return taskPath;
    }

    
    public void setTaskPath(String taskPath) {
        this.taskPath = taskPath;
    }
    
    
}
