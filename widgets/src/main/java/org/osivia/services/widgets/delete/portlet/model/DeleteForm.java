package org.osivia.services.widgets.delete.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.SortedSet;

/**
 * Delete form.
 *
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeleteForm {

    /**
     * Delete items.
     */
    private SortedSet<DeleteItem> items;

    /**
     * Remote proxies count.
     */
    private int remoteProxiesCount;

    /**
     * Redirection path.
     */
    private String redirectionPath;


    public DeleteForm() {
        super();
    }


    public SortedSet<DeleteItem> getItems() {
        return items;
    }

    public void setItems(SortedSet<DeleteItem> items) {
        this.items = items;
    }

    public int getRemoteProxiesCount() {
        return remoteProxiesCount;
    }

    public void setRemoteProxiesCount(int remoteProxiesCount) {
        this.remoteProxiesCount = remoteProxiesCount;
    }

    public String getRedirectionPath() {
        return redirectionPath;
    }

    public void setRedirectionPath(String redirectionPath) {
        this.redirectionPath = redirectionPath;
    }
}
