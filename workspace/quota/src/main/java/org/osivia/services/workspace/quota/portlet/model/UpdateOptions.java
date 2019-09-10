package org.osivia.services.workspace.quota.portlet.model;

import java.util.ArrayList;
import java.util.List;

import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

// TODO: Auto-generated Javadoc
/**
 * Update quotas options java-bean.
 * 
 * @author Jean-Sébastien Steux
 */
/**
 * @author Jean-Sébastien
 */
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_APPLICATION)
public class UpdateOptions {
    
    /** The sizes. */
    private List<String> sizes;

    /**
     * Gets the sizes.
     *
     * @return the sizes
     */
    public List<String> getSizes() {
        return sizes;
    }

    /**
     * Sets the sizes.
     *
     * @param sizes the new sizes
     */
    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }


    /**
     * Constructor.
     */
    public UpdateOptions() {
        super();
        sizes = new ArrayList<>();
    }


}
