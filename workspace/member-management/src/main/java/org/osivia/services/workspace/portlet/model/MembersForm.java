package org.osivia.services.workspace.portlet.model;

import java.util.Set;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Members form java-bean.
 *
 * @author Cédric Krommenhoek
 * @see AbstractMembersForm
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class MembersForm extends AbstractMembersForm {

    /** Member identifiers. */
    private Set<String> identifiers;
    /** Loaded members indicator. */
    private boolean loaded;


    /**
     * Constructor.
     */
    public MembersForm() {
        super();
    }


    /**
     * Getter for identifiers.
     * 
     * @return the identifiers
     */
    public Set<String> getIdentifiers() {
        return identifiers;
    }

    /**
     * Setter for identifiers.
     * 
     * @param identifiers the identifiers to set
     */
    public void setIdentifiers(Set<String> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * Getter for loaded.
     *
     * @return the loaded
     */
    public boolean isLoaded() {
        return this.loaded;
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
