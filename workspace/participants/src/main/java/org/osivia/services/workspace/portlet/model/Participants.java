package org.osivia.services.workspace.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Participant container java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class Participants {

    /** Participant groups. */
    private List<Group> groups;
    /** Loaded indicator. */
    private boolean loaded;


    /**
     * Constructor.
     */
    public Participants() {
        super();
    }


    /**
     * Getter for groups.
     * 
     * @return the groups
     */
    public List<Group> getGroups() {
        return groups;
    }

    /**
     * Setter for groups.
     * 
     * @param groups the groups to set
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    /**
     * Getter for loaded.
     * 
     * @return the loaded
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Setter for loaded.
     * 
     * @param loaded the loaded to set
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

}
