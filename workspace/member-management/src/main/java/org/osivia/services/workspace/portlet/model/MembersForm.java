package org.osivia.services.workspace.portlet.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Members form java-bean.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class MembersForm {

    /** Members. */
    private List<Member> members;
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
     * Get member identifiers.
     *
     * @return member identifiers
     */
    public Set<String> getIdentifiers() {
        if (this.identifiers == null) {
            this.identifiers = new HashSet<String>();
            for (Member member : this.members) {
                this.identifiers.add(member.getId());
            }
        }
        return this.identifiers;
    }


    /**
     * Getter for members.
     *
     * @return the members
     */
    public List<Member> getMembers() {
        return this.members;
    }

    /**
     * Setter for members.
     *
     * @param members the members to set
     */
    public void setMembers(List<Member> members) {
        this.members = members;
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
