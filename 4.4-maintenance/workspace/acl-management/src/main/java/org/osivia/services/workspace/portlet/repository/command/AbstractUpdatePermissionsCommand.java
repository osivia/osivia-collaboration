package org.osivia.services.workspace.portlet.repository.command;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.DocumentPermissions;
import org.osivia.services.workspace.portlet.model.Permission;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Update permissions Nuxeo command abstract super class.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public abstract class AbstractUpdatePermissionsCommand implements INuxeoCommand {

    /** Document. */
    private final Document document;
    /** Nuxeo document permissions. */
    private final DocumentPermissions documentPermissions;
    /** Inherited permissions identifier. */
    private final boolean inherited;


    /**
     * Constructor.
     * 
     * @param document Nuxeo document
     * @param permissions permissions
     * @param inherited inherited permissions indicator
     */
    public AbstractUpdatePermissionsCommand(Document document, List<Permission> permissions, boolean inherited) {
        super();
        this.document = document;
        this.documentPermissions = this.toDocumentPermissions(permissions);
        this.inherited = inherited;
    }


    /**
     * Transform portlet model to document permissions
     * 
     * @param permissions portlet model
     * @return document permissions
     */
    private DocumentPermissions toDocumentPermissions(List<Permission> permissions) {
        DocumentPermissions documentPermissions;
        if (CollectionUtils.isEmpty(permissions)) {
            documentPermissions = new DocumentPermissions(0);
        } else {
            documentPermissions = new DocumentPermissions(permissions.size());

            for (Permission permission : permissions) {
                documentPermissions.setPermissions(permission.getName(), permission.getValues());
            }
        }
        return documentPermissions;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }


    /**
     * Getter for document.
     * 
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Getter for documentPermissions.
     * 
     * @return the documentPermissions
     */
    public DocumentPermissions getDocumentPermissions() {
        return documentPermissions;
    }

    /**
     * Getter for inherited.
     * 
     * @return the inherited
     */
    public boolean isInherited() {
        return inherited;
    }

}
