package org.osivia.services.widgets.delete.portlet.model;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeleteItem {

    /**
     * Document DTO.
     */
    private DocumentDTO document;
    /**
     * Document children count.
     */
    private Integer childrenCount;
    /**
     * Document remote proxies indicator.
     */
    private boolean remoteProxies;


    /**
     * Constructor.
     */
    public DeleteItem() {
        super();
    }


    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Integer childrenCount) {
        this.childrenCount = childrenCount;
    }

    public boolean isRemoteProxies() {
        return remoteProxies;
    }

    public void setRemoteProxies(boolean remoteProxies) {
        this.remoteProxies = remoteProxies;
    }
}
